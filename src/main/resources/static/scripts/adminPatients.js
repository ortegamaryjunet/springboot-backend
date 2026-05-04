document.addEventListener("DOMContentLoaded", () => {
	setupPatientViewModal();
    setupProfileDropdown();
    setupLogoutModal();
    updateBadge("notificationCount", 0);
    setupPatientTable();
});

function setupProfileDropdown() {
    const adminProfile = document.getElementById("adminProfile");
    const profileDropdown = document.getElementById("profileDropdown");

    if (!adminProfile || !profileDropdown) return;

    adminProfile.addEventListener("click", e => {
        e.stopPropagation();
        profileDropdown.classList.toggle("show");
    });

    profileDropdown.addEventListener("click", e => {
        e.stopPropagation();
    });

    document.addEventListener("click", () => {
        profileDropdown.classList.remove("show");
    });
}

function setupLogoutModal() {
    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    logoutLink?.addEventListener("click", e => {
        e.preventDefault();
        logoutModal.style.display = "flex";
    });

    cancelBtn?.addEventListener("click", () => {
        logoutModal.style.display = "none";
    });

    logoutModal?.addEventListener("click", e => {
        if (e.target === logoutModal) logoutModal.style.display = "none";
    });

    logoutBtn?.addEventListener("click", () => {
        window.location.href = "/";
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (!badge) return;

    badge.textContent = count || 0;
    badge.style.display = "inline-flex";
}

function setupPatientTable() {
    const tableBody = document.getElementById("patientTable");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const pageInfo = document.getElementById("pageInfo");
    const genderFilter = document.getElementById("genderFilter");
    const searchInput = document.getElementById("searchInput");

    if (!tableBody) return;

    const rowsPerPage = 10;
    let currentPage = 1;

    const originalRows = Array.from(tableBody.querySelectorAll("tr"))
        .filter(row => row.querySelectorAll("td").length >= 7);

    const patients = originalRows.map(row => {
        const cells = row.querySelectorAll("td");

        return {
            id: cells[0]?.textContent.trim() || "",
            lastName: cells[1]?.textContent.trim() || "",
            firstName: cells[2]?.textContent.trim() || "",
            middleName: cells[3]?.textContent.trim() || "",
            age: cells[4]?.textContent.trim() || "",
            gender: cells[5]?.textContent.trim() || "",
            row: row
        };
    });

    function getFilteredPatients() {
        const searchValue = searchInput?.value.toLowerCase().trim() || "";
        const genderValue = genderFilter?.value.toLowerCase() || "all";

        return patients.filter(patient => {
            const fullName = `${patient.lastName} ${patient.firstName} ${patient.middleName}`.toLowerCase();
            const matchSearch = fullName.includes(searchValue) || patient.id.toLowerCase().includes(searchValue);
            const matchGender = genderValue === "all" || patient.gender.toLowerCase() === genderValue;

            return matchSearch && matchGender;
        });
    }

    function renderTable() {
        const filteredPatients = getFilteredPatients();
        const totalPages = Math.ceil(filteredPatients.length / rowsPerPage);

        tableBody.innerHTML = "";

        if (filteredPatients.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="empty-row">No patient found.</td>
                </tr>
            `;

            if (pageInfo) pageInfo.textContent = "Page 0 of 0";
            if (prevBtn) prevBtn.disabled = true;
            if (nextBtn) nextBtn.disabled = true;
            return;
        }

        if (currentPage > totalPages) currentPage = 1;

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        filteredPatients.slice(start, end).forEach(patient => {
            tableBody.appendChild(patient.row);
        });

        if (pageInfo) pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        if (prevBtn) prevBtn.disabled = currentPage === 1;
        if (nextBtn) nextBtn.disabled = currentPage >= totalPages;
    }

    searchInput?.addEventListener("input", () => {
        currentPage = 1;
        renderTable();
    });

    genderFilter?.addEventListener("change", () => {
        currentPage = 1;
        renderTable();
    });

    prevBtn?.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            renderTable();
        }
    });

    nextBtn?.addEventListener("click", () => {
        const totalPages = Math.ceil(getFilteredPatients().length / rowsPerPage);

        if (currentPage < totalPages) {
            currentPage++;
            renderTable();
        }
    });

    renderTable();
}

function setupPatientViewModal() {
    const modal = document.getElementById("patientModal");
    const closeBtn = document.getElementById("closePatientModal");

    document.querySelectorAll(".view-btn").forEach(btn => {
        btn.addEventListener("click", function (e) {
            e.preventDefault();

            document.getElementById("modalName").innerText = this.dataset.name || "-";
            document.getElementById("modalUsername").innerText = this.dataset.username || "-";
            document.getElementById("modalEmail").innerText = this.dataset.email || "-";
            document.getElementById("modalPhone").innerText = this.dataset.phone || "-";

            document.getElementById("modalDob").innerText = this.dataset.dob || "-";
            document.getElementById("modalGender").innerText = this.dataset.gender || "-";
            document.getElementById("modalAddress").innerText = this.dataset.address || "-";

            document.getElementById("modalCreated").innerText = formatDate(this.dataset.created);
            document.getElementById("modalUpdated").innerText = formatDate(this.dataset.updated);

            modal.style.display = "flex";
        });
    });

    closeBtn?.addEventListener("click", () => {
        modal.style.display = "none";
    });

    modal?.addEventListener("click", (e) => {
        if (e.target === modal) modal.style.display = "none";
    });
}

function formatDate(dateString) {
    if (!dateString) return "-";

    const date = new Date(dateString);

    if (isNaN(date)) return dateString;

    return date.toLocaleString("en-PH", {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit"
    });
}