package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcNotification;
import cloud.multimicro.mmc.Util.NullAwareBeanUtilsBean;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.BeanUtilsBean;

@Stateless
@SuppressWarnings("unchecked")
public class NotificationDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public TMmcNotification createNotif(JsonObject resa) {
        String numResa = resa.getString("numeroReservation");
        int id = resa.getInt("id");
        TMmcNotification ret = new TMmcNotification();
        ret.setNotification("Une nouvelle réservation sous le numéro "+numResa+" a été créée en ligne");
        ret.setAction("/reservation?id="+id);
        ret.setStatut("NONLU");
        entityManager.persist(ret);
        return ret;
    }
    
    public List<Long> getAllIdNotifResaNonlu() {
        return (List<Long>) entityManager.createNativeQuery("select id from t_mmc_notification where instr(action, '/reservation?id=') > 0 and statut = 'NONLU' and date_deletion is null").getResultList();
    }
    
    public TMmcNotification updateNotif(TMmcNotification newNotif) {
        try {
            TMmcNotification oldNotif = entityManager.find(TMmcNotification.class, newNotif.getId());
            BeanUtilsBean notif = new NullAwareBeanUtilsBean();
            notif.copyProperties(oldNotif, newNotif);
            return entityManager.merge(oldNotif);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}