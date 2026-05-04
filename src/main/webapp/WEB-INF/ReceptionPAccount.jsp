<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Patient Account</title>
	<link rel="stylesheet" href="/css/recepPAccount.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="/images/clinic-logo.png" alt="Clinic Logo">
    </div>

    <div class="menu">
        <a href="/web/recepDashboard" class="menu-item">
            <i class="fi fi-rr-apps"></i>
            <span>Dashboard</span>
        </a>

        <a href="/web/recepAppointment" class="menu-item">
            <i class="fi fi-rr-calendar-clock"></i>
            <span>Appointment</span>
        </a>

        <a href="/web/recepPatients" class="menu-item">
            <i class="fi fi-rr-clipboard-user"></i>
            <span>Patient</span>
        </a>

        <a href="/web/recepPAccount" class="menu-item active">
            <i class="fi fi-rr-id-badge"></i>
            <span>Patient Account</span>
        </a>

        <a href="/web/recepInventory" class="menu-item">
            <i class="fi fi-rr-boxes"></i>
            <span>Inventory</span>
        </a>
    </div>
</div>

<div class="main-container">

    <div class="top-header">
        <div class="page-title">
            <h1>Patient Account</h1>
            <p>Manage patient login accounts, account status, and records.</p>
        </div>

        <div class="profile-icons-wrapper">
            <a href="/web/receptionMessages" class="message-icon">
                <img src="/images/message-icon.png" alt="Messages">
                <span class="badge" id="messageCount" style="display:none;">0</span>
            </a>

            <div class="recept-profile" id="receptProfile">
                <div class="avatar">
                    <i class="fi fi-rr-user"></i>
                </div>

                <div class="recept-info">
                    <div class="name">Recept Name</div>
                    <div class="position">Receptionist</div>
                </div>

                <i class="fi fi-rr-angle-small-down arrow"></i>

                <div class="profile-dropdown" id="profileDropdown">
                    <a href="#" class="dropdown-item" id="logoutLink">
                        <i class="fi fi-rr-sign-out-alt"></i>
                        <span>Logout</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <main class="main-content">

        <section class="summary-grid">
            <div class="summary-card">
                <div class="summary-icon blue">
                    <i class="fi fi-rr-users"></i>
                </div>

                <div>
                    <p>Total Patient Accounts</p>
                    <h2>${totalPatientAccounts}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon green">
                    <i class="fi fi-rr-user-check"></i>
                </div>

                <div>
                    <p>Active Patients</p>
                    <h2>${activePatients}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon orange">
                    <i class="fi fi-rr-user-xmark"></i>
                </div>

                <div>
                    <p>Inactive Patients</p>
                    <h2>${inactivePatients}</h2>
                </div>
            </div>
        </section>

        <section class="dashboard-card">
            <div class="card-header">
                <div>
                    <h3>Patient Account List</h3>
                    <p>View, search, filter, and update patient account information.</p>
                </div>
            </div>

			<div class="filters">
			    <div class="left-actions">
			        <div class="search-box">
			            <i class="fi fi-rr-search"></i>
			            <input type="text" id="searchInput" placeholder="Search patient name or username">
			        </div>
			    </div>
			
			    <div class="right-actions">
			        <select id="statusFilter" class="patient-filter">
			            <option value="all">All Patients</option>
			            <option value="active">Active Patients</option>
			            <option value="inactive">Inactive Patients</option>
			        </select>
			
			        <button type="button" class="add-account-btn" id="openCreateBtn">
			            <i class="fi fi-rr-plus"></i>
			            <span>Add Account</span>
			        </button>
			    </div>
			</div>
			
            <div class="table-scroll">
                <table class="account-table">
                    <thead>
                        <tr>
                            <th>Patient ID</th>
                            <th>Patient Name</th>
                            <th>Username</th>
                            <th>Date Registered</th>
                            <th>Date Deactivate</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody id="patientAccountTable">
                        <c:choose>
                            <c:when test="${not empty patientAccounts}">
                                <c:forEach var="patient" items="${patientAccounts}">
                                    <tr class="patient-row"
                                        data-info-id="${patient.id}"
                                        data-user-id="${patient.userModel.id}"
                                        data-first-name="${patient.firstName}"
                                        data-middle-name="${patient.middleName}"
                                        data-last-name="${patient.lastName}"
                                        data-name="${patient.firstName} ${patient.middleName} ${patient.lastName}"
                                        data-username="${patient.username}"
                                        data-registered="${patient.userModel.createdAt}"
                                        data-deactivated="${patient.userModel.deactivatedAt}"
                                        data-status="${empty patient.userModel.deactivatedAt ? 'ACTIVE' : 'INACTIVE'}">

                                        <td>P${patient.id}</td>

                                        <td class="patient-name">
                                            ${patient.firstName} ${patient.middleName} ${patient.lastName}
                                        </td>

                                        <td class="patient-username">${patient.username}</td>
                                        <td>${patient.userModel.createdAt}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${empty patient.userModel.deactivatedAt}">-</c:when>
                                                <c:otherwise>${patient.userModel.deactivatedAt}</c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${empty patient.userModel.deactivatedAt}">
                                                    <span class="status active">
                                                        <i class="fi fi-rr-check-circle"></i>
                                                        Active
                                                    </span>
                                                </c:when>

                                                <c:otherwise>
                                                    <span class="status inactive">
                                                        <i class="fi fi-rr-cross-circle"></i>
                                                        Inactive
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <button type="button" class="edit-btn" onclick="openPatientOverlayFromRow(this)">
                                                <i class="fi fi-rr-user-pen"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <tr class="empty-row">
                                    <td colspan="7">No patient account found.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

                        <tr id="noResultsRow" class="no-result-row" style="display:none;">
                            <td colspan="7">No matching patient account found.</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <button type="button" id="prevBtn" class="page-btn" disabled>
                    <i class="fi fi-rr-angle-left"></i>
                </button>

                <span id="pageInfo">Page 0 of 0</span>

                <button type="button" id="nextBtn" class="page-btn next" disabled>
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
        </section>

    </main>
</div>

<div id="patientOverlay" class="overlay">
    <div class="overlay-content">
        <div class="overlay-header">
            <div>
                <h3>Update Patient Account</h3>
                <p>Edit patient account details and status.</p>
            </div>

            <button type="button" class="close-btn" onclick="closePatientOverlay()">&times;</button>
        </div>

        <div class="overlay-body">
            <form method="post" action="/web/recePaccount/update" id="updatePatientForm">
                <input type="hidden" id="ovInfoId" name="infoId">
                <input type="hidden" id="ovUserId" name="userId">

                <div class="form-grid">
                    <div class="field">
                        <label for="ovFirstName">First Name</label>
                        <input type="text" id="ovFirstName" name="firstName" required>
                    </div>

                    <div class="field">
                        <label for="ovMiddleName">Middle Name</label>
                        <input type="text" id="ovMiddleName" name="middleName">
                    </div>

                    <div class="field">
                        <label for="ovLastName">Last Name</label>
                        <input type="text" id="ovLastName" name="lastName" required>
                    </div>

                    <div class="field">
                        <label for="ovUsername">Username</label>
                        <input type="text" id="ovUsername" name="username" required>
                    </div>

                    <div class="field">
                        <label for="ovRegistered">Date Registered</label>
                        <input type="text" id="ovRegistered" readonly>
                    </div>

                    <div class="field">
                        <label for="ovStatus">Status</label>
                        <select id="ovStatus" name="status" required>
                            <option value="ACTIVE">Active</option>
                            <option value="INACTIVE">Inactive</option>
                        </select>
                    </div>
                </div>

                <div class="overlay-actions">
                    <button type="button" class="cancel-overlay-btn" onclick="closePatientOverlay()">Cancel</button>
                    <button type="submit" class="submit-btn">Update</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="createOverlay" class="overlay">
    <div class="overlay-content">
        <div class="overlay-header">
            <div>
                <h3>Create Patient Account</h3>
                <p>Add a new login account for a patient.</p>
            </div>

            <button type="button" class="close-btn" onclick="closeCreateOverlay()">&times;</button>
        </div>

        <div class="overlay-body">
            <c:if test="${not empty createErrorMessage}">
                <div class="modal-error-alert">
                    ${createErrorMessage}
                </div>
            </c:if>

            <form method="post" action="/web/recepPaccounts/create" id="createPatientForm">
                <div class="form-grid">
                    <div class="field">
                        <label for="newFirstName">First Name</label>
                        <input type="text" id="newFirstName" name="firstName" value="${oldFirstName}" required>
                    </div>

                    <div class="field">
                        <label for="newMiddleName">Middle Name</label>
                        <input type="text" id="newMiddleName" name="middleName" value="${oldMiddleName}">
                    </div>

                    <div class="field">
                        <label for="newLastName">Last Name</label>
                        <input type="text" id="newLastName" name="lastName" value="${oldLastName}" required>
                    </div>

                    <div class="field">
                        <label for="newEmail">Email</label>
                        <input type="email" id="newEmail" name="email" value="${oldEmail}" required>
                    </div>

                    <div class="field">
                        <label for="newUsername">Username</label>
                        <input type="text" id="newUsername" name="username" value="${oldUsername}" required>
                    </div>

                    <div class="field">
                        <label for="newPassword">Temporary Password</label>
                        <input type="password" id="newPassword" name="password" required>
                    </div>
                </div>

                <div class="overlay-actions">
                    <button type="button" class="cancel-overlay-btn" onclick="closeCreateOverlay()">Cancel</button>
                    <button type="submit" class="submit-btn">Create Account</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="logoutModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon">
            <i class="fi fi-rr-sign-out-alt"></i>
        </div>

        <h2 class="modal-title">Confirm Logout</h2>
        <p>Are you sure you want to log out?</p>

        <div class="modal-actions">
            <button type="button" id="cancelBtn">Cancel</button>
            <button type="button" id="logoutBtn">Logout</button>
        </div>
    </div>
</div>

<c:if test="${openCreateModal}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            document.getElementById("createOverlay").style.display = "flex";
        });
    </script>
</c:if>

<script src="/scripts/recepPAccount.js"></script>
    
</body>
</html>