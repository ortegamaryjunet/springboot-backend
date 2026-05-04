document.addEventListener("DOMContentLoaded", () => {
	setupEmployeeViewModal();
    setupProfileDropdown();
    setupLogoutModal();
    updateBadge("notificationCount", 0);
    setupEmployeeTable();
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
        if (e.target === logoutModal) {
            logoutModal.style.display = "none";
        }
    });

    logoutBtn?.addEventListener("click", () => {
        window.location.href = "/web/logout";
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (!badge) return;

    badge.textContent = count || 0;
    badge.style.display = "inline-flex";
}

function setupEmployeeTable() {
    const tableBody = document.getElementById("employeeTableBody");
    const searchInput = document.getElementById("searchInput");
    const roleFilter = document.getElementById("roleFilter");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const pageInfo = document.getElementById("pageInfo");

    const totalEmployeeEl = document.getElementById("totalEmployee");
    const dentistsEl = document.getElementById("totalDentists");
    const assistantsEl = document.getElementById("totalDentalAssistants");
    const receptionistsEl = document.getElementById("totalReceptionists");

    if (!tableBody) return;

    const rowsPerPage = 10;
    let currentPage = 1;

    const originalRows = Array.from(tableBody.querySelectorAll("tr"))
        .filter(row => row.querySelectorAll("td").length >= 8);

    const employees = originalRows.map(row => {
        const cells = row.querySelectorAll("td");

        return {
            id: cells[0]?.textContent.trim() || "",
            role: cells[1]?.textContent.trim() || "",
            lastName: cells[2]?.textContent.trim() || "",
            firstName: cells[3]?.textContent.trim() || "",
            middleName: cells[4]?.textContent.trim() || "",
            age: cells[5]?.textContent.trim() || "",
            gender: cells[6]?.textContent.trim() || "",
            row: row
        };
    });

    function getFilteredData() {
        const searchValue = searchInput?.value.toLowerCase().trim() || "";
        const roleValue = roleFilter?.value || "";

        return employees.filter(emp => {
            const fullName = `${emp.firstName} ${emp.middleName} ${emp.lastName}`.toLowerCase();
            const matchSearch =
                fullName.includes(searchValue) ||
                emp.id.toLowerCase().includes(searchValue);

            const matchRole = !roleValue || emp.role === roleValue;

            return matchSearch && matchRole;
        });
    }

    function updateSummary(data) {
        setText(totalEmployeeEl, data.length);
        setText(dentistsEl, data.filter(e => e.role === "Dentist").length);
        setText(assistantsEl, data.filter(e => e.role === "Dental Assistant").length);
        setText(receptionistsEl, data.filter(e => e.role === "Receptionist").length);
    }

    function renderTable() {
        const data = getFilteredData();
        const totalPages = Math.ceil(data.length / rowsPerPage);

        tableBody.innerHTML = "";

        if (data.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="8" class="empty-row">No employee found.</td>
                </tr>
            `;

            updateSummary([]);
            if (pageInfo) pageInfo.textContent = "Page 0 of 0";
            if (prevBtn) prevBtn.disabled = true;
            if (nextBtn) nextBtn.disabled = true;
            return;
        }

        if (currentPage > totalPages) currentPage = 1;

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        data.slice(start, end).forEach(emp => {
            tableBody.appendChild(emp.row);
        });

        updateSummary(data);

        if (pageInfo) pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        if (prevBtn) prevBtn.disabled = currentPage === 1;
        if (nextBtn) nextBtn.disabled = currentPage >= totalPages;
    }

    searchInput?.addEventListener("input", () => {
        currentPage = 1;
        renderTable();
    });

    roleFilter?.addEventListener("change", () => {
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
        const data = getFilteredData();
        const totalPages = Math.ceil(data.length / rowsPerPage);

        if (currentPage < totalPages) {
            currentPage++;
            renderTable();
        }
    });

    renderTable();
}

function setText(element, value) {
    if (element) element.textContent = value;
}

function setupEmployeeViewModal() {
    const tableBody = document.getElementById("employeeTableBody");
    const modal = document.getElementById("employeeModal");
    const closeBtn = document.getElementById("closeEmployeeModal");

    if (!tableBody || !modal) return;

    tableBody.addEventListener("click", function (e) {
        const btn = e.target.closest(".view-btn");
        if (!btn) return;

        e.preventDefault();

        const first = btn.dataset.first || "";
        const middle = btn.dataset.middle || "";
        const last = btn.dataset.last || "";
        const fullName = `${first} ${middle} ${last}`.replace(/\s+/g, " ").trim();

        setModalText("modalEmployeeId", btn.dataset.id);
        setModalText("modalUserId", btn.dataset.userid || "No login account");
        setModalText("modalBranch", btn.dataset.branch);
        setModalText("modalRole", btn.dataset.role);

        setModalText("modalFullName", fullName || "-");
        setModalText("modalEmail", btn.dataset.email);
        setModalText("modalPhone", btn.dataset.phone);

        setModalText("modalDob", btn.dataset.dob);
        setModalText("modalAge", btn.dataset.age);
        setModalText("modalGender", btn.dataset.gender);
        setModalText("modalAddress", btn.dataset.address);

        const isActive = btn.dataset.active;
        setModalText("modalActive", isActive === "true" ? "Active" : "Inactive");

        setModalText("modalCreated", formatDate(btn.dataset.created));
        setModalText("modalUpdated", formatDate(btn.dataset.updated));

        modal.style.display = "flex";
    });

    closeBtn?.addEventListener("click", () => {
        modal.style.display = "none";
    });

    modal.addEventListener("click", function (e) {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });
}

function setModalText(id, value) {
    const el = document.getElementById(id);
    if (el) el.innerText = value && value !== "null" ? value : "-";
}

function formatDate(dateString) {
    if (!dateString || dateString === "null") return "-";

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