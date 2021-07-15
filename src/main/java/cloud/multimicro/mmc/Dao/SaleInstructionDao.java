/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsConsigneVente;
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
@SuppressWarnings("unchecked")
public class SaleInstructionDao {
    @PersistenceContext
    EntityManager entityManager;
    
       //GET FAMILY
    public List<TPmsConsigneVente> getAll() {
        List<TPmsConsigneVente> result = entityManager.createQuery("FROM TPmsConsigneVente WHERE dateDeletion is null").getResultList();
        return result;
    }

    //GET FAMILY BY ID
    public TPmsConsigneVente getById(int id) {
        TPmsConsigneVente mmcFamilies = entityManager.find(TPmsConsigneVente.class, id);
        return mmcFamilies;
    }

    //SET FAMILY
    public void set(TPmsConsigneVente family) throws CustomConstraintViolationException {
        try {
            entityManager.persist(family);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsConsigneVente update(TPmsConsigneVente family) throws CustomConstraintViolationException {     
        try {
            return entityManager.merge(family);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void delete(int id) {        
        entityManager.createNativeQuery("UPDATE t_pms_consigne_vente SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
              .setParameter("id", id)               
              .executeUpdate();
    }
    
}
