/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcService;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class ServiceDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<TMmcService> getAllService() {
        List<TMmcService> service = entityManager.createQuery("FROM TMmcService").getResultList();
        return service;
    }
    
    public TMmcService getByIdService(int id) {
        TMmcService service = entityManager.find(TMmcService.class, id);
        return service;
    }
    
    public void setService(TMmcService service) throws CustomConstraintViolationException{
        try {
            entityManager.persist(service);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcService updateService(TMmcService service) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(service);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
}
