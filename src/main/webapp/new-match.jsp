<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
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
        <title>New Match</title>

        <h2> New Match </h2>

    </head>
    <body>

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

    </body>
</html>
