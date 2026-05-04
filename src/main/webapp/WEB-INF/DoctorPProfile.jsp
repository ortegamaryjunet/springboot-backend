<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Patient Profile</title>

    <link rel="stylesheet" href="/css/dPatientProfile.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<div class="top-header">
    <a href="/doctorPatient">
        <img src="/images/back-btn.png" id="back-btn" class="backBtn" alt="Back">
    </a>
    <h2>Patient Information Record</h2>
</div>

<div class="container">

    <div class="sidebar">
        <div class="menu-item active" onclick="showSection('profile', this)">
            <i class="fi fi-rr-id-badge"></i> Profile Information
        </div>

        <div class="menu-item" onclick="showSection('record', this)">
            <i class="fi fi-rr-clipboard-list"></i> Treatment Record
        </div>
    </div>

    <div class="main-container">

        <!-- PROFILE -->
        <div id="profile" class="content-section active">
            <div class="main-content">
                <h3>Profile</h3>
                <p>Patient basic information will appear here.</p>
            </div>
        </div>

        <!-- RECORD -->
        <div id="record" class="content-section">
            <div class="main-content">

                <div class="record-header">
                    <button class="add-plan-btn" onclick="openAddPlanForm()">
                        <i class="fi fi-rr-file-medical"></i> Add Plan
                    </button>
                </div>

                <div class="table-container">
                    <table class="record-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Tooth No/s</th>
                                <th>Procedure</th>
                                <th>Dentist Name</th>
                                <th>Amount Charged</th>
                                <th>Amount Paid</th>
                                <th>Balance</th>
                                <th>Next Appointment</th>
                            </tr>
                        </thead>

                        <tbody id="recordTableBody"></tbody>
                    </table>
                    
		            <div class="pagination">
		            	<button id="prevBtn">
		                	<i class="fi fi-rr-angle-left"></i>
		            	</button>
		
		            	<span id="pageInfo"></span>
		
		            	<button id="nextBtn">
		                	<i class="fi fi-rr-angle-right"></i>
		             	</button>
		        	</div>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="/scripts/doctorPatientProfile.js"></script>

</body>
</html>