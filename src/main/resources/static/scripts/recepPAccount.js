document.addEventListener("DOMContentLoaded", function () {
    const receptProfile = document.getElementById("receptProfile");
    const profileDropdown = document.getElementById("profileDropdown");
    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    const openCreateBtn = document.getElementById("openCreateBtn");
    const createOverlay = document.getElementById("createOverlay");
    const patientOverlay = document.getElementById("patientOverlay");

    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const patientTable = document.getElementById("patientAccountTable");
    const noResultsRow = document.getElementById("noResultsRow");

    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const pageInfo = document.getElementById("pageInfo");

    const rowsPerPage = 8;
    let currentPage = 1;

    function showModal(element) {
        if (element) {
            element.style.display = "flex";
        }
    }

    function hideModal(element) {
        if (element) {
            element.style.display = "none";
        }
    }

    function updateBadge(id, count) {
        const badge = document.getElementById(id);

        if (!badge) {
            return;
        }

        badge.textContent = count;

        if (count > 0) {
            badge.style.display = "inline-flex";
        } else {
            badge.style.display = "none";
        }
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

    if (openCreateBtn) {
        openCreateBtn.addEventListener("click", function () {
            showModal(createOverlay);
        });
    }

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("overlay")) {
            event.target.style.display = "none";
        }

        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
        }
    });

    function getRows() {
        if (!patientTable) {
            return [];
        }

        return Array.from(patientTable.querySelectorAll(".patient-row"));
    }

    function getFilteredRows() {
        const searchValue = searchInput ? searchInput.value.trim().toLowerCase() : "";
        const selectedStatus = statusFilter ? statusFilter.value : "all";

        return getRows().filter(function (row) {
            const name = row.dataset.name || "";
            const username = row.dataset.username || "";
            const id = row.dataset.infoId || "";
            const rowStatus = (row.dataset.status || "").toLowerCase();

            const searchData = (id + " " + name + " " + username).toLowerCase();
            const matchesSearch = searchData.includes(searchValue);

            let matchesStatus = true;

            if (selectedStatus === "active") {
                matchesStatus = rowStatus === "active";
            } else if (selectedStatus === "inactive") {
                matchesStatus = rowStatus === "inactive";
            }

            return matchesSearch && matchesStatus;
        });
    }

    function updateTable() {
        const rows = getRows();
        const filteredRows = getFilteredRows();
        const totalPages = filteredRows.length === 0 ? 0 : Math.ceil(filteredRows.length / rowsPerPage);

        rows.forEach(function (row) {
            row.style.display = "none";
        });

        if (noResultsRow) {
            noResultsRow.style.display = "none";
        }

        if (totalPages === 0) {
            currentPage = 0;

            if (rows.length > 0 && noResultsRow) {
                noResultsRow.style.display = "";
            }

            if (pageInfo) {
                pageInfo.textContent = "Page 0 of 0";
            }

            if (prevBtn) {
                prevBtn.disabled = true;
            }

            if (nextBtn) {
                nextBtn.disabled = true;
            }

            return;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        filteredRows.slice(start, end).forEach(function (row) {
            row.style.display = "";
        });

        if (pageInfo) {
            pageInfo.textContent = "Page " + currentPage + " of " + totalPages;
        }

        if (prevBtn) {
            prevBtn.disabled = currentPage <= 1;
        }

        if (nextBtn) {
            nextBtn.disabled = currentPage >= totalPages;
        }
    }

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            currentPage = 1;
            updateTable();
        });
    }

    if (statusFilter) {
        statusFilter.addEventListener("change", function () {
            currentPage = 1;
            updateTable();
        });
    }

    if (prevBtn) {
        prevBtn.addEventListener("click", function () {
            if (currentPage > 1) {
                currentPage--;
                updateTable();
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener("click", function () {
            const totalPages = Math.ceil(getFilteredRows().length / rowsPerPage);

            if (currentPage < totalPages) {
                currentPage++;
                updateTable();
            }
        });
    }

    updateTable();
});

function openPatientOverlayFromRow(button) {
    const row = button.closest("tr");

    if (!row) {
        return;
    }

    document.getElementById("ovInfoId").value = row.dataset.infoId || "";
    document.getElementById("ovUserId").value = row.dataset.userId || "";
    document.getElementById("ovFirstName").value = row.dataset.firstName || "";
    document.getElementById("ovMiddleName").value = row.dataset.middleName || "";
    document.getElementById("ovLastName").value = row.dataset.lastName || "";
    document.getElementById("ovUsername").value = row.dataset.username || "";
    document.getElementById("ovRegistered").value = row.dataset.registered || "";
    document.getElementById("ovStatus").value = row.dataset.status || "ACTIVE";

    document.getElementById("patientOverlay").style.display = "flex";
}

function closePatientOverlay() {
    const overlay = document.getElementById("patientOverlay");

    if (overlay) {
        overlay.style.display = "none";
    }
}

function closeCreateOverlay() {
    const overlay = document.getElementById("createOverlay");

    if (overlay) {
        overlay.style.display = "none";
    }
}