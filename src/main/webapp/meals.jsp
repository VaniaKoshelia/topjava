<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.MealTo"/>
        <tr bgcolor="${meal.excess eq true ? "red" : "green"}">
            <td>
                <c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}" />
            </td>
            <td>
                <c:out value="${meal.description}" />
            </td>
            <td>
                <c:out value="${meal.calories}" />
            </td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>

</body>
</html>