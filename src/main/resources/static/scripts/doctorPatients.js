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
    if (badge) { badge.textContent = count; badge.style.display = "inline-block"; }
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
    cancelBtn.addEventListener("click", () => { logoutModal.style.display = "none"; });
}
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => { window.location.href = "/web/logout"; });
}

// patientsData is injected by the JSP above this script tag.
const patients = (typeof patientsData !== "undefined") ? patientsData : [];

document.addEventListener("DOMContentLoaded", function () {

    let filteredPatients = [...patients];
    let currentPage = 1;
    const rowsPerPage = 5;

    const tableBody       = document.getElementById("tableBody");
    const searchInput     = document.getElementById("searchInput");
    const treatmentFilter = document.getElementById("treatmentFilter");
    const nameFilter      = document.getElementById("nameFilter");
    const dateFilter      = document.getElementById("dateFilter");
    const prevBtn         = document.getElementById("prevBtn");
    const nextBtn         = document.getElementById("nextBtn");
    const pageInfo        = document.getElementById("pageInfo");

    function renderTable() {
        if (!tableBody) return;
        tableBody.innerHTML = "";

        const start = (currentPage - 1) * rowsPerPage;
        const end   = start + rowsPerPage;

        const pageData = filteredPatients.slice(start, end);

        if (pageData.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="8" style="text-align:center;padding:16px;">No patients found.</td></tr>`;
        } else {
            pageData.forEach(p => {
                const lastVisitDisplay = (p.lastVisit && p.lastVisit !== "—" && p.lastVisit !== "null")
                    ? p.lastVisit : "—";

                tableBody.innerHTML += `
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.lastName   || "—"}</td>
                        <td>${p.firstName  || "—"}</td>
                        <td>${p.middleName || "—"}</td>
                        <td>${p.contact    || "—"}</td>
                        <td>${lastVisitDisplay}</td>
                        <td>${p.lastTreatment || "—"}</td>
                        <td class="actions-column">
                            <div class="btn-group">
                                <a href="/patientProfile?id=${p.id}" class="view-btn">
                                    <i class="fi fi-rs-file-invoice"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                `;
            });
        }

        updateButtons();
    }

    function updateButtons() {
        const totalPages = Math.ceil(filteredPatients.length / rowsPerPage) || 1;
        if (prevBtn)   prevBtn.disabled = currentPage === 1;
        if (nextBtn)   nextBtn.disabled = currentPage >= totalPages;
        if (pageInfo)  pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
    }

    function applyFilters() {
        filteredPatients = [...patients];

        const searchVal = searchInput?.value.toLowerCase() || "";
        if (searchVal) {
            filteredPatients = filteredPatients.filter(p =>
                (p.firstName || "").toLowerCase().includes(searchVal) ||
                (p.lastName  || "").toLowerCase().includes(searchVal)
            );
        }

        const treatmentVal = treatmentFilter?.value || "";
        if (treatmentVal) {
            filteredPatients = filteredPatients.filter(p =>
                (p.lastTreatment || "") === treatmentVal
            );
        }

        // dateFilter value is "yyyy-MM-dd" — direct string match with lastVisit
        const dateVal = dateFilter?.value || "";
        if (dateVal) {
            filteredPatients = filteredPatients.filter(p => p.lastVisit === dateVal);
        }

        const sortVal = nameFilter?.value || "";
        if (sortVal === "a-z") {
            filteredPatients.sort((a, b) => (a.lastName || "").localeCompare(b.lastName || ""));
        } else if (sortVal === "z-a") {
            filteredPatients.sort((a, b) => (b.lastName || "").localeCompare(a.lastName || ""));
        } else {
            // most-recent: sort by lastVisit descending ("yyyy-MM-dd" string sort works)
            filteredPatients.sort((a, b) => {
                if (!a.lastVisit || a.lastVisit === "—") return 1;
                if (!b.lastVisit || b.lastVisit === "—") return -1;
                return b.lastVisit.localeCompare(a.lastVisit);
            });
        }

        currentPage = 1;
        renderTable();
    }

    searchInput?.addEventListener("input",  applyFilters);
    treatmentFilter?.addEventListener("change", applyFilters);
    nameFilter?.addEventListener("change",  applyFilters);
    dateFilter?.addEventListener("change",  applyFilters);

    prevBtn?.addEventListener("click", () => {
        if (currentPage > 1) { currentPage--; renderTable(); }
    });
    nextBtn?.addEventListener("click", () => {
        const totalPages = Math.ceil(filteredPatients.length / rowsPerPage);
        if (currentPage < totalPages) { currentPage++; renderTable(); }
    });

    applyFilters();
});
