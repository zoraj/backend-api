/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsSousSaison;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
public class SubSeasonDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<TPmsSousSaison> getAllSubSeason() {
        List<TPmsSousSaison> saisonList = entityManager.createQuery("FROM TPmsSousSaison ss WHERE ss.dateDeletion = null  ORDER BY ss.libelle").getResultList();
        return saisonList;
    }
    
    public List<TPmsSousSaison> getSubSeasonBySeason(Integer pmsSaisonId) {
        List<TPmsSousSaison> saisonList = entityManager.createQuery("FROM TPmsSousSaison WHERE pmsSaisonId=:pmsSaisonId and dateDeletion = null  ORDER BY libelle").setParameter("pmsSaisonId", pmsSaisonId).getResultList();
        return saisonList;
    }

    public TPmsSousSaison getSubSeasonById(int id) {
        TPmsSousSaison sousSaison = entityManager.find(TPmsSousSaison.class, id);
        return sousSaison;
    }

    public void addSubSeason(TPmsSousSaison sousSaison) throws CustomConstraintViolationException {
        try {
            entityManager.persist(sousSaison);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsSousSaison updateSubSeason(TPmsSousSaison sousSaison) throws CustomConstraintViolationException {  
        try {
            return entityManager.merge(sousSaison);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteSubSeason(int id) {        
        entityManager.createNativeQuery("UPDATE t_pms_sous_saison SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
             .setParameter("id", id)               
             .executeUpdate();
    }
}
