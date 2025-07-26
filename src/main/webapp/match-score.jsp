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
    <form action="match-score" method="post">
        <p>${name1} score: ${score1}</p>
        <label>
            <input type="hidden" name="id" value=${player1}>
        </label><br>

        <button type="submit">Point</button>
    </form>
    <br>

    <form action="match-score" method="post">
        <p>${name2} score: ${score2}</p>
        <label>
            <input type="hidden" name="id" value=${player2}>
        </label><br>


        <button type="submit">Point</button>
    </form>
</head>
<body>

</body>
</html>
