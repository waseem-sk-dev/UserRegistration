<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-details.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <script src="${pageContext.request.contextPath}/js/user-details.js" defer></script>
</head>
<body>
    <div class="container">
        <!-- Flash Message -->
        <c:if test="${not empty promptMessage}">
            <div class="flash-message">${promptMessage}</div>
        </c:if>

        <div class="card">
            <h2 class="card-title"><i class="fas fa-user-circle"></i> User Profile</h2>
            <table class="user-table">
                <tr><th><i class="fas fa-user"></i> Username:</th><td>${selectedUser.username}</td></tr>
                <tr><th><i class="fas fa-envelope"></i> Email:</th><td>${selectedUser.email}</td></tr>
                <tr><th><i class="fas fa-phone"></i> Mobile:</th><td>${selectedUser.mobile}</td></tr>
                <tr><th><i class="fas fa-map-marker-alt"></i> Location:</th><td>${selectedUser.location}</td></tr>
                <tr>
                    <th><i class="fas fa-circle-check"></i> Status:</th>
                    <td><span class="badge ${selectedUser.status eq 'ACTIVE' ? 'badge-success' : 'badge-danger'}">${selectedUser.status}</span></td>
                </tr>
                <tr><th><i class="fas fa-user-shield"></i> Role:</th><td>${selectedUser.role}</td></tr>
            </table>


                <!-- Back Button -->
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn"><i class="fas fa-arrow-left"></i> Back to Dashboard</a>
        
        </div>
    </div>
</body>
</html>