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

    <!-- Font Awesome for Eye Icon -->
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>
<body>

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

    <c:if test="${not empty error}">
        <div class="message error-message-block">${error}</div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="message success-message">${message}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm()">

        <div class="form-group">
            <label for="firstName">First Name: <span class="required">*</span></label>
            <input type="text" id="firstName" name="firstName" required>
        </div>

        <div class="form-group">
            <label for="lastName">Last Name: <span class="required">*</span></label>
            <input type="text" id="lastName" name="lastName" required>
        </div>

        <div class="form-group">
            <label for="mobile">Mobile Number: <span class="required">*</span></label>
            <input type="text" id="mobile" name="mobile" required onkeyup="validateMobile()">
            <span id="mobile-error" class="error-message"></span>
        </div>

        <div class="form-group">
            <label for="email">Email: <span class="required">*</span></label>
            <input type="email" id="email" name="email" required onkeyup="validateEmail()">
            <span id="email-error" class="error-message"></span>
        </div>

        <div class="form-group">
            <label for="location">Location:</label>
            <input type="text" id="location" name="location">
        </div>

        <div class="form-group">
            <label for="username">Username: <span class="required">*</span></label>
            <input type="text" id="username" name="username" required>
        </div>

       <div class="form-group password-wrapper">
    <label for="password">Password: <span class="required">*</span></label>
    <div class="password-field">
        <input type="password" id="password" name="password" required onkeyup="validatePassword()">
        <i class="fas fa-eye" id="togglePassword"></i>
    </div>
    <span id="password-strength" class="error-message"></span>
</div>


        <button type="submit">Register</button>
    </form>
</div>

</body>
</html>
