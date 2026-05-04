let editingRow = null;

let branchCurrentPage = 1;
let branchRowsPerPage = 10;
let filteredBranches = [];

let serviceCurrentPage = 1;
let serviceRowsPerPage = 10;
let filteredServices = [];

function showSection(sectionId, el) {
    document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('.menu-item').forEach(item => item.classList.remove('active'));

    document.getElementById(sectionId)?.classList.add('active');
    el?.classList.add('active');
}

/* SERVICES */
const services = [
    { name: "Complete Partial Denture", price: 8000, status: "Inactive" },
    { name: "Dental Implants", price: 45000, status: "Discontinued" },
    { name: "Invisalign", price: 120000, status: "Active" },
    { name: "Orthodontic", price: 30000, status: "Active" },
    { name: "Porcelain Jacket Crowns", price: 6500, status: "Active" },
    { name: "Removable Partial Denture", price: 7500, status: "Active" },
    { name: "Smile Make-Overs", price: 25000, status: "Active" },
    { name: "Teeth Whitening", price: 3500, status: "Active" },
    { name: "Veneers", price: 12000, status: "Active" }
];

function initServiceEvents() {
    document.getElementById("serviceSearch").addEventListener("input", renderServices);
    document.getElementById("serviceStatusFilter").addEventListener("change", renderServices);
    document.getElementById("serviceSortFilter").addEventListener("change", renderServices);

    document.getElementById("service-prevBtn").addEventListener("click", () => {
        if (serviceCurrentPage > 1) {
            serviceCurrentPage--;
            renderServices();
        }
    });

    document.getElementById("service-nextBtn").addEventListener("click", () => {
        const totalPages = Math.ceil(filteredServices.length / serviceRowsPerPage);
        if (serviceCurrentPage < totalPages) {
            serviceCurrentPage++;
            renderServices();
        }
    });
}

function getServiceStatus(status) {
    const s = status.toLowerCase();

    if (s === "active") {
        return `
            <span class="service-status active">
                <i class="fi fi-rr-check-circle"></i> Active
            </span>
        `;
    }

    if (s === "inactive") {
        return `
            <span class="service-status inactive">
                <i class="fi fi-rr-cross-circle"></i> Inactive
            </span>
        `;
    }

    if (s === "discontinued") {
        return `
            <span class="service-status discontinued">
                <i class="fi fi-rr-pause"></i> Discontinued
            </span>
        `;
    }

    return status;
}

function renderServices() {
    const search = document.getElementById("serviceSearch").value.toLowerCase();
    const status = document.getElementById("serviceStatusFilter").value;
    const sort = document.getElementById("serviceSortFilter").value;
    const tbody = document.getElementById("serviceTableBody");

    filteredServices = services.filter(s => {
        const matchSearch = s.name.toLowerCase().includes(search);
        const matchStatus = status === "All" || s.status === status;
        return matchSearch && matchStatus;
    });

    if (sort === "az") filteredServices.sort((a, b) => a.name.localeCompare(b.name));
    if (sort === "za") filteredServices.sort((a, b) => b.name.localeCompare(a.name));
    if (sort === "low") filteredServices.sort((a, b) => a.price - b.price);
    if (sort === "high") filteredServices.sort((a, b) => b.price - a.price);

    const start = (serviceCurrentPage - 1) * serviceRowsPerPage;
    const end = start + serviceRowsPerPage;
    const pageData = filteredServices.slice(start, end);

    tbody.innerHTML = "";

    pageData.forEach(s => {
        tbody.innerHTML += `
            <tr>
                <td>${s.name}</td>
                <td>₱ ${s.price.toLocaleString()}</td>
                <td>${getServiceStatus(s.status)}</td>
                <td>
                    <button class="edit-btn">
                        <i class="fi fi-rr-file-edit"></i>
                    </button>
                </td>
            </tr>
        `;
    });

    updateServicePagination();
}

function updateServicePagination() {
    const totalPages = Math.ceil(filteredServices.length / serviceRowsPerPage);
    const pageInfo = document.getElementById("service-pageInfo");

    pageInfo.textContent =
        totalPages === 0 ? "Page 0 of 0" : `Page ${serviceCurrentPage} of ${totalPages}`;

    document.getElementById("service-prevBtn").disabled = serviceCurrentPage === 1;
    document.getElementById("service-nextBtn").disabled =
        serviceCurrentPage >= totalPages || totalPages === 0;
}

/* BRANCHES */
const branches = [
    { name: "Smile Empress Dental Hub", est: "2023-01-15", opened: "2023-02-01", location: "Makati City", type: "Main Branch", contact: "09171234567", person: "Twinky Belino", email: "main@clinic.com", status: "Active" },
    { name: "Smile Empress Dental Clinic", est: "2022-06-10", opened: "2022-07-01", location: "Las Pinas City", type: "Branch", contact: "09181234567", person: "Anna Reyes", email: "brightsmile@clinic.com", status: "Inactive" },
];

function initBranchEvents() {
    document.getElementById("branchSearch").addEventListener("input", renderBranches);
    document.getElementById("branchStatusFilter").addEventListener("change", renderBranches);

    document.getElementById("prevBtn").addEventListener("click", () => {
        if (branchCurrentPage > 1) {
            branchCurrentPage--;
            renderBranches();
        }
    });

    document.getElementById("nextBtn").addEventListener("click", () => {
        const totalPages = Math.ceil(filteredBranches.length / branchRowsPerPage);
        if (branchCurrentPage < totalPages) {
            branchCurrentPage++;
            renderBranches();
        }
    });
}

function getBranchStatus(status) {
    const s = status.toLowerCase();

	if (s === "active") {
	    return `
	        <span class="branch-status active">
	            <i class="fi fi-rr-check-circle"></i> Active
	        </span>
	    `;
	}

	if (s === "inactive") {
	    return `
	        <span class="branch-status inactive">
	            <i class="fi fi-rr-cross-circle"></i> Inactive
	        </span>
	    `;
	}

	if (s === "renovation") {
	    return `
	        <span class="branch-status renovation">
	            <i class="fi fi-rr-tool-box"></i> Renovation
	        </span>
	    `;
	}

	if (s === "opening") {
	    return `
	        <span class="branch-status opening">
	            <i class="fi fi-rr-hourglass-start"></i> Opening Soon
	        </span>
	    `;
	}

	if (s === "closed") {
	    return `
	        <span class="branch-status closed">
	            <i class="fi fi-rr-door-closed"></i> Closed
	        </span>
	    `;
	}

	return status;
}

function renderBranches() {
    const search = document.getElementById("branchSearch").value.toLowerCase();
    const status = document.getElementById("branchStatusFilter").value;
    const tbody = document.getElementById("branchTableBody");

    filteredBranches = branches.filter(b => {
        const matchSearch =
            b.name.toLowerCase().includes(search) ||
            b.location.toLowerCase().includes(search);

        const matchStatus = status === "All" || b.status === status;

        return matchSearch && matchStatus;
    });

    const start = (branchCurrentPage - 1) * branchRowsPerPage;
    const end = start + branchRowsPerPage;
    const pageData = filteredBranches.slice(start, end);

    tbody.innerHTML = "";

    pageData.forEach(b => {
        tbody.innerHTML += `
            <tr>
                <td>${b.name}</td>
                <td>${b.est}</td>
                <td>${b.opened}</td>
                <td>${b.location}</td>
                <td>${b.type}</td>
                <td>${b.contact}</td>
                <td>${b.person}</td>
                <td>${b.email}</td>
                <td>${getBranchStatus(b.status)}</td>
                <td>
                    <button class="edit-btn" onclick="openUpdateOverlay(this)">
                        <i class="fi fi-rr-file-edit"></i>
                    </button>
                </td>
            </tr>
        `;
    });

    updateBranchPagination();
}

function updateBranchPagination() {
    const totalPages = Math.ceil(filteredBranches.length / branchRowsPerPage);
    const pageInfo = document.getElementById("pageInfo");

    pageInfo.textContent =
        totalPages === 0 ? "Page 0 of 0" : `Page ${branchCurrentPage} of ${totalPages}`;

    document.getElementById("prevBtn").disabled = branchCurrentPage === 1;
    document.getElementById("nextBtn").disabled =
        branchCurrentPage >= totalPages || totalPages === 0;
}

document.addEventListener("DOMContentLoaded", () => {
    initServiceEvents();
    initBranchEvents();

    renderServices();
    renderBranches();
});