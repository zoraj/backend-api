/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Hibernate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import org.jboss.logging.Logger;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;

public class CurrentTenantResolver implements CurrentTenantIdentifierResolver {
    private static final Logger LOGGER = Logger.getLogger(CurrentTenantResolver.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        try {
            InputStream input;
            
            String mmcPath = System.getProperty("jboss.server.data.dir").concat("/MMC");
            final HttpServletRequest request = CDI.current().select(HttpServletRequest.class).get();
            String apiKey = request.getHeader("x-api-key");
            final Properties properties = new Properties();
            input = new FileInputStream(mmcPath + "/mmc.apikey.properties");
            properties.load(input);
            String dbName = properties.getProperty(apiKey);
            if (dbName == null) {
                throw new Exception("API KEY not found");
            }
            return dbName;
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
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
