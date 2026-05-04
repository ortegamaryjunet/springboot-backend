document.addEventListener("DOMContentLoaded", function () {

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

    if (type === "error") {
        showModal("errorModal", "errorMessage");
    }

    if (type === "validation") {
        showModal("validationModal", "validationMessage");
    }

    if (type === "password") {
        showModal("passwordPolicyModal", "passwordPolicyMessage");
    }

    if (type === "success") {
        showModal("successModal", "successMessage");
    }

    const errorOk = document.getElementById("errorOk");
    if (errorOk) {
        errorOk.onclick = function () {
            document.getElementById("errorModal").style.display = "none";
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

    const successOk = document.getElementById("successOk");
    if (successOk) {
        successOk.onclick = function () {
            document.getElementById("successModal").style.display = "none";
        };
    }

    const backBtn = document.getElementById("back-btn");
    const confirmModal = document.getElementById("confirmModal");
    const confirmYes = document.getElementById("confirmYes");
    const confirmNo = document.getElementById("confirmNo");

    const form = document.getElementById("loginForm");
    const inputs = form ? form.querySelectorAll("input") : [];

    function hasInput() {
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].value.trim() !== "") {
                return true;
            }
        }
        return false;
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
            window.location.href = "/role";
        };
    }

    if (confirmNo && confirmModal) {
        confirmNo.onclick = function () {
            confirmModal.style.display = "none";
        };
    }

});