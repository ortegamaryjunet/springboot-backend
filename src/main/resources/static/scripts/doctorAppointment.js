const doctorProfile   = document.getElementById("doctorProfile");
const profileDropdown = document.getElementById("profileDropdown");

if (doctorProfile && profileDropdown) {
    doctorProfile.addEventListener("click", e => {
        e.stopPropagation();
        profileDropdown.classList.toggle("show");
    });
    document.addEventListener("click", () => {
        profileDropdown.classList.remove("show");
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (badge) {
        badge.textContent = count;
        badge.style.display = "inline-block";
    }
}
updateBadge("messageCount", 0);

const logoutLink  = document.getElementById("openLogoutModal");
const logoutModal = document.getElementById("logoutModal");
const cancelBtn   = document.getElementById("cancelBtn");
const logoutBtn   = document.getElementById("logoutBtn");

if (logoutLink && logoutModal) {
    logoutLink.addEventListener("click", e => {
        e.preventDefault();
        logoutModal.style.display = "flex";
    });
}
if (cancelBtn) {
    cancelBtn.addEventListener("click", () => {
        logoutModal.style.display = "none";
    });
}
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        window.location.href = "/web/logout";
    });
}

// appointmentsData is injected by the JSP above this script tag.
// Fall back to empty array if somehow not defined.
const appointments = (typeof appointmentsData !== "undefined") ? appointmentsData : [];

let currentPage = 1;
const rowsPerPage = 5;
let filteredAppointments = [...appointments];

document.addEventListener("DOMContentLoaded", () => {
    loadAppointments();
});

function loadAppointments() {
    const table    = document.getElementById("appointmentTable");
    if (!table) return;

    table.innerHTML = "";

    const start    = (currentPage - 1) * rowsPerPage;
    const end      = start + rowsPerPage;
    const pageData = filteredAppointments.slice(start, end);

    if (pageData.length === 0) {
        table.innerHTML = `<tr><td colspan="4" style="text-align:center;padding:16px;">No appointments found.</td></tr>`;
    } else {
        pageData.forEach(appt => {
            const statusLabel =
                appt.status === "noshow"
                    ? "No Show"
                    : (appt.status || "pending").charAt(0).toUpperCase() + (appt.status || "pending").slice(1);

            table.innerHTML += `
                <tr>
                    <td>${appt.patientName || "—"}</td>
                    <td>${appt.reason      || "—"}</td>
                    <td>${appt.time        || "—"}</td>
                    <td><span class="${appt.status || 'pending'}">${statusLabel}</span></td>
                </tr>
            `;
        });
    }

    updateSummary();
    updatePagination();
}

function updatePagination() {
    const totalPages = Math.ceil(filteredAppointments.length / rowsPerPage) || 1;
    document.getElementById("prevBtn").disabled = currentPage === 1;
    document.getElementById("nextBtn").disabled = currentPage >= totalPages;
    document.getElementById("pageInfo").textContent = `Page ${currentPage} of ${totalPages}`;
}

function prevPage() {
    if (currentPage > 1) { currentPage--; loadAppointments(); }
}

function nextPage() {
    const totalPages = Math.ceil(filteredAppointments.length / rowsPerPage);
    if (currentPage < totalPages) { currentPage++; loadAppointments(); }
}

function filterAppointments() {
    const filter = document.getElementById("statusFilter").value;
    filteredAppointments = filter === "All"
        ? [...appointments]
        : appointments.filter(a => a.status === filter);
    currentPage = 1;
    loadAppointments();
}

function updateSummary() {
    let confirmed = 0, waiting = 0, noshow = 0;
    filteredAppointments.forEach(a => {
        if (a.status === "confirmed")  confirmed++;
        else if (a.status === "waiting") waiting++;
        else if (a.status === "noshow")  noshow++;
    });
    const el = id => document.getElementById(id);
    if (el("confirmedCount")) el("confirmedCount").textContent = confirmed;
    if (el("waitingCount"))   el("waitingCount").textContent   = waiting;
    if (el("noshowCount"))    el("noshowCount").textContent    = noshow;
}