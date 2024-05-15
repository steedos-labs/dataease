package io.dataease.auth.interceptor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class HtmlController {

    @GetMapping("/{filename}.html")
    public String serveHtml(@PathVariable String filename) throws IOException {
        // Load the HTML file from the classpath
        Resource resource = new ClassPathResource("static/" + filename + ".html");
        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // Replace placeholders with environment variables
        String updatedHtmlContent = htmlContent
                .replace("${DE_FRONTEND_CSS}", System.getenv("DE_FRONTEND_CSS"));
                .replace("${DE_FRONTEND_JS}", System.getenv("DE_FRONTEND_JS"));

        return updatedHtmlContent;
    }
}