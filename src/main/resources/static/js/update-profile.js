document.addEventListener("DOMContentLoaded", function () {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener("click", () => {
            const input = checkbox.parentElement.nextElementSibling;
            if (input && input.tagName === 'INPUT') {
                input.disabled = !input.disabled;
                if (!input.disabled) {
                    input.classList.add("active-field");
                    input.focus();
                } else {
                    input.classList.remove("active-field");
                    input.value = "";  // Clear if re-disabled
                }
            }
        });
    });
});
