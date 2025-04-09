<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Apply for Loan</title>

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loan-form.css">

    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>

    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/loan-form.js" defer></script>
</head>
<body>

<div class="form-container">
    <h2>Loan Application</h2>

    <form action="${pageContext.request.contextPath}/apply-loan" method="post" class="loan-form">
        <label for="loanAmount">Loan Amount:</label>
        <input type="number" name="loanAmount" id="loanAmount" required />

        <label for="loanType">Loan Type:</label>
        <select name="loanType" id="loanType" required>
            <option value="">--Select--</option>
            <option>Personal</option>
            <option>Home</option>
            <option>Education</option>
            <option>Car</option>
        </select>

        <input type="hidden" name="interestRate" id="interestRate" />
        <p><strong>Interest Rate (%):</strong> <span id="rateDisplay">Please select a loan type</span></p>

        <label for="loanDuration">Duration (months):</label>
        <input type="number" name="loanDuration" id="loanDuration" required />

        <label for="annualIncome">Annual Income:</label>
        <input type="number" name="annualIncome" id="annualIncome" required />

        <label for="purpose">Purpose:</label>
        <textarea name="purpose" id="purpose" required></textarea>

        <button type="submit" class="btn-submit">Submit</button>
    </form>
</div>

</body>
</html>
