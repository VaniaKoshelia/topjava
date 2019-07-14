<%--
  Created by IntelliJ IDEA.
  User: ikoshelia
  Date: 11.07.2019
  Time: 1:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Add new meal</title>
</head>
<body>

<form method="POST" action='meals' name="addMeal">
    ID : <input type="text" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />" /> <br />
    <%--<fmt:parseDate  value="${meal.dateTime}"  type="date" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" />--%>
    DATETIME : <input
        type="datetime-local" name="dateTime"/> <br />
        <%--value="<fmt:formatDate type="date" pattern="yyyy-MM-dd'T'HH:mm:ss" value="${parsedDate}" />" /> <br />--%>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>
