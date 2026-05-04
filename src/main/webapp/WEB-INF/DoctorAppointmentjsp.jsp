<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Doctor Appointment</title>
    <link rel="stylesheet" href="/css/doctorAppointment.css">
    
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
            <a href="/doctorDashboard" class="menu-item">
                <i class="fi fi-rr-apps"></i>
                <span>Dashboard</span>
            </a>
            <a href="/doctorAppointment" class="menu-item active">
                <i class="fi fi-rr-calendar-clock"></i>
                <span>Appointment</span>
            </a>
            <a href="/doctorPatient" class="menu-item">
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

                <%-- FIXED: dentistName + dentistSpecialization from real DB --%>
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
            <div class="calendarContainer">
                <div class="calendar">
                    <div class="controls">
                        <div class="calendarButton">
                            <img src="/images/left.png" class="prevMonthBtn" alt="Previous">
                        </div>
                        <div class="calendarButton">
                            <select class="monthSelect"></select>
                            <select class="yearSelect"></select>
                        </div>
                        <div class="calendarButton">
                            <img src="/images/right.png" class="nextMonthBtn" alt="Next">
                        </div>
                    </div>
                    <div class="currentMonthLabel"></div>
                    <table class="calendar-table">
                        <thead class="calendar-thead">
                            <tr class="calendar-tr">
                                <th class="calendar-th">Su</th>
                                <th class="calendar-th">Mo</th>
                                <th class="calendar-th">Tu</th>
                                <th class="calendar-th">We</th>
                                <th class="calendar-th">Th</th>
                                <th class="calendar-th">Fr</th>
                                <th class="calendar-th">Sa</th>
                            </tr>
                        </thead>
                        <tbody class="calendarBody calendar-tbody"></tbody>
                    </table>
                </div>
            </div>

            <div class="right-panel">

                <%-- FIXED: todayCount from real DB --%>
                <div class="today-appointment">
                    <h3>Today's Appointment</h3>
                    <h2 id="todayAppt">${todayCount}</h2>
                    <div class="appointment-summary">
                        <%--
                            confirmed/waiting/noshow counts are computed in doctorAppointment.js
                            from the appointmentsData array injected below.
                            Element IDs are unchanged so existing JS still works.
                        --%>
                        <div class="summary-card confirmed">
                            <span>Confirmed</span>
                            <h2 id="confirmedCount">0</h2>
                        </div>
                        <div class="summary-card waiting">
                            <span>Waiting</span>
                            <h2 id="waitingCount">0</h2>
                        </div>
                        <div class="summary-card noshow">
                            <span>No Show</span>
                            <h2 id="noshowCount">0</h2>
                        </div>
                    </div>
                </div>

                <div class="appointment-container">
                    <div class="header">
                        <h3>Today's Appointment List</h3>
                        <select class="dropdown-status" id="statusFilter" onchange="filterAppointments()">
                            <option value="All" selected>All Status</option>
                            <option value="confirmed">Confirmed</option>
                            <option value="waiting">Waiting</option>
                            <option value="noshow">No Show</option>
                        </select>
                    </div>

                    <table class="doctor-table">
                        <thead>
                            <tr>
                                <th>Patient Name</th>
                                <th>Reason</th>
                                <th>Time</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody class="doctor-tbody" id="appointmentTable"></tbody>
                    </table>

                    <div class="pagination">
                        <button id="prevBtn" class="page-btn" onclick="prevPage()">
                            <i class="fi fi-rr-angle-left"></i>
                        </button>
                        <span id="pageInfo"></span>
                        <button id="nextBtn" class="page-btn" onclick="nextPage()">
                            <i class="fi fi-rr-angle-right"></i>
                        </button>
                    </div>
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

    <%--
        FIXED: Inject today's appointments as a JS array.
        appointmentsData is read by doctorAppointment.js to render the table,
        filter by status, and update confirmed/waiting/noshow counts.

        appt.scheduledStart is already a pre-formatted "hh:mm a" String from the
        service layer — no fmt:formatDate needed (and it would crash if used here
        because fmt:formatDate cannot handle LocalDateTime).

        c:out escapes special characters (quotes, <, >) safely in JS string context.
    --%>
    <script>
        const appointmentsData = [
            <c:forEach var="appt" items="${todayAppointments}" varStatus="loop">
            {
                id:          ${appt.id},
                patientName: "<c:out value='${appt.patientFullName}'/>",
                reason:      "<c:out value='${appt.serviceName}'/>",
                time:        "<c:out value='${appt.scheduledStart}'/>",
                status:      "<c:out value='${appt.status}'/>"
            }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];
    </script>

<script src="/scripts/doctorAppointment.js"></script>
<script src="/scripts/Calendar.js"></script>

</body>
</html>