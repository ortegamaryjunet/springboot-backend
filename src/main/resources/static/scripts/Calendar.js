function generateCalendar(calendar, year, month) {
    const calendarBody = calendar.querySelector(".calendarBody");
    calendarBody.innerHTML = "";

    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    let row = document.createElement("tr");

    const today = new Date();
    const todayYear = today.getFullYear();
    const todayMonth = today.getMonth();
    const todayDay = today.getDate();

    for (let i = 0; i < firstDay; i++) row.appendChild(document.createElement("td"));

    for (let day = 1; day <= daysInMonth; day++) {
        const cell = document.createElement("td");
        cell.textContent = day;

        const isPast =
            year < todayYear ||
            (year === todayYear && month < todayMonth) ||
            (year === todayYear && month === todayMonth && day < todayDay);

        if (isPast) {
            cell.classList.add("disabled");
        } else {
            if (day === todayDay && month === todayMonth && year === todayYear) cell.classList.add("today");

            cell.addEventListener("click", function () {
                calendar.querySelectorAll(".calendarBody td").forEach(td => td.classList.remove("selected"));
                cell.classList.add("selected");
            });
        }

        row.appendChild(cell);

        if ((day + firstDay) % 7 === 0) {
            calendarBody.appendChild(row);
            row = document.createElement("tr");
        }
    }

    if (row.children.length > 0) {
        while (row.children.length < 7) row.appendChild(document.createElement("td"));
        calendarBody.appendChild(row);
    }

    const months = [
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    ];
    calendar.querySelector(".currentMonthLabel").textContent = months[month] + " " + year;
}

function setupCalendar(calendar) {
    let now = new Date();
    let currentYear = now.getFullYear();
    let currentMonth = now.getMonth();

    const monthSelect = calendar.querySelector(".monthSelect");
    const yearSelect = calendar.querySelector(".yearSelect");

    const months = [
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    ];

    months.forEach((m,i) => {
        const option = document.createElement("option");
        option.value = i;
        option.textContent = m;
        monthSelect.appendChild(option);
    });

    for (let y = now.getFullYear(); y <= now.getFullYear() + 50; y++) {
        const option = document.createElement("option");
        option.value = y;
        option.textContent = y;
        yearSelect.appendChild(option);
    }

    function updateDropdowns() {
        monthSelect.value = currentMonth;
        yearSelect.value = currentYear;
    }

    function updateCalendar() {
        currentMonth = parseInt(monthSelect.value);
        currentYear = parseInt(yearSelect.value);
        generateCalendar(calendar, currentYear, currentMonth);
    }

    calendar.querySelector(".prevMonthBtn").onclick = function () {
        currentMonth--;
        if (currentMonth < 0) { currentMonth = 11; currentYear--; }
        updateDropdowns();
        generateCalendar(calendar, currentYear, currentMonth);
    };

    calendar.querySelector(".nextMonthBtn").onclick = function () {
        currentMonth++;
        if (currentMonth > 11) { currentMonth = 0; currentYear++; }
        updateDropdowns();
        generateCalendar(calendar, currentYear, currentMonth);
    };

    monthSelect.addEventListener("change", updateCalendar);
    yearSelect.addEventListener("change", updateCalendar);

    updateDropdowns();
    generateCalendar(calendar, currentYear, currentMonth);
}

document.querySelectorAll(".calendar").forEach(setupCalendar);
 