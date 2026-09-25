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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        
        // Handle clear action
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
        }
        
        // Insert name into SQLite if provided and not clearing
        else if (username != null && !username.trim().isEmpty()) {
            try {
                Class.forName("org.sqlite.JDBC");
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users(name) VALUES(?)")) {
                    pstmt.setString(1, username.trim());
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
        } else if (username != null && !username.trim().isEmpty()) {
            out.println("<h1>Hello, " + username + "!</h1>");
            out.println("<p style='color: #666; font-size: 14px;'>Successfully saved to your SQLite database.</p>");
        } else {
            out.println("<h1>User Directory</h1>");
        }
        
        // Fetch and display registered users with timestamps
        out.println("<h3>Registered Users:</h3><ul>");
        boolean hasUsers = false;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, created_at FROM users")) {
                while (rs.next()) {
                    hasUsers = true;
                    String name = rs.getString("name");
                    String time = rs.getString("created_at");
                    out.println("<li style='display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;'>");
                    out.println("<span><strong>" + name + "</strong></span>");
                    out.println("<span style='color: #888; font-size: 12px;'>" + (time != null ? time : "") + "</span>");
                    out.println("</li>");
                }
            }
        } catch (Exception e) {
            out.println("<li>Error loading users: " + e.getMessage() + "</li>");
        }
        
        if (!hasUsers) {
            out.println("<li style='color: #888; font-style: italic;'>No users registered yet.</li>");
        }
        out.println("</ul>");
        
        // Footer navigation and clear action button
        out.println("<div class='footer-actions'>");
        out.println("<a href='index.jsp'>&larr; Back to Home</a>");
        if (hasUsers) {
            out.println("<form action='hello' method='GET' style='margin:0;'>");
            out.println("<input type='hidden' name='action' value='clear'>");
            out.println("<button type='submit' class='btn-danger' style='margin-top:0; padding: 6px 12px; font-size: 14px;'>Clear All</button>");
            out.println("</form>");
        }
        out.println("</div>");
        
        out.println("</div></body></html>");
    }
}

