<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <!-- External CSS for styling -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

    <!-- Font Awesome for eye icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <!-- SweetAlert2 CDN -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>

    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/login.js" defer></script>
</head>
<body>

<div class="container">
    <h2>Login</h2>

    <!-- Flash Message (Login error OR Logout success) -->
    <c:if test="${not empty promptMessage}">
        <div class="flash-message" id="flashMessage" data-message="${promptMessage}"></div>
    </c:if>

    <form id="loginForm" action="${pageContext.request.contextPath}/perform-login" method="post" class="login-form">
        <div class="form-group">
            <label for="usernameOrEmail">Username or Email</label>
            <input type="text" id="usernameOrEmail" name="usernameOrEmail" required placeholder="Enter your username or email">
            <div class="error-message"></div>
        </div>

        <div class="form-group">
            <label for="rawPassword">Password</label>
            <div class="password-wrapper">
                <input type="password" id="rawPassword" name="rawPassword" required placeholder="Enter your password">
                <span class="toggle-password" onclick="togglePassword()">
                    <i id="eyeIcon" class="fa-regular fa-eye"></i>
                </span>
            </div>
            <div class="error-message"></div>
        </div>

        <button type="submit" class="btn-login">Login</button>
    </form>

    <div class="links">
        <a href="${pageContext.request.contextPath}/register">Don't have an account? Register</a>
        <span>|</span>
        <a href="${pageContext.request.contextPath}/forgot-password">Forgot Password?</a>
    </div>
</div>

</body>
</html>
