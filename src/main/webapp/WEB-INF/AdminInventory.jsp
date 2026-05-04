<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Inventory</title>

<link rel="stylesheet" href="/css/recepInventory.css">
<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-solid-rounded/css/uicons-solid-rounded.css">
<link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-straight/css/uicons-regular-straight.css">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="/images/clinic-logo.png" alt="Clinic Logo">
    </div>

    <div class="menu">
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

        <a href="/web/admin/inventory" class="menu-item active">
            <i class="fi fi-rr-boxes"></i>
            <span>Inventory</span>
        </a>

        <a href="/web/admin/audit" class="menu-item">
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
            <h1>Inventory</h1>
            <p>Monitor dental medicines, equipment, and supplies.</p>
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

                    <a href="#" class="dropdown-item" id="logoutLink">
                        <i class="fi fi-rr-sign-out-alt"></i>
                        <span>Logout</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="main-content">

        <div class="tab-card">
            <button class="tab-btn active" type="button">Dental Medicine</button>
            <button class="tab-btn" type="button">Dental Equipment</button>
            <button class="tab-btn" type="button">Dental Supplies</button>
        </div>

        <div class="filter-card">
            <div class="search-box">
                <i class="fi fi-rr-search"></i>
                <input type="text" id="searchInput" placeholder="Search inventory item">
            </div>
        </div>

        <div id="medicineTable" class="table-card table-section show">
            <div class="table-header">
                <h3>Dental Medicine</h3>
                <p>View medicine information and stock status.</p>
            </div>

            <div class="table-wrapper">
                <table class="medicine-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Medicine Name</th>
                            <th>Generic Name</th>
                            <th>Category</th>
                            <th>Form</th>
                            <th>Dosage</th>
                            <th>Unit</th>
                            <th>Status</th>
                        </tr>
                    </thead>

                    <tbody id="medicineTbody">
                        <c:forEach var="medicine" items="${medicines}">
                            <tr>
                                <td>${medicine.medicineId}</td>
                                <td>${medicine.medicineName}</td>
                                <td>${medicine.genericName}</td>
                                <td>${medicine.category}</td>
                                <td>${medicine.form}</td>
                                <td>${medicine.dosage}</td>

                                <td>
                                    <c:forEach var="stock" items="${medicineStocks}">
                                        <c:if test="${stock.medicine.medicineId == medicine.medicineId}">
                                            ${stock.unit}
                                        </c:if>
                                    </c:forEach>
                                </td>

                                <td>
                                    <span class="status">${medicine.status}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <button id="medicinePrev" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-left"></i>
                </button>

                <span id="medicinePageInfo"></span>

                <button id="medicineNext" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
        </div>

        <div id="equipmentTable" class="table-card table-section">
            <div class="table-header">
                <h3>Dental Equipment</h3>
                <p>View dental equipment details, warranty, and location.</p>
            </div>

            <div class="table-wrapper">
                <table class="equipment-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Equipment Name</th>
                            <th>Brand</th>
                            <th>Category</th>
                            <th>Model Number</th>
                            <th>Serial Number</th>
                            <th>Purchase Date</th>
                            <th>Warranty Date</th>
                            <th>Location</th>
                            <th>Status</th>
                        </tr>
                    </thead>

                    <tbody id="equipmentTbody">
                        <c:forEach var="equipment" items="${equipmentList}">
                            <tr>
                                <td>${equipment.equipmentId}</td>
                                <td>${equipment.equipmentName}</td>
                                <td>${equipment.brandName}</td>
                                <td>${equipment.category}</td>
                                <td>${equipment.modelNumber}</td>
                                <td>${equipment.serialNumber}</td>
                                <td>${equipment.purchaseDate}</td>
                                <td>${equipment.warrantyExpiry}</td>
                                <td>${equipment.location}</td>
                                <td>
                                    <span class="status">${equipment.status}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <button id="equipmentPrev" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-left"></i>
                </button>

                <span id="equipmentPageInfo"></span>

                <button id="equipmentNext" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
        </div>

        <div id="supplyTable" class="table-card table-section">
            <div class="table-header">
                <h3>Dental Supplies</h3>
                <p>View supplies, units, and availability status.</p>
            </div>

            <div class="table-wrapper">
                <table class="supply-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Supply Name</th>
                            <th>Brand</th>
                            <th>Category</th>
                            <th>Unit</th>
                            <th>Status</th>
                        </tr>
                    </thead>

                    <tbody id="supplyTbody">
                        <c:forEach var="supply" items="${supplies}">
                            <tr>
                                <td>${supply.supplyId}</td>
                                <td>${supply.supplyName}</td>
                                <td>${supply.brandName}</td>
                                <td>${supply.category}</td>
                                <td>${supply.unit}</td>
                                <td>
                                    <span class="status">${supply.status}</span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <button id="supplyPrev" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-left"></i>
                </button>

                <span id="supplyPageInfo"></span>

                <button id="supplyNext" class="page-btn" type="button">
                    <i class="fi fi-rr-angle-right"></i>
                </button>
            </div>
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

<script src="/scripts/recepInventory.js"></script>

</body>
</html>