<%--
  Created by IntelliJ IDEA.
  User: applo
  Date: 26.07.2025
  Time: 1:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Score winner </title>
        <p> winner is  ${winner} !!! </p>
        <p>${name1} sets: ${match.sets1}, games: ${match.games1}, points: ${match.points1}</p>
    <br>
        <p>${name2} sets: ${match.sets2}, games: ${match.games2}, points: ${match.points2}</p>


    <a href="${pageContext.request.contextPath}" class="button"> Home </a>

</head>
<body>

</body>
</html>
