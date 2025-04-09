window.addEventListener('DOMContentLoaded', () => {
    // SweetAlert2 Flash Message
    const flashMessage = document.getElementById("flash-message");
    if (flashMessage) {
        const message = flashMessage.dataset.message;
        const type = flashMessage.dataset.type || "info";

        Swal.fire({
            toast: true,
            position: "top-end",
            icon: type,
            title: message,
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
    }

    // Classic Message Fade-Out (fallback)
    const messageBox = document.querySelector('.message');
    if (messageBox) {
        setTimeout(() => {
            messageBox.style.transition = "opacity 0.6s";
            messageBox.style.opacity = '0';
            setTimeout(() => messageBox.remove(), 600);
        }, 3000);
    }

    // Profile Dropdown Toggle
    const toggleBtn = document.getElementById("profileToggle");
    const panel = document.getElementById("profilePanel");

    if (toggleBtn && panel) {
        toggleBtn.addEventListener("click", function () {
            panel.style.display = (panel.style.display === "block") ? "none" : "block";
        });

        // Optional: Click outside to close
        document.addEventListener("click", function (event) {
            if (!toggleBtn.contains(event.target) && !panel.contains(event.target)) {
                panel.style.display = "none";
            }
        });
    }
});
