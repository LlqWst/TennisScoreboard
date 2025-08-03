<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>New Match</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_new-match.css">
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
                    <img src="${pageContext.request.contextPath}/images/new-match.png" alt="Tennis" class="tennis-image">
                    <div class="buttons-column">
                        <p class="new-match_header"> Ready to Play? </p>
                        <p class="error-message">${error}</p>
                        <form action="new-match" method="post">
                            <div class="form-group">
                                <label for="player1Name" class="form-label">Player 1 Name:</label>
                                <input type="text" id="player1Name" name="player1Name" required class="form-input">
                            </div>

                            <div class="form-group">
                                    <label for="player2Name" class="form-label">Player 2 Name:</label>
                                    <input type="text" id="player2Name" name="player2Name" required class="form-input">
                            </div>

                            <div class="button-container">
                                <button type="submit" class="submit-button">Start</button>
                            </div>

                        </form>
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
