document.addEventListener("DOMContentLoaded", function () {
    const resetForm = document.getElementById("resetForm");
    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");

    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modalTitle");
    const modalMessage = document.getElementById("modalMessage");
    const modalOk = document.getElementById("modalOk");

    function showModal(title, message) {
        modalTitle.innerText = title;
        modalMessage.innerText = message;
        modal.style.display = "flex";
    }

    resetForm.addEventListener("submit", function (e) {
        const passwordValue = newPassword.value.trim();
        const confirmValue = confirmPassword.value.trim();

        if (!passwordValue || !confirmValue) {
            e.preventDefault();
            showModal("Error", "All fields are required.");
            return;
        }

        if (passwordValue.length < 6) {
            e.preventDefault();
            showModal("Error", "Password must be at least 6 characters.");
            return;
        }

        if (passwordValue !== confirmValue) {
            e.preventDefault();
            showModal("Error", "Passwords do not match.");
        }
    });

    modalOk.addEventListener("click", function () {
        modal.style.display = "none";
    });
});