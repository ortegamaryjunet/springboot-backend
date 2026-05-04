<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Forgot Password</title>
<link rel="stylesheet" href="/css/ClinicFP.css">
</head>
<body>

<div class="page-wrapper">
    <div class="forgot-card">

        <a href="#" class="back-button" id="back-btn">
            <span class="back-circle">←</span>
            <span>Back</span>
        </a>

        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="header-text">
            <h1>Forgot Password</h1>
            <p>Enter your email address and we will send you a verification code.</p>
        </div>

        <form method="post" action="/web/forgot" id="forgotForm">
            <div class="input-group">
                <img src="/images/user.png" alt="Email Icon" class="icon">
                <input type="email" name="email" id="email" placeholder="Email address">
            </div>

            <button type="submit" class="forgot-btn">Send Code</button>
        </form>
    </div>
</div>

<div id="confirmModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon warning-icon">
            <img src="/images/cancel-pw.png" alt="warning">
        </div>

        <h2 class="modal-title">Cancel Password Change?</h2>
        <p>Are you sure you want to go back? The password reset process will be canceled.</p>

        <div class="modal-actions">
            <button id="confirmNo" type="button">No</button>
            <button id="confirmYes" type="button">Yes</button>
        </div>
    </div>
</div>

<div id="emptyEmailModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/cancel-pw.png" alt="error">
        </div>

        <h2 class="modal-title">Missing Email</h2>
        <p>Please enter your email address before continuing.</p>

        <div class="modal-actions single-action">
            <button id="emptyEmailOk" type="button">OK</button>
        </div>
    </div>
</div>

<script>
    const loginType = "${sessionScope.loginType}";
    const backendModalType = "${sessionScope.modalType}";
    const backendModalMessage = "${sessionScope.modalMessage}";
</script>

<%
    session.removeAttribute("modalType");
    session.removeAttribute("modalMessage");
%>

<script src="/scripts/ClinicFP.js"></script>

</body>
</html>