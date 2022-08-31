package cloud.multimicro.mmc.Listener;

import java.io.InputStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Persistence;
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
    private static Map<String, String> apiKeyHash = new HashMap<String, String>();
    private static final Logger LOGGER = Logger.getLogger(StartupListener.class);

    private List<TSite> getSites() {
        var result = new ArrayList<TSite>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cloud.multimicro_Establishement_PU");
        EntityManager em = emf.createEntityManager();

        //List<TSite> apiList = em.createQuery("FROM TSite", TSite.class).getResultList();
        List<Object[]> sites = em.createNativeQuery("SELECT name, api_key FROM t_site").getResultList();
        LOGGER.info("Did we find it ? ");
        LOGGER.info(sites.size());

        if (sites.size() > 0) {
            for (Object[] s : sites) {
                if (s.length > 1) {
                    TSite site = new TSite();
                    site.setName(s[0].toString());
                    site.setApiKey(s[1].toString());
                    result.add(site);
                }
            }
        }   
        em.close();
        return result;
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

    public void init() {
        //var apiList = siteDao.getAll();
        //apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
        var apiList = getSites();
        apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
    }

    public String getDBNameByAPIKey(String apiKey) {
        String establishmentName = null;
        if (apiKeyHash.containsKey(apiKey)) {
            establishmentName = apiKeyHash.get(apiKey);  
            return transformENameToDBName(establishmentName); 
        }
        else { 
            var apiList = getSites();
            LOGGER.info("API list refreshed:");
            LOGGER.info(apiList);
            apiKeyHash = (HashMap<String, String>) apiList.stream().collect(Collectors.toMap(TSite::getApiKey, Site -> Site.getName()));
            if (apiKeyHash.containsKey(apiKey)) {
                establishmentName = apiKeyHash.get(apiKey);  
                return transformENameToDBName(establishmentName); 
            }
       }
       LOGGER.info("APY KEY definitely not exists");
       return null; // TODO - Should raise an exception
    }
}
