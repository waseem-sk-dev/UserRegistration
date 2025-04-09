<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Search Results</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search-results.css">
    <script src="${pageContext.request.contextPath}/js/search-results.js" defer></script>
</head>
<body>

    <h2>Search Results for "<c:out value='${searchedKeyword}'/>"</h2>

    <c:if test="${empty userList}">
        <p>No users found.</p>
    </c:if>

    <c:if test="${not empty userList}">
        <table class="user-table">
            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>UserName</th>
                    <th>Mobile Number</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="userregistration" items="${userList}">
                    <tr>
                        <td>
                            <span class="clickable-name"
                                  onclick="showUserDetails(
                                    '${userregistration.firstName}', 
                                    '${userregistration.lastName}', 
                                    '${userregistration.email}', 
                                    '${userregistration.username}', 
                                    '${userregistration.mobile}', 
                                    '${userregistration.status}')">
                                <c:out value="${userregistration.firstName}" />
                            </span>
                        </td>
                        <td><c:out value="${userregistration.lastName}" /></td>
                        <td><c:out value="${userregistration.email}" /></td>
                        <td><c:out value="${userregistration.username}" /></td>
                        <td><c:out value="${userregistration.mobile}" /></td>
                        <td><c:out value="${userregistration.status}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <div id="userDetails" class="user-details"></div>

    <!-- Back to Dashboard Button -->
    <div class="dashboard-button-container">
        <form action="${pageContext.request.contextPath}/admin/dashboard" method="get">
            <button type="submit" class="back-button-modern">Back to Dashboard</button>
        </form>
    </div>

</body>
</html>
