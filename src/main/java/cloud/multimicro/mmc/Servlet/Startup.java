package cloud.multimicro.mmc.Servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.jboss.logging.Logger;

import static java.util.stream.Collectors.toMap;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import cloud.multimicro.mmc.Entity.TSite;

/**
 * Startup
 * This servlet is run everytime we deploy the application. Configured as such in web.xml
 *
 * Author: Zo
 */
@SuppressWarnings("unchecked")
public class Startup extends HttpServlet {
    private static Startup instance;
    @PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    private EntityManager entityManagerEstablishement;

    private static Map<String, String> apiKeyHash = new HashMap<String, String>();
    private static final Logger LOGGER = Logger.getLogger(Startup.class);

    private void refreshAPIKeys() {
        var apiList = entityManagerEstablishement.createQuery("FROM TSite").getResultList();
        apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
        apiKeyHash.forEach((x, y )  -> LOGGER.info(x + " - " + y));
    }

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
 
    public static Startup getInstance() {
        if (instance == null) 
            instance = new Startup();
        return instance;
    }

    public void init() throws ServletException {
        // Retrieve existing APIKEY - Establishment 
        LOGGER.info("Rest API Application is loading");    
        refreshAPIKeys(); 
    }

    public String getDBNameByAPIKey(String apiKey) {
        if (apiKeyHash.containsKey(apiKey)) {
            final String establishmentName = apiKeyHash.get(apiKey);  
            return transformENameToDBName(establishmentName); 
        }
        else { 
            // Hack !
            // This occurs when a new establishment was created bu the apiKey lists is not up to date
            // for optimisation's sake, we won't refresh unless we didn't find at first attempt 
            refreshAPIKeys();
            if (apiKeyHash.containsKey(apiKey)) {
                final String establishmentName = apiKeyHash.get(apiKey);  
                return transformENameToDBName(establishmentName); 
            }
            return null; // Todo, should raise exception
        }
    }
}
