<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-dashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
<script src="https://kit.fontawesome.com/a076d05399.js"></script>
    <script src="${pageContext.request.contextPath}/js/user-dashboard.js" defer></script>
</head>
<body>

<!-- SweetAlert2 Flash Messages -->
<c:if test="${not empty promptMessage}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: '${promptMessage}',
                confirmButtonColor: '#3085d6'
            });
        });
    </script>
</c:if>

<c:if test="${not empty errorMessage}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: '${errorMessage}',
                confirmButtonColor: '#d33'
            });
        });
    </script>
</c:if>

<!-- Header -->
<header class="header">
    <div class="header-left">
        <!-- Profile Icon with Tooltip -->
        <c:if test="${not empty sessionScope.loggedInUser}">
            <div class="profile-icon-wrapper">
                <a href="${pageContext.request.contextPath}/profile/update" class="profile-icon" title="Update Profile">
                    <i class="fas fa-user-circle fa-2x"></i>
                </a>
                <div class="profile-tooltip">
                    <div><strong>${sessionScope.loggedInUser.username}</strong></div>
                    <div>Email: ${sessionScope.loggedInUser.email}</div>
                    <div>Role: USER</div>
                </div>
            </div>
        </c:if>

        <div>
            <h1>User Dashboard</h1>
            <p class="subtext">Welcome to your secure space</p>
        </div>
    </div>

  <!-- Top-Right Profile Dropdown -->
<div class="header-right">
    <div class="profile-container">
        <button class="btn profile-btn" id="profileToggle" title="View Profile">
            <i class="fas fa-user-circle fa-2x"></i>
        </button>

        <!-- Hidden Profile Info Panel -->
        <div class="profile-panel" id="profilePanel">
            <h3>Your Profile</h3>
            <p><strong>Full Name:</strong> ${sessionScope.loggedInUser.fullName}</p>
            <p><strong>UserName:</strong> ${sessionScope.loggedInUser.username}</p>
            <p><strong>Email:</strong> ${sessionScope.loggedInUser.email}</p>
            <p><strong>Role:</strong> USER</p>
            <a href="${pageContext.request.contextPath}/profile/update" class="btn update-btn">Update Profile</a>
        </div>
    </div>

    <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit" class="btn logout-btn" title="Logout">
            <i class="fas fa-sign-out-alt"></i>
        </button>
    </form>
</div>


</header>

<!-- Main Content -->
<main class="main-section">

    <!-- Welcome Message -->
    <section class="welcome">
        <h2>Hello, 
            <span class="username">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        ${sessionScope.loggedInUser.fullName}
                    </c:when>
                    <c:otherwise>Guest</c:otherwise>
                </c:choose>
            </span>
        </h2>
        <p class="role-note">You are logged in as a <strong>USER</strong>.</p>
    </section>

    <!-- Loan Application Section -->
    <section class="loan-section">
        <h3>Need a Loan?</h3>
        <p>You can apply for personal, home, car, or education loans.</p>
        <a href="${pageContext.request.contextPath}/loan/apply" class="btn apply-loan-btn">Apply for Loan</a>
    </section>

    <!-- Applied Loans -->
    <section class="applied-loans-section">
        <h3>Your Applied Loans</h3>
        <c:choose>
            <c:when test="${not empty userLoans}">
                <div class="table-wrapper">
                    <table class="loan-table">
                        <thead>
                            <tr>
                                <th>Loan Type</th>
                                <th>Amount</th>
                                <th>Duration (Months)</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="loan" items="${userLoans}">
                                <tr>
                                    <td>${loan.loanType}</td>
                                    <td>${loan.loanAmount}</td>
                                    <td>${loan.loanDuration}</td>
                                    <td>
                                        <span class="status ${fn:toLowerCase(loan.status)}">${loan.status}</span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="no-loans-message">You haven’t applied for any loans yet.</p>
            </c:otherwise>
        </c:choose>
    </section>

</main>

</body>
</html>