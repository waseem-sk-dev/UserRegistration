<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Loan Application Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loan-details.css">
</head>
<body>

<div class="loan-details-container">
    <h2 class="title">Loan Application Details</h2>

    <div class="loan-card">
        <table class="loan-table">
            <tr><th>Loan ID:</th><td>${loan.id}</td></tr>
            <tr><th>User Email:</th><td>${loan.user.email}</td></tr>
            <tr><th>Loan Type:</th><td>${loan.loanType}</td></tr>
            <tr><th>Amount:</th><td>${loan.loanAmount}</td></tr>
            <tr><th>Duration:</th><td>${loan.loanDuration} months</td></tr>
            <tr><th>Interest Rate:</th><td>${loan.interestRate}%</td></tr>
            <tr><th>Annual Income:</th><td>${loan.annualIncome}</td></tr>
            <tr><th>Purpose:</th><td>${loan.purpose}</td></tr>
            <tr><th>Status:</th>
                <td>
                    <span class="status-badge 
                        ${loan.status eq 'APPROVED' ? 'approved' : 
                          loan.status eq 'REJECTED' ? 'rejected' : 
                          'pending'}">
                        ${loan.status}
                    </span>
                </td>
            </tr>
            <c:if test="${loan.status eq 'REJECTED'}">
                <tr><th>Rejection Reason:</th><td>${loan.rejectionReason}</td></tr>
            </c:if>
        </table>

        <div class="back-btn">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn">Back to Dashboard</a>
        </div>
    </div>
</div>

</body>
</html>
