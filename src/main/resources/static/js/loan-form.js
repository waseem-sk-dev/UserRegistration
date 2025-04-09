window.addEventListener('DOMContentLoaded', () => {
    // Flash message handling
    const message = document.body.getAttribute("data-message");
    if (message) {
        Swal.fire({
            icon: 'info',
            title: 'Loan Application',
            text: message
        });
    }

    // Interest rate mapping
    const loanTypeSelect = document.getElementById("loanType");
    const interestRateInput = document.getElementById("interestRate");
    const rateDisplay = document.getElementById("rateDisplay");

    const interestRates = {
        Personal: 12.5,
        Home: 8.75,
        Education: 7.5,
        Car: 9.5
    };

    loanTypeSelect.addEventListener("change", () => {
        const selectedType = loanTypeSelect.value;
        const rate = interestRates[selectedType];

        if (rate) {
            interestRateInput.value = rate;
            rateDisplay.textContent = rate + " %";
        } else {
            interestRateInput.value = "";
            rateDisplay.textContent = "Please select a loan type";
        }
    });
});
