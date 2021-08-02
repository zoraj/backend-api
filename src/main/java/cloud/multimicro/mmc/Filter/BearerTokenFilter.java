/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Filter;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import cloud.multimicro.mmc.Util.Jwt;

@Provider
@PreMatching
public class BearerTokenFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = Logger.getLogger(BearerTokenFilter.class);

    public void filter(ContainerRequestContext context) throws IOException {
        String path = context.getUriInfo().getPath();
        if (path.equalsIgnoreCase("/users/signin")) { // We don't need token for login authorization
            return;
        }

        if (path.equalsIgnoreCase("/takeway/group-products")) { // We don't need token for takeaway API
            return;
        }
        if (path.equalsIgnoreCase("/takeway/products")) { // We don't need token for takeaway API
            return;
        }
        if (path.equalsIgnoreCase("/takeway/cuisson")) { // We don't need token for takeaway API
            return;
        }
        if (path.equalsIgnoreCase("/takeway/accompagnement")) { // We don't need token for takeaway API
            return;
        }
        if (path.equalsIgnoreCase("/takeway/detailscommande")) { // We don't need token for takeaway API
            return;
        }
        if (path.equalsIgnoreCase("/takeway/reservation")) { // We don't need token for takeaway API
            return;
        }
        
        String authHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(context.getMethod() != "OPTIONS"){
            if (authHeader == null) {
                throw new NotAuthorizedException("Bearer error=\"token_expected\"");
            }
            String token = parseToken(authHeader);
            if (verifyToken(token) == false) {
                throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
            } 
        } 
    } 
    

    private String parseToken(String header) {
        String[] parts = header.split(" ");
        if (parts.length > 0) {
            //LOGGER.info("parts 1 : " + parts[0]);
            //LOGGER.info("parts 2 : " + parts[1]);
            return parts[1];
        }
        return null;
    }

    private boolean verifyToken(String token) {
        return Jwt.isTokenValid(token);
    }
}
