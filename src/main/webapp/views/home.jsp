<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Welcome, User!</h2>
    
    <p>You are logged in successfully.</p>

    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Logout</button>
    </form>
</body>
</html>
