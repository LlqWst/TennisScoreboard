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
                <div class="column">
                    <p class="match-score_header"> Choose Point Winner</p>

                    <div class="score">
                        <table class="match-score">
                            <tr>
                                <th>Name</th>
                                <th>Sets</th>
                                <th>Games</th>
                                <th>Points</th>
                            </tr>
                            <tr>
                                <td>${matchScore.matchScoreDto.player1.name}</td>
                                <td>${matchScore.matchScoreDto.score1.sets}</td>
                                <td>${matchScore.matchScoreDto.score1.games}</td>
                                <td>${matchScore.matchScoreDto.score1.points}</td>
                            </tr>
                            <tr>
                                <td>${matchScore.matchScoreDto.player2.name}</td>
                                <td>${matchScore.matchScoreDto.score2.sets}</td>
                                <td>${matchScore.matchScoreDto.score2.games}</td>
                                <td>${matchScore.matchScoreDto.score2.points}</td>
                            </tr>
                        </table>

                        <div class="buttons-column">
                            <form action="match-score?uuid=${param.uuid}" method="post">
                                <label>
                                    <input type="hidden" name="winnerPointId" value=${matchScore.matchScoreDto.player1.id}>
                                </label>

                                <button type="submit" class="submit-button">Point🎾</button>
                            </form>

                            <form action="match-score?uuid=${param.uuid}" method="post">
                                <label>
                                    <input type="hidden" name="winnerPointId" value=${matchScore.matchScoreDto.player2.id}>
                                </label>
                                <button type="submit" class="submit-button">Point🎾</button>
                            </form>
                        </div>

                    </div>

                </div>
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
