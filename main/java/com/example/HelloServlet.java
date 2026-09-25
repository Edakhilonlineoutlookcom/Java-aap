package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get the parameter sent from index.jsp
        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }
        
        out.println("<html><body>");
        out.println("<h1>Hello, " + username + "!</h1>");
        out.println("<a href='index.jsp'>Back to Home</a>");
        out.println("</body></html>");
    }
}

