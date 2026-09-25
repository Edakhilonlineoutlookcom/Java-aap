        // Footer navigation and clear action button
        out.println("<div class='footer-actions'>");
        out.println("<div style='display: flex; gap: 15px;'>");
        out.println("<a href='index.jsp'>&larr; Home</a>");
        out.println("<a href='about'>About</a>");
        out.println("</div>");
        
        if (userCount > 0) {
            out.println("<form action='hello' method='GET' style='margin:0;'>");
            out.println("<input type='hidden' name='action' value='clear'>");
            out.println("<button type='submit' class='btn-danger' style='margin-top:0; padding: 6px 12px; font-size: 14px;'>Clear All</button>");
            out.println("</form>");
        }
        out.println("</div>");

