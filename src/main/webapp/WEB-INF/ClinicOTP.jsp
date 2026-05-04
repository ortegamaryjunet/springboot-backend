<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    String otpPurpose = (String) session.getAttribute("otpPurpose");
    String otpAction = "REGISTER".equals(otpPurpose)
            ? "/web/register/verify"
            : "/web/forgot/verify";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OTP Verification</title>
<link rel="stylesheet" href="/css/ClinicOTP.css">
</head>
<body>

<div class="page-wrapper">
    <div class="otp-card">

        <a href="#" class="back-button" id="back-btn">
            <span class="back-circle">←</span>
            <span>Back</span>
        </a>

        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="header-text">
            <h1>OTP Verification</h1>
            <p>Enter the 6-digit code sent to your email</p>
        </div>

        <form method="post" action="<%= otpAction %>" id="otpForm">
            <div class="otp-inputs">
                <input type="text" maxlength="1" inputmode="numeric">
                <input type="text" maxlength="1" inputmode="numeric">
                <input type="text" maxlength="1" inputmode="numeric">
                <input type="text" maxlength="1" inputmode="numeric">
                <input type="text" maxlength="1" inputmode="numeric">
                <input type="text" maxlength="1" inputmode="numeric">
            </div>

            <input type="hidden" name="otp" id="otpValue">

            <p class="timer" id="timerText">
                Request a new code in <span id="countdown">60</span> seconds.
            </p>

            <p class="resend-text" id="resendText">
                Didn’t receive the code?
                <a href="#" id="resendBtn">Resend Code</a>
            </p>

            <button type="submit" class="submit-btn" id="submitOtpBtn">Submit</button>
        </form>
        
        <form id="resendForm" method="post" action="/web/otp/resend"></form>
        
    </div>
</div>

<div id="limitOverlay" class="limit-overlay">
    <div class="limit-box">
        <h2>Too Many Attempts</h2>
        <p>You have reached the maximum resend attempts. Try again next day.</p>
    </div>
</div>

<div id="emptyOtpModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/otp.png" alt="error">
        </div>

        <h2 class="modal-title">Missing Code</h2>
        <p>Please fill in all OTP fields before submitting.</p>

        <div class="modal-actions single-action">
            <button id="emptyOtpOk" type="button">OK</button>
        </div>
    </div>
</div>

<div id="confirmModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon warning-icon">
            <img src="/images/back-warning.png" alt="warning">
        </div>

        <h2 class="modal-title">Cancel Verification?</h2>
        <p>Are you sure you want to go back? The process will be canceled.</p>

        <div class="modal-actions">
            <button id="confirmYes" type="button">Yes</button>
            <button id="confirmNo" type="button">No</button>
        </div>
    </div>
</div>

<div id="otpErrorModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/otp.png" alt="error">
        </div>

        <h2 class="modal-title">Incorrect OTP</h2>
        <p id="otpErrorMessage">The verification code you entered is incorrect. Please try again.</p>

        <div class="modal-actions single-action">
            <button id="otpErrorOk" type="button">OK</button>
        </div>
    </div>
</div>

<script>
    const otpModalType = "${sessionScope.otpModalType != null ? sessionScope.otpModalType : ''}";
    const otpModalMessage = "${sessionScope.otpModalMessage != null ? sessionScope.otpModalMessage : ''}";
    const otpPurpose = "${sessionScope.otpPurpose != null ? sessionScope.otpPurpose : ''}";
    const loginType = "${sessionScope.loginType != null ? sessionScope.loginType : ''}";
</script>

<%
    session.removeAttribute("otpModalType");
    session.removeAttribute("otpModalMessage");
%>

<script src="/scripts/ClinicOTP.js"></script>

</body>
</html>