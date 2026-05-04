<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Patients</title>

<link rel="stylesheet" href="/css/adminPatients.css">

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

        <a href="/web/admin/patients" class="menu-item active">
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
            <h1>Patients</h1>
            <p>View and manage patient records.</p>
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
            <div class="search-box">
                <i class="fi fi-rr-search"></i>
                <input type="text" id="searchInput" placeholder="Search patient name or ID">
            </div>

            <div class="right-actions">
                <select id="genderFilter" class="gender-filter">
                    <option value="all">All Gender</option>
                    <option value="female">Female</option>
                    <option value="male">Male</option>
                </select>
            </div>
        </div>

        <div class="table-card">
            <div class="table-header">
                <h3>Patient List</h3>
                <p>Manage and view patient profiles.</p>
            </div>

            <div class="table-wrapper">
                <table class="patient-table">
                    <thead>
                        <tr>
                            <th>Patient ID</th>
                            <th>Last Name</th>
                            <th>First Name</th>
                            <th>Middle Name</th>
                            <th>Age</th>
                            <th>Gender</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody id="patientTable">

					<c:choose>
					    <c:when test="${not empty patients}">
					        <c:forEach var="p" items="${patients}">
					            <tr>
					                <td>${p.id}</td>
					                <td>${p.lastName}</td>
					                <td>${p.firstName}</td>
					                <td>${p.middleName}</td>
					                <td>${p.age}</td>
					                <td>${p.gender}</td>
					                <td>
					                    <a href="#" class="view-btn"
										   data-name="${p.firstName} ${p.lastName}"
										   data-email="${p.email}"
										   data-phone="${p.phoneNumber}"
										   data-dob="${p.dateOfBirth}"
										   data-gender="${p.gender}"
										   data-address="${p.address}"
										   data-username="${p.username}"
										   data-created="${p.createAt}"
										   data-updated="${p.updatedAt}">
										   View
										</a>
					                </td>
					            </tr>
					        </c:forEach>
					    </c:when>
					
					    <c:otherwise>
					        <tr>
					            <td colspan="7" class="empty-row">No patients found.</td>
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

<div id="patientModal" class="modal">
    <div class="modal-content">

        <h2 class="modal-title">Patient Details</h2>

        <p><strong>Full Name:</strong> <span id="modalName"></span></p>
        <p><strong>Username:</strong> <span id="modalUsername"></span></p>
        <p><strong>Email:</strong> <span id="modalEmail"></span></p>
        <p><strong>Phone:</strong> <span id="modalPhone"></span></p>

        <p><strong>Date of Birth:</strong> <span id="modalDob"></span></p>
        <p><strong>Gender:</strong> <span id="modalGender"></span></p>
        <p><strong>Address:</strong> <span id="modalAddress"></span></p>

        <p><strong>Created At:</strong> <span id="modalCreated"></span></p>
        <p><strong>Last Updated:</strong> <span id="modalUpdated"></span></p>

        <div class="modal-actions">
            <button id="closePatientModal">Close</button>
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

<script src="/scripts/adminPatients.js"></script>

</body>
</html>