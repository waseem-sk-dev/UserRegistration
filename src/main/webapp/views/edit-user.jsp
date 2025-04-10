<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit User Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edit-user.css">
    <script src="${pageContext.request.contextPath}/js/edit-user.js" defer></script>
</head>
<body>
    <div class="edit-user-card">
        <h2><i class="fas fa-user-edit"></i> Edit User</h2>
        <form action="${pageContext.request.contextPath}/admin/updateUser" method="post" class="edit-user-form">
            <input type="hidden" name="id" value="${user.id}" />

            <div class="form-group">
                <label>First Name:</label>
                <input type="text" name="firstName" value="${user.firstName}" required />
            </div>

            <div class="form-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" value="${user.lastName}" required />
            </div>

            <div class="form-group">
                <label>Mobile:</label>
                <input type="text" name="mobile" value="${user.mobile}" required />
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="${user.email}" required />
            </div>

            <div class="form-group">
                <label>Location:</label>
                <input type="text" name="location" value="${user.location}" required />
            </div>

            <div class="form-group">
                <label>Username:</label>
                <input type="text" name="username" value="${user.username}" required />
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-save">Save Changes</button>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-cancel">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
