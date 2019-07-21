<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<form method="post" action="users">
    <p><select name="userId">
        <option value="2">User</option>
        <option selected value="1">Admin</option>
    </select></p>
    <p><input type="submit" value="Choose"></p>
</form>
</body>
</html>