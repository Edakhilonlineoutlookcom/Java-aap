        out.println("<html><head><link rel='stylesheet' href='style.css'><title>Result</title></head>");
        out.println("<body><div class='container'>");
        
        if (username != null && !username.trim().isEmpty()) {
            out.println("<h1>Hello, " + username + "!</h1>");
            out.println("<p style='color: #666; font-size: 14px;'>Successfully saved to your SQLite database.</p>");
        } else {
            out.println("<h1>Hello, Guest!</h1>");
        }
        
        // Fetch and display all registered users from the database
        out.println("<h3>Registered Users:</h3><ul>");
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name FROM users")) {
                while (rs.next()) {
                    out.println("<li>" + rs.getString("name") + "</li>");
                }
            }
        } catch (Exception e) {
            out.println("<li>Error loading users: " + e.getMessage() + "</li>");
        }
        out.println("</ul>");
        
        out.println("<br><a href='index.jsp'>&larr; Back to Home</a>");
        out.println("</div></body></html>");

