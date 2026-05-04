window.onload = function () {
    const inputs = document.querySelectorAll(".otp-inputs input");
    const otpForm = document.getElementById("otpForm");
    const otpValue = document.getElementById("otpValue");

    const countdown = document.getElementById("countdown");
    const timerText = document.getElementById("timerText");
    const resendBtn = document.getElementById("resendBtn");
    const resendForm = document.getElementById("resendForm");

    const emptyOtpModal = document.getElementById("emptyOtpModal");
    const emptyOtpOk = document.getElementById("emptyOtpOk");

    const confirmModal = document.getElementById("confirmModal");
    const backBtn = document.getElementById("back-btn");
    const confirmYes = document.getElementById("confirmYes");
    const confirmNo = document.getElementById("confirmNo");

    const otpMessageModal = document.getElementById("otpMessageModal");
    const otpModalTitle = document.getElementById("otpModalTitle");
    const otpModalMessageText = document.getElementById("otpModalMessage");
    const otpMessageOk = document.getElementById("otpMessageOk");

    const otpErrorModal = document.getElementById("otpErrorModal");
    const otpErrorMessage = document.getElementById("otpErrorMessage");
    const otpErrorOk = document.getElementById("otpErrorOk");

    const limitOverlay = document.getElementById("limitOverlay");

    let timeLeft = typeof backendRemainingTime !== "undefined" ? backendRemainingTime : 60;
    const resendCount = typeof backendResendCount !== "undefined" ? backendResendCount : 0;
    const maxResend = typeof backendMaxResend !== "undefined" ? backendMaxResend : 3;

    function showModal(modal) {
        if (modal) {
            modal.style.display = "flex";
        }
    }

    function hideModal(modal) {
        if (modal) {
            modal.style.display = "none";
        }
    }

    function showOtpMessage(title, message) {
        if (otpMessageModal && otpModalTitle && otpModalMessageText) {
            otpModalTitle.innerText = title;
            otpModalMessageText.innerText = message;
            showModal(otpMessageModal);
            return;
        }

        if (otpErrorModal && otpErrorMessage) {
            otpErrorMessage.innerText = message;
            showModal(otpErrorModal);
        }
    }

    function goBackByPurpose() {
        if (typeof otpPurpose !== "undefined" && otpPurpose === "REGISTER") {
            window.location.href = "/adminRegister";
            return;
        }

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

    if (typeof otpModalType !== "undefined" && otpModalType !== "") {
        if (otpModalType === "error") {
            showOtpMessage("Incorrect OTP", otpModalMessage || "The verification code is incorrect.");
        }

        if (otpModalType === "expired") {
            showOtpMessage("OTP Expired", otpModalMessage || "OTP has expired.");
        }

        if (otpModalType === "resent") {
            showOtpMessage("OTP Sent", otpModalMessage || "A new OTP has been sent.");
        }

        if (otpModalType === "limit") {
            showModal(limitOverlay);
        }
    }

    if (otpMessageOk) {
        otpMessageOk.onclick = function () {
            hideModal(otpMessageModal);
        };
    }

    if (otpErrorOk) {
        otpErrorOk.onclick = function () {
            hideModal(otpErrorModal);
        };
    }

    inputs.forEach((input, index) => {
        input.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, "");

            if (this.value && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener("keydown", function (e) {
            if (e.key === "Backspace" && !this.value && index > 0) {
                inputs[index - 1].focus();
            }
        });

        input.addEventListener("paste", function (e) {
            e.preventDefault();

            const pasted = e.clipboardData.getData("text").replace(/[^0-9]/g, "");

            if (pasted.length === 6) {
                inputs.forEach((box, i) => {
                    box.value = pasted[i] || "";
                });
            }
        });
    });

    if (otpForm) {
        otpForm.addEventListener("submit", function (e) {
            let otp = "";

            inputs.forEach(input => {
                otp += input.value.trim();
            });

            if (otp.length !== 6) {
                e.preventDefault();
                showModal(emptyOtpModal);
                return;
            }

            if (otpValue) {
                otpValue.value = otp;
            }
        });
    }

    if (emptyOtpOk) {
        emptyOtpOk.onclick = function () {
            hideModal(emptyOtpModal);
            if (inputs.length > 0) {
                inputs[0].focus();
            }
        };
    }

    function updateCountdown() {
        if (!countdown || !timerText) {
            return;
        }

		if (timeLeft <= 0) {
		    countdown.innerText = "0";
		    timerText.innerHTML = "OTP expired. Please request a new code.";

		    const resendText = document.getElementById("resendText");

		    if (resendText) {
		        resendText.style.display = "block";
		    }

		    if (resendBtn && resendCount < maxResend) {
		        resendBtn.classList.remove("disabled");
		        resendBtn.style.pointerEvents = "auto";
		    }

		    return;
		}

        countdown.innerText = timeLeft;

        if (resendBtn) {
            resendBtn.classList.add("disabled");
            resendBtn.style.pointerEvents = "none";
        }

        timeLeft--;

        setTimeout(updateCountdown, 1000);
    }

    updateCountdown();

    if (resendBtn) {
        if (resendCount >= maxResend) {
            resendBtn.classList.add("disabled");
            resendBtn.style.pointerEvents = "none";
        }

        resendBtn.addEventListener("click", function (e) {
            e.preventDefault();

            if (resendCount >= maxResend) {
                showModal(limitOverlay);
                return;
            }

            if (timeLeft > 0) {
                showOtpMessage("Please Wait", "You can request a new OTP after the current code expires.");
                return;
            }

            if (resendForm) {
                resendForm.submit();
            } else {
                showOtpMessage("Resend Not Available", "Resend OTP is not connected yet.");
            }
        });
    }

    if (backBtn && confirmModal) {
        backBtn.addEventListener("click", function (e) {
            e.preventDefault();
            showModal(confirmModal);
        });
    }

    if (confirmYes) {
        confirmYes.onclick = function () {
            goBackByPurpose();
        };
    }

    if (confirmNo) {
        confirmNo.onclick = function () {
            hideModal(confirmModal);
        };
    }
};