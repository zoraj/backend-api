/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPosActivite;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
@SuppressWarnings("unchecked")
public class SectionDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    //ACTIVITE
    public List<TPosActivite> getAll() {
        List<TPosActivite> activities = entityManager.createQuery("FROM TPosActivite a WHERE a.dateDeletion = null ORDER BY libelle").getResultList();
        return activities;
    }
    
    public TPosActivite getActivityById(int id) {
        TPosActivite activities = entityManager.find(TPosActivite.class, id);
        return activities;
    }
    
    public void setPosActivite(TPosActivite activity) throws CustomConstraintViolationException { 
        try {
            entityManager.persist(activity);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPosActivite updatePosActivite(TPosActivite activity) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(activity);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deletePosActivite(int id) {
        entityManager.createNativeQuery("UPDATE t_pos_activite SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
             .setParameter("id", id)               
             .executeUpdate();
    }
}
