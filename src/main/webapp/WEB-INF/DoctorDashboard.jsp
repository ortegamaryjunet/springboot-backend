<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>Doctor Dashboard</title>
    <link rel="stylesheet" href="/css/doctorDashboard.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>
    
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
            <a href="/doctorDashboard" class="menu-item active">
                <i class="fi fi-rr-apps"></i>
                <span>Dashboard</span>
            </a>
            <a href="/doctorAppointment" class="menu-item">
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
            <div class="doctor-grid">

                <div class="card">
                    <div class="card-icon">
                        <img src="/images/patients.png" alt="Total Patients">
                    </div>
                    <div class="card-text">
                        <h3>Total Patients</h3>
                        <p>${totalPatients}</p>
                    </div>
                </div>

                <div class="card">
                    <div class="card-icon">
                        <img src="/images/appt.png" alt="Total Appointments">
                    </div>
                    <div class="card-text">
                        <h3>Total Appointments</h3>
                        <p>${totalAppointments}</p>
                    </div>
                </div>

                <div class="card new-patients">
                    <div class="card-icon">
                        <img src="/images/new.png" alt="New Patients">
                    </div>
                    <div class="card-text">
                        <h3>New Patients</h3>
                        <p>${newPatients}</p>
                    </div>
                </div>

                <div class="card returning-patients">
                    <div class="card-icon">
                        <img src="/images/returning.png" alt="Returning Patients">
                    </div>
                    <div class="card-text">
                        <h3>Returning Patients</h3>
                        <p>${returningPatients}</p>
                    </div>
                </div>

                <div class="chart">
                    <h3>Patient Overview</h3>
                    <div class="chart-wrapper">
                        <div class="legend">
                            <div class="legend-item">
                                <span class="legend-color" style="background:#FFD700"></span>Child
                            </div>
                            <div class="legend-item">
                                <span class="legend-color" style="background:#1E90FF"></span>Teen
                            </div>
                            <div class="legend-item">
                                <span class="legend-color" style="background:#32CD32"></span>Adult
                            </div>
                            <div class="legend-item">
                                <span class="legend-color" style="background:#9370DB"></span>Older
                            </div>
                        </div>
                        <canvas id="patientOverview" width="100" height="100"></canvas>
                    </div>
                </div>

            </div>

            <div class="overview feedback">
                <div class="overview-header">
                    <h3>Patient Feedback</h3>
                </div>
                <div class="feedback-list">
                    <div class="feedback-item">
                        <p>"Great service and very accommodating staff."</p>
                        <span>- Patient A</span>
                    </div>
                    <div class="feedback-item">
                        <p>"Clean clinic and smooth appointment process."</p>
                        <span>- Patient B</span>
                    </div>
                    <div class="feedback-item">
                        <p>"Dentist was very professional and gentle."</p>
                        <span>- Patient C</span>
                    </div>
                </div>
            </div>

            <div class="feedback-satisfaction-grid">
                <div class="overview satisfaction-rating">
                    <div class="overview-header">
                        <h3>Patient Satisfaction Rating</h3>
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

<script src="/scripts/doctorDashboard.js"></script>

</body>
</html>