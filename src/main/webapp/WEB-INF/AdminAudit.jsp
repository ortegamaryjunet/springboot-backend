<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Admin Audit Logs</title>
	<link rel="stylesheet" href="/css/adminAudit.css">
	
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

			<a href="/web/admin/dashboard" class="menu-item">
			    <i class="fi fi-rr-apps"></i>
			    <span>Dashboard</span>
			</a>
			
			<a href="/web/admin/patients" class="menu-item">
			    <i class="fi fi-rr-clipboard-user"></i>
			    <span>Patients</span>
			</a>
			
			<a href="/web/admin/employees" class="menu-item">
			    <i class="fi fi-rr-stethoscope"></i>
			    <span>Clinic Employee</span>
			</a>
			
			<a href="/web/admin/inventory" class="menu-item">
			    <i class="fi fi-rr-boxes"></i>
			    <span>Inventory</span>
			</a>
			
			<a href="/web/admin/audit" class="menu-item active">
			    <i class="fi fi-rr-clipboard-list"></i>
			    <span>Audit Logs</span>
			</a>
			
			<a href="/web/admin/report" class="menu-item">
			    <i class="fi fi-rr-document-signed"></i>
			    <span>Reports</span>
			</a>
			
        </div>
    </div>

    <div class="main-container">
		<div class="top-header">
		    <div class="page-title">
		        <h1>Audit Logs</h1>
		        <p>Track clinic staff activity records.</p>
		    </div>
		
		    <div class="header-actions">
		        <div class="notification-icon">
		            <i class="fi fi-rr-bell"></i>
		            <span class="badge" id="notificationCount">0</span>
		        </div>
		
		        <div class="admin-profile" id="adminProfile">
		            <div class="avatar">
		                <i class="fi fi-rr-user"></i>
		            </div>
		
		            <div class="admin-info">
		                <div class="name">Admin Full Name</div>
		                <div class="position">Admin</div>
		            </div>
		
		            <i class="fi fi-rr-angle-small-down arrow"></i>
		
		            <div class="profile-dropdown" id="profileDropdown">
		                <a href="/web/admin/settings" class="dropdown-item">
		                    <i class="fi fi-rr-settings"></i>
		                    <span>Settings</span>
		                </a>
		
		                <div class="dropdown-divider"></div>
		
		                <a href="#" class="dropdown-item" id="openLogoutModal">
		                    <i class="fi fi-rr-sign-out-alt"></i>
		                    <span>Logout</span>
		                </a>
		            </div>
		        </div>
		    </div>
		</div>

		<div class="main-content">
		    <div class="filter-card">
		        <div class="search-box">
		            <i class="fi fi-rr-search"></i>
		            <input type="text" id="searchInput" placeholder="Search name, action, or module">
		        </div>
		
		        <div class="right-actions">
		            <input type="date" id="dateFilter">
		
		            <select id="roleFilter" class="role-filter">
		                <option value="">All Roles</option>
		                <option value="Admin">Admin</option>
		                <option value="Dentist">Dentist</option>
		                <option value="Receptionist">Receptionist</option>
		            </select>
		        </div>
		    </div>
		
		    <div class="table-card">
		        <div class="table-header">
		            <h3>Activity Records</h3>
		            <p>View system activities, access logs, and status details.</p>
		        </div>
		
		        <div class="table-wrapper">
		            <table class="audit-table" id="auditTable">
                    <thead>
                        <tr>
                            <th>Date | Time</th>
                            <th>Role Type</th>
                            <th>Name</th>
                            <th>Action Type</th>
                            <th>Module / Section</th>
                            <th>IP Address</th>
                            <th>Status</th>
                        </tr>
                    </thead>

					<tbody id="auditTableBody">
					    <c:forEach var="log" items="${logs}">
					        <tr>
					        	<td>${log.timestamp.toString().replace('T', ' ')}</td>
					            <td>${log.roleType}</td>
					            <td>${log.name}</td>
					            <td>${log.actionType}</td>
					            <td>${log.moduleSection}</td>
					            <td>${log.ipAddress}</td>
								<td>
								    <span class="status 
								        ${log.status.equalsIgnoreCase('Success') ? 'status-success' : 'status-failed'}">
								        
								        <i class="fi 
								            ${log.status.equalsIgnoreCase('Success') ? 'fi-rr-check-circle' : 'fi-rr-cross-circle'}">
								        </i>
								
								        ${log.status.substring(0,1).toUpperCase().concat(log.status.substring(1).toLowerCase())}
								    </span>
								</td>
					        </tr>
					    </c:forEach>
					</tbody>
                </table>
                
	        <div class="pagination">
	            <button id="prevBtn" class="page-btn" type="button">
	                <i class="fi fi-rr-angle-left"></i>
	            </button>
	
	            <span id="pageInfo"></span>
	
	            <button id="nextBtn" class="page-btn" type="button">
	                <i class="fi fi-rr-angle-right"></i>
	            </button>
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

<script src="/scripts/adminAudit.js"></script>

</body>
</html>