<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Score winner </title>
        <title>Match-Score</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_match-winner.css">
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
                    <img src="${pageContext.request.contextPath}/images/match-winner.jpg" alt="Tennis" class="tennis-image">
                    <div class="header-column">
                        <p class="match-score-winner">
                            <span class="match-score-winner-text">!!! Winner is </span>
                            <span class="match-score-winner-name">${matchScore.matchScoreDto.winner.name}</span>
                            <span class="match-score-winner-text"> !!!</span>
                        </p>
                        <a href="${pageContext.request.contextPath}" class="button_home">Home</a>
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
