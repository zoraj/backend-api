/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcTva;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
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
public class VATDao {

    @PersistenceContext
    EntityManager entityManager;

    //GET 
    public List<TMmcTva> getAll() {
        List<TMmcTva> tvaList = entityManager.createQuery("FROM TMmcTva tva order by id").getResultList();
        return tvaList;
    }

    //SET
    public void add(TMmcTva tva) throws CustomConstraintViolationException {
        try {
            entityManager.persist(tva);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    //UPDATE
    public TMmcTva update(TMmcTva tva) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(tva);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
}
