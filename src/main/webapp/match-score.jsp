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
    <form action="match-score?uuid=${param.uuid}" method="post">
        <p>${name1} sets: ${match.sets1}, games: ${match.games1}, points: ${match.points1}</p>
        <label>
            <input type="hidden" name="playerNumber" value=${numberPlayer1}>
        </label>

        <button type="submit">Point</button>
    </form>
    <br>

    <form action="match-score?uuid=${param.uuid}" method="post">
        <p>${name2} sets: ${match.sets2}, games: ${match.games2}, points: ${match.points2}</p>
        <label>
            <input type="hidden" name="playerNumber" value=${numberPlayer2}>
        </label>

        <button type="submit">Point</button>
    </form>
</head>
<body>

</body>
</html>
