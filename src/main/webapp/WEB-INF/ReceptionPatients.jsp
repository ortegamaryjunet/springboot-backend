<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Patients</title>
	<link rel="stylesheet" href="/css/recepPatients.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-solid-rounded/css/uicons-solid-rounded.css">
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

        <a href="/web/recepPatients" class="menu-item active">
            <i class="fi fi-rr-clipboard-user"></i>
            <span>Patient</span>
        </a>

        <a href="/web/recepPaccounts" class="menu-item">
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
            <h1>Patients</h1>
            <p>View and update patient information records.</p>
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

        <section class="dashboard-card">
            <div class="card-header">
                <div>
                    <h3>Patient List</h3>
                    <p>Search, filter, view, and update patient records.</p>
                </div>
            </div>

            <div class="filters">
                <div class="search-box">
                    <i class="fi fi-rr-search"></i>
                    <input type="text" id="searchInput" placeholder="Search patient name">
                </div>

                <select id="genderFilter" class="gender-filter">
                    <option value="all">All Patients</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
            </div>

            <div class="table-scroll">
                <table class="patient-table">
                    <thead>
                        <tr>
                            <th>Patient ID</th>
                            <th>Last Name</th>
                            <th>First Name</th>
                            <th>Middle Name</th>
                            <th>Age</th>
                            <th>Gender</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody id="patientTable">
                        <c:choose>
                            <c:when test="${empty patients}">
                                <tr class="empty-row">
                                    <td colspan="7">No patients found.</td>
                                </tr>
                            </c:when>

                            <c:otherwise>
                                <c:forEach var="patient" items="${patients}">
                                    <tr class="patient-row"
                                        data-info-id="${patient.id}"
                                        data-name="${patient.lastName} ${patient.firstName} ${patient.middleName}"
                                        data-gender="${patient.gender}"
                                        data-id="P${patient.id}"
                                        data-last-name="${patient.lastName}"
                                        data-first-name="${patient.firstName}"
                                        data-middle-name="${patient.middleName}"
                                        data-age="${patient.age}"
                                        data-dob="${patient.dateOfBirth}"
                                        data-email="${patient.email}"
                                        data-phone="${patient.phoneNumber}"
                                        data-address="${patient.address}"
                                        data-username="${patient.username}">

                                        <td>P${patient.id}</td>
                                        <td>${patient.lastName}</td>
                                        <td>${patient.firstName}</td>
                                        <td>${patient.middleName}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${empty patient.age}">-</c:when>
                                                <c:otherwise>${patient.age}</c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <span class="gender-badge">${patient.gender}</span>
                                        </td>

                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="action-btn view" onclick="openPatientDetails(this)" title="View">
                                                    <i class="fi fi-rr-eye"></i>
                                                </button>

                                                <button type="button" class="action-btn edit" onclick="openEditPatient(this)" title="Edit">
                                                    <i class="fi fi-rr-user-pen"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                        <tr id="noResultsRow" class="no-result-row" style="display:none;">
                            <td colspan="7">No matching patients found.</td>
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

<div id="patientDetailsModal" class="modal">
    <div class="modal-content large-modal">
        <div class="modal-header">
            <div>
                <h2>Patient Information</h2>
                <p>Complete patient profile details.</p>
            </div>

            <button type="button" class="modal-x" id="closePatientDetailsBtn">×</button>
        </div>

        <div class="details-grid">
            <div><label>Patient ID</label><p id="detailId">-</p></div>
            <div><label>Username</label><p id="detailUsername">-</p></div>
            <div><label>Last Name</label><p id="detailLastName">-</p></div>
            <div><label>First Name</label><p id="detailFirstName">-</p></div>
            <div><label>Middle Name</label><p id="detailMiddleName">-</p></div>
            <div><label>Date of Birth</label><p id="detailDob">-</p></div>
            <div><label>Age</label><p id="detailAge">-</p></div>
            <div><label>Gender</label><p id="detailGender">-</p></div>
            <div><label>Email</label><p id="detailEmail">-</p></div>
            <div><label>Phone Number</label><p id="detailPhone">-</p></div>
            <div class="full-row"><label>Address</label><p id="detailAddress">-</p></div>
        </div>

        <div class="modal-actions">
            <button type="button" id="closePatientDetailsBtn2">Close</button>
        </div>
    </div>
</div>

<div id="editPatientModal" class="modal">
    <form class="modal-content large-modal" method="post" action="/web/recepPatients/update">
        <div class="modal-header">
            <div>
                <h2>Edit Patient Information</h2>
                <p>Update the selected patient record.</p>
            </div>

            <button type="button" class="modal-x" id="closeEditPatientBtn">×</button>
        </div>

        <input type="hidden" id="editInfoId" name="infoId">

        <div class="form-grid">
            <div class="field">
                <label>Last Name</label>
                <input type="text" id="editLastName" name="lastName" required>
            </div>

            <div class="field">
                <label>First Name</label>
                <input type="text" id="editFirstName" name="firstName" required>
            </div>

            <div class="field">
                <label>Middle Name</label>
                <input type="text" id="editMiddleName" name="middleName">
            </div>

            <div class="field">
                <label>Date of Birth</label>
                <input type="date" id="editDob" name="dateOfBirth">
            </div>

            <div class="field">
                <label>Gender</label>
                <select id="editGender" name="gender">
                    <option value="">Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>

            <div class="field">
                <label>Email</label>
                <input type="email" id="editEmail" name="email">
            </div>

            <div class="field">
                <label>Phone Number</label>
                <input type="text" id="editPhone" name="phoneNumber">
            </div>

            <div class="field full-row">
                <label>Address</label>
                <input type="text" id="editAddress" name="address">
            </div>
        </div>

        <div class="modal-actions">
        	<button type="submit" class="save-btn">Save Changes</button>
            <button type="button" id="closeEditPatientBtn2">Cancel</button>
        </div>
    </form>
</div>

<div id="logoutModal" class="modal">
    <div class="modal-content small-modal">
        <div class="modal-icon">
            <img src="/images/logout-modal.png" alt="warning">
        </div>

        <h2 class="modal-title">Confirm Logout</h2>
        <p>Are you sure you want to log out?</p>

        <div class="modal-actions center">
        	<button type="button" id="logoutBtn">Logout</button>
            <button type="button" id="cancelBtn">Cancel</button>
        </div>
    </div>
</div>

<script src="/scripts/recepPatients.js"></script>

</body>
</html>