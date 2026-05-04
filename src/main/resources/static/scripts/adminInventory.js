document.addEventListener("DOMContentLoaded", () => {
    setupProfileDropdown();
    setupLogoutModal();
    updateBadge("notificationCount", 0);
    setupInventoryTables();
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
        window.location.href = "/";
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (!badge) return;

    badge.textContent = count || 0;
    badge.style.display = "inline-flex";
}

function setupInventoryTables() {
    const tabButtons = document.querySelectorAll(".tab-btn");
    const tableSections = document.querySelectorAll(".table-section");
    const searchInput = document.getElementById("searchInput");

    const tableMap = {
        "Dental Medicine": {
            section: document.getElementById("medicineTable"),
            tbody: document.getElementById("medicineTbody"),
            pageInfo: document.getElementById("medicinePageInfo"),
            prev: document.getElementById("medicinePrev"),
            next: document.getElementById("medicineNext"),
            colspan: 8
        },
        "Dental Equipment": {
            section: document.getElementById("equipmentTable"),
            tbody: document.getElementById("equipmentTbody"),
            pageInfo: document.getElementById("equipmentPageInfo"),
            prev: document.getElementById("equipmentPrev"),
            next: document.getElementById("equipmentNext"),
            colspan: 10
        },
        "Dental Supplies": {
            section: document.getElementById("supplyTable"),
            tbody: document.getElementById("supplyTbody"),
            pageInfo: document.getElementById("supplyPageInfo"),
            prev: document.getElementById("supplyPrev"),
            next: document.getElementById("supplyNext"),
            colspan: 6
        }
    };

    const rowsPerPage = 10;
    let activeTab = "Dental Medicine";

    Object.values(tableMap).forEach(table => {
        table.rows = table.tbody
            ? Array.from(table.tbody.querySelectorAll("tr")).filter(row => row.querySelectorAll("td").length > 1)
            : [];

        table.filteredRows = [...table.rows];
        table.currentPage = 1;
    });

    function renderTable(tabName) {
        const table = tableMap[tabName];

        if (!table || !table.tbody) return;

        table.tbody.innerHTML = "";

        const totalRows = table.filteredRows.length;
        const totalPages = Math.ceil(totalRows / rowsPerPage);

        if (totalRows === 0) {
            table.tbody.innerHTML = `
                <tr>
                    <td colspan="${table.colspan}" class="empty-row">No item found.</td>
                </tr>
            `;

            if (table.pageInfo) table.pageInfo.textContent = "Page 0 of 0";
            if (table.prev) table.prev.disabled = true;
            if (table.next) table.next.disabled = true;
            return;
        }

        if (table.currentPage > totalPages) {
            table.currentPage = totalPages;
        }

        const start = (table.currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        table.filteredRows.slice(start, end).forEach(row => {
            table.tbody.appendChild(row);
        });

        if (table.pageInfo) {
            table.pageInfo.textContent = `Page ${table.currentPage} of ${totalPages}`;
        }

        if (table.prev) {
            table.prev.disabled = table.currentPage === 1;
        }

        if (table.next) {
            table.next.disabled = table.currentPage >= totalPages;
        }
    }

    function searchTable() {
        const table = tableMap[activeTab];

        if (!table || !searchInput) return;

        const searchValue = searchInput.value.toLowerCase().trim();

        table.filteredRows = table.rows.filter(row =>
            row.textContent.toLowerCase().includes(searchValue)
        );

        table.currentPage = 1;
        renderTable(activeTab);
    }

    tabButtons.forEach(button => {
        button.addEventListener("click", () => {
            const tabName = button.textContent.trim();

            activeTab = tabName;

            tabButtons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");

            tableSections.forEach(section => section.classList.remove("show"));

            if (tableMap[tabName]?.section) {
                tableMap[tabName].section.classList.add("show");
            }

            if (searchInput) {
                searchInput.value = "";
            }

            tableMap[tabName].filteredRows = [...tableMap[tabName].rows];
            tableMap[tabName].currentPage = 1;

            renderTable(tabName);
        });
    });

    Object.keys(tableMap).forEach(tabName => {
        const table = tableMap[tabName];

        table.prev?.addEventListener("click", () => {
            if (table.currentPage > 1) {
                table.currentPage--;
                renderTable(tabName);
            }
        });

        table.next?.addEventListener("click", () => {
            const totalPages = Math.ceil(table.filteredRows.length / rowsPerPage);

            if (table.currentPage < totalPages) {
                table.currentPage++;
                renderTable(tabName);
            }
        });
    });

    searchInput?.addEventListener("input", searchTable);

    renderTable(activeTab);
}