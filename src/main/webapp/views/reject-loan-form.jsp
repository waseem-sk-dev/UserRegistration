<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reject Loan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loan-details.css">
</head>
<body>

<div class="loan-reject-form-container">
    <h2>Reject Loan Application</h2>

    <form action="${pageContext.request.contextPath}/admin/loan/reject" method="post">
        <input type="hidden" name="loanId" value="${loan.id}" />

        <label for="reason">Rejection Reason:</label><br/>
        <textarea name="reason" id="reason" rows="5" cols="50" required></textarea><br/>

        <button type="submit" class="btn reject-btn">Submit Rejection</button>
        <a href="${pageContext.request.contextPath}/admin/loans" class="btn">Cancel</a>
    </form>
</div>

</body>
</html>
