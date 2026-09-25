package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String user = request.getParameter("user");
        if (user == null || user.trim().isEmpty()) {
            user = "Guest";
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Response</title></head>");
            out.println("<body style='font-family:Arial; margin:50px;'>");
            out.println("<h2>Hello, " + user + "!</h2>");
            out.println("<p>Successfully processed by Jakarta EE Servlet on Termux.</p>");
            out.println("<br><a href='index.jsp'>&larr; Back to Home</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
