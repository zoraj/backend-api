package cloud.multimicro.mmc.Listener;

import java.io.InputStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import cloud.multimicro.mmc.Dao.SiteDao;
import cloud.multimicro.mmc.Entity.TSite;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * Helper
 */
@Stateless
public class Helper {
    @Inject
    SiteDao siteDao;
    private static Map<String, String> apiKeyHash = new HashMap<String, String>();
    private static final Logger LOGGER = Logger.getLogger(StartupListener.class);

    // The same function can be found in Establishment Management Project 
    private String transformENameToDBName(String establishmentName) {
        // Remove space, accent, capital case, 
        String result = Normalizer.normalize(establishmentName, Form.NFD);
        result = result.replaceAll("\\p{M}", "");
        result = "db_".concat(result.replace(" ", "_").replace("-", "_").toLowerCase());
        if (result.length() > 30) {
            return result.substring(0, 30);
        }
        return result;
    }

    public void init() {
        var apiList = siteDao.getAll();
        apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
    }

    public String getDBNameByAPIKey(String apiKey) {
        String establishmentName = null;
        if (apiKeyHash.containsKey(apiKey)) {
            establishmentName = apiKeyHash.get(apiKey);  
            return transformENameToDBName(establishmentName); 
        }
        else { 
            try {
                // The following codes are a Hack !. Can do better
                // This occurs when a new establishment was created bu the apiKey lists is not up to date
                // for optimisation's sake, we won't refresh unless we didn't find at first attempt 
                LOGGER.info("The API KEY provided " + apiKey + " can't be found. Let's retry the list");
                // Let's look inside temporary folder if any
                String appServerTempDir = System.getProperty("jboss.server.temp.dir");             
                final String apiKeyFileFullPath = appServerTempDir + "/" + apiKey + ".properties";

                final Properties properties = new Properties();
                InputStream input = new FileInputStream(apiKeyFileFullPath); ; 
                properties.load(input);
                establishmentName = properties.getProperty(apiKey);
                input.close();
                if (establishmentName != null) {
                    apiKeyHash.put(apiKey, establishmentName);
                    // I had tried the following code to retrieve the current APIKEY list with success. EJB Transaction fuckery
                    //var apiList = siteDao.getAll();
                    //apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
                    LOGGER.info("API KEY lists now up to date");

                    // Delete the API KEY properties file
                    File apiKeyFile = new File(apiKeyFileFullPath);
                    if (!apiKeyFile.delete()) {
                        LOGGER.error("Error deleting properties file " + apiKeyFileFullPath);
                    }

                    if (apiKeyHash.containsKey(apiKey)) {
                        establishmentName = apiKeyHash.get(apiKey);  
                        return transformENameToDBName(establishmentName); 
                    }
                }
                     
                } catch(IOException e){
                    e.printStackTrace();
                }
       }
       LOGGER.info("APY KEY definitely not exists");
       return null; // TODO - Should raise an exception
    }
}
