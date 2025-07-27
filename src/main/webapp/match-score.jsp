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
    <title>Score</title>
    <form action="match-score" method="post">
        <p>${name1} sets: ${sets1}, games: ${games1}, points: ${points1}</p>
        <label>
            <input type="hidden" name="player" value=1>
            <input type="hidden" name="uuid" value=${uuid}>
        </label><br>

        <button type="submit">Point</button>
    </form>
    <br>

    <form action="match-score" method="post">
        <p>${name2} sets: ${sets2}, games: ${games2}, points: ${points2}</p>
        <label>
            <input type="hidden" name="player" value=2>
            <input type="hidden" name="uuid" value=${uuid}>
        </label><br>


        <button type="submit">Point</button>
    </form>
</head>
<body>

</body>
</html>
