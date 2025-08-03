<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <title>Home </title>
    </head>

    <body>
        <header>

            <a href="${pageContext.request.contextPath}" class="button"> Home </a>
            <a href="matches" class="button"> Finished Matches </a>
            <a href="new-match" class="button"> Start a new match </a>

        </header>
        <main>

            <p>${error}</p>

            <p class="index_header"> Tennis Scoreboard </p>

            <div class="top-centered-container">
                <div class="image-buttons-wrapper">
                    <img src="${pageContext.request.contextPath}/images/home.png" alt="Tennis" class="tennis-image">
                    <div class="buttons-column">
                        <a href="new-match" class="index_button_start_match">Start a new match</a>
                        <a href="matches" class="index_button_matches">Finished Matches</a>
                    </div>
                </div>
            </div>


        </main>
        <footer>

            <p class="button_footer">
                by<a href="https://github.com/LlqWst/TennisScoreboard">LlqWst</a> All Rights DGAF<br>
                &copy;  Tennis Scoreboard, project from<a href="https://zhukovsd.github.io/java-backend-learning-course/">Sergey Zhukov's Roadmap</a>
            </p>

        </footer>
    </body>
</html>