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
        // Create the database table automatically on startup if it doesn't exist
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "name TEXT NOT NULL)";
                stmt.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        
        // Insert name into SQLite if provided
        if (username != null && !username.trim().isEmpty()) {
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
        
        out.println("<html><body>");
        if (username != null && !username.trim().isEmpty()) {
            out.println("<h1>Hello, " + username + "! (Saved to SQLite Database)</h1>");
        } else {
            out.println("<h1>Hello, Guest!</h1>");
        }
        
        // Fetch and display all registered users from the database
        out.println("<h3>Registered Users List:</h3><ul>");
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
        
        out.println("<br><a href='index.jsp'>Back to Home</a>");
        out.println("</body></html>");
    }
}

