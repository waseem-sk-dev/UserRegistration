window.addEventListener('DOMContentLoaded', () => {
    console.log("Admin Dashboard loaded");

    // 1. SweetAlert2 toast support via hidden flash element
    const flashSuccess = document.getElementById("flash-success");
    if (flashSuccess) {
        const message = flashSuccess.getAttribute("data-message");
        if (message) {
            Swal.fire({
                toast: true,
                icon: 'success',
                title: message,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });
        }
    }

    // 2. Classic .message element fade-out fallback
    const message = document.querySelector('.message');
    if (message) {
        message.style.transition = 'opacity 0.5s ease';
        setTimeout(() => {
            message.style.opacity = '0';
            setTimeout(() => {
                message.style.display = 'none';
            }, 500);
        }, 3000);
    }
});
