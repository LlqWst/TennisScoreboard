<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Menu" %>
</h1>
<br/>

<a href="findAllPlayers">Players</a>
<br><br>

<a href="findAllMatches">Matches</a>
<br><br>

<a href="new-match">Start new Match</a>
<br><br>

<h2> Add Player </h2>
<form action="findAllPlayers" method="post">
    <label>Name:
        <input type="text" name="name" required>
    </label><br>

    <button type="submit">Submit</button>
</form>
<br>

</body>
</html>