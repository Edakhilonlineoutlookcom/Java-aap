<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Java App Home</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <h2>Welcome to My Java App!</h2>
        <form action="hello" method="GET">
            <label for="username">Enter your name:</label>
            <input type="text" id="username" name="username" placeholder="Type your name here..." required>
            <button type="submit">Submit</button>
        </form>
    </div>
</body>
</html>

