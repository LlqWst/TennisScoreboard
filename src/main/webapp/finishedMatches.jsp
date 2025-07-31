<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: applo
  Date: 26.07.2025
  Time: 1:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <title>All Finished matches</title>
</head>
<body>
    <h2>All Finished matches</h2>

    <br><br>
    <a href="${pageContext.request.contextPath}" class="button"> Home </a>
    <br><br>

    <form action="matches?filter_by_player_name=${filter_by_player_name}" method="get">
        <label>Player

            <input type="text" name="filter_by_player_name">

        </label><br>

        <button type="submit">Search</button>
    </form>

    <br>

    <c:forEach items="${allFinishedMatches}" var="match">
        <div>
                player1: ${match.player1.name} | player2: ${match.player2.name} | winner: ${match.winner.name}
        </div>
    </c:forEach>

    <br><br>

    <c:forEach begin= "1" end="${pages}" var="page_number">
        <div>
            <a href="matches?page=${page_number}&filter_by_player_name=${param.filter_by_player_name}" class="button"> ${page_number} </a>
        </div>
    </c:forEach>


    </body>
</html>
