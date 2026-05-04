<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Appointment</title>
	<link rel="stylesheet" href="/css/recepAppt.css">
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

        <a href="/web/recepAppointment" class="menu-item active">
            <i class="fi fi-rr-calendar-clock"></i>
            <span>Appointment</span>
        </a>

        <a href="/web/recepPatients" class="menu-item">
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
            <h1>Appointment</h1>
            <p>Manage appointment queue, dentist schedule, and walk-in booking.</p>
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

        <div class="view-tabs">
            <button id="calendarBtn" type="button">
                <i class="fi fi-rr-calendar"></i>
                Calendar View
            </button>

            <button id="queueBtn" type="button" class="active">
                <i class="fi fi-rr-list-check"></i>
                Queue View
            </button>
        </div>

        <section id="calendarView" class="calendar-view" style="display:none;">
            <div class="dashboard-card">
                <div class="card-header">
                    <div>
                        <h3>Calendar View</h3>
                        <p>Calendar and dentist schedule can be placed here.</p>
                    </div>
                </div>

                <div class="calendar-placeholder">
                    <i class="fi fi-rr-calendar-clock"></i>
                    <h4>Calendar View</h4>
                    <p>Connect your calendar component and dentist list here.</p>
                </div>
            </div>
        </section>

        <section id="queueView" class="queue-wrapper">

            <div class="dashboard-card">
                <div class="card-header">
                    <div>
                        <h3>Appointment Queue</h3>
                        <p>Search and filter pending appointments from the database.</p>
                    </div>
                </div>

                <div class="filters">
	                <div class="left-actions">
	                    <div class="search-box">
	                        <i class="fi fi-rr-search"></i>
	                        <input type="text" id="searchInput" placeholder="Search patient or doctor">
	                    </div>
	                </div>

                    <div class="right-actions">
                        <select id="dentistFilter" class="dentist-filter">
                            <option value="all">All Dentist</option>
                            <c:forEach var="d" items="${dentists}">
                                <c:if test="${d.active}">
                                    <option value="${d.id}">
                                        Dr. ${d.firstname} ${d.surname}
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>

                        <select id="treatmentFilter" class="treatment-filter">
                            <option value="">All Treatment</option>
                            <c:forEach var="s" items="${services}">
                                <c:if test="${s.active}">
                                    <option value="${s.id}">${s.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>

	                    <button type="button" class="add-appt" id="openWalkInBtn">
	                        <i class="fi fi-rr-plus"></i>
	                        Add Appointment
	                    </button>
                    </div>
                </div>
            </div>

            <div class="appointments-row">
                <div class="appointment-card pending-card">
                    <div class="list-header">
                        <div>
                            <h3>Pending Appointment</h3>
                            <p>Patients waiting for arrival confirmation.</p>
                        </div>

                        <span class="list-count" id="pendingCount">0</span>
                    </div>

                    <div id="pendingList" class="appointment-list"></div>

                    <div class="pagination">
                        <button id="pendingPrev" class="page-btn" type="button" disabled>
                            <i class="fi fi-rr-angle-left"></i>
                        </button>

                        <span id="pendingPageInfo">Page 0 of 0</span>

                        <button id="pendingNext" class="page-btn next" type="button" disabled>
                            <i class="fi fi-rr-angle-right"></i>
                        </button>
                    </div>
                </div>

                <div class="appointment-card queue-card">
                    <div class="list-header">
                        <div>
                            <h3>Appointment Queue</h3>
                            <p>Arrived patients ready for service.</p>
                        </div>

                        <span class="list-count" id="queueCount">0</span>
                    </div>

                    <div id="queueList" class="appointment-list"></div>

                    <div class="pagination">
                        <button id="queuePrev" class="page-btn" type="button" disabled>
                            <i class="fi fi-rr-angle-left"></i>
                        </button>

                        <span id="queuePageInfo">Page 0 of 0</span>

                        <button id="queueNext" class="page-btn next" type="button" disabled>
                            <i class="fi fi-rr-angle-right"></i>
                        </button>
                    </div>
                </div>
            </div>

        </section>
    </main>
</div>

<div id="walkInModal" class="modal">
    <div class="modal-content walkin-modal">
        <div class="modal-header">
            <div>
                <h2>Add Walk-in Appointment</h2>
                <p>Create a new walk-in appointment record.</p>
            </div>

            <button type="button" class="modal-x" id="wiCloseBtn">×</button>
        </div>

        <div class="form-group">
            <label class="form-label">Patient Name</label>
            <input type="text" id="wi-patientName" class="form-input" placeholder="Full name">
        </div>

        <div class="form-group">
            <label class="form-label">Dentist</label>
            <select id="wi-dentist" class="form-input">
                <option value="">Select dentist</option>
                <c:forEach var="d" items="${dentists}">
                    <c:if test="${d.active}">
                        <option value="${d.id}">Dr. ${d.firstname} ${d.surname}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label">Service</label>
            <select id="wi-service" class="form-input">
                <option value="">Select service</option>
                <c:forEach var="s" items="${services}">
                    <c:if test="${s.active}">
                        <option value="${s.id}">${s.name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label">Date and Time</label>
            <input type="datetime-local" id="wi-datetime" class="form-input">
        </div>

        <div class="form-group">
            <label class="form-label">Notes</label>
            <textarea id="wi-notes" class="form-input" rows="3" placeholder="Optional notes"></textarea>
        </div>

        <p id="wi-error" class="form-error"></p>

        <div class="modal-actions">
            <button type="button" id="wi-cancelBtn">Cancel</button>
            <button type="button" id="wi-submitBtn" class="save-btn">Add Walk-in</button>
        </div>
    </div>
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

<script>
    window.INITIAL_APPOINTMENTS = ${appointmentsJson};
    window.TODAY = "${today}";
</script>

<script src="/scripts/recepAppt.js"></script>
    
</body>
</html>