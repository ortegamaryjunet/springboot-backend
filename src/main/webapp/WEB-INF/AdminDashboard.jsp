<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
    
    <link rel="stylesheet" href="/css/adminDashboard.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
</head>
<body>

<div class="sidebar">
    <div class="menu">
        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="up-divider"></div>

        <a href="/web/admin/dashboard" class="menu-item active">
            <i class="fi fi-rr-apps"></i>
            <span>Dashboard</span>
        </a>

        <a href="/web/admin/patients" class="menu-item">
            <i class="fi fi-rr-clipboard-user"></i>
            <span>Patients</span>
        </a>

        <a href="/web/admin/employees" class="menu-item">
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

        <a href="/web/admin/report" class="menu-item">
            <i class="fi fi-rr-document-signed"></i>
            <span>Reports</span>
        </a>
    </div>
</div>

<div class="main-container">
	<div class="top-header">
	    <div class="page-title">
	        <h1>Dashboard</h1>
	        <p>Monitor clinic performance, inventory, and patient activity.</p>
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
		<div class="dashboard-hero">
		    <div>
		        <span class="hero-badge">Overview</span>
		        <h2>Keep track of clinic performance, inventory, and reports.</h2>
		        <p>Monitor patients, appointments, employee records, revenue, and clinic activity in one clean dashboard.</p>
		    </div>
		
		    <div class="hero-icon">
		        <i class="fi fi-rr-chart-histogram"></i>
		    </div>
		</div>
		
        <div class="summary-grid">
            <div class="summary-card">
                <div class="summary-icon blue">
                    <img src="/images/patients.png" alt="Patients">
                </div>
                <div>
                    <p>Total Patients</p>
                    <h2>${totalPatients}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon green">
                    <img src="/images/appt.png" alt="Appointments">
                </div>
                <div>
                    <p>Total Appointments</p>
                    <h2>${totalAppointments}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon purple">
                    <img src="/images/clinic-staff.png" alt="Employees">
                </div>
                <div>
                    <p>Total Employees</p>
                    <h2 id="totalEmployee">${totalUsers}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon orange">
                    <img src="/images/revenue.png" alt="Revenue">
                </div>
                <div>
                    <p>Revenue</p>
                    <h2>₱ 0</h2>
                </div>
            </div>
        </div>

        <div class="dashboard-grid">

            <div class="dashboard-card patient-visit-card">
                <div class="card-header">
                    <div>
                        <h3>Patient Visit</h3>
                        <p>Weekly patient visits by selected month and year.</p>
                    </div>

                    <div class="date-filter">
                        <select id="visitMonth">
                            <option value="0">January</option>
                            <option value="1">February</option>
                            <option value="2">March</option>
                            <option value="3">April</option>
                            <option value="4">May</option>
                            <option value="5">June</option>
                            <option value="6">July</option>
                            <option value="7">August</option>
                            <option value="8">September</option>
                            <option value="9">October</option>
                            <option value="10">November</option>
                            <option value="11">December</option>
                        </select>

                        <select id="visitYear">
                            <option value="2024">2024</option>
                            <option value="2025">2025</option>
                            <option value="2026" selected>2026</option>
                            <option value="2027">2027</option>
                            <option value="2028">2028</option>
                            <option value="2029">2029</option>
                            <option value="2030">2030</option>
                        </select>
                    </div>
                </div>

                <div class="chart-box large-chart">
                    <canvas id="visitChart"></canvas>
                </div>
            </div>

            <div class="dashboard-card stock-card">
                <div class="card-header">
                    <div>
                        <h3>Stock Availability</h3>
                        <p>Inventory stock summary.</p>
                    </div>
                    <a href="#" id="openStockModal">View All</a>
                </div>

                <div class="stock-numbers">
                    <div>
                        <p>Total Asset Value</p>
                        <h4>₱ 0</h4>
                    </div>

                    <div>
                        <p>Total Product</p>
                        <h4>${totalProducts}</h4>
                    </div>
                </div>

                <div class="stock-bar">
                    <div class="in-stock"></div>
                    <div class="low-stock"></div>
                    <div class="out-stock"></div>
                </div>

                <div class="stock-labels">
                    <span>In Stock</span>
                    <span>Low Stock</span>
                    <span>Out of Stock</span>
                </div>
            </div>

            <div class="dashboard-card income-card">
                <div class="card-header">
                    <div>
                        <h3>Income & Expenses</h3>
                        <p>Monthly financial overview.</p>
                    </div>

                    <select id="incomeYear">
                        <option value="2024">2024</option>
                        <option value="2025">2025</option>
                        <option value="2026" selected>2026</option>
                        <option value="2027">2027</option>
                        <option value="2028">2028</option>
                        <option value="2029">2029</option>
                        <option value="2030">2030</option>
                    </select>
                </div>

                <div class="chart-box">
                    <canvas id="incomeChart"></canvas>
                </div>
            </div>

            <div class="dashboard-card expense-card">
                <div class="card-header">
                    <div>
                        <h3>Expenses</h3>
                        <p>Expense category breakdown.</p>
                    </div>
                </div>

                <div class="chart-box">
                    <canvas id="expenseChart"></canvas>
                </div>
            </div>

            <div class="dashboard-card activity-card">
                <div class="card-header">
                    <div>
                        <h3>Patient Feedback</h3>
                        <p>Latest patient feedback and ratings.</p>
                    </div>
                    <a href="/adminReport">View Reports</a>
                </div>

                <div class="activity-list">
                    <div class="activity-item">
                        <span class="dot success"></span>
                        <div>
                            <p>Maria Santos rated 5 stars</p>
                            <small>Excellent service and friendly staff.</small>
                        </div>
                    </div>

                    <div class="activity-item">
                        <span class="dot warning"></span>
                        <div>
                            <p>Juan Dela Cruz rated 4 stars</p>
                            <small>Good experience, but waiting time can improve.</small>
                        </div>
                    </div>

                    <div class="activity-item">
                        <span class="dot failed"></span>
                        <div>
                            <p>Ana Reyes rated 3 stars</p>
                            <small>Service was okay, but appointment was delayed.</small>
                        </div>
                    </div>
                </div>
            </div>

            <div class="dashboard-card rating-card">
                <div class="card-header">
                    <div>
                        <h3>Patient Satisfaction</h3>
                        <p>Average clinic rating.</p>
                    </div>
                </div>

                <div class="rating-box">
                    <h2>4.8</h2>
                    <p>Average Rating</p>
                </div>

                <div class="rating-bars">
                    <div class="rating-row">
                        <span>5</span>
                        <div class="bar"><div class="fill" style="width:80%"></div></div>
                    </div>
                    <div class="rating-row">
                        <span>4</span>
                        <div class="bar"><div class="fill" style="width:15%"></div></div>
                    </div>
                    <div class="rating-row">
                        <span>3</span>
                        <div class="bar"><div class="fill" style="width:3%"></div></div>
                    </div>
                    <div class="rating-row">
                        <span>2</span>
                        <div class="bar"><div class="fill" style="width:1%"></div></div>
                    </div>
                    <div class="rating-row">
                        <span>1</span>
                        <div class="bar"><div class="fill" style="width:1%"></div></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div id="stockModal" class="modal">
    <div class="stock-modal-content">

        <div class="stock-modal-header">
            <div>
                <h2>Stock Summary</h2>
                <p>Overview of medicines, supplies, and equipment inventory.</p>
            </div>

            <button type="button" id="closeStockModal" class="stock-close-btn">×</button>
        </div>

        <div class="stock-summary-cards">
            <div class="stock-summary-box blue">
                <p>Total Items</p>
                <h3>${totalProducts}</h3>
                <span>All inventory</span>
            </div>

            <div class="stock-summary-box orange">
                <p>Low Stock</p>
                <h3>${lowStock}</h3>
                <span>Needs reorder</span>
            </div>

            <div class="stock-summary-box red">
                <p>Out of Stock</p>
                <h3>${outStock}</h3>
                <span>Urgent</span>
            </div>
        </div>

        <div class="stock-table-wrapper">
            <h3>Medicine</h3>
            <table class="stock-table">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Reorder Level</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${medicines}">
                        <tr>
                            <td>${m.medicineName}</td>
                            <td>${m.category}</td>
                            <td>${m.quantity}</td>
                            <td>${m.lowStockThreshold}</td>
                            <td>${m.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h3>Supplies</h3>
            <table class="stock-table">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Reorder Level</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${supplies}">
                        <tr>
                            <td>${s.supplyName}</td>
                            <td>${s.category}</td>
                            <td>${s.quantity}</td>
                            <td>${s.lowStockThreshold}</td>
                            <td>${s.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h3>Equipment</h3>
            <table class="stock-table">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Reorder Level</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="e" items="${equipmentList}">
                        <tr>
                            <td>${e.equipmentName}</td>
                            <td>${e.category}</td>
                            <td>${e.quantity}</td>
                            <td>${e.lowStockThreshold}</td>
                            <td>${e.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
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

<script>
    const stockData = {
        inStock: ${inStock != null ? inStock : 0},
        lowStock: ${lowStock != null ? lowStock : 0},
        outStock: ${outStock != null ? outStock : 0}
    };
</script>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/scripts/adminDashboard.js"></script>

</body>
</html>