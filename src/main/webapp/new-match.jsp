<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>New Match</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <header>

            <a href="${pageContext.request.contextPath}" class="button"> Home </a>
            <a href="matches" class="button"> Finished Matches </a>
            <a href="new-match" class="button"> Start a new match </a>

        </header>
        <main>
            <p>${error}</p>

            <br>

            <form action="new-match" method="post">
                <label>Player1

                    <input type="text" name="player1Name" required>

                </label><br>

                <label>Player2
                    <input type="text" name="player2Name" required>

                </label><br>

                <button type="submit">Start</button>
            </form>
        </main>
        <footer>

            <p class="button_footer">
                by<a href="https://github.com/LlqWst/TennisScoreboard">LlqWst</a> All Rights DGAF<br>
                &copy;  Tennis Scoreboard, project from<a href="https://zhukovsd.github.io/java-backend-learning-course/">Sergey Zhukov's Roadmap</a>
            </p>

        </footer>
    </body>
</html>
