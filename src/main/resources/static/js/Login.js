document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    const usernameOrEmail = document.getElementById("usernameOrEmail");
    const rawPassword = document.getElementById("rawPassword");

    // ===== Show SweetAlert popup from backend message (login error OR logout success) =====
    const flash = document.getElementById("flashMessage");
    if (flash) {
        const msg = flash.getAttribute("data-message");
        Swal.fire({
            icon: msg.toLowerCase().includes("success") ? 'success' : 'error',
            title: msg.toLowerCase().includes("success") ? 'Logged Out' : 'Login Failed',
            text: msg,
            confirmButtonColor: '#3085d6'
        });
    }

    loginForm.addEventListener("submit", function (event) {
        clearErrors();

        const identifier = usernameOrEmail.value.trim();
        const password = rawPassword.value.trim();
        let isValid = true;

        if (!identifier || (identifier.includes("@") && !validateEmail(identifier))) {
            showError(usernameOrEmail, "Enter a valid email or username.");
            isValid = false;
        }

        if (!validatePassword(password)) {
            showError(rawPassword, "Password must be at least 6 characters.");
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        } else {
            Swal.fire({
                title: 'Logging in...',
                didOpen: () => Swal.showLoading(),
                allowOutsideClick: false,
                showConfirmButton: false
            });
        }
    });

    rawPassword.addEventListener("input", () => {
        const password = rawPassword.value.trim();
        if (!validatePassword(password)) {
            showError(rawPassword, "Password must be at least 6 characters.");
        } else {
            clearError(rawPassword);
        }
    });

    function validateEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    function validatePassword(password) {
        return password.length >= 6;
    }

    function showError(input, message) {
        const error = input.nextElementSibling;
        error.textContent = message;
        input.classList.add("error-border");
    }

    function clearError(input) {
        const error = input.nextElementSibling;
        error.textContent = "";
        input.classList.remove("error-border");
    }

    function clearErrors() {
        document.querySelectorAll(".error-message").forEach(e => e.textContent = "");
        document.querySelectorAll(".error-border").forEach(e => e.classList.remove("error-border"));
    }
});
