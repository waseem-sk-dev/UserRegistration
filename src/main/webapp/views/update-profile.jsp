<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update-profile.css">
    <script src="${pageContext.request.contextPath}/js/update-profile.js" defer></script>
</head>
<body>
    <div class="container">
        <h2 class="title">Update Your Profile</h2>
        <form action="${pageContext.request.contextPath}/profile/update" method="post" class="profile-form">

            <div class="form-group">
                <label>
                    <input type="checkbox"> Edit First Name
                </label>
                <input type="text" name="firstName" id="firstName" placeholder="Enter First Name" disabled />
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox"> Edit Last Name
                </label>
                <input type="text" name="lastName" id="lastName" placeholder="Enter Last Name" disabled />
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox"> Edit Mobile
                </label>
                <input type="text" name="mobile" id="mobile" placeholder="Enter Mobile Number" disabled />
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox"> Edit Location
                </label>
                <input type="text" name="location" id="location" placeholder="Enter Location" disabled />
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox"> Change Password
                </label>
                <input type="password" name="password" id="password" placeholder="Enter New Password" disabled />
            </div>

            <div class="form-actions">
                <button type="submit" class="btn save-btn">Save Changes</button>
                <a href="${pageContext.request.contextPath}/user-dashboard" class="btn cancel-btn">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
