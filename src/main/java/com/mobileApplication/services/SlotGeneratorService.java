package com.mobileApplication.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.SlotDTO;
import com.mobileApplication.models.Appointments;
import com.mobileApplication.models.DentistSchedule;
import com.mobileApplication.repositories.AppointmentRepository;
import com.mobileApplication.repositories.DentistScheduleRepository;

@Service
public class SlotGeneratorService {

	@Autowired private DentistScheduleRepository scheduleRepo;
    @Autowired private AppointmentRepository appointmentRepo;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    public List<SlotDTO> generateSlots(List<Long> dentistIds, int durationMin,
                                       String urgencyLevel, int batchNumber) {

        LocalDate startDate = getStartDateByUrgency(urgencyLevel);
        List<SlotDTO> allSlots = new ArrayList<>();

        for (Long dentistId : dentistIds) {
            List<SlotDTO> slots = getSlotsForDentist(dentistId, durationMin, startDate, 30);
            allSlots.addAll(slots);
        }

        int urgencyScore = getUrgencyScore(urgencyLevel);

        allSlots.sort(Comparator
            .comparing((SlotDTO s) -> {
                long daysDiff = Duration.between(
                    LocalDateTime.now(), s.getSlotStart()
                ).toDays();
                return daysDiff + urgencyScore;
            })
            .thenComparing(SlotDTO::getSlotStart)
        );

        int offset = (batchNumber - 1) * 3;
        if (offset >= allSlots.size()) return List.of();

        return allSlots.subList(offset, Math.min(offset + 3, allSlots.size()));
    }

    private List<SlotDTO> getSlotsForDentist(Long dentistId, int durationMin,
                                              LocalDate fromDate, int daysAhead) {
        List<SlotDTO> available = new ArrayList<>();

        for (int i = 0; i < daysAhead; i++) {
            LocalDate checkDate = fromDate.plusDays(i);
            int dayOfWeek = checkDate.getDayOfWeek().getValue();

            List<DentistSchedule> schedules = scheduleRepo
                .findByDentistIdAndDayOfWeekAndIsAvailableTrue(dentistId, dayOfWeek);

            if (schedules.isEmpty()) continue;

            LocalDateTime dayStart = checkDate.atStartOfDay();
            LocalDateTime dayEnd   = checkDate.plusDays(1).atStartOfDay();

            List<Appointments> booked = appointmentRepo
                .findBookedSlots(dentistId, dayStart, dayEnd);

            DentistSchedule sched = schedules.get(0);

            List<SlotDTO> daySlots = computeFreeSlots(
                dentistId,
                checkDate,
                sched.getStartTime(),
                sched.getEndTime(),
                durationMin,
                booked
            );

            available.addAll(daySlots);
        }

        return available;
    }

    private List<SlotDTO> computeFreeSlots(Long dentistId, LocalDate date,
                                           LocalTime start, LocalTime end,
                                           int durationMin, List<Appointments> booked) {

        List<SlotDTO> free = new ArrayList<>();

        LocalDateTime current = LocalDateTime.of(date, start);
        LocalDateTime dayEnd  = LocalDateTime.of(date, end);

        while (!current.plusMinutes(durationMin).isAfter(dayEnd)) {

            final LocalDateTime slotStart = current;
            final LocalDateTime slotEnd   = current.plusMinutes(durationMin);

            if (slotStart.isBefore(LocalDateTime.now())) {
                current = current.plusMinutes(durationMin);
                continue;
            }

            boolean conflict = booked.stream().anyMatch(a ->
                slotStart.isBefore(a.getScheduledEnd()) &&
                slotEnd.isAfter(a.getScheduledStart())
            );

            if (!conflict) {
                SlotDTO dto = new SlotDTO(dentistId, null, slotStart, slotEnd);

                dto.setMonth(slotStart.getMonth()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH));

                dto.setDay(String.valueOf(slotStart.getDayOfMonth()));

                dto.setDayOfWeek(slotStart.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH));

                dto.setTimeRange(
                    slotStart.format(TIME_FMT) + " - " + slotEnd.format(TIME_FMT)
                );

                dto.setDuration(durationMin + " min");

                free.add(dto);
            }

            current = current.plusMinutes(durationMin);
        }

        return free;
    }

    public LocalDate getStartDateByUrgency(String urgency) {
        return LocalDate.now();
    }

    private int getUrgencyScore(String urgency) {
        if (urgency == null) return 2;

        return switch (urgency.toLowerCase()) {
            case "emergency" -> 0;
            case "urgent"    -> 0;
            case "moderate"  -> 1;
            default          -> 2;
        };
    }
}