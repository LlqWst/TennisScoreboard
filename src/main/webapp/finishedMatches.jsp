<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<form action="matches" method="get">
    <label>
        Player
        <input type="text" name="filter_by_player_name">
    </label>
    <br>
    <button type="submit">Search</button>
</form>

<br>

<c:forEach items="${FinishedMatches}" var="match">
    <div>
        player1: ${match.player1.name} | player2: ${match.player2.name} | winner: ${match.winner.name}
    </div>
</c:forEach>

<br><br>

<c:forEach begin="1" end="${pages}" var="page_number">
    <div>

        <a href="<c:url value='/matches'>
                <c:param name='page' value='${page_number}'/>
                <c:if test='${not empty param.filter_by_player_name}'>
                    <c:param name='filter_by_player_name' value='${param.filter_by_player_name}'/>
                </c:if>
                </c:url>"
           class="button"> ${page_number}
        </a>

    </div>
</c:forEach>


</body>
</html>
