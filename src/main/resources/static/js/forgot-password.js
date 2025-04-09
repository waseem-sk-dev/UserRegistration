document.addEventListener("DOMContentLoaded", () => {
    const successMessage = document.querySelector(".success-message");
    const errorMessage = document.querySelector(".error-message");

    if (successMessage && successMessage.textContent.trim()) {
        Swal.fire({
            icon: "success",
            title: "Link Sent!",
            text: successMessage.textContent.trim(),
            confirmButtonColor: "#5c6bc0"
        });
    }

    if (errorMessage && errorMessage.textContent.trim()) {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: errorMessage.textContent.trim(),
            confirmButtonColor: "#d33"
        });
    }
});
