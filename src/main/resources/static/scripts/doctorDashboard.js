const doctorProfile = document.getElementById("doctorProfile");
const profileDropdown = document.getElementById("profileDropdown");

if (doctorProfile && profileDropdown) {
    doctorProfile.addEventListener("click", e => {
        e.stopPropagation();
        profileDropdown.classList.toggle("show");
    });

    document.addEventListener("click", () => {
        profileDropdown.classList.remove("show");
    });
}

function updateBadge(id, count) {
    const badge = document.getElementById(id);
    if (badge) {
        badge.textContent = count;
        badge.style.display = "inline-block";
    }
}

updateBadge("messageCount", 0);

const logoutLink = document.querySelector(".profile-dropdown a:last-child");
const logoutModal = document.getElementById("logoutModal");
const cancelBtn = document.getElementById("cancelBtn");
const logoutBtn = document.getElementById("logoutBtn");

if (logoutLink && logoutModal) {
    logoutLink.addEventListener("click", e => {
        e.preventDefault();
        logoutModal.style.display = "flex";
    });
}

if (cancelBtn) {
    cancelBtn.addEventListener("click", () => {
        logoutModal.style.display = "none";
    });
}

if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      const form = document.createElement("form");
      form.method = "POST";
      form.action = "/web/logout";
      document.body.appendChild(form);
      form.submit();
    });
}

createDoughnutChart(
  "patientOverview",
  ["Child", "Teen", "Adult", "Older"],
  [12, 19, 25, 8],
  ["#FFD700", "#1E90FF", "#32CD32", "#9370DB"]
);

function createDoughnutChart(canvasId, labels, dataValues, colors) {
  new Chart(document.getElementById(canvasId), {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [{
        data: dataValues,
        backgroundColor: colors
      }]
    },
    options: {
      responsive: false,
      maintainAspectRatio: false,
      cutout: "50%",
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              const label = context.label || "";
              const value = context.raw;
              const sum = context.chart.data.datasets[0].data.reduce((a,b)=>a+b,0);
              const percentage = ((value / sum) * 100).toFixed(1) + "%";
              return `${label}: (${percentage})`;
            }
          }
        }
      }
    }
  });
}
