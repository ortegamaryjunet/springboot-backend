document.addEventListener("DOMContentLoaded", function () {
    const receptProfile = document.getElementById("receptProfile");
    const profileDropdown = document.getElementById("profileDropdown");
    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    const calendarBtn = document.getElementById("calendarBtn");
    const queueBtn = document.getElementById("queueBtn");
    const calendarView = document.getElementById("calendarView");
    const queueView = document.getElementById("queueView");

    const searchInput = document.getElementById("searchInput");
    const dentistFilter = document.getElementById("dentistFilter");
    const treatmentFilter = document.getElementById("treatmentFilter");

    const pendingList = document.getElementById("pendingList");
    const queueList = document.getElementById("queueList");

    const pendingPrev = document.getElementById("pendingPrev");
    const pendingNext = document.getElementById("pendingNext");
    const queuePrev = document.getElementById("queuePrev");
    const queueNext = document.getElementById("queueNext");

    const pendingPageInfo = document.getElementById("pendingPageInfo");
    const queuePageInfo = document.getElementById("queuePageInfo");
    const pendingCount = document.getElementById("pendingCount");
    const queueCount = document.getElementById("queueCount");

    const walkInModal = document.getElementById("walkInModal");
    const openWalkInBtn = document.getElementById("openWalkInBtn");
    const wiCloseBtn = document.getElementById("wiCloseBtn");
    const wiCancelBtn = document.getElementById("wi-cancelBtn");
    const wiSubmitBtn = document.getElementById("wi-submitBtn");
    const wiError = document.getElementById("wi-error");

    let pendingPage = 1;
    let queuePage = 1;

    const pendingPerPage = 4;
    const queuePerPage = 3;

    let pendingDataStore = normalizeAppointments(window.INITIAL_APPOINTMENTS || []);
    let queueListData = [];

    function showModal(modal) {
        if (modal) {
            modal.style.display = "flex";
        }
    }

    function hideModal(modal) {
        if (modal) {
            modal.style.display = "none";
        }
    }

    function updateBadge(id, count) {
        const badge = document.getElementById(id);

        if (!badge) {
            return;
        }

        badge.textContent = count;
        badge.style.display = count > 0 ? "inline-flex" : "none";
    }

    updateBadge("messageCount", 0);

    if (receptProfile && profileDropdown) {
        receptProfile.addEventListener("click", function (event) {
            event.stopPropagation();
            profileDropdown.classList.toggle("show");
        });

        document.addEventListener("click", function () {
            profileDropdown.classList.remove("show");
        });
    }

    if (logoutLink) {
        logoutLink.addEventListener("click", function (event) {
            event.preventDefault();
            showModal(logoutModal);
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", function () {
            hideModal(logoutModal);
        });
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", function () {
            window.location.href = "/";
        });
    }

    if (calendarBtn && queueBtn && calendarView && queueView) {
        calendarBtn.addEventListener("click", function () {
            calendarView.style.display = "block";
            queueView.style.display = "none";
            calendarBtn.classList.add("active");
            queueBtn.classList.remove("active");
        });

        queueBtn.addEventListener("click", function () {
            calendarView.style.display = "none";
            queueView.style.display = "flex";
            queueBtn.classList.add("active");
            calendarBtn.classList.remove("active");
        });
    }

    function normalizeAppointments(items) {
        if (!Array.isArray(items)) {
            return [];
        }

        return items.map(function (item) {
            // The backend DTO (AppointmentRowDTO) has these exact fields:
            //   id, patientName, dentistName, serviceName,
            //   scheduledStart ("hh:mm a"), scheduledEnd, dayOfWeek, dayOfMonth,
            //   status, bookingType, urgencyLevel, patientNotes
            // Read those directly. The fallback probes are for window.INITIAL_APPOINTMENTS
            // and any future schema additions.

            // Never fabricate an id — sending a fake id to /arrive, /cancel,
            // or /done would silently update the wrong DB row.
            let rawId = item.id;
            if (rawId === undefined || rawId === null || rawId === "") rawId = item.appointmentId;

            if (rawId === undefined || rawId === null || rawId === "") {
                console.warn("Appointment without backend id — actions will be disabled. Raw item:", item);
                rawId = null;
            }

            const patientName  = item.patientName  || item.name      || "Unnamed Patient";
            const doctorName   = item.dentistName  || item.doctor    || "Dentist not set";
            const treatmentName = item.serviceName || item.treatment || "Treatment not set";
            const dentistId    = String(item.dentistId || "");
            const serviceId    = String(item.serviceId || "");

            // The DTO already pre-formats day-of-week ("Wed"), day-of-month (4),
            // and scheduledStart ("10:00 AM"). Use them as-is rather than
            // trying to parse "10:00 AM" as a Date (which yields Invalid Date).
            const week = item.dayOfWeek || "Today";
            const day  = item.dayOfMonth !== undefined && item.dayOfMonth !== null && item.dayOfMonth !== 0
                ? String(item.dayOfMonth).padStart(2, "0")
                : "--";
            const time = item.scheduledStart || "--";

            return {
                id: rawId,
                name: patientName,
                dentistId: dentistId,
                serviceId: serviceId,
                doctor: doctorName,
                treatment: treatmentName,
                date: week,
                day: day,
                time: time,
                type: item.bookingType || item.type || "Scheduled",
                status: String(item.status || "").toLowerCase(),
                notes: item.patientNotes || item.notes || ""
            };
        });
    }

    function buildDateParts(rawDateTime, fallbackWeek, fallbackDay, fallbackTime) {
        if (rawDateTime) {
            const date = new Date(rawDateTime);

            if (!Number.isNaN(date.getTime())) {
                return {
                    week: date.toLocaleDateString("en-US", { weekday: "short" }),
                    day: String(date.getDate()).padStart(2, "0"),
                    time: date.toLocaleTimeString("en-US", {
                        hour: "2-digit",
                        minute: "2-digit"
                    })
                };
            }
        }

        return {
            week: fallbackWeek || "Today",
            day: fallbackDay || "--",
            time: fallbackTime || "--"
        };
    }

    function filterData(data) {
        const search = (searchInput ? searchInput.value : "").toLowerCase().trim();
        const dentist = dentistFilter ? dentistFilter.value : "all";
        const treatment = treatmentFilter ? treatmentFilter.value : "";

        return data.filter(function (app) {
            const searchData = (app.name + " " + app.doctor + " " + app.treatment).toLowerCase();
            const matchSearch = searchData.includes(search);
            const matchDentist = dentist === "all" || String(app.dentistId) === String(dentist);
            const matchTreatment = !treatment || String(app.serviceId) === String(treatment);

            return matchSearch && matchDentist && matchTreatment;
        });
    }

    function renderPending(data) {
        pendingList.innerHTML = "";

        if (!data || data.length === 0) {
            pendingList.innerHTML = '<div class="empty-state">No pending appointments found.</div>';
            return;
        }

        data.forEach(function (app) {
            const el = document.createElement("div");
            el.className = "appointment pending-style";

            el.innerHTML =
                '<div class="appointment-info">' +
                    '<div class="date-box">' +
                        '<div class="week">' + escapeHtml(app.date) + '</div>' +
                        '<div class="day">' + escapeHtml(app.day) + '</div>' +
                    '</div>' +

                    '<div class="info-content">' +
                        '<span class="appointment-type">' + escapeHtml(app.type) + '</span>' +
                        '<strong class="patient-name">' + escapeHtml(app.name) + '</strong>' +

                        '<div class="grid-info">' +
                            '<div class="info-row">' +
                                '<i class="fi fi-rr-clock-three"></i>' +
                                '<span>' + escapeHtml(app.time) + '</span>' +
                            '</div>' +

                            '<div class="info-row">' +
                                '<i class="fi fi-rr-stethoscope"></i>' +
                                '<span>' + escapeHtml(app.doctor) + '</span>' +
                            '</div>' +

                            '<div class="info-row">' +
                                '<i class="fi fi-rr-tooth"></i>' +
                                '<span>' + escapeHtml(app.treatment) + '</span>' +
                            '</div>' +
                        '</div>' +
                    '</div>' +
                '</div>' +

                '<div class="edit-actions">' +
                    '<div class="edit-dropdown">' +
                        '<button class="edit-btn edit-toggle" type="button">Edit ▾</button>' +

                        '<div class="edit-dropdown-menu">' +
                            '<div class="edit-dropdown-item" data-action="arrived" data-id="' + app.id + '">' +
                                '<i class="fi fi-rr-check"></i> Mark as Arrived' +
                            '</div>' +

                            '<div class="edit-dropdown-item" data-action="reschedule" data-id="' + app.id + '">' +
                                '<i class="fi fi-rr-calendar"></i> Request Reschedule' +
                            '</div>' +

                            '<div class="edit-dropdown-item danger" data-action="cancel" data-id="' + app.id + '">' +
                                '<i class="fi fi-rr-cross-circle"></i> Cancel Appointment' +
                            '</div>' +

                            '<div class="edit-dropdown-item" data-action="edit" data-id="' + app.id + '">' +
                                '<i class="fi fi-rr-edit"></i> Edit Appointment' +
                            '</div>' +
                        '</div>' +
                    '</div>' +
                '</div>';

            pendingList.appendChild(el);
        });
    }

    function renderQueue(data) {
        queueList.innerHTML = "";

        if (!data || data.length === 0) {
            queueList.innerHTML = '<div class="empty-state">No patients in queue.</div>';
            return;
        }

        data.forEach(function (app) {
            const el = document.createElement("div");
            el.className = "appointment";

            el.innerHTML =
                '<div class="queue-content">' +
                    '<strong class="patient-name">' + escapeHtml(app.name) + '</strong>' +
                    '<div class="queue-sub">' + escapeHtml(app.date) + ' ' + escapeHtml(app.day) + ' | ' + escapeHtml(app.time) + '</div>' +
                    '<div class="queue-sub">' + escapeHtml(app.doctor) + '</div>' +
                    '<div class="queue-sub">' + escapeHtml(app.treatment) + '</div>' +
                '</div>' +

                '<div class="actions">' +
                    '<button class="btn btn-pay" type="button">Validate Payment Receipt</button>' +
                    '<button class="btn btn-proceed" type="button" data-action="done" data-id="' + app.id + '">Proceed</button>' +
                '</div>';

            queueList.appendChild(el);
        });
    }

    function parseTime(timeStr) {
        const value = String(timeStr || "").trim();
        const parts = value.split(" ");

        if (parts.length < 2) {
            return 0;
        }

        const time = parts[0];
        const modifier = parts[1];
        const hm = time.split(":");

        let hours = Number(hm[0]);
        let minutes = Number(hm[1]);

        if (Number.isNaN(hours)) {
            hours = 0;
        }

        if (Number.isNaN(minutes)) {
            minutes = 0;
        }

        if (modifier === "PM" && hours !== 12) {
            hours += 12;
        }

        if (modifier === "AM" && hours === 12) {
            hours = 0;
        }

        return hours * 60 + minutes;
    }

    async function moveToQueue(id) {
        if (!isValidId(id)) {
            console.error("moveToQueue: invalid id", id);
            alert("Cannot mark as arrived — appointment id is missing.");
            return;
        }

        try {
            const res = await fetch("/web/api/appointments/" + id + "/arrive", {
                method: "POST"
            });

            if (!res.ok) {
                const msg = await readErrorMessage(res, "mark as arrived");
                throw new Error(msg);
            }

            await fetchAndRefresh();
        } catch (err) {
            console.error("Arrive error:", err);
            alert(err.message);
        }
    }

    function handleAction(type, id) {
        if (type === "arrived") {
            moveToQueue(id);
            return;
        }

        if (type === "cancel") {
            handleCancel(id);
            return;
        }

        if (type === "done") {
            handleDone(id);
            return;
        }

        if (type === "reschedule") {
            alert("Reschedule feature coming soon.");
            return;
        }

        if (type === "edit") {
            alert("Edit appointment feature coming soon.");
        }
    }

    async function handleCancel(id) {
        if (!isValidId(id)) {
            console.error("handleCancel: invalid id", id);
            alert("Cannot cancel — appointment id is missing.");
            return;
        }

        if (!confirm("Cancel this appointment?")) {
            return;
        }

        try {
            const res = await fetch("/web/api/appointments/" + id + "/cancel", {
                method: "POST"
            });

            if (!res.ok) {
                const msg = await readErrorMessage(res, "cancel appointment");
                throw new Error(msg);
            }

            await fetchAndRefresh();
        } catch (err) {
            console.error("Cancel error:", err);
            alert(err.message);
        }
    }

    async function handleDone(id) {
        if (!isValidId(id)) {
            console.error("handleDone: invalid id", id);
            alert("Cannot complete — appointment id is missing.");
            return;
        }

        try {
            const res = await fetch("/web/api/appointments/" + id + "/done", {
                method: "POST"
            });

            if (!res.ok) {
                const msg = await readErrorMessage(res, "complete appointment");
                throw new Error(msg);
            }

            await fetchAndRefresh();
        } catch (err) {
            console.error("Done error:", err);
            alert(err.message);
        }
    }

    // Spring's @PathVariable Long id rejects anything non-numeric with HTTP
    // 400 before our service even runs. Guard the call so the user sees a
    // clear message instead of a confusing HTTP error.
    function isValidId(id) {
        if (id === null || id === undefined || id === "") return false;
        if (String(id).startsWith("WI-")) return false;
        return /^\d+$/.test(String(id));
    }

    // Read either {error: "..."} JSON or plain text from a failed response,
    // log it for debugging, and return a human-readable message.
    async function readErrorMessage(res, action) {
        const fallback = "Failed to " + action + " (HTTP " + res.status + ").";
        try {
            const ct = res.headers.get("content-type") || "";
            if (ct.indexOf("application/json") !== -1) {
                const body = await res.json();
                console.error(action + " failed:", res.status, body);
                return body.error || body.message || fallback;
            }
            const text = await res.text();
            console.error(action + " failed:", res.status, text);
            return fallback;
        } catch (e) {
            console.error(action + " failed:", res.status, e);
            return fallback;
        }
    }

    function updateUI() {
        const filteredPending = filterData(pendingDataStore);
        const filteredQueue = filterData(queueListData);

        const totalPendingPages = filteredPending.length === 0 ? 0 : Math.ceil(filteredPending.length / pendingPerPage);
        const totalQueuePages = filteredQueue.length === 0 ? 0 : Math.ceil(filteredQueue.length / queuePerPage);

        if (totalPendingPages === 0) {
            pendingPage = 0;
        } else if (pendingPage < 1) {
            pendingPage = 1;
        } else if (pendingPage > totalPendingPages) {
            pendingPage = totalPendingPages;
        }

        if (totalQueuePages === 0) {
            queuePage = 0;
        } else if (queuePage < 1) {
            queuePage = 1;
        } else if (queuePage > totalQueuePages) {
            queuePage = totalQueuePages;
        }

        const startPending = pendingPage > 0 ? (pendingPage - 1) * pendingPerPage : 0;
        const startQueue = queuePage > 0 ? (queuePage - 1) * queuePerPage : 0;

        renderPending(filteredPending.slice(startPending, startPending + pendingPerPage));
        renderQueue(filteredQueue.slice(startQueue, startQueue + queuePerPage));

        if (pendingPageInfo) {
            pendingPageInfo.textContent = "Page " + pendingPage + " of " + totalPendingPages;
        }

        if (queuePageInfo) {
            queuePageInfo.textContent = "Page " + queuePage + " of " + totalQueuePages;
        }

        if (pendingPrev) {
            pendingPrev.disabled = pendingPage <= 1;
        }

        if (pendingNext) {
            pendingNext.disabled = pendingPage >= totalPendingPages || totalPendingPages === 0;
        }

        if (queuePrev) {
            queuePrev.disabled = queuePage <= 1;
        }

        if (queueNext) {
            queueNext.disabled = queuePage >= totalQueuePages || totalQueuePages === 0;
        }

        if (pendingCount) {
            pendingCount.textContent = filteredPending.length;
        }

        if (queueCount) {
            queueCount.textContent = filteredQueue.length;
        }
    }

    pendingNext?.addEventListener("click", function () {
        const totalPages = Math.ceil(filterData(pendingDataStore).length / pendingPerPage);

        if (pendingPage < totalPages) {
            pendingPage++;
            updateUI();
        }
    });

    pendingPrev?.addEventListener("click", function () {
        if (pendingPage > 1) {
            pendingPage--;
            updateUI();
        }
    });

    queueNext?.addEventListener("click", function () {
        const totalPages = Math.ceil(filterData(queueListData).length / queuePerPage);

        if (queuePage < totalPages) {
            queuePage++;
            updateUI();
        }
    });

    queuePrev?.addEventListener("click", function () {
        if (queuePage > 1) {
            queuePage--;
            updateUI();
        }
    });

    searchInput?.addEventListener("input", function () {
        pendingPage = 1;
        queuePage = 1;
        updateUI();
    });

    dentistFilter?.addEventListener("change", function () {
        pendingPage = 1;
        queuePage = 1;
        updateUI();
    });

    treatmentFilter?.addEventListener("change", function () {
        pendingPage = 1;
        queuePage = 1;
        updateUI();
    });

    document.addEventListener("click", function (event) {
        const toggle = event.target.closest(".edit-toggle");
        const actionItem = event.target.closest(".edit-dropdown-item");
        const proceedBtn = event.target.closest(".btn-proceed[data-action]");

        document.querySelectorAll(".edit-dropdown").forEach(function (dropdown) {
            if (!dropdown.contains(event.target)) {
                dropdown.classList.remove("active");
            }
        });

        if (toggle) {
            const dropdown = toggle.closest(".edit-dropdown");
            dropdown.classList.toggle("active");
        }

        if (actionItem) {
            const action = actionItem.dataset.action;
            const id = actionItem.dataset.id;
            handleAction(action, id);
        }

        if (proceedBtn) {
            const action = proceedBtn.dataset.action;
            const id = proceedBtn.dataset.id;
            handleAction(action, id);
        }

        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
        }
    });

    openWalkInBtn?.addEventListener("click", function () {
        clearWalkInForm();
        showModal(walkInModal);
    });

    wiCloseBtn?.addEventListener("click", function () {
        hideModal(walkInModal);
    });

    wiCancelBtn?.addEventListener("click", function () {
        hideModal(walkInModal);
    });

    wiSubmitBtn?.addEventListener("click", function () {
        addWalkInAppointment();
    });

    function clearWalkInForm() {
        setInputValue("wi-patientName", "");
        setInputValue("wi-dentist", "");
        setInputValue("wi-service", "");
        setInputValue("wi-datetime", "");
        setInputValue("wi-notes", "");

        if (wiError) {
            wiError.style.display = "none";
            wiError.textContent = "";
        }
    }

    async function addWalkInAppointment() {
        const patientName = getInputValue("wi-patientName");
        const dentistId = getInputValue("wi-dentist");
        const serviceId = getInputValue("wi-service");
        const scheduledStart = getInputValue("wi-datetime");
        const notes = getInputValue("wi-notes");

        if (!patientName || !dentistId || !serviceId || !scheduledStart) {
            showWalkInError("Please complete patient name, dentist, service, and date/time.");
            return;
        }

        if (wiSubmitBtn) {
            wiSubmitBtn.disabled = true;
            wiSubmitBtn.textContent = "Adding...";
        }

        try {
            const res = await fetch("/web/api/appointments/walkin", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    patientName: patientName,
                    dentistId: dentistId,
                    serviceId: serviceId,
                    scheduledStart: scheduledStart,
                    patientNotes: notes
                })
            });

            if (!res.ok) {
                const body = await res.json().catch(function () { return {}; });
                throw new Error(body.error || "Failed to add walk-in appointment.");
            }

            hideModal(walkInModal);
            await fetchAndRefresh();

        } catch (err) {
            showWalkInError(err.message);
        } finally {
            if (wiSubmitBtn) {
                wiSubmitBtn.disabled = false;
                wiSubmitBtn.textContent = "Add Walk-in";
            }
        }
    }

    function showWalkInError(message) {
        if (wiError) {
            wiError.textContent = message;
            wiError.style.display = "block";
        }
    }

    function getInputValue(id) {
        const element = document.getElementById(id);
        return element ? element.value.trim() : "";
    }

    function setInputValue(id, value) {
        const element = document.getElementById(id);

        if (element) {
            element.value = value;
        }
    }

    function getSelectedText(id) {
        const element = document.getElementById(id);

        if (!element || element.selectedIndex < 0) {
            return "";
        }

        return element.options[element.selectedIndex].text.trim();
    }

    function escapeHtml(value) {
        return String(value || "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }

    async function fetchAndRefresh() {
        try {
            const params = new URLSearchParams();
            const dentistVal = dentistFilter ? dentistFilter.value : "";
            const treatmentVal = treatmentFilter ? treatmentFilter.value : "";

            if (dentistVal && dentistVal !== "all") {
                params.set("dentistId", dentistVal);
            }

            if (treatmentVal) {
                params.set("serviceId", treatmentVal);
            }

            const url = "/web/api/appointments/today" + (params.toString() ? "?" + params.toString() : "");
            const res = await fetch(url);

            if (!res.ok) {
                throw new Error("Server error " + res.status);
            }

            const data = await res.json();

            // The service writes only these status values:
            //   "pending"   — set by walk-in creation and mobile booking (initial)
            //   "confirmed" — set when patient confirms the booking on mobile
            //   "arrived"   — set by markArrived()
            //   "completed" — set by markDone()
            //   "cancelled" — set by cancelAppointment()
            //   "rescheduled" — patient requested reschedule (handled separately)
            // Use an ALLOWLIST so completed/cancelled/rescheduled rows can never
            // reappear in the lists. Receptionist needs to see both pending
            // (newly booked) and confirmed (patient-confirmed) appointments
            // because both are awaiting arrival check-in.

            const pending = data.filter(function (a) {
                const s = String(a.status || "").toLowerCase();
                return s === "pending" || s === "confirmed";
            });

            const queue = data.filter(function (a) {
                return String(a.status || "").toLowerCase() === "arrived";
            });

            pendingDataStore = normalizeAppointments(pending);
            queueListData = normalizeAppointments(queue);

            updateUI();
        } catch (err) {
            console.error("Refresh error:", err);
        }
    }

    // Pull fresh data from the server on every dentist/treatment filter change
    // (the listeners further up only call updateUI() locally; this gives the
    // server a chance to apply its own filter params too)
    dentistFilter?.addEventListener("change", fetchAndRefresh);
    treatmentFilter?.addEventListener("change", fetchAndRefresh);

    // Initial sync with server on load + auto-refresh every 30s so
    // mobile-booked appointments and other reception sessions stay in sync
    fetchAndRefresh();
    setInterval(fetchAndRefresh, 30000);

    updateUI();
});