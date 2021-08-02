/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import cloud.multimicro.mmc.Entity.TPosClientVae;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosRevervation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Util.Util;


/**
 *
 * @author herizo
 */
@Stateless
@SuppressWarnings("unchecked")
public class VaeDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<TPosClientVae> getAll() {
        List<TPosClientVae> customer = entityManager.createQuery(" FROM TPosClientVae  where dateDeletion is null ").getResultList();
        return customer;
    }
    public List<TPosRevervation> getAllPosRevervation() {
        List<TPosRevervation> reservation = entityManager.createQuery(" FROM TPosRevervation  where dateDeletion is null ").getResultList();
        return reservation;
    }

    public TPosClientVae checkCredentials(String nom, String pass) {
        final String pepper = Util.getEnvString("pepper");
        String hashedPassword = Util.sha256(pepper + pass);
        try {
            TPosClientVae user = (TPosClientVae) entityManager.createQuery("FROM TPosClientVae WHERE nom =:nom and  pass =:pass and dateDeletion = null").setParameter("nom", nom).setParameter("pass", hashedPassword).getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
        //return null;
    }
    
    public TPosClientVae create(TPosClientVae client) throws CustomConstraintViolationException {
        try{
            final String pepper = Util.getEnvString("pepper");
            String hashedPassword = Util.sha256(pepper + client.getPass());
            client.setPass(hashedPassword);
            entityManager.persist(client);
            return client;
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void setPosRevervation(TPosRevervation reservation) throws CustomConstraintViolationException {
        try{
            entityManager.persist(reservation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    public TPosClientVae updateClient(TPosClientVae vae) throws CustomConstraintViolationException{
        try{
            return entityManager.merge(vae);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    public TPosRevervation updatePosRevervation(TPosRevervation PosRevervation) throws CustomConstraintViolationException{
        try{
            return entityManager.merge(PosRevervation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosClientVae getByIdCustomer(int id) {
        TPosClientVae customer = entityManager.find(TPosClientVae.class, id);
        return customer;
    }
    
    public TPosRevervation getByIdPosRevervation(int id) {
        TPosRevervation reservation = entityManager.find(TPosRevervation.class, id);
        return reservation;
    }
    
    public void deleteClient(int id) throws CustomConstraintViolationException {
         TPosClientVae customer = getByIdCustomer(id);
         Date dateDel = new Date();
         customer.setDateDeletion(dateDel);
         try{
         entityManager.merge(customer);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
     public void deletePosRevervation(int id) throws CustomConstraintViolationException {
         TPosRevervation customer = getByIdPosRevervation(id);
         Date dateDel = new Date();
         customer.setDateDeletion(dateDel);
         try{
         entityManager.merge(customer);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public List<TPosPrestation> getPosProductByIdGroupVae(int idGroup) {
        List<TPosPrestation> products = entityManager.createQuery("FROM TPosPrestation "
                + "WHERE dateDeletion = null and posPrestationGroupe.id =:idGroup  and autoriseVAE = 1 "
                + "ORDER BY libelle").setParameter("idGroup", idGroup).getResultList();
        return products;
    }

    public List<TPosPrestation> getProductOrderVae() {
        List<TPosPrestation> products = entityManager
                .createQuery("FROM TPosPrestation  WHERE autoriseVAE = 1 and dateDeletion is null ORDER BY libelle")
                .getResultList();
        return products;

    }
}
