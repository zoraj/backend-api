/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsSaison;
import cloud.multimicro.mmc.Entity.TPmsSousSaison;
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
public class SeasonDao {

    @PersistenceContext
    EntityManager entityManager;
    
    private static final Logger LOGGER = Logger.getLogger(BookingService.class);

    // SEASON
    public List<TPmsSaison> getAllSeason() {
        List<TPmsSaison> pmsSaisonList = entityManager.createQuery("FROM TPmsSaison s WHERE s.dateDeletion = null  ORDER BY s.libelle").getResultList();
        return pmsSaisonList;
    }

    public TPmsSaison getSeasonById(int id) {
        TPmsSaison pmsSaison = entityManager.find(TPmsSaison.class, id);
        return pmsSaison;
    }

    public void addSeason(TPmsSaison saison) throws CustomConstraintViolationException {
        try {
            entityManager.persist(saison);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsSaison updateSeason(TPmsSaison saison)  throws CustomConstraintViolationException {
        try {
            return entityManager.merge(saison);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }        
    }

    public boolean saisonUsed(int id) {
        List<TPmsSousSaison> sousSaisonList = entityManager.createQuery("FROM TPmsSousSaison s WHERE s.dateDeletion = null AND pmsSaisonId =:pmsSaisonId ORDER BY s.libelle").setParameter("pmsSaisonId", id).getResultList();
        if (sousSaisonList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    public void deleteSeason(int saisonId) {        
        entityManager.createNativeQuery("UPDATE t_pms_saison SET date_deletion = CURRENT_TIMESTAMP WHERE id=:saisonId")              
              .setParameter("saisonId", saisonId)               
              .executeUpdate();
        
         entityManager.createNativeQuery("UPDATE t_pms_sous_saison SET date_deletion = CURRENT_TIMESTAMP WHERE pms_saison_id=:saisonId")              
              .setParameter("saisonId", saisonId)               
              .executeUpdate();
    }
}
