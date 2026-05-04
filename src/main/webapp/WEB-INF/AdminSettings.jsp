<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Admin Settings</title>
	<link rel="stylesheet" href="/css/adminSetting.css">
	
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
	<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-straight/css/uicons-regular-straight.css">
	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/css/intlTelInput.css">
</head>
<body>

	<div class="top-header">
	    <a href="javascript:history.back()">
	        <img src="/images/back-btn.png" id="back-btn" class="backBtn" alt="Back">
	    </a>
	    <h2>Settings</h2>
	</div>
	
	<div class="container">
	
	    <!-- SIDEBAR -->
	    <div class="sidebar">
	        <div class="menu-item active" onclick="showSection('branch', this)">
	            <i class="fi fi-rr-hospital"></i> Branch Configuration
	        </div>
	
	        <div class="menu-item" onclick="showSection('service', this)">
	            <i class="fi fi-rr-receipt"></i> Services & Pricing
	        </div>
	    </div>
	
	    <div class="main-container">

	        <div id="branch" class="content-section active">
	            <div class="main-content">
	
	                <div class="branch-actions">
	                    <div class="search-box">
	                        <img src="/images/search-icon.png" class="search-icon" alt="Search">
	                        <input type="text" id="branchSearch" placeholder="Search...">
	                    </div>
	
	                    <div class="right-actions">
	                    	<select id="branchStatusFilter">
	                        	<option value="All">All Status</option>
	                        	<option value="Active">Active</option>
	                        	<option value="Inactive">Inactive</option>
	                        	<option value="Opening">Opening</option>
	                        	<option value="Closed">Closed</option>
	                        	<option value="Renovation">Renovation</option>
	                    	</select>
	                    	
	                        <button class="add-branch-btn" onclick="openBranchForm()">
	                            <i class="fi fi-rr-building"></i>
	                            <span>Add Branch</span>
	                        </button>
	                    </div>
	                </div>
	
	                <div class="table-container">
	                    <table class="branch-table">
	                        <thead>
	                        <tr>
	                            <th>Branch Name</th>
	                            <th>Date of Establishment</th>
	                            <th>Date Opened</th>
	                            <th>Clinic Location</th>
	                            <th>Clinic Type</th>
	                            <th>Contact Number</th>
	                            <th>Contact Person</th>
	                            <th>Email Address</th>
	                            <th>Status</th>
	                            <th>Actions</th>
	                        </tr>
	                        </thead>
	
	                        <tbody id="branchTableBody"></tbody>
	                    </table>
	
	                    <div class="pagination">
	                        <button id="prevBtn" class="page-btn">
	                            <i class="fi fi-rr-angle-left"></i>
	                        </button>
	
	                        <span id="pageInfo"></span>
	
	                        <button id="nextBtn" class="page-btn">
	                            <i class="fi fi-rr-angle-right"></i>
	                        </button>
	                    </div>
	                </div>
	
	            </div>
	        </div>

	        <div id="service" class="content-section">
	            <div class="main-content">
	            
	                    <div class="service-tools">
	                        <div class="search-box">
	                            <img src="/images/search-icon.png" class="search-icon" alt="Search">
	                            <input type="text" id="serviceSearch" placeholder="Search...">
	                        </div>
	
	                        <div class="right-actions">
	                            <select id="serviceStatusFilter">
	                                <option value="All">All Status</option>
	                                <option value="Active">Active</option>
	                                <option value="Inactive">Inactive</option>
	                                <option value="Discontinued">Discontinued</option>
	                            </select>
	
	                            <select id="serviceSortFilter">
	                                <option value="default">Sort By</option>
	                                <option value="az">Name A - Z</option>
	                                <option value="za">Name Z - A</option>
	                                <option value="low">Price Low - High</option>
	                                <option value="high">Price High - Low</option>
	                            </select>
	
	                            <button class="add-service-btn" onclick="openServiceForm()">
	                                <i class="fi fi-rr-stethoscope"></i> Add Service
	                            </button>
	                        </div>
	                    </div>
	
	                <div class="service-table-container">
	                    <table class="service-table">
	                        <thead>
	                        <tr>
	                            <th>Service Name</th>
	                            <th>Price</th>
	                            <th>Status</th>
	                            <th>Actions</th>
	                        </tr>
	                        </thead>
	
	                        <tbody id="serviceTableBody"></tbody>
	                    </table>
	
	                    <div class="pagination">
	                        <button id="service-prevBtn" class="page-btn">
	                            <i class="fi fi-rr-angle-left"></i>
	                        </button>
	
	                        <span id="service-pageInfo"></span>
	
	                        <button id="service-nextBtn" class="page-btn">
	                            <i class="fi fi-rr-angle-right"></i>
	                        </button>
	                    </div>
	                </div>
	
	            </div>
	        </div>
	
	    </div>
	</div>
	
	<!-- BRANCH FORM OVERLAY -->
	<div id="branchOverlay" class="overlay">
	    <div class="overlay-content">
	
	        <div class="overlay-header">
	            <h3>Branch Form</h3>
	            <button type="button" class="close-btn" onclick="closeBranchForm()">&times;</button>
	        </div>
	
	        <div class="overlay-body">
	            <form onsubmit="event.preventDefault(); saveBranch();">
	
	                <div class="form-grid">
	
	                    <div class="field">
	                        <label>Branch Name</label>
	                        <div class="input-group">
	                            <input type="text" id="branchName">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Clinic Location</label>
	                        <div class="input-group">
	                            <input type="text" id="branchLocation">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Date of Establishment</label>
	                        <div class="input-group">
	                            <input type="date" id="branchEst">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Date Opened</label>
	                        <div class="input-group">
	                            <input type="date" id="branchOpened">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Clinic Type</label>
	                        <div class="input-group">
	                            <input type="text" id="branchType">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Email Address</label>
	                        <div class="input-group">
	                            <input type="email" id="branchEmail">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Contact Number</label>
	                        <div class="input-group phone-input">
	                            <input type="tel" id="branchContact">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Contact Person</label>
	                        <div class="input-group">
	                            <input type="text" id="branchPerson">
	                        </div>
	                    </div>
	
	                    <div class="field">
	                        <label>Status</label>
	                        <div class="input-group">
	                            <select id="branchStatus">
	                                <option value="" disabled selected></option>
	                                <option value="Active">Active</option>
	                                <option value="Inactive">Inactive</option>
	                                <option value="Renovation">Renovation</option>
	                                <option value="Opening">Opening Soon</option>
	                                <option value="Closed">Closed</option>
	                            </select>
	                        </div>
	                    </div>
	
	                </div>
	
	                <div class="overlay-actions">
	                    <button type="submit" class="save-btn">Save</button>
	                </div>
	
	            </form>
	        </div>
	
	    </div>
	</div>

<script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/intlTelInput.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/utils.js"></script>
<script src="/scripts/adminSetting.js"></script>

</body>
</html>