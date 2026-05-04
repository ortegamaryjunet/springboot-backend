document.addEventListener("DOMContentLoaded", function () {
    const receptProfile = document.getElementById("receptProfile");
    const profileDropdown = document.getElementById("profileDropdown");
    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    const searchInput = document.getElementById("searchInput");
    const genderFilter = document.getElementById("genderFilter");
    const patientTable = document.getElementById("patientTable");
    const noResultsRow = document.getElementById("noResultsRow");

    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const pageInfo = document.getElementById("pageInfo");

    const closePatientDetailsBtn = document.getElementById("closePatientDetailsBtn");
    const closePatientDetailsBtn2 = document.getElementById("closePatientDetailsBtn2");
    const closeEditPatientBtn = document.getElementById("closeEditPatientBtn");
    const closeEditPatientBtn2 = document.getElementById("closeEditPatientBtn2");

    const rowsPerPage = 10;
    let currentPage = 1;

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

    function getRows() {
        if (!patientTable) {
            return [];
        }

        return Array.from(patientTable.querySelectorAll(".patient-row"));
    }

    function getFilteredRows() {
        const searchValue = searchInput ? searchInput.value.trim().toLowerCase() : "";
        const genderValue = genderFilter ? genderFilter.value.toLowerCase() : "all";

        return getRows().filter(function (row) {
            const name = row.dataset.name || "";
            const id = row.dataset.id || "";
            const gender = (row.dataset.gender || "").toLowerCase();
            const searchData = (id + " " + name).toLowerCase();

            const matchesSearch = searchData.includes(searchValue);
            const matchesGender = genderValue === "all" || gender === genderValue;

            return matchesSearch && matchesGender;
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

    if (genderFilter) {
        genderFilter.addEventListener("change", function () {
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

    if (closePatientDetailsBtn) {
        closePatientDetailsBtn.addEventListener("click", function () {
            hideModal(document.getElementById("patientDetailsModal"));
        });
    }

    if (closePatientDetailsBtn2) {
        closePatientDetailsBtn2.addEventListener("click", function () {
            hideModal(document.getElementById("patientDetailsModal"));
        });
    }

    if (closeEditPatientBtn) {
        closeEditPatientBtn.addEventListener("click", function () {
            hideModal(document.getElementById("editPatientModal"));
        });
    }

    if (closeEditPatientBtn2) {
        closeEditPatientBtn2.addEventListener("click", function () {
            hideModal(document.getElementById("editPatientModal"));
        });
    }

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("modal")) {
            event.target.style.display = "none";
        }
    });

    updateTable();
});

function openPatientDetails(button) {
    const row = button.closest("tr");

    if (!row) {
        return;
    }

    setText("detailId", row.dataset.id);
    setText("detailUsername", row.dataset.username);
    setText("detailLastName", row.dataset.lastName);
    setText("detailFirstName", row.dataset.firstName);
    setText("detailMiddleName", row.dataset.middleName);
    setText("detailDob", row.dataset.dob);
    setText("detailAge", row.dataset.age);
    setText("detailGender", row.dataset.gender);
    setText("detailEmail", row.dataset.email);
    setText("detailPhone", row.dataset.phone);
    setText("detailAddress", row.dataset.address);

    document.getElementById("patientDetailsModal").style.display = "flex";
}

function openEditPatient(button) {
    const row = button.closest("tr");

    if (!row) {
        return;
    }

    setValue("editInfoId", row.dataset.infoId);
    setValue("editLastName", row.dataset.lastName);
    setValue("editFirstName", row.dataset.firstName);
    setValue("editMiddleName", row.dataset.middleName);
    setValue("editDob", row.dataset.dob);
    setValue("editGender", row.dataset.gender);
    setValue("editEmail", row.dataset.email);
    setValue("editPhone", row.dataset.phone);
    setValue("editAddress", row.dataset.address);

    document.getElementById("editPatientModal").style.display = "flex";
}

function setText(id, value) {
    const element = document.getElementById(id);

    if (element) {
        element.textContent = value || "-";
    }
}

function setValue(id, value) {
    const element = document.getElementById(id);

    if (element) {
        element.value = value || "";
    }
}