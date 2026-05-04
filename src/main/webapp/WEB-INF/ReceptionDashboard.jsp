<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Dashboard</title>
	<link rel="stylesheet" href="/css/recepDashboard.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="/images/clinic-logo.png" alt="Clinic Logo">
    </div>

    <div class="menu">
        <a href="/web/recepDashboard" class="menu-item active">
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
            <h1>Dashboard</h1>
            <p>Monitor appointments, patients, dentists, and clinic status.</p>
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

    <div class="main-content">

        <div class="dashboard-hero">
            <div>
                <span class="hero-badge">Overview</span>
                <h2>Keep track of appointments, patients, and clinic operations.</h2>
                <p>Monitor patient records, dentist availability, appointment status, and room status in one clean dashboard.</p>
            </div>

            <div class="hero-icon">
                <i class="fi fi-rr-chart-histogram"></i>
            </div>
        </div>

        <div class="summary-grid">
            <div class="summary-card">
                <div class="summary-icon blue">
                    <i class="fi fi-rr-users"></i>
                </div>
                <div>
                    <p>Total Patients</p>
                    <h2>${totalPatients}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon green">
                    <i class="fi fi-rr-calendar-check"></i>
                </div>
                <div>
                    <p>Total Appointments</p>
                    <h2>${totalAppointments}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon purple">
                    <i class="fi fi-rr-calendar-lines-pen"></i>
                </div>
                <div>
                    <p>Rescheduled Appointments</p>
                    <h2>${rescheduledAppointments}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon orange">
                    <i class="fi fi-rr-time-quarter-past"></i>
                </div>
                <div>
                    <p>Pending Appointments</p>
                    <h2>${pendingAppointments}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon blue">
                    <i class="fi fi-rr-doctor"></i>
                </div>
                <div>
                    <p>Active Dentists</p>
                    <h2>${activeDentists}</h2>
                </div>
            </div>

            <div class="summary-card">
                <div class="summary-icon orange">
                    <i class="fi fi-rr-calendar-xmark"></i>
                </div>
                <div>
                    <p>Cancelled Appointments</p>
                    <h2>${cancelledAppointments}</h2>
                </div>
            </div>
        </div>

        <div class="dashboard-grid full-width">
            <div class="dashboard-card clinic-overview-card">
                <div class="card-header">
                    <div>
                        <h3>Clinic Overview</h3>
                        <p>Doctor and room availability summary.</p>
                    </div>
                </div>

                <div class="status-grid">
                    <div class="status-box">
                        <h4>Doctor Status</h4>

                        <div class="status-item">
                            <span class="dot success"></span>
                            <span>Available</span>
                        </div>

                        <div class="status-item">
                            <span class="dot warning"></span>
                            <span>In Surgery</span>
                        </div>

                        <div class="status-item">
                            <span class="dot orange"></span>
                            <span>Not Available</span>
                        </div>
                    </div>

                    <div class="status-box">
                        <h4>Room Status</h4>

                        <div class="status-item">
                            <span class="dot success"></span>
                            <span>Available</span>
                        </div>

                        <div class="status-item">
                            <span class="dot failed"></span>
                            <span>In Use</span>
                        </div>
                    </div>
                </div>
            </div>
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
        	<button id="logoutBtn">Logout</button>
            <button id="cancelBtn">Cancel</button>
        </div>
    </div>
</div>

<script src="/scripts/recepDashboard.js"></script>

</body>
</html>