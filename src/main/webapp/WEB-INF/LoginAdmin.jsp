<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Login</title>
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
            <h1>Welcome Back, Admin!</h1>
            <p>Login as admin to manage your dental system.</p>
        </div>

        <form id="loginForm" action="/web/login" method="POST">

            <div class="input-group">
                <img src="/images/admin-icon.png" class="icon" alt="Email Icon">
                <input type="email" id="email" name="email" placeholder="Email address">
            </div>

            <div class="input-group">
                <img src="/images/password.png" class="icon" alt="Password Icon">
                <input type="password" id="password" name="password" placeholder="Password">
            </div>

            <div class="options">
                <a href="/web/forgot">Forgot password?</a>
            </div>

            <button type="submit" class="login-btn">Login</button>

            <p class="register-text">
                No account yet? <a href="/adminRegister">Register</a>
            </p>

        </form>
    </div>
</div>

<div id="confirmModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon warning-icon">
            <img src="/images/back-warning.png" alt="warning">
        </div>

        <h2 class="modal-title">Go Back?</h2>
        <p>Are you sure you want to go back? Unsaved changes may be lost.</p>

        <div class="modal-actions">
            <button id="confirmYes" type="button">Yes</button>
            <button id="confirmNo" type="button">No</button>
        </div>
    </div>
</div>

<div id="errorModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/login-failed.png" alt="error">
        </div>

        <h2 class="modal-title">Login Failed</h2>
        <p id="errorMessage">Incorrect email or password.</p>

        <div class="modal-actions single-action">
            <button id="errorOk" type="button">OK</button>
        </div>
    </div>
</div>

<div id="validationModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/invalid-input.png" alt="warning">
        </div>

        <h2 class="modal-title">Invalid Input</h2>
        <p id="validationMessage">Please complete all fields.</p>

        <div class="modal-actions single-action">
            <button id="validationOk" type="button">OK</button>
        </div>
    </div>
</div>

<div id="successModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon success-icon">
            <img src="/images/register-success.png" alt="success">
        </div>

        <h2 class="modal-title">Success</h2>
        <p id="successMessage">Account verified successfully. You can now login.</p>

        <div class="modal-actions single-action">
            <button id="successOk" type="button">OK</button>
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