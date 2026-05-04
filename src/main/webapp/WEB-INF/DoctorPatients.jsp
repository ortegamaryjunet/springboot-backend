<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <link rel="stylesheet" href="/css/doctorPatients.css">
    
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-straight/css/uicons-regular-straight.css">
</head>
<body>

    <div class="sidebar">
        <div class="menu">
            <div class="logo">
                <img src="/images/clinic-logo.png" alt="Clinic Logo">
            </div>
            <div class="up-divider"></div>
            <a href="/doctorDashboard" class="menu-item">
                <i class="fi fi-rr-apps"></i>
                <span>Dashboard</span>
            </a>
            <a href="/doctorAppointment" class="menu-item">
                <i class="fi fi-rr-calendar-clock"></i>
                <span>Appointment</span>
            </a>
            <a href="/doctorPatient" class="menu-item active">
                <i class="fi fi-rr-clipboard-user"></i>
                <span>Patients</span>
            </a>
        </div>
    </div>

    <div class="main-container">
        <div class="top-header">
            <div class="profile-icons-wrapper">
                <div class="icons-wrapper">
                    <div class="icon-container message">
                        <a href="/doctorMessage">
                            <img src="/images/message-icon.png" alt="Messages" class="icon">
                            <span class="badge" id="messageCount">0</span>
                        </a>
                    </div>
                </div>
                <div class="doctor-profile" id="doctorProfile">
                    <div class="avatar"></div>
                    <div class="doctor-info">
                        <div class="name"><c:out value="${dentistName}"/></div>
                        <div class="specialization"><c:out value="${dentistSpecialization}"/></div>
                    </div>
                    <img src="/images/arrow-down.png" class="dropdown-arrow" alt="menu">
                    <div class="profile-dropdown" id="profileDropdown">
                        <a href="/doctorProfile" class="dropdown-item">
                            <i class="fi fi-rr-id-badge"></i>
                            <span>View Profile</span>
                        </a>
                        <div class="pr-divider"></div>
                        <a href="" class="dropdown-item" id="openLogoutModal">
                            <i class="fi fi-rr-sign-out-alt"></i>
                            <span>Logout</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="main-content">
            <div class="filters">
                <div class="search-box">
                    <img src="/images/search-icon.png" class="search-icon" alt="Search">
                    <input type="text" id="searchInput" placeholder="Search patient name">
                </div>
                <div class="dropdown-container">
                    <input type="date" id="dateFilter">
                    <select class="sort-dropdown" id="nameFilter">
                        <option value="most-recent" selected>Most Recent</option>
                        <option value="a-z">Name A-Z</option>
                        <option value="z-a">Name Z-A</option>
                    </select>
                    <select class="sort-dropdown" id="treatmentFilter">
                        <option value="">All Treatment</option>
                        <option value="Complete Partial Denture">Complete Partial Denture</option>
                        <option value="Dental Implants">Dental Implants</option>
                        <option value="Invisalign">Invisalign</option>
                        <option value="Orthodontic">Orthodontic</option>
                        <option value="Porcelain Jacket Crowns">Porcelain Jacket Crowns</option>
                        <option value="Removable Partial Denture">Removable Partial Denture</option>
                        <option value="Smile Make-Overs">Smile Make-Overs</option>
                        <option value="Teeth Whitening">Teeth Whitening</option>
                        <option value="Veneers">Veneers</option>
                    </select>
                </div>
            </div>

            <div class="table-container">
                <table id="patientTable">
                    <thead>
                        <tr>
                            <th class="id-column">ID</th>
                            <th class="last-name-column">Last Name</th>
                            <th class="first-name-column">First Name</th>
                            <th class="middle-name-column">Middle Name</th>
                            <th class="contact-column">Contact Number</th>
                            <th class="last-visit-column">Last Visit</th>
                            <th class="last-treatment-column">Last Treatment</th>
                            <th class="actions-column"></th>
                        </tr>
                    </thead>
                    <tbody id="tableBody"></tbody>
                </table>

                <div class="pagination">
                    <button id="prevBtn" class="page-btn">
                        <i class="fi fi-rr-angle-left"></i>
                    </button>
                    <span id="pageInfo"></span>
                    <button id="nextBtn" class="page-btn">
                        <i class="fi fi-rr-angle-right"></i>
                    </button>
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

    <%-- Inject real DB patients as JS array for doctorPatients.js --%>
    <script>
        const patientsData = [
            <c:forEach var="p" items="${patients}" varStatus="loop">
            {
                id:            ${p.id},
                lastName:      "<c:out value='${p.lastName}'/>",
                firstName:     "<c:out value='${p.firstName}'/>",
                middleName:    "<c:out value='${p.middleName}'/>",
                contact:       "<c:out value='${p.phoneNumber}'/>",
                lastVisit:     "<c:out value='${p.lastVisit}'/>",
                lastTreatment: "<c:out value='${p.lastTreatment}'/>"
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];
    </script>

<script src="/scripts/doctorPatients.js"></script>

</body>
</html>