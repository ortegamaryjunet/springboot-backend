<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Clinic Employee</title>
	
	<link rel="stylesheet" href="/css/adminEmployee.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-straight/css/uicons-regular-straight.css">
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

        <a href="/web/admin/employees" class="menu-item active">
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
            <h1>Clinic Employee</h1>
            <p>View dentists, assistants, and receptionist employee records.</p>
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

        <div class="employee-summary">
            <div class="card">
                <div class="card-icon">
                    <img src="/images/doctor.png" alt="Dentists">
                </div>
                <div class="card-text">
                    <h3>Dentists</h3>
                    <p id="totalDentists">0</p>
                </div>
            </div>

            <div class="card">
                <div class="card-icon">
                    <img src="/images/dental-assistant.png" alt="Dental Assistants">
                </div>
                <div class="card-text">
                    <h3>Dental Assistant</h3>
                    <p id="totalDentalAssistants">0</p>
                </div>
            </div>

            <div class="card">
                <div class="card-icon">
                    <img src="/images/receptionist.png" alt="Receptionists">
                </div>
                <div class="card-text">
                    <h3>Receptionists</h3>
                    <p id="totalReceptionists">0</p>
                </div>
            </div>
        </div>

        <div class="filter-card">
            <div class="search-box">
                <i class="fi fi-rr-search"></i>
                <input type="text" id="searchInput" placeholder="Search employee name or ID">
            </div>

            <div class="right-actions">
                <select id="roleFilter" class="role-filter">
                    <option value="">All Roles</option>
                    <option value="Dentist">Dentist</option>
                    <option value="Dental Assistant">Dental Assistant</option>
                    <option value="Receptionist">Receptionist</option>
                </select>
            </div>
        </div>

        <div class="table-card">
            <div class="table-header">
                <h3>Employee List</h3>
                <p>Manage and view clinic employee profiles.</p>
            </div>

            <div class="table-wrapper">
                <table class="employee-table" id="employeeTable">
                    <thead>
                        <tr>
                            <th>Employee ID</th>
                            <th>Clinic Position</th>
                            <th>Last Name</th>
                            <th>First Name</th>
                            <th>Middle Name</th>
                            <th>Age</th>
                            <th>Gender</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody id="employeeTableBody">
						<c:choose>
						    <c:when test="${not empty employees}">
						        <c:forEach var="e" items="${employees}">
						            <tr>
						                <td>${e.id}</td>
						                <td>${e.displayRole}</td>
						                <td>${e.lastName}</td>
						                <td>${e.firstName}</td>
						                <td>${e.middleName}</td>
						                <td>${e.age}</td>
						                <td>${e.gender}</td>
						                <td>
						                    <a href="#" class="view-btn"
						                       data-id="${e.id}"
						                       data-userid="${e.userId}"
						                       data-branch="${e.branchId}"
						                       data-role="${e.displayRole}"
						                       data-first="${e.firstName}"
						                       data-middle="${e.middleName}"
						                       data-last="${e.lastName}"
						                       data-email="${e.email}"
						                       data-phone="${e.phoneNumber}"
						                       data-gender="${e.gender}"
						                       data-dob="${e.dateOfBirth}"
						                       data-age="${e.age}"
						                       data-address="${e.address}"
						                       data-active="${e.isActive}"
						                       data-created="${e.createdAt}"
						                       data-updated="${e.updatedAt}">
						                       View
						                    </a>
						                </td>
						            </tr>
						        </c:forEach>
						    </c:when>
						
						    <c:otherwise>
						        <tr>
						            <td colspan="8" class="empty-row">No employees found.</td>
						        </tr>
						    </c:otherwise>
						</c:choose>
					</tbody>

                </table>
            </div>

            <div class="pagination">
                <button id="prevBtn" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-left"></i>
                </button>

                <span id="pageInfo"></span>

                <button id="nextBtn" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
        </div>

    </div>
</div>

<div id="employeeModal" class="modal">
    <div class="modal-content">
        <h2 class="modal-title">Employee Details</h2>

        <p><strong>Employee ID:</strong> <span id="modalEmployeeId"></span></p>
        <p><strong>User Account ID:</strong> <span id="modalUserId"></span></p>
        <p><strong>Branch ID:</strong> <span id="modalBranch"></span></p>
        <p><strong>Role:</strong> <span id="modalRole"></span></p>

        <hr>

        <p><strong>Full Name:</strong> <span id="modalFullName"></span></p>
        <p><strong>Email:</strong> <span id="modalEmail"></span></p>
        <p><strong>Phone:</strong> <span id="modalPhone"></span></p>

        <hr>

        <p><strong>Date of Birth:</strong> <span id="modalDob"></span></p>
        <p><strong>Age:</strong> <span id="modalAge"></span></p>
        <p><strong>Gender:</strong> <span id="modalGender"></span></p>
        <p><strong>Address:</strong> <span id="modalAddress"></span></p>
        <p><strong>Status:</strong> <span id="modalActive"></span></p>

        <hr>

        <p><strong>Created At:</strong> <span id="modalCreated"></span></p>
        <p><strong>Last Updated:</strong> <span id="modalUpdated"></span></p>

        <div class="modal-actions">
            <button id="closeEmployeeModal" type="button">Close</button>
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

<script src="/scripts/adminEmployee.js"></script>

</body>
</html>