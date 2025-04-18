/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Hibernate;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.jboss.logging.Logger;

import cloud.multimicro.mmc.Listener.Helper;
import cloud.multimicro.mmc.Servlet.Startup;

public class CurrentTenantResolver implements CurrentTenantIdentifierResolver {
    private static final Logger LOGGER = Logger.getLogger(CurrentTenantResolver.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        try {
            final HttpServletRequest request = CDI.current().select(HttpServletRequest.class).get();
            String apiKey = request.getHeader("x-api-key");
            final Helper helper = CDI.current().select(Helper.class).get();
            String dbName = helper.getDBNameByAPIKey(apiKey);
            return dbName;
       } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
