package cloud.multimicro.mmc.Servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.jboss.logging.Logger;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import cloud.multimicro.mmc.Dao.SiteDao;
import cloud.multimicro.mmc.Entity.TSite;

/**
 * Startup
 * This servlet is run everytime we deploy the application. Configured as such in web.xml
 *
 * Author: Zo
 */
@WebServlet
public class Startup extends HttpServlet {
    //private static Startup instance;
    //@PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    //private EntityManager entityManagerEstablishement;
    @Inject
    private SiteDao siteDao;
    private static Map<String, String> apiKeyHash = new HashMap<String, String>();
    private static final Logger LOGGER = Logger.getLogger(Startup.class);

    private void refreshAPIKeys() {
        try {
            LOGGER.info("Begin refreshing API KEY lists");
            //LOGGER.info(entityManagerEstablishement);
            //var apiList = entityManagerEstablishement.createQuery("FROM TSite").getResultList();
            LOGGER.info("Site dao --> ");
            LOGGER.info(siteDao);
            var apiList = siteDao.getAll();
            apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
            printAPIKeys();
                 
        } catch(Exception e){
            LOGGER.info("Looks like something went wrong");
            //e.printStackTrace();
        }
    }

    private void printAPIKeys() {
        LOGGER.info("Actual API KEY are: ");
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
    /* 
    public static Startup getInstance() {
        if (instance == null) 
            instance = new Startup();
        return instance;
    }*/

    public void init() throws ServletException {
        // Retrieve existing APIKEY - Establishment 
        LOGGER.info("Rest API Application is loading");    
        refreshAPIKeys(); 
    }

    public String getDBNameByAPIKey(String apiKey) {
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info(siteDao);
        printAPIKeys();

        if (apiKeyHash.containsKey(apiKey)) {
            final String establishmentName = apiKeyHash.get(apiKey);  
            return transformENameToDBName(establishmentName); 
        }
        else { 
            // Hack !
            // This occurs when a new establishment was created bu the apiKey lists is not up to date
            // for optimisation's sake, we won't refresh unless we didn't find at first attempt 
            LOGGER.info("The API KEY provided " + apiKey + "can't be found. Let's retry the list");
            refreshAPIKeys();
            LOGGER.info("API KEY lists now up to date");
            if (apiKeyHash.containsKey(apiKey)) {
                final String establishmentName = apiKeyHash.get(apiKey);  
                return transformENameToDBName(establishmentName); 
            }
            LOGGER.info("APY KEY definitely not exists");
            return null; // Todo, should raise exception
        }
    }
}
