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
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
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
<header class="header modern-header">
    <div class="header-left">
        <h1 class="dashboard-title">User Dashboard</h1>
        <p class="subtext">Welcome to your secure space</p>
    </div>

    <div class="header-right">
        <div class="user-dropdown">
            <i class="fas fa-user-circle fa-2x user-avatar" id="profileToggle" title="View Profile"></i>
            <div class="dropdown-content" id="profilePanel">
                <a href="${pageContext.request.contextPath}/profile/update" target="_blank" class="btn-link">Update Profile</a>
            </div>
        </div>

        <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit" class="btn logout-btn" title="Logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </button>
        </form>
    </div>
</header>

<!-- Welcome Message -->
<section class="welcome">
  <h2>Hello, 
  <span id="profileName" class="username clickable">
    ${fn:toUpperCase(fn:substring(sessionScope.loggedInUser.firstName, 0, 1))}${fn:toLowerCase(fn:substring(sessionScope.loggedInUser.firstName, 1, fn:length(sessionScope.loggedInUser.firstName)))} 
    ${fn:toUpperCase(fn:substring(sessionScope.loggedInUser.lastName, 0, 1))}${fn:toLowerCase(fn:substring(sessionScope.loggedInUser.lastName, 1, fn:length(sessionScope.loggedInUser.lastName)))}
  </span>
</h2>

    <div id="profileInfoPanel" class="profile-info hidden">
        <p><strong>First Name:</strong> ${sessionScope.loggedInUser.firstName}</p>
        <p><strong>Last Name:</strong> ${sessionScope.loggedInUser.lastName}</p>
        <p><strong>Email:</strong> ${sessionScope.loggedInUser.email}</p>
        <p><strong>Mobile:</strong> ${sessionScope.loggedInUser.mobile}</p>
        <p><strong>Location:</strong> ${sessionScope.loggedInUser.location}</p>
    </div>
</section>

<!-- Profile Section -->
<section id="profile-section" class="profile-section">
    <div class="profile-card">
        <div class="profile-img-container">
            <i class="fas fa-user-circle profile-img"></i>
        </div>
        <div class="update-profile-btn-wrapper">
            <a href="${pageContext.request.contextPath}/profile/update" target="_blank" class="btn update-profile-btn">
                <i class="fas fa-edit"></i> Update Profile
            </a>
        </div>
    </div>
</section>

<!-- Loan Application Section -->
<section class="loan-section">
    <h3>Need a Loan?</h3>
    <p>You can apply for personal, home, car, or education loans.</p><br>
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
                                    <span class="status ${fn:toLowerCase(loan.status)}">
                                        ${loan.status}
                                    </span>
                                    <c:if test="${loan.status eq 'REJECTED' && not empty loan.rejectionReason}">
                                        <span class="rejection-tooltip">
                                            <i class="fas fa-info-circle"></i>
                                            <span class="tooltip-text">${loan.rejectionReason}</span>
                                        </span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <p class="no-loans-message">You havent applied for any loans yet.</p>
        </c:otherwise>
    </c:choose>
</section>

</body>
</html>
