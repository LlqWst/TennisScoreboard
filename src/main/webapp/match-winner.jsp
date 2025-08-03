<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Score winner </title>
        <p> winner is  ${matchScore.winnerName} !!! </p>
        <p>${matchScore.player1Name} sets: ${matchScore.matchScoreDto.sets1}, games: ${matchScore.matchScoreDto.games1}, points: ${matchScore.matchScoreDto.points1}</p>
    <br>
        <p>${matchScore.player2Name} sets: ${matchScore.matchScoreDto.sets2}, games: ${matchScore.matchScoreDto.games2}, points: ${matchScore.matchScoreDto.points2}</p>


    <a href="${pageContext.request.contextPath}" class="button"> Home </a>

</head>
<body>

</body>
</html>
