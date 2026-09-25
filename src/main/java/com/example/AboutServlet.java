package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><link rel='stylesheet' href='style.css'><title>About - Java App</title></head>");
        out.println("<body><div class='container'>");
        
        out.println("<h1>About This App</h1>");
        out.println("<p style='color: #666; font-size: 14px;'>A secure, high-performance data-driven Java web application engineered entirely inside an Android Termux environment.</p>");
        
        out.println("<h3 style='margin-top: 20px; border: none;'>Tech Stack & Architecture:</h3>");
        out.println("<hr style='border: 0; border-top: 2px solid #f0f2f5; margin: 5px 0 15px 0;'>");
        
        out.println("<ul style='text-align: left; color: #444; font-size: 14px; line-height: 1.8; padding-left: 20px;'>");
        out.println("<li><strong>Backend:</strong> Jakarta Servlet 6.0 running on Embedded Jetty 12</li>");
        out.println("<li><strong>Persistence:</strong> Serverless SQLite with automatic timestamp tracking</li>");
        out.println("<li><strong>Security:</strong> Robust HTML input sanitization to block XSS attacks</li>");
        out.println("<li><strong>Testing & CI:</strong> JUnit 5 automated unit testing with GitHub Actions CI</li>");
        out.println("</ul>");
        
        // Navigation footer links
        out.println("<div class='footer-actions' style='margin-top: 30px;'>");
        out.println("<a href='index.jsp'>&larr; Home</a>");
        out.println("<a href='hello'>View Directory &rarr;</a>");
        out.println("</div>");
        
        out.println("</div></body></html>");
    }
}
