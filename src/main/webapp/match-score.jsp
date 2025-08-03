<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Match-Score</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_match-score.css">
    </head>
    <body>
    <header>
        <a href="${pageContext.request.contextPath}" class="header_button"> Home </a>
        <a href="matches" class="header_button"> Finished Matches </a>
        <a href="new-match" class="header_button"> Start a new match </a>
    </header>


    <main>
        <div class="top-centered-container">
            <div class="image-buttons-wrapper">
                <img src="${pageContext.request.contextPath}/images/match-score.png" alt="Tennis" class="tennis-image">
                <div class="header-column">
                    <p class="match-score_header">
                        <span class="first-line">Choose Point</span><br>
                        <span class="second-line">Winner</span>
                    </p>
                </div>
            </div>
        </div>

        <div class="score">
            <table class="match-score">
                <tr>
                    <th>Name</th>
                    <th>Sets</th>
                    <th>Games</th>
                    <th>Points</th>
                </tr>
                <tr>
                    <td>${matchScore.player1Name}</td>
                    <td>${matchScore.matchScoreDto.sets1}</td>
                    <td>${matchScore.matchScoreDto.games1}</td>
                    <td>${matchScore.matchScoreDto.points1}</td>
                </tr>
                <tr>
                    <td>${matchScore.player2Name}</td>
                    <td>${matchScore.matchScoreDto.sets2}</td>
                    <td>${matchScore.matchScoreDto.games2}</td>
                    <td>${matchScore.matchScoreDto.points2}</td>
                </tr>
            </table>

            <div class="buttons-column">
                <form action="match-score?uuid=${param.uuid}" method="post">
                    <label>
                        <input type="hidden" name="playerNumber" value=${matchScore.numberPlayer1}>
                    </label>

                    <button type="submit" class="submit-button">Point🎾</button>
                </form>

                <form action="match-score?uuid=${param.uuid}" method="post">
                    <label>
                        <input type="hidden" name="playerNumber" value=${matchScore.numberPlayer2}>
                    </label>
                    <button type="submit" class="submit-button">Point🎾</button>
                </form>
            </div>

        </div>

    </main>

    <footer>
        <p class="footer_button">
            by<a href="https://github.com/LlqWst/TennisScoreboard">LlqWst</a> All Rights DGAF<br>
            &copy;  Tennis Scoreboard, project from<a href="https://zhukovsd.github.io/java-backend-learning-course/">Sergey Zhukov's Roadmap</a>
        </p>
    </footer>
    </body>
</html>
