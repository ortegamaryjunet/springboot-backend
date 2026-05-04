<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Doctor Login</title>
	<link rel="stylesheet" href="/css/ClinicLogin.css">
</head>
<body>

<div class="page-wrapper">
    <div class="login-card">

        <a href="/role" class="back-button" id="back-btn">
            <span class="back-circle">←</span>
            <span>Back</span>
        </a>

        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="header-text">
            <h1>Doctor Portal</h1>
            <p>Login to manage your patients and appointments.</p>
        </div>

        <form id="loginForm" action="/web/login" method="POST">

            <div class="input-group">
                <img src="/images/doctor-icon.png" class="icon">
                <input type="email" id="email" name="email" placeholder="Email address">
            </div>

            <div class="input-group">
                <img src="/images/password.png" class="icon">
                <input type="password" id="password" name="password" placeholder="Password">
            </div>

            <div class="options">
                <a href="/web/forgot">Forgot password?</a>
            </div>

            <button type="submit" class="login-btn">Login</button>

        </form>

    </div>
</div>

<div id="confirmModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon">
            <img src="/images/back-warning.png">
        </div>

        <h2 class="modal-title">Go Back?</h2>
        <p>Unsaved changes may be lost.</p>

        <div class="modal-actions">
            <button id="confirmNo">No</button>
            <button id="confirmYes">Yes</button>
        </div>
    </div>
</div>

<div id="errorModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/login-failed.png">
        </div>

        <h2 class="modal-title">Login Failed</h2>
        <p id="errorMessage">Incorrect email or password.</p>

        <div class="modal-actions single-action">
            <button id="errorOk">OK</button>
        </div>
    </div>
</div>

<div id="validationModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/invalid-input.png">
        </div>

        <h2 class="modal-title">Missing Fields</h2>
        <p id="validationMessage">Please complete all fields.</p>

        <div class="modal-actions single-action">
            <button id="validationOk">OK</button>
        </div>
    </div>
</div>

<script>
    const modalType = "${sessionScope.modalType != null ? sessionScope.modalType : ''}";
    const modalMessage = "${sessionScope.modalMessage != null ? sessionScope.modalMessage : ''}";
</script>

<%
    session.removeAttribute("modalType");
    session.removeAttribute("modalMessage");
%>

<script src="/scripts/ClinicLogin.js"></script>

</body>
</html>