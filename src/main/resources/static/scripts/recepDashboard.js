document.addEventListener("DOMContentLoaded", function () {
    const receptProfile = document.getElementById("receptProfile");
    const profileDropdown = document.getElementById("profileDropdown");

    if (receptProfile && profileDropdown) {
        receptProfile.addEventListener("click", function (e) {
            e.stopPropagation();
            profileDropdown.classList.toggle("show");
        });

        document.addEventListener("click", function (e) {
            if (!receptProfile.contains(e.target)) {
                profileDropdown.classList.remove("show");
            }
        });
    }

    function updateBadge(id, count) {
        const badge = document.getElementById(id);

        if (!badge) {
            return;
        }

        badge.textContent = count;
        badge.style.display = Number(count) > 0 ? "flex" : "none";
    }

    updateBadge("messageCount", 0);

    const logoutLink = document.getElementById("logoutLink");
    const logoutModal = document.getElementById("logoutModal");
    const cancelBtn = document.getElementById("cancelBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutLink && logoutModal) {
        logoutLink.addEventListener("click", function (e) {
            e.preventDefault();
            logoutModal.style.display = "flex";
        });
    }

    if (cancelBtn && logoutModal) {
        cancelBtn.addEventListener("click", function () {
            logoutModal.style.display = "none";
        });
    }

    if (logoutModal) {
        logoutModal.addEventListener("click", function (e) {
            if (e.target === logoutModal) {
                logoutModal.style.display = "none";
            }
        });
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", function () {
            window.location.href = "/";
        });
    }
});