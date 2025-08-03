<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_finished_matches.css">
    <title>Finished matches</title>
</head>
<body>
    <header>
        <a href="${pageContext.request.contextPath}" class="header_button"> Home </a>
        <a href="matches" class="header_button"> Finished Matches </a>
        <a href="new-match" class="header_button"> Start a new match </a>
    </header>
    <main>

        <div class="finished-matches">
            <form class="search-form" action="matches" method="get">
                <label class="search-label" for="search_name">Find games by Player name:</label>
                <input class="search-input" type="text" id="search_name" name="filter_by_player_name"
                       value="${param.filter_by_player_name}">
                <button class="search-button search-button-search" type="submit">Search</button>
                <a href="matches" class="search-button search-button-clear">Clear</a>
            </form>

            <table class="tb-matches">
                <tr>
                    <th>Player 1</th>
                    <th>Player 2</th>
                    <th>Winner</th>
                </tr>

                <c:forEach items="${FinishedMatches}" var="match">
                    <tr>
                        <td>${match.player1.name}</td>
                        <td>${match.player2.name}</td>
                        <td>${match.winner.name} 🏆</td>
                    </tr>
                </c:forEach>

            </table>

            <div>
                <c:forEach begin="1" end="${pages}" var="page_number">
                    <a href="<c:url value='/matches'>
                            <c:param name='page' value='${page_number}'/>
                            <c:if test='${not empty param.filter_by_player_name}'>
                                <c:param name='filter_by_player_name' value='${param.filter_by_player_name}'/>
                            </c:if>
                            </c:url>"
                       class="button"> ${page_number}
                    </a>
                </c:forEach>
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
