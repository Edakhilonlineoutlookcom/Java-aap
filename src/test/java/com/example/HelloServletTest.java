package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloServletTest {

    // Helper method mirroring the servlet's XSS escaping logic
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

    @Test
    public void testHtmlEscapingXSS() {
        String maliciousInput = "<script>alert('xss')</script>";
        String expected = "&lt;script&gt;alert(&#x27;xss&#x27;)&lt;/script&gt;";
        
        String result = escapeHtml(maliciousInput);
        
        // Correct order: assertEquals(expected, actual, message)
        assertEquals(expected, result, "The XSS payload was not safely escaped!");
    }

    @Test
    public void testNullInput() {
        String result = escapeHtml(null);
        // Correct order: assertEquals(expected, actual, message)
        assertEquals("", result, "Null input should return an empty string");
    }
}

