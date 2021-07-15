/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcClientFacturation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Service.BookingService;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class BillingCustomerDao {

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(BookingService.class);

    // SEASON
    public List<TMmcClientFacturation> getAll() {
        List<TMmcClientFacturation> billingCustomer = entityManager.createQuery("FROM TMmcClientFacturation c WHERE c.dateDeletion = null").getResultList();
        return billingCustomer;
    }

    public TMmcClientFacturation getByIdClient(Integer mmcClienId) {
        TMmcClientFacturation billingCustomer = (TMmcClientFacturation)entityManager.createQuery("FROM TMmcClientFacturation c WHERE c.mmcClientId =:mmcClienId")
                                                        .setParameter("mmcClienId", mmcClienId)
                                                        .getSingleResult();
        return billingCustomer;
    }

    public void add(TMmcClientFacturation billingCustomer) throws CustomConstraintViolationException {
        try {
            entityManager.persist(billingCustomer);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TMmcClientFacturation update(TMmcClientFacturation billingCustomer) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(billingCustomer);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void delete(Integer id) {
        entityManager.createNativeQuery("UPDATE t_mmc_client_facturation SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
