package com.adobe.aem.support.authentication;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(property = {
        "service.ranking=10000",
        "path=/services/support/auth"
})
public class SupportAuthHandler implements AuthenticationHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TYPE = "Basic";

    public AuthenticationInfo extractCredentials(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> authorization = Optional.ofNullable(request.getHeader("Authorization"));
        if (authorization.isPresent()) {
            logger.debug("Retrieved header for Authorization {}", authorization.get());
            if (this.authType(authorization.get())) {
                String authHeader = authorization.get().replace(SupportAuthHandler.TYPE, "").trim();
                String base64DecodedHeader = new String(Base64.getDecoder().decode(authHeader));
                String[] creds = base64DecodedHeader.split(":");
                if (creds.length == 2) {
                    logger.debug("Successfully identified the credentials and set the AuthenticationInfo");
                    return new AuthenticationInfo(AuthenticationInfo.AUTH_TYPE, creds[0], creds[1].toCharArray());
                } else {
                    logger.warn("Credentials is missing username or password failing Auth");
                    request.setAttribute(AuthenticationHandler.FAILURE_REASON, "Credentials is missing username or password");
                }
            } else {
                logger.warn("Only {} auth type is supported");
                request.setAttribute(AuthenticationHandler.FAILURE_REASON, "Auth is not of type Basic");
            }
        } else {
            logger.warn("Missing Authorization header failing authentication");
            request.setAttribute(AuthenticationHandler.FAILURE_REASON, "Missing Authorization header");
        }

        return null;
    }

    public boolean requestCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Optional.ofNullable(request.getHeader("Authorization")).isPresent()) {
            return true;
        }
        return false;
    }

    public void dropCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Dropping credentials due to invalid authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public boolean authType(String authorizationHeader) {
        if (authorizationHeader.startsWith(SupportAuthHandler.TYPE)) {
            return true;
        }
        return false;
    }

}
