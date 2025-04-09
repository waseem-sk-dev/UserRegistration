<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Reset Password</title>

    <!-- Modern CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset-password.css" />

    <!-- SweetAlert2 CDN -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>

    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/reset-password.js" defer></script>
</head>
<body>
    <div class="container">
        <h2>Reset Your Password</h2>

        <form action="${pageContext.request.contextPath}/reset-password" method="post">
            <input type="hidden" name="token" value="${token}" />

            <label for="newPassword">New Password:</label>
            <input type="password" name="newPassword" required />

            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" name="confirmPassword" required />

            <input type="submit" value="Reset Password" />
        </form>

        <p class="success-message">${message}</p>
        <p class="error-message">${error}</p>
    </div>
</body>
</html>
