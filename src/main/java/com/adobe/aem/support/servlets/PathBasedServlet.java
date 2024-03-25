package com.adobe.aem.support.servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {
    "sling.servlet.paths=" + "/services/support/auth",
    "sling.servlet.methods=POST"
})
public class PathBasedServlet extends SlingAllMethodsServlet {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        logger.info("In the path based servlet");
        try (Writer writer = response.getWriter()) {
            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            writer.write("Hello World");
            writer.flush();
        }

    }
}
