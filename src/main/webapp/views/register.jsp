<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>

    <!-- Link to external CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css">

    <!-- Link to JS validation -->
    <script defer src="${pageContext.request.contextPath}/js/formValidation.js"></script>
</head>
<body>

    <%-- Show Java-based alert for error message if present --%>
    <% 
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <script>
            alert("<%= errorMessage %>");
        </script>
    <% } %>

    <div class="container">
        <h2>User Registration</h2>

        <!-- Display JSTL flash messages -->
        <c:if test="${not empty error}">
            <div class="message error-message">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="message success-message">${message}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm()">
            
            <!-- First Name -->
            <div class="form-group">
                <label for="firstName">First Name: <span class="required">*</span></label>
                <input type="text" id="firstName" name="firstName" placeholder="Enter your first name" required>
            </div>

            <!-- Last Name -->
            <div class="form-group">
                <label for="lastName">Last Name: <span class="required">*</span></label>
                <input type="text" id="lastName" name="lastName" placeholder="Enter your last name" required>
            </div>

            <!-- Mobile Number -->
            <div class="form-group">
                <label for="mobile">Mobile Number: <span class="required">*</span></label>
                <input type="text" id="mobile" name="mobile" placeholder="Enter your mobile number" required onkeyup="validateMobile()">
                <span id="mobile-error" class="error-message"></span>
            </div>

            <!-- Email -->
            <div class="form-group">
                <label for="email">Email: <span class="required">*</span></label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required onkeyup="validateEmail()">
                <span id="email-error" class="error-message"></span>
            </div>

            <!-- Location -->
            <div class="form-group">
                <label for="location">Location:</label>
                <input type="text" id="location" name="location" placeholder="Enter your location">
            </div>

            <!-- Username -->
            <div class="form-group">
                <label for="username">Username: <span class="required">*</span></label>
                <input type="text" id="username" name="username" placeholder="Choose a username" required>
            </div>

            <!-- Password -->
            <div class="form-group">
                <label for="password">Password: <span class="required">*</span></label>
                <input type="password" id="password" name="password" placeholder="Enter a strong password" required autocomplete="new-password" onkeyup="validatePassword()">
                <span id="password-strength" class="error-message"></span>
            </div>

            <button type="submit">Register</button>
        </form>
    </div>
</body>
</html>
