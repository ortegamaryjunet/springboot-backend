<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Password</title>
<link rel="stylesheet" href="/css/ClinicResetPassword.css">
</head>
<body>

<div class="page-wrapper">
    <div class="register-card">

        <a href="/web/forgot/verify" class="back-button">
            <span class="back-circle">←</span>
            <span>Back</span>
        </a>
        
		<div class="logo">
		    <img src="/images/clinic-logo.png" alt="Clinic Logo">
		</div>

        <div class="header-text">
            <h1>Reset Password</h1>
            <p>Enter your new password below to update your account.</p>
        </div>

        <form id="resetForm" action="/web/forgot/reset" method="post">

            <div class="input-group">
                <input type="password" id="newPassword" name="password" placeholder="Enter new password" required>
            </div>

            <div class="input-group">
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password" required>
            </div>

            <button type="submit" class="register-btn">Reset Password</button>

        </form>

    </div>
</div>

<div class="modal" id="modal">
    <div class="modal-content">
        <h2 class="modal-title" id="modalTitle">Error</h2>
        <p id="modalMessage"></p>

        <div class="modal-actions single-action">
            <button id="modalOk" type="button">OK</button>
        </div>
    </div>
</div>

<script src="/scripts/clinicResetPassword.js"></script>

</body>
</html>