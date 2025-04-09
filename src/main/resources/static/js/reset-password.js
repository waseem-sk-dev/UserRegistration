document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const newPassword = form.querySelector("input[name='newPassword']");
    const confirmPassword = form.querySelector("input[name='confirmPassword']");

    form.addEventListener("submit", (e) => {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            Swal.fire({
                icon: "error",
                title: "Password Mismatch",
                text: "New password and confirm password do not match.",
                confirmButtonColor: "#d33"
            });
        } else if (newPassword.value.length < 8) {
            e.preventDefault();
            Swal.fire({
                icon: "warning",
                title: "Weak Password",
                text: "Password should be at least 8 characters long.",
                confirmButtonColor: "#f39c12"
            });
        }
    });

    // Show server messages if any
    const success = document.querySelector(".success-message");
    const error = document.querySelector(".error-message");

    if (success && success.textContent.trim()) {
        Swal.fire({
            icon: "success",
            title: "Success",
            text: success.textContent.trim(),
            confirmButtonColor: "#5c6bc0"
        });
    }

    if (error && error.textContent.trim()) {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: error.textContent.trim(),
            confirmButtonColor: "#d33"
        });
    }
});
