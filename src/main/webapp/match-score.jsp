<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    </head>

    <body>

    <br>

        <form action="match-score?uuid=${param.uuid}" method="post">
            <p>${matchScore.player1Name} sets: ${matchScore.matchScoreDto.sets1}, games: ${matchScore.matchScoreDto.games1}, points: ${matchScore.matchScoreDto.points1}</p>
            <label>
                <input type="hidden" name="playerNumber" value=${matchScore.numberPlayer1}>
            </label>

            <button type="submit">Point</button>
        </form>

        <br>

        <form action="match-score?uuid=${param.uuid}" method="post">
            <p>${matchScore.player2Name} sets: ${matchScore.matchScoreDto.sets2}, games: ${matchScore.matchScoreDto.games2}, points: ${matchScore.matchScoreDto.points2}</p>
            <label>
                <input type="hidden" name="playerNumber" value=${matchScore.numberPlayer2}>
            </label>

            <button type="submit">Point</button>
        </form>

    </body>
</html>
