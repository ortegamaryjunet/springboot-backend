document.addEventListener("DOMContentLoaded", function () {
    const receptProfile = document.getElementById("receptProfile");
    const profileDropdown = document.getElementById("profileDropdown");
    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");
    const searchInput = document.getElementById("searchInput");

    const tabButtons = document.querySelectorAll(".tab-btn");
    const tableSections = document.querySelectorAll(".table-section");

    const rowsPerPage = 10;

    const pageState = {
        medicine: 1,
        equipment: 1,
        supply: 1
    };

    let activeType = "medicine";

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

    function getRows(type) {
        const tbody = document.getElementById(type + "Tbody");

        if (!tbody) {
            return [];
        }

        return Array.from(tbody.querySelectorAll(".inventory-row"));
    }

    function getFilteredRows(type) {
        const value = searchInput ? searchInput.value.trim().toLowerCase() : "";

        return getRows(type).filter(function (row) {
            const searchData = (row.dataset.search || row.textContent || "").toLowerCase();
            return searchData.includes(value);
        });
    }

    function removeNoResultRow(tbody) {
        const oldRow = tbody.querySelector(".no-result-row");

        if (oldRow) {
            oldRow.remove();
        }
    }

    function addNoResultRow(tbody, colspan) {
        const row = document.createElement("tr");
        row.className = "no-result-row";
        row.innerHTML = `<td colspan="${colspan}">No item found.</td>`;
        tbody.appendChild(row);
    }

    function updateTable(type) {
        const tbody = document.getElementById(type + "Tbody");

        if (!tbody) {
            return;
        }

        const section = document.querySelector(`.table-section[data-type="${type}"]`);
        const rows = getRows(type);
        const filteredRows = getFilteredRows(type);
        const totalPages = filteredRows.length === 0 ? 0 : Math.ceil(filteredRows.length / rowsPerPage);
        const colspan = section ? section.querySelectorAll("thead th").length : 1;

        if (totalPages === 0) {
            pageState[type] = 0;
        } else if (pageState[type] < 1) {
            pageState[type] = 1;
        } else if (pageState[type] > totalPages) {
            pageState[type] = totalPages;
        }

        removeNoResultRow(tbody);

        rows.forEach(function (row) {
            row.style.display = "none";
        });

        if (filteredRows.length === 0) {
            if (rows.length > 0) {
                addNoResultRow(tbody, colspan);
            }

            const pageInfo = document.getElementById(type + "PageInfo");
            const prevBtn = document.getElementById(type + "Prev");
            const nextBtn = document.getElementById(type + "Next");

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

        const start = (pageState[type] - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        filteredRows.slice(start, end).forEach(function (row) {
            row.style.display = "";
        });

        const pageInfo = document.getElementById(type + "PageInfo");
        const prevBtn = document.getElementById(type + "Prev");
        const nextBtn = document.getElementById(type + "Next");

        if (pageInfo) {
            pageInfo.textContent = `Page ${pageState[type]} of ${totalPages}`;
        }

        if (prevBtn) {
            prevBtn.disabled = pageState[type] <= 1;
        }

        if (nextBtn) {
            nextBtn.disabled = pageState[type] >= totalPages;
        }
    }

    function updateAllTables() {
        updateTable("medicine");
        updateTable("equipment");
        updateTable("supply");
    }

    function switchTab(button) {
        const targetId = button.dataset.target;
        const targetSection = document.getElementById(targetId);

        if (!targetSection) {
            return;
        }

        tabButtons.forEach(function (btn) {
            btn.classList.remove("active");
        });

        tableSections.forEach(function (section) {
            section.classList.remove("show");
        });

        button.classList.add("active");
        targetSection.classList.add("show");

        activeType = targetSection.dataset.type;
        pageState[activeType] = 1;

        if (searchInput) {
            searchInput.value = "";
        }

        updateTable(activeType);
    }

    tabButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            switchTab(button);
        });
    });

    ["medicine", "equipment", "supply"].forEach(function (type) {
        const prevBtn = document.getElementById(type + "Prev");
        const nextBtn = document.getElementById(type + "Next");

        if (prevBtn) {
            prevBtn.addEventListener("click", function () {
                if (pageState[type] > 1) {
                    pageState[type]--;
                    updateTable(type);
                }
            });
        }

        if (nextBtn) {
            nextBtn.addEventListener("click", function () {
                const filteredCount = getFilteredRows(type).length;
                const totalPages = filteredCount === 0 ? 0 : Math.ceil(filteredCount / rowsPerPage);

                if (totalPages > 0 && pageState[type] < totalPages) {
                    pageState[type]++;
                    updateTable(type);
                }
            });
        }
    });

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            pageState[activeType] = 1;
            updateTable(activeType);
        });
    }

    updateAllTables();

    const stockModal = document.getElementById("stockModal");
    const viewStockBtn = document.getElementById("viewStockBtn");
    const closeStockModalBtn = document.getElementById("closeStockModalBtn");
    const closeStockModalBtn2 = document.getElementById("closeStockModalBtn2");
    const stockTabs = document.querySelectorAll(".stock-tab");
    const stockSections = document.querySelectorAll(".stock-section");
    const stockSearchInput = document.getElementById("stockSearchInput");
    const stockStatusFilter = document.getElementById("stockStatusFilter");

    let activeStockTable = "medicine";

    if (viewStockBtn) {
        viewStockBtn.addEventListener("click", function () {
            showModal(stockModal);
            filterStockRows();
        });
    }

    if (closeStockModalBtn) {
        closeStockModalBtn.addEventListener("click", function () {
            hideModal(stockModal);
        });
    }

    if (closeStockModalBtn2) {
        closeStockModalBtn2.addEventListener("click", function () {
            hideModal(stockModal);
        });
    }

    stockTabs.forEach(function (button) {
        button.addEventListener("click", function () {
            const tab = button.dataset.stockTab;

            activeStockTable = tab;

            stockTabs.forEach(function (btn) {
                btn.classList.remove("active");
            });

            stockSections.forEach(function (section) {
                section.classList.remove("show");
            });

            button.classList.add("active");

            const table = document.getElementById("stock" + capitalize(tab) + "Table");

            if (table) {
                table.classList.add("show");
            }

            filterStockRows();
        });
    });

    function capitalize(value) {
        return value.charAt(0).toUpperCase() + value.slice(1);
    }

    function normalizeStatus(status) {
        return String(status || "").toLowerCase();
    }

    function matchesStatus(status, filter) {
        const normalized = normalizeStatus(status);

        if (filter === "all") {
            return true;
        }

        if (filter === "low") {
            return normalized.includes("low");
        }

        if (filter === "out") {
            return normalized.includes("out") || normalized.includes("unavailable");
        }

        if (filter === "in") {
            return normalized.includes("in") || normalized.includes("available");
        }

        return true;
    }

    function filterStockRows() {
        const table = document.getElementById("stock" + capitalize(activeStockTable) + "Table");

        if (!table) {
            return;
        }

        const tbody = table.querySelector("tbody");
        const rows = Array.from(tbody.querySelectorAll(".stock-row"));
        const searchValue = stockSearchInput ? stockSearchInput.value.trim().toLowerCase() : "";
        const filterValue = stockStatusFilter ? stockStatusFilter.value : "all";

        const oldRow = tbody.querySelector(".no-result-row");

        if (oldRow) {
            oldRow.remove();
        }

        let visibleCount = 0;

        rows.forEach(function (row) {
            const searchData = (row.dataset.search || row.textContent || "").toLowerCase();
            const status = row.dataset.stockStatus || "";
            const isVisible = searchData.includes(searchValue) && matchesStatus(status, filterValue);

            row.style.display = isVisible ? "" : "none";

            if (isVisible) {
                visibleCount++;
            }

            const badge = row.querySelector(".stock-status");

            if (badge) {
                badge.classList.remove("stock-low", "stock-out", "stock-ok");

                const normalized = normalizeStatus(badge.textContent);

                if (normalized.includes("low")) {
                    badge.classList.add("stock-low");
                } else if (normalized.includes("out") || normalized.includes("unavailable")) {
                    badge.classList.add("stock-out");
                } else {
                    badge.classList.add("stock-ok");
                }
            }
        });

        if (rows.length > 0 && visibleCount === 0) {
            const colspan = table.querySelectorAll("thead th").length;
            const row = document.createElement("tr");
            row.className = "no-result-row";
            row.innerHTML = `<td colspan="${colspan}">No stock item found.</td>`;
            tbody.appendChild(row);
        }
    }

    if (stockSearchInput) {
        stockSearchInput.addEventListener("input", filterStockRows);
    }

    if (stockStatusFilter) {
        stockStatusFilter.addEventListener("change", filterStockRows);
    }

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
        }
    });
});

function openMedicineEditFromRow(button) {
    const row = button.closest("tr");

    document.getElementById("editMedId").value = row.dataset.id || "";
    document.getElementById("editMedName").value = row.dataset.name || "";
    document.getElementById("editMedGeneric").value = row.dataset.generic || "";
    document.getElementById("editMedCategory").value = row.dataset.category || "";
    document.getElementById("editMedForm").value = row.dataset.form || "";
    document.getElementById("editMedDosage").value = row.dataset.dosage || "";
    document.getElementById("editMedUnit").value = row.dataset.unit || "";
    document.getElementById("editMedQuantity").value = row.dataset.quantity || "";
    document.getElementById("editMedThreshold").value = row.dataset.threshold || "";

    document.getElementById("editMedicineModal").style.display = "flex";
}

function openEquipmentEditFromRow(button) {
    const row = button.closest("tr");

    document.getElementById("editEqId").value = row.dataset.id || "";
    document.getElementById("editEqName").value = row.dataset.name || "";
    document.getElementById("editEqBrand").value = row.dataset.brand || "";
    document.getElementById("editEqCategory").value = row.dataset.category || "";
    document.getElementById("editEqModel").value = row.dataset.model || "";
    document.getElementById("editEqSerial").value = row.dataset.serial || "";
    document.getElementById("editEqLocation").value = row.dataset.location || "";
    document.getElementById("editEqQuantity").value = row.dataset.quantity || "";
    document.getElementById("editEqThreshold").value = row.dataset.threshold || "";

    document.getElementById("editEquipmentModal").style.display = "flex";
}

function openSupplyEditFromRow(button) {
    const row = button.closest("tr");

    document.getElementById("editSupId").value = row.dataset.id || "";
    document.getElementById("editSupName").value = row.dataset.name || "";
    document.getElementById("editSupBrand").value = row.dataset.brand || "";
    document.getElementById("editSupCategory").value = row.dataset.category || "";
    document.getElementById("editSupUnit").value = row.dataset.unit || "";
    document.getElementById("editSupQuantity").value = row.dataset.quantity || "";
    document.getElementById("editSupThreshold").value = row.dataset.threshold || "";

    document.getElementById("editSupplyModal").style.display = "flex";
}

function openEquipmentDetailsFromRow(button) {
    const row = button.closest("tr");

    document.getElementById("viewEqName").textContent = row.dataset.name || "-";
    document.getElementById("viewEqBrand").textContent = row.dataset.brand || "-";
    document.getElementById("viewEqCategory").textContent = row.dataset.category || "-";
    document.getElementById("viewEqModel").textContent = row.dataset.model || "-";
    document.getElementById("viewEqSerial").textContent = row.dataset.serial || "-";
    document.getElementById("viewEqPurchaseDate").textContent = row.dataset.purchase || "-";
    document.getElementById("viewEqWarrantyDate").textContent = row.dataset.warranty || "-";
    document.getElementById("viewEqLocation").textContent = row.dataset.location || "-";
    document.getElementById("viewEqQuantity").textContent = row.dataset.quantity || "-";
    document.getElementById("viewEqThreshold").textContent = row.dataset.threshold || "-";
    document.getElementById("viewEqStockStatus").textContent = row.dataset.stockStatus || "-";

    document.getElementById("equipmentViewModal").style.display = "flex";
}

function closeEquipmentViewModal() {
    document.getElementById("equipmentViewModal").style.display = "none";
}

function closeEditModals() {
    document.getElementById("editMedicineModal").style.display = "none";
    document.getElementById("editEquipmentModal").style.display = "none";
    document.getElementById("editSupplyModal").style.display = "none";
}