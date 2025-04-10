function displayError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    errorElement.innerHTML = message;
    errorElement.style.display = message ? "block" : "none";
}

function validateMobile() {
    const mobile = document.getElementById("mobile").value;
    const mobileRegex = /^[6-9]\d{9}$/;
    if (!mobileRegex.test(mobile)) {
        displayError("mobile-error", "Invalid mobile number (10-digit, starts with 6-9)");
        return false;
    } else {
        displayError("mobile-error", "");
        return true;
    }
}

function validateEmail() {
    const email = document.getElementById("email").value;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        displayError("email-error", "Invalid email format");
        return false;
    } else {
        displayError("email-error", "");
        return true;
    }
}

function validatePassword() {
    const password = document.getElementById("password").value;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
        displayError("password-strength", "Must contain 1 uppercase, 1 special char, min 8 chars");
        return false;
    } else {
        displayError("password-strength", "");
        return true;
    }
}

function validateForm() {
    return validateMobile() && validateEmail() && validatePassword();
}

// Password toggle functionality
document.addEventListener("DOMContentLoaded", () => {
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    togglePassword.addEventListener("click", () => {
        const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);
        togglePassword.classList.toggle("fa-eye");
        togglePassword.classList.toggle("fa-eye-slash");
    });
});
