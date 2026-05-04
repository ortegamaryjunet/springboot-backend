<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Account Registration</title>
<link rel="stylesheet" href="/css/adminRegister.css">
</head>
<body>

<div class="page-wrapper">
    <div class="register-card">

        <a href="/adminLogin" class="back-button" id="back-btn">
            <span class="back-circle">←</span>
            <span>Back</span>
        </a>

        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="header-text">
            <h1>Admin Registration</h1>
            <p>Create an admin account for Smile Empress Dental Hub.</p>
        </div>

        <form id="registerForm" action="/registerProcess" method="POST">

            <div class="input-group">
                <img src="/images/user.png" class="icon" alt="User Icon">
                <input type="text" id="fullname" name="fullname" placeholder="Enter your full name"
                       value="${sessionScope.oldFullname != null ? sessionScope.oldFullname : ''}">
            </div>

            <div class="input-group">
                <img src="/images/admin-icon.png" class="icon" alt="Email Icon">
                <input type="email" id="email" name="email" placeholder="Enter your email address"
                       value="${sessionScope.oldEmail != null ? sessionScope.oldEmail : ''}">
            </div>

            <div class="input-group">
                <img src="/images/password.png" class="icon" alt="Password Icon">
                <input type="password" id="password" name="password" placeholder="Enter your password">
            </div>

            <div class="input-group">
                <img src="/images/password.png" class="icon" alt="Confirm Password Icon">
                <input type="password" id="repassword" name="repassword" placeholder="Confirm password">
            </div>

            <button type="submit" class="register-btn">Register</button>
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
            <button id="confirmNo" type="button">No</button>
            <button id="confirmYes" type="button">Yes</button>
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

<div id="passwordPolicyModal" class="modal">
    <div class="modal-content">
        <div class="modal-icon error-icon">
            <img src="/images/invalid-input.png" alt="warning">
        </div>

        <h2 class="modal-title">Password Issue</h2>
        <p id="passwordPolicyMessage">Password must be 8 to 20 characters and contain only letters and numbers.</p>

        <div class="modal-actions single-action">
            <button id="passwordPolicyOk" type="button">OK</button>
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

<script src="/scripts/adminRegister.js"></script>

</body>
</html>