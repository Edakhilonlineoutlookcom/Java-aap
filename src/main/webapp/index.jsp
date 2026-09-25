<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Jakarta EE Browser App</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; background: #f4f4f9; color: #333; }
        .card { background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); max-width: 400px; }
        input[type="text"] { padding: 8px; width: 65%; margin-right: 10px; border: 1px solid #ccc; border-radius: 4px; }
        input[type="submit"] { padding: 8px 15px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        input[type="submit"]:hover { background: #0056b3; }
    </style>
</head>
<body>
    <div class="card">
        <h2>Welcome to GlassFish on Termux</h2>
        <form action="hello" method="GET">
            <label for="user">Enter your name:</label><br><br>
            <input type="text" id="user" name="user" placeholder="Your name...">
            <input type="submit" value="Submit">
        </form>
    </div>
</body>
</html>
