package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:sqlite:app.db";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "name TEXT NOT NULL, " +
                             "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
                stmt.execute(sql);
                
                try {
                    stmt.execute("ALTER TABLE users ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP");
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        String rawUsername = request.getParameter("username");
        
        if ("clear".equals(action)) {
            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     Statement stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM users");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (rawUsername != null && !rawUsername.trim().isEmpty()) {
            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users(name) VALUES(?)")) {
                    pstmt.setString(1, rawUsername.trim());
                    pstmt.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        out.println("<html><head><link rel='stylesheet' href='style.css'><title>Result</title></head>");
        out.println("<body><div class='container'>");
        
        if ("clear".equals(action)) {
            out.println("<h1>Database Cleared!</h1>");
            out.println("<p style='color: #666; font-size: 14px;'>All user records have been removed.</p>");
        } else if (rawUsername != null && !rawUsername.trim().isEmpty()) {
            String safeUsername = escapeHtml(rawUsername.trim());
            out.println("<h1>Hello, " + safeUsername + "!</h1>");
            out.println("<p style='color: #666; font-size: 14px;'>Successfully saved to your SQLite database.</p>");
        } else {
            out.println("<h1>User Directory</h1>");
        }
        
        // Fetch exact user count and check existence
        int userCount = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) {
                    userCount = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        // Render user directory header with live count badge
        out.println("<div style='display: flex; justify-content: space-between; align-items: center; margin-top: 15px;'>");
        out.println("<h3 style='margin: 0; border: none;'>Registered Users</h3>");
        out.println("<span style='background-color: #1a73e8; color: white; padding: 2px 10px; border-radius: 12px; font-size: 12px; font-weight: 600;'>" + userCount + " Total</span>");
        out.println("</div><hr style='border: 0; border-top: 2px solid #f0f2f5; margin: 10px 0 15px 0;'>");
        
        if (userCount > 0) {
            out.println("<input type='text' id='searchInput' class='search-box' onkeyup='filterUsers()' placeholder='Search names...'>");
        }
        
        out.println("<ul id='userList'>");
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, created_at FROM users")) {
                while (rs.next()) {
                    String name = escapeHtml(rs.getString("name"));
                    String time = escapeHtml(rs.getString("created_at"));
                    
                    out.println("<li class='user-item' style='display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;'>");
                    out.println("<span><strong>" + name + "</strong></span>");
                    out.println("<span style='color: #888; font-size: 12px;'>" + time + "</span>");
                    out.println("</li>");
                }
            }
        } catch (Exception e) {
            out.println("<li>Error loading users: " + e.getMessage() + "</li>");
        }
        
        if (userCount == 0) {
            out.println("<li style='color: #888; font-style: italic;'>No users registered yet.</li>");
        }
        out.println("</ul>");
        
        out.println("<div class='footer-actions'>");
        out.println("<a href='index.jsp'>&larr; Back to Home</a>");
        if (userCount > 0) {
            out.println("<form action='hello' method='GET' style='margin:0;'>");
            out.println("<input type='hidden' name='action' value='clear'>");
            out.println("<button type='submit' class='btn-danger' style='margin-top:0; padding: 6px 12px; font-size: 14px;'>Clear All</button>");
            out.println("</form>");
        }
        out.println("</div>");

        out.println("<script>");
        out.println("function filterUsers() {");
        out.println("  let input = document.getElementById('searchInput').value.toLowerCase();");
        out.println("  let items = document.getElementsByClassName('user-item');");
        out.println("  for (let i = 0; i < items.length; i++) {");
        out.println("    let text = items[i].textContent || items[i].innerText;");
        out.println("    if (text.toLowerCase().indexOf(input) > -1) {");
        out.println("      items[i].style.display = '';");
        out.println("    } else {");
        out.println("      items[i].style.display = 'none';");
        out.println("    }");
        out.println("  }");
        out.println("}");
        out.println("</script>");
        
        out.println("</div></body></html>");
    }
}

