<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Admin Reports</title>
	
	<link rel="stylesheet" href="/css/adminReport.css">
	
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
	
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.8.2/jspdf.plugin.autotable.min.js"></script>
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="/images/clinic-logo.png" alt="Clinic Logo">
    </div>

    <div class="menu">
        <a href="/web/admin/dashboard" class="menu-item">
            <i class="fi fi-rr-apps"></i>
            <span>Dashboard</span>
        </a>

        <a href="/web/admin/patients" class="menu-item">
            <i class="fi fi-rr-clipboard-user"></i>
            <span>Patients</span>
        </a>

        <a href=/web/admin/employees class="menu-item">
            <i class="fi fi-rr-stethoscope"></i>
            <span>Clinic Employee</span>
        </a>

        <a href="/web/admin/inventory" class="menu-item">
            <i class="fi fi-rr-boxes"></i>
            <span>Inventory</span>
        </a>

        <a href="/web/admin/audit" class="menu-item">
            <i class="fi fi-rr-clipboard-list"></i>
            <span>Audit Logs</span>
        </a>

        <a href="/web/admin/report" class="menu-item active">
            <i class="fi fi-rr-document-signed"></i>
            <span>Reports</span>
        </a>
    </div>
</div>

<div class="main-container">

    <div class="top-header">
        <div class="page-title">
            <h1>Reports</h1>
            <p>Generate clinic reports, records, charts, PDF, and CSV files.</p>
        </div>

        <div class="header-actions">
            <div class="notification-icon">
                <i class="fi fi-rr-bell"></i>
                <span class="badge" id="notificationCount">0</span>
            </div>

            <div class="admin-profile" id="adminProfile">
                <div class="avatar">
                    <i class="fi fi-rr-user"></i>
                </div>

                <div class="admin-info">
                    <div class="name">Admin Full Name</div>
                    <div class="position">Admin</div>
                </div>

                <i class="fi fi-rr-angle-small-down arrow"></i>

                <div class="profile-dropdown" id="profileDropdown">
                    <a href="/web/admin/settings" class="dropdown-item">
                        <i class="fi fi-rr-settings"></i>
                        <span>Settings</span>
                    </a>

                    <div class="dropdown-divider"></div>

                    <a href="#" class="dropdown-item" id="logoutLink">
                        <i class="fi fi-rr-sign-out-alt"></i>
                        <span>Logout</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="main-content">

        <div class="filter-card">
            <div class="filter-group report-type">
                <label>Report Type</label>
                <select id="reportType">
                    <option value="clinicDentist">Clinic and Dentist Performance</option>
                    <option value="satisfaction">Patient Satisfaction Ratings and Feedback</option>
                    <option value="revenue">Revenue, Income, and Expense</option>
                    <option value="visits">Patient Visit</option>
                    <option value="treatments">Patient Treatment</option>
                    <option value="stockAvailability">Stock Items Availability</option>
                    <option value="consumption">Monthly and Quarterly Consumption</option>
                    <option value="inventoryUsage">Inventory Usage</option>
                    <option value="audit">Audit Trails and Activity Logs</option>
                </select>
            </div>

            <div class="filter-group">
                <label>From</label>
                <input type="date" id="fromDate">
            </div>

            <div class="filter-group">
                <label>To</label>
                <input type="date" id="toDate">
            </div>

            <button class="filter-btn" id="applyFilter" type="button">
                Apply Filter
            </button>
        </div>

        <div class="summary-grid">
            <div class="summary-card">
                <div class="summary-icon blue">
                    <i class="fi fi-rr-document"></i>
                </div>
                <div>
                    <p>Total Records</p>
                    <h2 id="totalRecords">0</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon green">
                    <i class="fi fi-rr-check-circle"></i>
                </div>
                <div>
                    <p>Active Data</p>
                    <h2 id="activeData">0</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon yellow">
                    <i class="fi fi-rr-calendar"></i>
                </div>
                <div>
                    <p>This Month</p>
                    <h2 id="thisMonth">0</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon red">
                    <i class="fi fi-rr-exclamation"></i>
                </div>
                <div>
                    <p>Needs Attention</p>
                    <h2 id="attention">0</h2>
                </div>
            </div>
        </div>

        <div class="charts-grid">
            <div class="report-card chart-large">
                <div class="card-header">
                    <div>
                        <h3 id="mainChartTitle">Clinic and Dentist Performance Overview</h3>
                        <p>Selected report chart overview.</p>
                    </div>
                </div>

                <div class="chart-box">
                    <canvas id="mainReportChart"></canvas>
                </div>
            </div>

            <div class="report-card">
                <div class="card-header">
                    <div>
                        <h3 id="statusChartTitle">Performance Status</h3>
                        <p>Status breakdown.</p>
                    </div>
                </div>

                <div class="chart-box small-chart">
                    <canvas id="statusChart"></canvas>
                </div>
            </div>
        </div>

        <div class="report-card table-card">
            <div class="table-header">
                <div>
                    <h3 id="tableTitle">Clinic and Dentist Performance Reports</h3>
                    <p>View, search, and export report records.</p>
                </div>

                <div class="table-actions">
                    <div class="table-search">
                        <i class="fi fi-rr-search"></i>
                        <input type="text" id="searchInput" placeholder="Search records">
                    </div>

                    <button class="export-btn csv" id="exportCSV" type="button">
                        <i class="fi fi-rr-file"></i>
                        CSV
                    </button>

                    <button class="export-btn pdf" id="exportPDF" type="button">
                        <i class="fi fi-rr-file-pdf"></i>
                        PDF
                    </button>
                </div>
            </div>

            <div class="table-container">
                <table id="reportsTable">
                    <thead id="tableHead"></thead>
                    <tbody id="tableBody"></tbody>
                </table>

                <div id="noDataMessage" class="no-data">No records found.</div>
            </div>
        </div>

    </div>
</div>

<div id="logoutModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon">
            <img src="/images/logout-modal.png" alt="warning">
        </div>

        <h2 class="modal-title">Confirm Logout</h2>
        <p>Are you sure you want to log out?</p>

        <div class="modal-actions">
            <button id="logoutBtn">Logout</button>
            <button id="cancelBtn">Cancel</button>
        </div>
    </div>
</div>

<script src="/scripts/adminReport.js"></script>

</body>
</html>