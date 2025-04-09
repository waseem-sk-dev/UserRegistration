<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Forgot Password</title>
    
    <!-- Include modern CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgot-password.css">
    
    <!-- SweetAlert2 for popup messages -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/forgot-password.js" defer></script>
</head>
<body>
    <div class="container">
        <h2>Forgot Password</h2>

        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
            <label for="emailOrUsername">Enter Email or Username:</label>
            <input type="text" name="emailOrUsername" required />
            <input type="submit" value="Send Reset Link" />
        </form>

        <p class="success-message">${message}</p>
        <p class="error-message">${error}</p>
    </div>
</body>
</html>
