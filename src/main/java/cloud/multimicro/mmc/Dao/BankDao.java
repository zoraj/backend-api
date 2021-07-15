/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcBanque;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author HERIZO
 */
@Stateless
@SuppressWarnings("unchecked")
public class BankDao {
   
    @PersistenceContext
    private EntityManager entityManager;
    
        //banque
     public List<TMmcBanque> getAll() {
        List<TMmcBanque> banque = entityManager.createQuery("FROM TMmcBanque b where b.dateDeletion = null ").getResultList();
        return banque;
             
    }
    public TMmcBanque getByIdBanque(int id) {
        TMmcBanque banque = entityManager.find(TMmcBanque.class, id);
        return banque;
    }
    public void setBanque(TMmcBanque banque) throws CustomConstraintViolationException {
        try { 
            entityManager.persist(banque);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void updateBanque(TMmcBanque banque) throws CustomConstraintViolationException {
        try {  
        entityManager.merge(banque); 
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    public void deleteBanque(int id) throws CustomConstraintViolationException{ 
         TMmcBanque banque = getByIdBanque(id);
         Date dateDel = new Date();
         banque.setDateDeletion(dateDel);
         try { 
         entityManager.merge(banque);
         }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
}
