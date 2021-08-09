package cloud.multimicro.mmc.Dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cloud.multimicro.mmc.Entity.TSite;
import org.jboss.logging.Logger;

/**
 * Author: Zo
 */
@Stateless
@SuppressWarnings("unchecked")
public class SiteDao {
    @PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    private EntityManager entityManagerEstablishment;
    private static final Logger LOGGER = Logger.getLogger(SiteDao.class);
    
    public List<TSite> getAll() {
        List<TSite> apiList = entityManagerEstablishment.createQuery("FROM TSite").getResultList();
        return apiList;
    }
}
