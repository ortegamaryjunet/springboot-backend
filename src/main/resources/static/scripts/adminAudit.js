document.addEventListener("DOMContentLoaded", () => {
    setupProfileDropdown();
    setupLogoutModal();
    updateBadge("notificationCount", 0);
    setupAuditTable();
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
    const openLogoutModal = document.getElementById("openLogoutModal");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    openLogoutModal?.addEventListener("click", e => {
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
        window.location.href = "/";
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (!badge) return;

    badge.textContent = count || 0;
    badge.style.display = "inline-flex";
}

function setupAuditTable() {
    const searchInput = document.getElementById("searchInput");
    const dateFilter = document.getElementById("dateFilter");
    const roleFilter = document.getElementById("roleFilter");
    const tableBody = document.getElementById("auditTableBody");
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const pageInfo = document.getElementById("pageInfo");

    if (!tableBody) return;

    const originalRows = Array.from(tableBody.querySelectorAll("tr"))
        .filter(row => row.querySelectorAll("td").length >= 7);

    let filteredRows = [...originalRows];
    let currentPage = 1;
    const rowsPerPage = 10;

    function filterRows() {
        const searchValue = searchInput?.value.toLowerCase().trim() || "";
        const dateValue = dateFilter?.value || "";
        const roleValue = roleFilter?.value.toLowerCase().trim() || "";

        filteredRows = originalRows.filter(row => {
            const cells = row.querySelectorAll("td");

            const dateTime = cells[0]?.textContent.trim() || "";
            const role = cells[1]?.textContent.toLowerCase().trim() || "";
            const name = cells[2]?.textContent.toLowerCase().trim() || "";
            const action = cells[3]?.textContent.toLowerCase().trim() || "";
            const module = cells[4]?.textContent.toLowerCase().trim() || "";

            const rowDate = dateTime.substring(0, 10);

            const matchSearch =
                name.includes(searchValue) ||
                action.includes(searchValue) ||
                module.includes(searchValue);

            const matchDate = !dateValue || rowDate === dateValue;
            const matchRole = !roleValue || role === roleValue;

            return matchSearch && matchDate && matchRole;
        });

        currentPage = 1;
        showPage();
    }

    function showPage() {
        tableBody.innerHTML = "";

        const totalPages = Math.ceil(filteredRows.length / rowsPerPage);

        if (filteredRows.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="empty-row">No records found.</td>
                </tr>
            `;

            if (pageInfo) pageInfo.textContent = "Page 0 of 0";
            if (prevBtn) prevBtn.disabled = true;
            if (nextBtn) nextBtn.disabled = true;
            return;
        }

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        filteredRows.slice(start, end).forEach(row => {
            tableBody.appendChild(row);
        });

        if (pageInfo) pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        if (prevBtn) prevBtn.disabled = currentPage === 1;
        if (nextBtn) nextBtn.disabled = currentPage >= totalPages;
    }

    searchInput?.addEventListener("input", filterRows);
    dateFilter?.addEventListener("change", filterRows);
    roleFilter?.addEventListener("change", filterRows);

    prevBtn?.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            showPage();
        }
    });

    nextBtn?.addEventListener("click", () => {
        const totalPages = Math.ceil(filteredRows.length / rowsPerPage);

        if (currentPage < totalPages) {
            currentPage++;
            showPage();
        }
    });

    showPage();
}