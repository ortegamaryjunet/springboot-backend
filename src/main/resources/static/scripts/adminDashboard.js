let visitChart;
let incomeChart;
let expenseChart;

document.addEventListener("DOMContentLoaded", () => {
    setupProfileDropdown();
    setupLogoutModal();
	setupStockModal();
    updateBadge("notificationCount", 0);

    initVisitDateFilter();
    loadVisitChart();
    loadIncomeChart();
    loadExpenseChart();

    if (typeof stockData !== "undefined") {
        updateStockBar(stockData);
    } else {
        updateStockBar({
            inStock: 0,
            lowStock: 0,
            outStock: 0
        });
    }
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
        const form = document.createElement("form");
        form.method = "POST";
        form.action = "/web/logout";
        document.body.appendChild(form);
        form.submit();
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (!badge) return;

    badge.textContent = count || 0;
    badge.style.display = "inline-flex";
}

function initVisitDateFilter() {
    const visitMonth = document.getElementById("visitMonth");
    const visitYear = document.getElementById("visitYear");

    if (!visitMonth || !visitYear) return;

    const today = new Date();

    visitMonth.value = String(today.getMonth());
    visitYear.value = String(today.getFullYear());

    visitMonth.addEventListener("change", updateVisitChart);
    visitYear.addEventListener("change", updateVisitChart);
}

function getWeekLabels(month, year) {
    const totalDays = new Date(year, month + 1, 0).getDate();
    const labels = [];

    let weekNumber = 1;
    let startDay = 1;

    while (startDay <= totalDays) {
        const endDay = Math.min(startDay + 6, totalDays);
        labels.push("Week " + weekNumber + " (" + startDay + "-" + endDay + ")");
        startDay += 7;
        weekNumber++;
    }

    return labels;
}

function getVisitData(month, year) {
    const labels = getWeekLabels(month, year);

    return {
        labels: labels,
        newPatients: Array(labels.length).fill(0),
        returningPatients: Array(labels.length).fill(0)
    };
}

function loadVisitChart() {
    const visitCanvas = document.getElementById("visitChart");
    const visitMonth = document.getElementById("visitMonth");
    const visitYear = document.getElementById("visitYear");

    if (!visitCanvas || !visitMonth || !visitYear || typeof Chart === "undefined") return;

    const month = parseInt(visitMonth.value, 10);
    const year = parseInt(visitYear.value, 10);
    const visitData = getVisitData(month, year);

    if (visitChart) visitChart.destroy();

    visitChart = new Chart(visitCanvas, {
        type: "bar",
        data: {
            labels: visitData.labels,
            datasets: [
                {
                    label: "New Patients",
                    data: visitData.newPatients,
                    backgroundColor: "#2563eb",
                    borderRadius: 6
                },
                {
                    label: "Returning Patients",
                    data: visitData.returningPatients,
                    backgroundColor: "#93c5fd",
                    borderRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: false,
            plugins: {
                legend: {
                    position: "top"
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            }
        }
    });
}

function updateVisitChart() {
    const visitMonth = document.getElementById("visitMonth");
    const visitYear = document.getElementById("visitYear");

    if (!visitMonth || !visitYear) return;

    const month = parseInt(visitMonth.value, 10);
    const year = parseInt(visitYear.value, 10);
    const visitData = getVisitData(month, year);

    if (!visitChart) {
        loadVisitChart();
        return;
    }

    visitChart.data.labels = visitData.labels;
    visitChart.data.datasets[0].data = visitData.newPatients;
    visitChart.data.datasets[1].data = visitData.returningPatients;
    visitChart.update("none");
}

function loadIncomeChart() {
    const incomeCanvas = document.getElementById("incomeChart");

    if (!incomeCanvas || typeof Chart === "undefined") return;

    if (incomeChart) incomeChart.destroy();

    incomeChart = new Chart(incomeCanvas, {
        type: "bar",
        data: {
            labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            datasets: [
                {
                    label: "Income",
                    data: Array(12).fill(0),
                    backgroundColor: "#2563eb",
                    borderRadius: 6
                },
                {
                    label: "Expenses",
                    data: Array(12).fill(0),
                    backgroundColor: "#7c3aed",
                    borderRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: false,
            plugins: {
                legend: {
                    position: "top"
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: value => "₱ " + value.toLocaleString("en-US")
                    }
                }
            }
        }
    });
}

function loadExpenseChart() {
    const expenseCanvas = document.getElementById("expenseChart");

    if (!expenseCanvas || typeof Chart === "undefined") return;

    if (expenseChart) expenseChart.destroy();

    expenseChart = new Chart(expenseCanvas, {
        type: "doughnut",
        data: {
            labels: ["Rental", "Wages", "Dental Supplies", "Dental Equipment", "Medicines", "Others"],
            datasets: [
                {
                    data: [10000, 10000, 10000, 10000, 10000, 10000],
                    backgroundColor: ["#2563eb", "#f59e0b", "#ef4444", "#7c3aed", "#22c55e", "#ec4899"],
                    borderWidth: 0
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: "65%",
            animation: false,
            plugins: {
                legend: {
                    position: "right",
                    labels: {
                        usePointStyle: true,
                        pointStyle: "circle",
                        padding: 14
                    }
                }
            }
        }
    });
}

function updateStockBar(data) {
    const inStockBar = document.querySelector(".in-stock");
    const lowStockBar = document.querySelector(".low-stock");
    const outStockBar = document.querySelector(".out-stock");

    if (!inStockBar || !lowStockBar || !outStockBar) return;

    const inStock = Number(data.inStock) || 0;
    const lowStock = Number(data.lowStock) || 0;
    const outStock = Number(data.outStock) || 0;

    const total = inStock + lowStock + outStock;

    if (total === 0) {
        inStockBar.style.width = "0%";
        lowStockBar.style.width = "0%";
        outStockBar.style.width = "0%";
        return;
    }

    inStockBar.style.width = ((inStock / total) * 100) + "%";
    lowStockBar.style.width = ((lowStock / total) * 100) + "%";
    outStockBar.style.width = ((outStock / total) * 100) + "%";
}

function setupStockModal() {
    const openStockModal = document.getElementById("openStockModal");
    const stockModal = document.getElementById("stockModal");
    const closeStockModal = document.getElementById("closeStockModal");

    if (!openStockModal || !stockModal || !closeStockModal) return;

    openStockModal.addEventListener("click", function (e) {
        e.preventDefault();
        stockModal.style.display = "flex";
    });

    closeStockModal.addEventListener("click", function () {
        stockModal.style.display = "none";
    });

    stockModal.addEventListener("click", function (e) {
        if (e.target === stockModal) {
            stockModal.style.display = "none";
        }
    });
}