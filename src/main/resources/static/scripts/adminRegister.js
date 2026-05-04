window.onload = function () {
    const type = typeof modalType !== "undefined" ? modalType : "";
    const message = typeof modalMessage !== "undefined" ? modalMessage : "";

    function showModal(modalId, messageId) {
        const modal = document.getElementById(modalId);
        const messageElement = document.getElementById(messageId);

        if (messageElement && message) {
            messageElement.innerText = message;
        }

        if (modal) {
            modal.style.display = "flex";
        }
    }

    if (type === "success") {
        showModal("successModal", "successMessage");
    }

    if (type === "error") {
        showModal("validationModal", "validationMessage");
    }

    if (type === "validation") {
        showModal("validationModal", "validationMessage");
    }

    if (type === "password") {
        showModal("passwordPolicyModal", "passwordPolicyMessage");
    }

    const successOk = document.getElementById("successOk");

    if (successOk) {
        successOk.onclick = function () {
            document.getElementById("successModal").style.display = "none";
            window.location.href = "/adminLogin";
        };
    }

    const validationOk = document.getElementById("validationOk");

    if (validationOk) {
        validationOk.onclick = function () {
            document.getElementById("validationModal").style.display = "none";
        };
    }

    const passwordPolicyOk = document.getElementById("passwordPolicyOk");

    if (passwordPolicyOk) {
        passwordPolicyOk.onclick = function () {
            document.getElementById("passwordPolicyModal").style.display = "none";
        };
    }

    const backBtn = document.getElementById("back-btn");
    const confirmModal = document.getElementById("confirmModal");
    const confirmYes = document.getElementById("confirmYes");
    const confirmNo = document.getElementById("confirmNo");

    const form = document.getElementById("registerForm");
    const inputs = form ? form.querySelectorAll("input") : [];

    function hasInput() {
        return Array.from(inputs).some(input => input.value.trim() !== "");
    }

    if (backBtn && confirmModal) {
        backBtn.addEventListener("click", function (e) {
            if (hasInput()) {
                e.preventDefault();
                confirmModal.style.display = "flex";
            }
        });
    }

    if (confirmYes) {
        confirmYes.onclick = function () {
            window.location.href = "/adminLogin";
        };
    }

    if (confirmNo && confirmModal) {
        confirmNo.onclick = function () {
            confirmModal.style.display = "none";
        };
    }
};