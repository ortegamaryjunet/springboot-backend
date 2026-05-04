<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Inventory</title>
	<link rel="stylesheet" href="/css/recepInventory.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-solid-rounded/css/uicons-solid-rounded.css">
</head>
<body>

	<div class="sidebar">
	    <div class="logo">
	        <img src="/images/clinic-logo.png" alt="Clinic Logo">
	    </div>
	
	    <div class="menu">
	        <a href="/web/recepDashboard" class="menu-item">
	            <i class="fi fi-rr-apps"></i>
	            <span>Dashboard</span>
	        </a>
	
	        <a href="/web/recepAppointment" class="menu-item">
	            <i class="fi fi-rr-calendar-clock"></i>
	            <span>Appointment</span>
	        </a>
	
	        <a href="/web/recepPatients" class="menu-item">
	            <i class="fi fi-rr-clipboard-user"></i>
	            <span>Patient</span>
	        </a>
	
	        <a href="/web/recepPaccounts" class="menu-item">
            	<i class="fi fi-rr-id-badge"></i>
            	<span>Patient Account</span>
        	</a>
	
	        <a href="/web/recepInventory" class="menu-item active">
	            <i class="fi fi-rr-boxes"></i>
	            <span>Inventory</span>
	        </a>
	    </div>
	</div>
	
	<div class="main-container">
	
	    <div class="top-header">
	        <div class="page-title">
	            <h1>Inventory</h1>
	            <p>Manage medicine, equipment, and supplies.</p>
	        </div>
	
	        <div class="header-actions">
	            <a href="/web/receptionMessages" class="message-icon">
	                <img src="/images/message-icon.png" alt="Messages">
	                <span class="badge" id="messageCount" style="display:none;">0</span>
	            </a>
	
	            <div class="recept-profile" id="receptProfile">
	                <div class="avatar">
	                    <i class="fi fi-rr-user"></i>
	                </div>
	
	                <div class="recept-info">
	                    <div class="name">Recept Name</div>
	                    <div class="position">Receptionist</div>
	                </div>
	
	                <i class="fi fi-rr-angle-small-down arrow"></i>
	
	                <div class="profile-dropdown" id="profileDropdown">
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
	            <button type="button" class="tab-btn active" data-target="medicineTable">Dental Medicine</button>
	            <button type="button" class="tab-btn" data-target="equipmentTable">Dental Equipment</button>
	            <button type="button" class="tab-btn" data-target="supplyTable">Dental Supplies</button>
	        </div>
	
	        <div class="filter-card">
	            <div class="search-box">
	                <i class="fi fi-rr-search"></i>
	                <input type="text" id="searchInput" placeholder="Search inventory item">
	            </div>
	
	            <div class="right-actions">
	                <button type="button" class="stock-btn" id="viewStockBtn">
	                    <i class="fi fi-rr-triangle-warning"></i>
	                    View Stock
	                    <span class="stock-count">${lowMedicineCount + lowSupplyCount + lowEquipmentCount}</span>
	                </button>
	
	                <a href="/web/recepInventory/add" class="add-btn">
	                    <i class="fi fi-rr-plus"></i>
	                    Add Items
	                </a>
	            </div>
	        </div>
	
	        <div id="medicineTable" class="table-card table-section show" data-type="medicine">
	            <div class="table-header">
	                <h3>Dental Medicine</h3>
	                <p>View medicine information, stock levels, and status.</p>
	            </div>
	
	            <div class="table-wrapper">
	                <table class="inventory-table medicine-table">
	                    <thead>
	                        <tr>
	                            <th>ID</th>
	                            <th>Medicine Name</th>
	                            <th>Generic Name</th>
	                            <th>Category</th>
	                            <th>Form</th>
	                            <th>Dosage</th>
	                            <th>Unit</th>
	                            <th>Qty</th>
	                            <th>Low Stock</th>
	                            <th>Status</th>
	                            <th>Action</th>
	                        </tr>
	                    </thead>
	
	                    <tbody id="medicineTbody">
	                        <c:forEach var="med" items="${medicines}">
	                            <tr class="inventory-row medicine-row"
	                                data-search="${med.id} ${med.medicineName} ${med.genericName} ${med.category} ${med.form} ${med.dosage} ${med.unit} ${med.quantity} ${med.lowStockThreshold} ${med.status}"
	                                data-id="${med.id}"
	                                data-name="${med.medicineName}"
	                                data-generic="${med.genericName}"
	                                data-category="${med.category}"
	                                data-form="${med.form}"
	                                data-dosage="${med.dosage}"
	                                data-unit="${med.unit}"
	                                data-quantity="${med.quantity}"
	                                data-threshold="${med.lowStockThreshold}">
	                                <td>${med.id}</td>
	                                <td>${med.medicineName}</td>
	                                <td>${med.genericName}</td>
	                                <td>${med.category}</td>
	                                <td>${med.form}</td>
	                                <td>${med.dosage}</td>
	                                <td>${med.unit}</td>
	                                <td>${med.quantity}</td>
	                                <td>${med.lowStockThreshold}</td>
	                                <td><span class="status-badge">${med.status}</span></td>
	                                <td>
	                                    <button type="button" class="action-btn edit" onclick="openMedicineEditFromRow(this)">
	                                        <i class="fi fi-rr-edit"></i>
	                                    </button>
	                                </td>
	                            </tr>
	                        </c:forEach>
	
	                        <c:if test="${empty medicines}">
	                            <tr class="empty-data-row">
	                                <td colspan="11">No medicine items found.</td>
	                            </tr>
	                        </c:if>
	                    </tbody>
	                </table>
	            </div>
	
				<div class="pagination">
				    <button type="button" class="page-btn" id="medicinePrev" disabled>
				        <i class="fi fi-rr-angle-left"></i>
				    </button>
				
				    <span id="medicinePageInfo">Page 0 of 0</span>
				
				    <button type="button" class="page-btn next" id="medicineNext" disabled>
				        <i class="fi fi-rr-angle-right"></i>
				    </button>
				</div>
	        </div>
	
	        <div id="equipmentTable" class="table-card table-section" data-type="equipment">
	            <div class="table-header">
	                <h3>Dental Equipment</h3>
	                <p>View equipment information, stock levels, and details.</p>
	            </div>
	
	            <div class="table-wrapper">
	                <table class="inventory-table equipment-table">
	                    <thead>
	                        <tr>
	                            <th>ID</th>
	                            <th>Equipment Name</th>
	                            <th>Brand</th>
	                            <th>Category</th>
	                            <th>Model</th>
	                            <th>Serial Number</th>
	                            <th>Location</th>
	                            <th>Qty</th>
	                            <th>Status</th>
	                            <th>Action</th>
	                        </tr>
	                    </thead>
	
	                    <tbody id="equipmentTbody">
	                        <c:forEach var="equip" items="${equipmentList}">
	                            <tr class="inventory-row equipment-row"
	                                data-search="${equip.id} ${equip.equipmentName} ${equip.brand} ${equip.category} ${equip.modelNumber} ${equip.serialNumber} ${equip.location} ${equip.quantity} ${equip.status}"
	                                data-id="${equip.id}"
	                                data-name="${equip.equipmentName}"
	                                data-brand="${equip.brand}"
	                                data-category="${equip.category}"
	                                data-model="${equip.modelNumber}"
	                                data-serial="${equip.serialNumber}"
	                                data-purchase="${equip.purchaseDate}"
	                                data-warranty="${equip.warrantyDate}"
	                                data-location="${equip.location}"
	                                data-quantity="${equip.quantity}"
	                                data-threshold="${equip.lowStockThreshold}"
	                                data-stock-status="${equip.status}">
	                                <td>${equip.id}</td>
	                                <td>${equip.equipmentName}</td>
	                                <td>${equip.brand}</td>
	                                <td>${equip.category}</td>
	                                <td>${equip.modelNumber}</td>
	                                <td>${equip.serialNumber}</td>
	                                <td>${equip.location}</td>
	                                <td>${equip.quantity}</td>
	                                <td><span class="status-badge">${equip.status}</span></td>
	                                <td>
	                                    <div class="btn-group">
	                                        <button type="button" class="action-btn view" onclick="openEquipmentDetailsFromRow(this)">
	                                            <i class="fi fi-rr-eye"></i>
	                                        </button>
	
	                                        <button type="button" class="action-btn edit" onclick="openEquipmentEditFromRow(this)">
	                                            <i class="fi fi-rr-edit"></i>
	                                        </button>
	                                    </div>
	                                </td>
	                            </tr>
	                        </c:forEach>
	
	                        <c:if test="${empty equipmentList}">
	                            <tr class="empty-data-row">
	                                <td colspan="10">No equipment items found.</td>
	                            </tr>
	                        </c:if>
	                    </tbody>
	                </table>
	            </div>
	
				<div class="pagination">
				    <button type="button" class="page-btn" id="equipmentPrev" disabled>
				        <i class="fi fi-rr-angle-left"></i>
				    </button>
				
				    <span id="equipmentPageInfo">Page 0 of 0</span>
				
				    <button type="button" class="page-btn next" id="equipmentNext" disabled>
				        <i class="fi fi-rr-angle-right"></i>
				    </button>
				</div>
	        </div>
	
	        <div id="supplyTable" class="table-card table-section" data-type="supply">
	            <div class="table-header">
	                <h3>Dental Supplies</h3>
	                <p>View supplies information, stock levels, and status.</p>
	            </div>
	
	            <div class="table-wrapper">
	                <table class="inventory-table supply-table">
	                    <thead>
	                        <tr>
	                            <th>ID</th>
	                            <th>Supply Name</th>
	                            <th>Brand</th>
	                            <th>Category</th>
	                            <th>Unit</th>
	                            <th>Qty</th>
	                            <th>Low Stock</th>
	                            <th>Status</th>
	                            <th>Action</th>
	                        </tr>
	                    </thead>
	
	                    <tbody id="supplyTbody">
	                        <c:forEach var="sup" items="${supplies}">
	                            <tr class="inventory-row supply-row"
	                                data-search="${sup.id} ${sup.supplyName} ${sup.brand} ${sup.category} ${sup.unit} ${sup.quantity} ${sup.lowStockThreshold} ${sup.status}"
	                                data-id="${sup.id}"
	                                data-name="${sup.supplyName}"
	                                data-brand="${sup.brand}"
	                                data-category="${sup.category}"
	                                data-unit="${sup.unit}"
	                                data-quantity="${sup.quantity}"
	                                data-threshold="${sup.lowStockThreshold}">
	                                <td>${sup.id}</td>
	                                <td>${sup.supplyName}</td>
	                                <td>${sup.brand}</td>
	                                <td>${sup.category}</td>
	                                <td>${sup.unit}</td>
	                                <td>${sup.quantity}</td>
	                                <td>${sup.lowStockThreshold}</td>
	                                <td><span class="status-badge">${sup.status}</span></td>
	                                <td>
	                                    <button type="button" class="action-btn edit" onclick="openSupplyEditFromRow(this)">
	                                        <i class="fi fi-rr-edit"></i>
	                                    </button>
	                                </td>
	                            </tr>
	                        </c:forEach>
	
	                        <c:if test="${empty supplies}">
	                            <tr class="empty-data-row">
	                                <td colspan="9">No supply items found.</td>
	                            </tr>
	                        </c:if>
	                    </tbody>
	                </table>
	            </div>
	
				<div class="pagination">
				    <button type="button" class="page-btn" id="supplyPrev" disabled>
				        <i class="fi fi-rr-angle-left"></i>
				    </button>
				
				    <span id="supplyPageInfo">Page 0 of 0</span>
				
				    <button type="button" class="page-btn next" id="supplyNext" disabled>
				        <i class="fi fi-rr-angle-right"></i>
				    </button>
				</div>
	        </div>
	
	    </div>
	</div>
	
	<div id="stockModal" class="modal">
	    <div class="modal-content stock-modal-content">
	        <div class="stock-modal-header">
	            <div>
	                <h2>Stock Monitoring</h2>
	                <p>
	                    <span class="stock-alert-icon">!</span>
	                    <strong>${lowMedicineCount + lowSupplyCount + lowEquipmentCount} low stock</strong>
	                    item(s) need attention.
	                </p>
	            </div>
	
	            <button type="button" class="modal-x" id="closeStockModalBtn">×</button>
	        </div>
	
	        <div class="stock-tabs">
	            <button type="button" class="stock-tab active" data-stock-tab="medicine">Dental Medicine</button>
	            <button type="button" class="stock-tab" data-stock-tab="equipment">Dental Equipment</button>
	            <button type="button" class="stock-tab" data-stock-tab="supply">Dental Supplies</button>
	        </div>
	
	        <div class="stock-tools">
	            <div class="stock-search">
	                <i class="fi fi-rr-search"></i>
	                <input type="text" id="stockSearchInput" placeholder="Search item...">
	            </div>
	
	            <div class="stock-filter">
	                <label>Filter:</label>
	                <select id="stockStatusFilter">
	                    <option value="all">All</option>
	                    <option value="low">Low Stock</option>
	                    <option value="out">Out of Stock</option>
	                    <option value="in">In Stock</option>
	                </select>
	            </div>
	        </div>
	
	        <div class="stock-table-wrap">
	            <table class="stock-table stock-section show" id="stockMedicineTable">
	                <thead>
	                    <tr>
	                        <th>Item Name</th>
	                        <th>Unit</th>
	                        <th>Current Stock</th>
	                        <th>Reorder Level</th>
	                        <th>Suggested Order</th>
	                        <th>Status</th>
	                        <th>Last Updated</th>
	                    </tr>
	                </thead>
	
	                <tbody>
	                    <c:forEach var="med" items="${medicines}">
	                        <tr class="stock-row" data-stock-status="${med.status}" data-search="${med.medicineName} ${med.unit} ${med.status}">
	                            <td>${med.medicineName}</td>
	                            <td>${med.unit}</td>
	                            <td>${med.quantity}</td>
	                            <td>${med.lowStockThreshold}</td>
	                            <td>
	                                <c:choose>
	                                    <c:when test="${med.quantity lt med.lowStockThreshold}">+${med.lowStockThreshold - med.quantity}</c:when>
	                                    <c:otherwise>None</c:otherwise>
	                                </c:choose>
	                            </td>
	                            <td><span class="stock-status">${med.status}</span></td>
	                            <td>${med.updatedAt}</td>
	                        </tr>
	                    </c:forEach>
	                </tbody>
	            </table>
	
	            <table class="stock-table stock-section" id="stockEquipmentTable">
	                <thead>
	                    <tr>
	                        <th>Item Name</th>
	                        <th>Unit</th>
	                        <th>Current Stock</th>
	                        <th>Reorder Level</th>
	                        <th>Suggested Order</th>
	                        <th>Status</th>
	                        <th>Last Updated</th>
	                    </tr>
	                </thead>
	
	                <tbody>
	                    <c:forEach var="equip" items="${equipmentList}">
	                        <tr class="stock-row" data-stock-status="${equip.status}" data-search="${equip.equipmentName} ${equip.status}">
	                            <td>${equip.equipmentName}</td>
	                            <td>Unit</td>
	                            <td>${equip.quantity}</td>
	                            <td>${equip.lowStockThreshold}</td>
	                            <td>
	                                <c:choose>
	                                    <c:when test="${equip.quantity lt equip.lowStockThreshold}">+${equip.lowStockThreshold - equip.quantity}</c:when>
	                                    <c:otherwise>None</c:otherwise>
	                                </c:choose>
	                            </td>
	                            <td><span class="stock-status">${equip.status}</span></td>
	                            <td>${equip.updatedAt}</td>
	                        </tr>
	                    </c:forEach>
	                </tbody>
	            </table>
	
	            <table class="stock-table stock-section" id="stockSupplyTable">
	                <thead>
	                    <tr>
	                        <th>Item Name</th>
	                        <th>Unit</th>
	                        <th>Current Stock</th>
	                        <th>Reorder Level</th>
	                        <th>Suggested Order</th>
	                        <th>Status</th>
	                        <th>Last Updated</th>
	                    </tr>
	                </thead>
	
	                <tbody>
	                    <c:forEach var="sup" items="${supplies}">
	                        <tr class="stock-row" data-stock-status="${sup.status}" data-search="${sup.supplyName} ${sup.unit} ${sup.status}">
	                            <td>${sup.supplyName}</td>
	                            <td>${sup.unit}</td>
	                            <td>${sup.quantity}</td>
	                            <td>${sup.lowStockThreshold}</td>
	                            <td>
	                                <c:choose>
	                                    <c:when test="${sup.quantity lt sup.lowStockThreshold}">+${sup.lowStockThreshold - sup.quantity}</c:when>
	                                    <c:otherwise>None</c:otherwise>
	                                </c:choose>
	                            </td>
	                            <td><span class="stock-status">${sup.status}</span></td>
	                            <td>${sup.updatedAt}</td>
	                        </tr>
	                    </c:forEach>
	                </tbody>
	            </table>
	        </div>
	
	        <div class="stock-modal-footer">
	            <button type="button" id="closeStockModalBtn2">Close</button>
	        </div>
	    </div>
	</div>
	
	<div id="equipmentViewModal" class="modal">
	    <div class="modal-content large-modal">
	        <div class="modal-header">
	            <h2>Equipment Details</h2>
	            <button type="button" class="modal-x" onclick="closeEquipmentViewModal()">×</button>
	        </div>
	
	        <div class="details-grid">
	            <div><label>Equipment Name</label><p id="viewEqName">-</p></div>
	            <div><label>Brand</label><p id="viewEqBrand">-</p></div>
	            <div><label>Category</label><p id="viewEqCategory">-</p></div>
	            <div><label>Model Number</label><p id="viewEqModel">-</p></div>
	            <div><label>Serial Number</label><p id="viewEqSerial">-</p></div>
	            <div><label>Purchase Date</label><p id="viewEqPurchaseDate">-</p></div>
	            <div><label>Warranty Date</label><p id="viewEqWarrantyDate">-</p></div>
	            <div><label>Location</label><p id="viewEqLocation">-</p></div>
	            <div><label>Quantity</label><p id="viewEqQuantity">-</p></div>
	            <div><label>Low Stock Threshold</label><p id="viewEqThreshold">-</p></div>
	            <div><label>Stock Status</label><p id="viewEqStockStatus">-</p></div>
	        </div>
	
	        <div class="modal-actions">
	            <button type="button" onclick="closeEquipmentViewModal()">OK</button>
	        </div>
	    </div>
	</div>
	
	<div id="editMedicineModal" class="modal">
	    <form class="modal-content large-modal" action="/web/inventory/medicine/update" method="post">
	        <div class="modal-header">
	            <h2>Edit Medicine</h2>
	            <button type="button" class="modal-x" onclick="closeEditModals()">×</button>
	        </div>
	
	        <input type="hidden" name="id" id="editMedId">
	
	        <div class="form-grid two-col">
	            <div class="field"><label>Medicine Name</label><input type="text" name="medicineName" id="editMedName" required></div>
	            <div class="field"><label>Generic Name</label><input type="text" name="genericName" id="editMedGeneric"></div>
	            <div class="field"><label>Category</label><input type="text" name="category" id="editMedCategory"></div>
	            <div class="field"><label>Form</label><input type="text" name="form" id="editMedForm"></div>
	            <div class="field"><label>Dosage</label><input type="text" name="dosage" id="editMedDosage"></div>
	            <div class="field"><label>Unit</label><input type="text" name="unit" id="editMedUnit"></div>
	            <div class="field"><label>Quantity</label><input type="number" name="quantity" id="editMedQuantity" required></div>
	            <div class="field"><label>Low Stock Threshold</label><input type="number" name="lowStockThreshold" id="editMedThreshold" required></div>
	        </div>
	
	        <div class="modal-actions">
	            <button type="button" onclick="closeEditModals()">Cancel</button>
	            <button type="submit" class="save-btn">Save Changes</button>
	        </div>
	    </form>
	</div>
	
	<div id="editEquipmentModal" class="modal">
	    <form class="modal-content large-modal" action="/web/inventory/equipment/update" method="post">
	        <div class="modal-header">
	            <h2>Edit Equipment</h2>
	            <button type="button" class="modal-x" onclick="closeEditModals()">×</button>
	        </div>
	
	        <input type="hidden" name="id" id="editEqId">
	
	        <div class="form-grid two-col">
	            <div class="field"><label>Equipment Name</label><input type="text" name="equipmentName" id="editEqName" required></div>
	            <div class="field"><label>Brand</label><input type="text" name="brand" id="editEqBrand"></div>
	            <div class="field"><label>Category</label><input type="text" name="category" id="editEqCategory"></div>
	            <div class="field"><label>Model Number</label><input type="text" name="modelNumber" id="editEqModel"></div>
	            <div class="field"><label>Serial Number</label><input type="text" name="serialNumber" id="editEqSerial"></div>
	            <div class="field"><label>Location</label><input type="text" name="location" id="editEqLocation"></div>
	            <div class="field"><label>Quantity</label><input type="number" name="quantity" id="editEqQuantity" required></div>
	            <div class="field"><label>Low Stock Threshold</label><input type="number" name="lowStockThreshold" id="editEqThreshold" required></div>
	        </div>
	
	        <div class="modal-actions">
	            <button type="button" onclick="closeEditModals()">Cancel</button>
	            <button type="submit" class="save-btn">Save Changes</button>
	        </div>
	    </form>
	</div>
	
	<div id="editSupplyModal" class="modal">
	    <form class="modal-content large-modal" action="/web/inventory/supply/update" method="post">
	        <div class="modal-header">
	            <h2>Edit Supply</h2>
	            <button type="button" class="modal-x" onclick="closeEditModals()">×</button>
	        </div>
	
	        <input type="hidden" name="id" id="editSupId">
	
	        <div class="form-grid two-col">
	            <div class="field"><label>Supply Name</label><input type="text" name="supplyName" id="editSupName" required></div>
	            <div class="field"><label>Brand</label><input type="text" name="brand" id="editSupBrand"></div>
	            <div class="field"><label>Category</label><input type="text" name="category" id="editSupCategory"></div>
	            <div class="field"><label>Unit</label><input type="text" name="unit" id="editSupUnit"></div>
	            <div class="field"><label>Quantity</label><input type="number" name="quantity" id="editSupQuantity" required></div>
	            <div class="field"><label>Low Stock Threshold</label><input type="number" name="lowStockThreshold" id="editSupThreshold" required></div>
	        </div>
	
	        <div class="modal-actions">
	            <button type="button" onclick="closeEditModals()">Cancel</button>
	            <button type="submit" class="save-btn">Save Changes</button>
	        </div>
	    </form>
	</div>
	
	<div id="logoutModal" class="modal">
	    <div class="modal-content small-modal">
	        <h2 class="modal-title">Confirm Logout</h2>
	        <p>Are you sure you want to log out?</p>
	
	        <div class="modal-actions center">
	            <button type="button" id="cancelBtn">Cancel</button>
	            <button type="button" id="logoutBtn">Logout</button>
	        </div>
	    </div>
	</div>
	
	<script src="/scripts/recepInventory.js"></script>
    
</body>
</html>