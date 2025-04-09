<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-dashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
    <script src="https://unpkg.com/lucide@latest"></script>
    <script src="${pageContext.request.contextPath}/js/admin-dashboard.js" defer></script>
</head>
<body>

<!-- Flash Message -->
<c:if test="${not empty promptMessage}">
    <div id="flash-success" data-message="${promptMessage}"></div>
</c:if>

<header class="dashboard-header">
    <div class="header-content">
        <div>
            <h1>Welcome, ${sessionScope.username}</h1>
            <p class="role-info">You're logged in as <strong>ADMIN</strong></p>
        </div>
        <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
            <button class="btn logout-btn" type="submit">
                <i data-lucide="log-out"></i> Logout
            </button>
        </form>
    </div>
</header>

<!-- USER Search Box -->
<section class="search-box">
    <h2>Search Users</h2>
    <form class="search-form" action="${pageContext.request.contextPath}/admin/searchUsers" method="get">
        <input type="text" name="keyword" placeholder="Search name, email, username, mobile..." required />
        <button class="btn search-btn" type="submit"><i data-lucide="search"></i> Search</button>
    </form>
</section>

<!-- Show users only when userList is present -->
<c:if test="${userList != null}">
    <section class="users-section">
        <h2>User Search Results</h2>
        <c:choose>
            <c:when test="${empty userList}">
                <p class="no-users">No users matched your search.</p>
            </c:when>
            <c:otherwise>
                <div class="card-table-wrapper">
                    <table class="user-table">
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>
                                        <span class="badge ${user.status == 'ACTIVE' ? 'active' : 'inactive'}">
                                            ${user.status}
                                        </span>
                                    </td>
                                    <td class="actions">
                                        <form action="${pageContext.request.contextPath}/viewUser" method="get">
                                            <input type="hidden" name="userId" value="${user.id}" />
                                            <button class="icon-btn view" type="submit" title="View User">
                                                <i data-lucide="eye"></i>
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/admin/editUserForm" method="get">
                                            <input type="hidden" name="userId" value="${user.id}" />
                                            <button class="icon-btn edit" type="submit" title="Edit User">
                                                <i data-lucide="pencil"></i>
                                            </button>
                                        </form>

                                        <c:choose>
                                            <c:when test="${user.status == 'ACTIVE'}">
                                                <form action="${pageContext.request.contextPath}/admin/deactivateUser" method="post"
                                                      onsubmit="return confirm('Deactivate this user?');">
                                                    <input type="hidden" name="userId" value="${user.id}" />
                                                    <button class="icon-btn deactivate" type="submit" title="Deactivate User">
                                                        <i data-lucide="user-x"></i>
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/admin/activateUser" method="post"
                                                      onsubmit="return confirm('Activate this user?');">
                                                    <input type="hidden" name="userId" value="${user.id}" />
                                                    <button class="icon-btn activate" type="submit" title="Activate User">
                                                        <i data-lucide="user-check"></i>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>

                                        <form action="${pageContext.request.contextPath}/admin/deleteUser" method="post"
                                              onsubmit="return confirm('Permanently delete this user?');">
                                            <input type="hidden" name="userId" value="${user.id}" />
                                            <button class="icon-btn delete" type="submit" title="Delete User">
                                                <i data-lucide="trash-2"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</c:if>

<!-- LOAN Search Box -->
<section class="search-box">
    <h2>Search Loan Applications</h2>
    <form class="search-form" action="${pageContext.request.contextPath}/admin/searchLoans" method="get">
        <input type="text" name="keyword" placeholder="Search by user, type, status..." required />
        <button class="btn search-btn" type="submit"><i data-lucide="search"></i> Search</button>
    </form>
</section>

<!-- Show loans only when loanApplications is present -->
<c:if test="${loanApplications != null}">
    <section class="loan-applications-section">
        <h2>Loan Search Results</h2>
        <c:choose>
            <c:when test="${empty loanApplications}">
                <p class="no-users">No loan applications matched your search.</p>
            </c:when>
            <c:otherwise>
                <div class="card-table-wrapper">
                    <table class="user-table">
                        <thead>
                            <tr>
                                <th>Loan ID</th>
                                <th>User</th>
                                <th>Amount</th>
                                <th>Type</th>
                                <th>Duration</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="loan" items="${loanApplications}">
                                <tr>
                                    <td>${loan.id}</td>
                                    <td>${loan.user.fullName}</td>
                                    <td>₹ ${loan.loanAmount}</td>
                                    <td>${loan.loanType}</td>
                                    <td>${loan.loanDuration} months</td>
                                    <td>
                                        <span class="badge ${loan.status eq 'PENDING' ? 'pending' : loan.status eq 'APPROVED' ? 'approved' : 'rejected'}">
                                            ${loan.status}
                                        </span>
                                    </td>
                                    <td class="actions">
                                        <form action="${pageContext.request.contextPath}/admin/viewLoan" method="get">
                                            <input type="hidden" name="loanId" value="${loan.id}" />
                                            <button class="icon-btn view" type="submit" title="View Loan">
                                                <i data-lucide="eye"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</c:if>

</body>
</html>
