const adminProfile = document.getElementById("adminProfile");
const profileDropdown = document.getElementById("profileDropdown");

if (adminProfile && profileDropdown) {
    adminProfile.addEventListener("click", e => {
        e.stopPropagation();
        profileDropdown.classList.toggle("show");
    });

    document.addEventListener("click", e => {
        if (!adminProfile.contains(e.target)) {
            profileDropdown.classList.remove("show");
        }
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);

    if (!badge) {
        return;
    }

    badge.textContent = count;
    badge.style.display = "inline-block";
}

updateBadge("notificationCount", 0);

const logoutLink = document.getElementById("logoutLink");
const logoutModal = document.getElementById("logoutModal");
const cancelBtn = document.getElementById("cancelBtn");
const logoutBtn = document.getElementById("logoutBtn");

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
        window.location.href = "/";
    });
}

let mainReportChart;
let statusChart;
let currentReportType = "clinicDentist";
let currentRows = [];

const reportData = {
    clinicDentist: {
        title: "Clinic and Dentist Performance Reports",
        mainChartTitle: "Clinic and Dentist Performance Overview",
        statusChartTitle: "Dentist Performance Status",
        summary: {
            totalRecords: 24,
            activeData: 20,
            thisMonth: 6,
            attention: 2
        },
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
        mainData: [18, 24, 22, 30, 28, 35],
        statusLabels: ["Excellent", "Good", "Needs Improvement"],
        statusData: [12, 9, 3],
        headers: ["ID", "Dentist Name", "Total Patients", "Treatments Done", "Rating", "Status", "Date"],
        rows: [
            ["1", "Dr. Anne Cruz", "45", "38", "4.8", "Excellent", "2026-05-01"],
            ["2", "Dr. Marco Santos", "32", "27", "4.5", "Good", "2026-05-02"],
            ["3", "Dr. Ella Reyes", "18", "14", "3.8", "Needs Improvement", "2026-04-28"]
        ]
    },

    satisfaction: {
        title: "Patient Satisfaction Ratings and Feedback Reports",
        mainChartTitle: "Patient Satisfaction Overview",
        statusChartTitle: "Rating Summary",
        summary: {
            totalRecords: 50,
            activeData: 45,
            thisMonth: 12,
            attention: 4
        },
        labels: ["1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"],
        mainData: [1, 2, 5, 15, 27],
        statusLabels: ["Positive", "Neutral", "Negative"],
        statusData: [38, 8, 4],
        headers: ["ID", "Patient Name", "Rating", "Feedback", "Dentist", "Status", "Date"],
        rows: [
            ["1", "Juan Dela Cruz", "5", "Very satisfied with the service", "Dr. Anne Cruz", "Positive", "2026-05-01"],
            ["2", "Maria Santos", "4", "Good clinic service", "Dr. Marco Santos", "Positive", "2026-05-02"],
            ["3", "Carlo Reyes", "2", "Long waiting time", "Dr. Ella Reyes", "Negative", "2026-04-27"]
        ]
    },

    revenue: {
        title: "Revenue, Income, and Expense Reports",
        mainChartTitle: "Revenue and Expense Overview",
        statusChartTitle: "Financial Summary",
        summary: {
            totalRecords: 36,
            activeData: 30,
            thisMonth: 8,
            attention: 1
        },
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
        mainData: [5000, 7000, 6000, 8000, 7500, 9000],
        statusLabels: ["Income", "Expenses", "Net Revenue"],
        statusData: [9000, 4000, 5000],
        headers: ["ID", "Date", "Description", "Income", "Expense", "Net Amount", "Status"],
        rows: [
            ["1", "2026-05-01", "Dental Cleaning Payments", "5000", "0", "5000", "Completed"],
            ["2", "2026-05-02", "Inventory Purchase", "0", "2500", "-2500", "Completed"],
            ["3", "2026-04-28", "Tooth Extraction Payments", "7500", "0", "7500", "Completed"]
        ]
    },

    visits: {
        title: "Patient Visit Records",
        mainChartTitle: "Patient Visit Overview",
        statusChartTitle: "Visit Status",
        summary: {
            totalRecords: 120,
            activeData: 95,
            thisMonth: 18,
            attention: 3
        },
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
        mainData: [12, 18, 15, 22, 25, 28],
        statusLabels: ["Completed", "Pending", "Cancelled"],
        statusData: [95, 15, 10],
        headers: ["ID", "Patient Name", "Dentist", "Visit Date", "Purpose", "Status"],
        rows: [
            ["1", "Juan Dela Cruz", "Dr. Anne Cruz", "2026-05-01", "Dental Cleaning", "Completed"],
            ["2", "Maria Santos", "Dr. Marco Santos", "2026-05-02", "Consultation", "Pending"],
            ["3", "Carlo Reyes", "Dr. Ella Reyes", "2026-04-27", "Tooth Extraction", "Cancelled"]
        ]
    },

    treatments: {
        title: "Patient Treatment Records",
        mainChartTitle: "Patient Treatment Overview",
        statusChartTitle: "Treatment Status",
        summary: {
            totalRecords: 86,
            activeData: 75,
            thisMonth: 14,
            attention: 5
        },
		labels: [
		    "Dental Cleaning",
		    "Smile Make-overs",
		    "Teeth Whitening",
		    "Veneers",
		    "Porcelain Jacket Crowns",
		    "Complete Partial Dentures",
		    "Removable Partial Dentures",
		    "Root Canal Treatment",
		    "Orthodontics",
		    "Clear Aligners",
		    "Dental Implants"
		],
		mainData: [30, 15, 22, 12, 9, 7, 8, 10, 14, 11, 6],
		statusLabels: ["Completed", "Ongoing", "Follow Up"],
		statusData: [60, 18, 8],
		headers: ["ID", "Patient Name", "Treatment", "Dentist", "Treatment Date", "Status"],
		rows: [
		    ["1", "Juan Dela Cruz", "Dental Cleaning", "Dr. Anne Cruz", "2026-05-01", "Completed"],
		    ["2", "Maria Santos", "Teeth Whitening", "Dr. Marco Santos", "2026-05-02", "Ongoing"],
		    ["3", "Carlo Reyes", "Root Canal Treatment", "Dr. Ella Reyes", "2026-04-27", "Follow Up"],
		    ["4", "Angela Ramos", "Veneers", "Dr. Anne Cruz", "2026-04-28", "Completed"],
		    ["5", "Miguel Lopez", "Clear Aligners", "Dr. Marco Santos", "2026-04-30", "Ongoing"],
		    ["6", "Sofia Garcia", "Dental Implants", "Dr. Ella Reyes", "2026-05-03", "Follow Up"]
		]
    },

    stockAvailability: {
        title: "Stock Items Availability Records",
        mainChartTitle: "Stock Items Availability Overview",
        statusChartTitle: "Stock Status",
        summary: {
            totalRecords: 80,
            activeData: 68,
            thisMonth: 15,
            attention: 7
        },
        labels: ["Medicines", "Dental Supplies", "Equipment"],
        mainData: [25, 30, 10, 15],
        statusLabels: ["Available", "Low Stock", "Out of Stock", "Expired"],
        statusData: [60, 10, 5, 5],
        headers: ["ID", "Item Name", "Category", "Quantity", "Expiration Date", "Status"],
        rows: [
            ["1", "Anesthesia", "Medicine", "20", "2027-01-15", "Available"],
            ["2", "Gloves", "Dental Supplies", "5", "2026-08-10", "Low Stock"],
            ["3", "Expired Syrup", "Medicine", "0", "2026-01-01", "Expired"]
        ]
    },

    consumption: {
        title: "Monthly and Quarterly Consumption Reports",
        mainChartTitle: "Consumption Overview",
        statusChartTitle: "Consumption Level",
        summary: {
            totalRecords: 42,
            activeData: 35,
            thisMonth: 9,
            attention: 4
        },
		labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
		mainData: [20, 25, 18, 30, 28, 35, 32, 27, 40, 38, 34, 45],
		statusLabels: ["Normal", "High", "Critical"],
		statusData: [7, 3, 2],
		headers: ["ID", "Item Name", "Month", "Quarter", "Consumed Quantity", "Status"],
		rows: [
		    ["1", "Gloves", "January", "Q1", "65", "Normal"],
		    ["2", "Face Mask", "February", "Q1", "72", "Normal"],
		    ["3", "Anesthesia", "March", "Q1", "35", "Critical"],
		    ["4", "Cotton Rolls", "April", "Q2", "90", "Normal"],
		    ["5", "Gloves", "May", "Q2", "120", "High"],
		    ["6", "Face Mask", "June", "Q2", "80", "Normal"],
		    ["7", "Dental Bibs", "July", "Q3", "95", "Normal"],
		    ["8", "Syringe", "August", "Q3", "115", "High"],
		    ["9", "Anesthesia", "September", "Q3", "40", "Critical"],
		    ["10", "Cotton Rolls", "October", "Q4", "100", "Normal"],
		    ["11", "Gloves", "November", "Q4", "125", "High"],
		    ["12", "Face Mask", "December", "Q4", "85", "Normal"]
		]
    },

    inventoryUsage: {
        title: "Inventory Usage Reports",
        mainChartTitle: "Inventory Usage Overview",
        statusChartTitle: "Usage Status",
        summary: {
            totalRecords: 64,
            activeData: 58,
            thisMonth: 16,
            attention: 6
        },
        labels: ["Gloves", "Masks", "Anesthesia", "Cotton", "Syringe"],
        mainData: [100, 90, 40, 70, 50],
        statusLabels: ["Used", "Remaining", "Wasted"],
        statusData: [55, 35, 10],
        headers: ["ID", "Item Name", "Used Quantity", "Remaining Quantity", "Used By", "Date Used", "Status"],
        rows: [
            ["1", "Gloves", "20", "80", "Dr. Anne Cruz", "2026-05-01", "Used"],
            ["2", "Face Mask", "15", "75", "Receptionist", "2026-05-02", "Used"],
            ["3", "Anesthesia", "5", "10", "Dr. Marco Santos", "2026-04-28", "Low Stock"]
        ]
    },

    audit: {
        title: "Audit Trails and Activity Logs",
        mainChartTitle: "System Activity Overview",
        statusChartTitle: "Activity Status",
        summary: {
            totalRecords: 300,
            activeData: 280,
            thisMonth: 50,
            attention: 8
        },
        labels: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        mainData: [30, 45, 38, 50, 60, 40, 37],
        statusLabels: ["Success", "Failed"],
        statusData: [270, 30],
        headers: ["ID", "Timestamp", "Role Type", "Name", "Action Type", "Module Section", "IP Address", "Status"],
        rows: [
            ["1", "2026-05-02 10:30 AM", "Admin", "Admin Full Name", "Login", "Dashboard", "127.0.0.1", "Success"],
            ["2", "2026-05-02 11:00 AM", "Admin", "Admin Full Name", "Update", "Inventory", "127.0.0.1", "Success"],
            ["3", "2026-05-02 11:20 AM", "Admin", "Unknown", "Login", "Login Page", "127.0.0.1", "Failed"]
        ]
    }
};

document.addEventListener("DOMContentLoaded", () => {
    loadReport("clinicDentist");

    const reportType = document.getElementById("reportType");
    const applyFilter = document.getElementById("applyFilter");
    const searchInput = document.getElementById("searchInput");
    const exportCSV = document.getElementById("exportCSV");
    const exportPDF = document.getElementById("exportPDF");

    if (reportType) {
        reportType.addEventListener("change", () => {
            loadReport(reportType.value);
        });
    }

    if (applyFilter) {
        applyFilter.addEventListener("click", () => {
            const selectedReport = document.getElementById("reportType").value;
            loadReport(selectedReport);
        });
    }

    if (searchInput) {
        searchInput.addEventListener("input", () => {
            searchTable(searchInput.value);
        });
    }

    if (exportCSV) {
        exportCSV.addEventListener("click", exportToCSV);
    }

    if (exportPDF) {
        exportPDF.addEventListener("click", exportToPDF);
    }
});

function loadReport(type) {
    const data = reportData[type];

    if (!data) {
        return;
    }

    currentReportType = type;
    currentRows = data.rows;

    document.getElementById("tableTitle").textContent = data.title;
    document.getElementById("mainChartTitle").textContent = data.mainChartTitle;
    document.getElementById("statusChartTitle").textContent = data.statusChartTitle;

    document.getElementById("totalRecords").textContent = data.summary.totalRecords;
    document.getElementById("activeData").textContent = data.summary.activeData;
    document.getElementById("thisMonth").textContent = data.summary.thisMonth;
    document.getElementById("attention").textContent = data.summary.attention;

    document.getElementById("searchInput").value = "";

    loadTable(data.headers, data.rows);
    loadMainChart(data.labels, data.mainData, data.mainChartTitle);
    loadStatusChart(data.statusLabels, data.statusData);
}

function loadTable(headers, rows) {
    const tableHead = document.getElementById("tableHead");
    const tableBody = document.getElementById("tableBody");
    const noDataMessage = document.getElementById("noDataMessage");

    tableHead.innerHTML = "";
    tableBody.innerHTML = "";

    let headerRow = "<tr>";

    headers.forEach(header => {
        headerRow += `<th>${header}</th>`;
    });

    headerRow += "</tr>";
    tableHead.innerHTML = headerRow;

    if (!rows || rows.length === 0) {
        noDataMessage.style.display = "block";
        return;
    }

    noDataMessage.style.display = "none";

    rows.forEach(row => {
        let tableRow = "<tr>";

        row.forEach(cell => {
            tableRow += formatTableCell(cell);
        });

        tableRow += "</tr>";
        tableBody.innerHTML += tableRow;
    });
}

function formatTableCell(cell) {
    const successStatus = [
        "Success",
        "Active",
        "Available",
        "Excellent",
        "Good",
        "Positive",
        "Completed",
        "Used",
        "Normal"
    ];

    const warningStatus = [
        "Low Stock",
        "Pending",
        "Ongoing",
        "Follow Up",
        "High",
        "Needs Improvement",
        "Neutral"
    ];

    const failedStatus = [
        "Failed",
        "Inactive",
        "Expired",
        "Cancelled",
        "Negative",
        "Critical",
        "Out of Stock"
    ];

    if (successStatus.includes(cell)) {
        return `<td><span class="status status-success">${cell}</span></td>`;
    }

    if (warningStatus.includes(cell)) {
        return `<td><span class="status status-warning">${cell}</span></td>`;
    }

    if (failedStatus.includes(cell)) {
        return `<td><span class="status status-failed">${cell}</span></td>`;
    }

    return `<td>${cell}</td>`;
}

function loadMainChart(labels, data, title) {
    const chartCanvas = document.getElementById("mainReportChart");

    if (!chartCanvas) {
        return;
    }

    const ctx = chartCanvas.getContext("2d");

    if (mainReportChart) {
        mainReportChart.destroy();
    }

    mainReportChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [
                {
                    label: title,
                    data: data,
                    backgroundColor: "#5b7fc1",
                    borderRadius: 5
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function loadStatusChart(labels, data) {
    const chartCanvas = document.getElementById("statusChart");

    if (!chartCanvas) {
        return;
    }

    const ctx = chartCanvas.getContext("2d");

    if (statusChart) {
        statusChart.destroy();
    }

    statusChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: labels,
            datasets: [
                {
                    data: data,
                    backgroundColor: [
                        "#5b7fc1",
                        "#f6c23e",
                        "#e74c3c",
                        "#1cc88a"
                    ]
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function searchTable(searchValue) {
    const tableRows = document.querySelectorAll("#tableBody tr");
    const noDataMessage = document.getElementById("noDataMessage");

    let found = false;
    const value = searchValue.toLowerCase().trim();

    tableRows.forEach(row => {
        const rowText = row.textContent.toLowerCase();

        if (rowText.includes(value)) {
            row.style.display = "";
            found = true;
        } else {
            row.style.display = "none";
        }
    });

    noDataMessage.style.display = found ? "none" : "block";
}

function exportToCSV() {
    const table = document.getElementById("reportsTable");
    const title = document.getElementById("tableTitle").textContent;
    let csv = [];

    for (let i = 0; i < table.rows.length; i++) {
        if (table.rows[i].style.display === "none") {
            continue;
        }

        let row = [];
        let cols = table.rows[i].querySelectorAll("td, th");

        for (let j = 0; j < cols.length; j++) {
            row.push('"' + cols[j].innerText.replace(/"/g, '""') + '"');
        }

        csv.push(row.join(","));
    }

    downloadFile(csv.join("\n"), createFileName(title, "csv"), "text/csv");
}

function exportToPDF() {
    const table = document.getElementById("reportsTable");
    const title = document.getElementById("tableTitle").textContent;

    if (!window.jspdf || !window.jspdf.jsPDF) {
        alert("PDF library is not loaded.");
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF("landscape");

    const headers = [];
    const rows = [];

    table.querySelectorAll("thead th").forEach(th => {
        headers.push(th.innerText.trim());
    });

    table.querySelectorAll("tbody tr").forEach(tr => {
        if (tr.style.display !== "none") {
            const row = [];

            tr.querySelectorAll("td").forEach(td => {
                row.push(td.innerText.trim());
            });

            rows.push(row);
        }
    });

    doc.setFontSize(16);
    doc.text(title, 14, 15);

    doc.setFontSize(10);
    doc.text("Generated Report", 14, 22);

    doc.autoTable({
        head: [headers],
        body: rows,
        startY: 28,
        styles: {
            fontSize: 9,
            cellPadding: 3
        },
        headStyles: {
            fillColor: [91, 127, 193],
            textColor: [255, 255, 255]
        }
    });

    doc.save(createFileName(title, "pdf"));
}

function createFileName(title, extension) {
    return title
        .toLowerCase()
        .replace(/ and /g, "-")
        .replace(/,/g, "")
        .replace(/\s+/g, "-")
        .replace(/[^a-z0-9-]/g, "") + "." + extension;
}

function downloadFile(content, fileName, fileType) {
    const blob = new Blob([content], { type: fileType });
    const link = document.createElement("a");

    link.href = URL.createObjectURL(blob);
    link.download = fileName;
    link.click();

    URL.revokeObjectURL(link.href);
}