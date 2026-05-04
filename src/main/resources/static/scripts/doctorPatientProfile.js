function showSection(sectionId, element) {
    document.querySelectorAll('.content-section')
        .forEach(sec => sec.classList.remove('active'));

    const target = document.getElementById(sectionId);
    if (target) target.classList.add('active');

    document.querySelectorAll('.menu-item')
        .forEach(item => item.classList.remove('active'));

    if (element) element.classList.add('active');
}


let currentPage = 1;
let rowsPerPage = 10;

const records = [
    { date: "2026-04-10", tooth: "11, 12", procedure: "Cleaning", dentist: "Dr. Reyes", charged: 1500, paid: 1000, next: "2026-05-10" },
    { date: "2026-03-22", tooth: "23", procedure: "Filling", dentist: "Dr. Santos", charged: 2000, paid: 2000, next: "2026-04-22" },
    { date: "2026-02-18", tooth: "14", procedure: "Root Canal", dentist: "Dr. Cruz", charged: 6500, paid: 3000, next: "2026-03-18" },
    { date: "2026-01-12", tooth: "18", procedure: "Extraction", dentist: "Dr. Lopez", charged: 3000, paid: 1500, next: "2026-02-12" },
    { date: "2025-12-05", tooth: "21", procedure: "Checkup", dentist: "Dr. Reyes", charged: 1000, paid: 1000, next: "2026-01-05" },
    { date: "2025-11-20", tooth: "32", procedure: "Filling", dentist: "Dr. Santos", charged: 2000, paid: 1000, next: "2025-12-20" },
    { date: "2026-04-02", tooth: "15", procedure: "Whitening", dentist: "Dr. Cruz", charged: 3500, paid: 3500, next: "2026-05-02" },
    { date: "2026-03-15", tooth: "16, 17", procedure: "Cleaning", dentist: "Dr. Lopez", charged: 1500, paid: 1500, next: "2026-04-15" },
    { date: "2026-03-01", tooth: "24", procedure: "Filling", dentist: "Dr. Reyes", charged: 2200, paid: 1200, next: "2026-04-01" },
    { date: "2026-02-10", tooth: "28", procedure: "Extraction", dentist: "Dr. Santos", charged: 2800, paid: 2000, next: "2026-03-10" },
    { date: "2026-01-28", tooth: "13", procedure: "Root Canal", dentist: "Dr. Cruz", charged: 7000, paid: 4000, next: "2026-02-28" },
    { date: "2026-01-05", tooth: "22", procedure: "Checkup", dentist: "Dr. Lopez", charged: 1000, paid: 1000, next: "2026-02-05" },
    { date: "2025-12-18", tooth: "31", procedure: "Filling", dentist: "Dr. Reyes", charged: 2000, paid: 1500, next: "2026-01-18" },
    { date: "2025-12-01", tooth: "19", procedure: "Cleaning", dentist: "Dr. Santos", charged: 1500, paid: 1000, next: "2026-01-01" },
    { date: "2025-11-10", tooth: "27", procedure: "Extraction", dentist: "Dr. Cruz", charged: 3200, paid: 2000, next: "2025-12-10" }
];

function renderTable() {
    const tbody = document.getElementById("recordTableBody");
    tbody.innerHTML = "";

    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    const pageData = records.slice(start, end);

    pageData.forEach(r => {
        const balance = r.charged - r.paid;

        tbody.innerHTML += `
            <tr>
                <td>${r.date}</td>
                <td>${r.tooth}</td>
                <td>${r.procedure}</td>
                <td>${r.dentist}</td>
                <td>₱ ${r.charged.toLocaleString()}</td>
                <td>₱ ${r.paid.toLocaleString()}</td>
                <td>₱ ${balance.toLocaleString()}</td>
                <td>${r.next}</td>
            </tr>
        `;
    });

    updatePagination();
}

function updatePagination() {
    const totalPages = Math.ceil(records.length / rowsPerPage);

    document.getElementById("pageInfo").textContent =
        `Page ${currentPage} of ${totalPages}`;

    document.getElementById("prevBtn").disabled = currentPage === 1;
    document.getElementById("nextBtn").disabled = currentPage === totalPages;
}

document.getElementById("prevBtn").addEventListener("click", () => {
    if (currentPage > 1) {
        currentPage--;
        renderTable();
    }
});

document.getElementById("nextBtn").addEventListener("click", () => {
    const totalPages = Math.ceil(records.length / rowsPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        renderTable();
    }
});

document.addEventListener("DOMContentLoaded", renderTable);