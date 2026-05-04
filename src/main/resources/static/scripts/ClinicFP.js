document.addEventListener("DOMContentLoaded", function () {
    const backBtn = document.getElementById("back-btn");
    const confirmModal = document.getElementById("confirmModal");
    const confirmYes = document.getElementById("confirmYes");
    const confirmNo = document.getElementById("confirmNo");

    const form = document.getElementById("forgotForm");
    const email = document.getElementById("email");

    const emptyEmailModal = document.getElementById("emptyEmailModal");
    const emptyEmailOk = document.getElementById("emptyEmailOk");

	function goBackToLogin() {
	    if (typeof loginType !== "undefined" && loginType === "ADMIN") {
	        window.location.href = "/adminLogin";
	    } else if (typeof loginType !== "undefined" && loginType === "DENTIST") {
	        window.location.href = "/doctorLogin";
	    } else if (typeof loginType !== "undefined" && loginType === "RECEPTIONIST") {
	        window.location.href = "/receptLogin";
	    } else {
	        window.location.href = "/role";
	    }
	}

    if (backBtn) {
        backBtn.addEventListener("click", function (e) {
            e.preventDefault();

            if (email && email.value.trim() !== "") {
                confirmModal.style.display = "flex";
            } else {
                goBackToLogin();
            }
        });
    }

    if (confirmYes) {
        confirmYes.addEventListener("click", function () {
            goBackToLogin();
        });
    }

    if (confirmNo && confirmModal) {
        confirmNo.addEventListener("click", function () {
            confirmModal.style.display = "none";
        });
    }

    if (form && email && emptyEmailModal) {
        form.addEventListener("submit", function (e) {
            if (email.value.trim() === "") {
                e.preventDefault();
                emptyEmailModal.style.display = "flex";
            }
        });
    }

    if (emptyEmailOk && emptyEmailModal) {
        emptyEmailOk.addEventListener("click", function () {
            emptyEmailModal.style.display = "none";
            email.focus();
        });
    }
	
	if (typeof backendModalType !== "undefined" && backendModalType === "error") {
	    emptyEmailModal.style.display = "flex";

	    const modalTitle = emptyEmailModal.querySelector(".modal-title");
	    const modalMessage = emptyEmailModal.querySelector("p");

	    if (modalTitle) {
	        modalTitle.innerText = "Email Not Found";
	    }

	    if (modalMessage) {
	        modalMessage.innerText = backendModalMessage || "Email does not exist.";
	    }
	}
	
});