function displayError(elementId, message) {
    let errorElement = document.getElementById(elementId);
    errorElement.innerHTML = message;
    errorElement.style.display = message ? "block" : "none";
}

// Validate Mobile Number (10-digit, starts with 6-9)
function validateMobile() {
    let mobile = document.getElementById("mobile").value;
    let mobileRegex = /^[6-9]\d{9}$/;
    if (!mobileRegex.test(mobile)) {
        displayError("mobile-error", "Invalid mobile number (10-digit, starts with 6-9)");
        return false;
    } else {
        displayError("mobile-error", "");
        return true;
    }
}

// Validate Email
function validateEmail() {
    let email = document.getElementById("email").value;
    let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        displayError("email-error", "Invalid email format");
        return false;
    } else {
        displayError("email-error", "");
        return true;
    }
}

// Validate Password (at least 1 uppercase, 1 special character, 8+ chars)
function validatePassword() {
    let password = document.getElementById("password").value;
    let passwordRegex = /^(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
        displayError("password-strength", "Must contain 1 uppercase, 1 special char, min 8 chars");
        return false;
    } else {
        displayError("password-strength", "");
        return true;
    }
}

// Validate Form Before Submission
function validateForm() {
    return validateMobile() && validateEmail() && validatePassword();
}
