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

<a href="all-currencies">All Currencies</a>
<%--<a href="index.jsp">Menu</a>--%>
<br><br>
<a href="all-rates">All Rates</a>
<br><br>

<h2> Add Currency </h2>
<form action="add-currency" method="post">
    <label>Код валюты:
        <input type="text" name="code" required>
    </label><br>

    <label>Полное название:
        <input type="text" name="fullName" required>
    </label><br>

    <label>Символ:
        <input type="text" name="sign" required>
    </label><br>

    <button type="submit">Добавить</button>
</form>
<br>

<h2> Add Rate </h2>
<form action="add-rate" method="post">
    <label>Base ID:
        <input type="text" name="baseCurrency" required>
    </label><br>

    <label>Target ID:
        <input type="text" name="target" required>
    </label><br>

    <label>Rate:
        <input type="text" name="rate" required>
    </label><br>

    <button type="submit">Добавить</button>
</form>

</body>
</html>