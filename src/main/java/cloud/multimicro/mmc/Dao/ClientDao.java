/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcSegmentClient;
import cloud.multimicro.mmc.Entity.TMmcSociete;
import cloud.multimicro.mmc.Entity.TMmcTypeClient;
import cloud.multimicro.mmc.Entity.TPmsPrescripteur;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class ClientDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    //Client
    
    public List<TMmcClient> getAll() {
        List<TMmcClient> customer = entityManager.createQuery(" FROM TMmcClient  where dateDeletion is null ").getResultList();
        return customer;
    }
    
    public List<TMmcClient> getClientCollectivite() {
        List<Object[]> customer = entityManager.createQuery(" FROM TMmcClient cl LEFT JOIN TMmcSociete societe ON societe.id = cl.mmcSocieteId where cl.dateDeletion is null ").getResultList();
        
        List<TMmcClient> result = new ArrayList<TMmcClient>();
        for (Object[] custt : customer) {
            if (custt.length > 1) {
                TMmcClient tp = (TMmcClient) custt[0];
                TMmcSociete soc = (TMmcSociete) custt[1];
                tp.setMmcSocieteLibelle(soc.getNom());

                result.add(tp);
            }
        }
        return result;
    }
    
    
    public TMmcClient getByIdCustomer(int id) {
        TMmcClient customer = entityManager.find(TMmcClient.class, id);
        return customer;
    }
    
    public void setClient(TMmcClient client) throws CustomConstraintViolationException {
        try{
        entityManager.persist(client);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcClient updateClient(TMmcClient client) throws CustomConstraintViolationException{
        try{
         return entityManager.merge(client);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteClient(int id) throws CustomConstraintViolationException {
         TMmcClient customer = getByIdCustomer(id);
         Date dateDel = new Date();
         customer.setDateDeletion(dateDel);
         try{
         entityManager.merge(customer);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    //Type client
    public List<TMmcTypeClient> getTypeClientAll() {
        List<TMmcTypeClient> typeClient = entityManager.createQuery("FROM TMmcTypeClient tc where tc.dateDeletion = null").getResultList();
        return typeClient;
    }
    
    public TMmcTypeClient getByIdTypeClient(int id) {
        TMmcTypeClient typeClient = entityManager.find(TMmcTypeClient.class, id);
        return typeClient;
    }
    
    public void setTypeClient(TMmcTypeClient typeClient) throws CustomConstraintViolationException {
        try{
        entityManager.persist(typeClient);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcTypeClient updateTypeClient(TMmcTypeClient typeClient) throws CustomConstraintViolationException{
        try{
         return entityManager.merge(typeClient);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteTypeClient(int id) throws CustomConstraintViolationException {
         TMmcTypeClient typeClient = getByIdTypeClient(id);
         Date dateDel = new Date();
         typeClient.setDateDeletion(dateDel);
         try{
         entityManager.merge(typeClient);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    //Segment
    public List<TMmcSegmentClient> getSegmentClientAll() {
        List<TMmcSegmentClient> segmentClient = entityManager.createQuery("FROM TMmcSegmentClient sc where sc.dateDeletion = null").getResultList();
        return segmentClient;
    }
    
    public TMmcSegmentClient getByIdSegmentClient(int id) {
        TMmcSegmentClient segmentClient = entityManager.find(TMmcSegmentClient.class, id);
        return segmentClient;
    }
    
    public void setSegmentClient(TMmcSegmentClient segmentClient) throws CustomConstraintViolationException {
        try{
        entityManager.persist(segmentClient);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcSegmentClient updateSegmentClient(TMmcSegmentClient segmentClient) throws CustomConstraintViolationException{
        try{
         return entityManager.merge(segmentClient);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteSegmentClient(int id) throws CustomConstraintViolationException {
         TMmcSegmentClient segmentClient = getByIdSegmentClient(id);
         Date dateDel = new Date();
         segmentClient.setDateDeletion(dateDel);
         try{
         entityManager.merge(segmentClient);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    //Prescripteur
     public List<TPmsPrescripteur> getPrescripteurAll() {
        List<TPmsPrescripteur> prescripteur = entityManager.createQuery("FROM TPmsPrescripteur p where p.dateDeletion = null").getResultList();
        return prescripteur;
    }
    
    public TPmsPrescripteur getByIdPrescripteur(int id) {
        TPmsPrescripteur prescripteur = entityManager.find(TPmsPrescripteur.class, id);
        return prescripteur;
    }
    
    public void setPrescripteur(TPmsPrescripteur prescripteur) throws CustomConstraintViolationException {
        try{
        entityManager.persist(prescripteur);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsPrescripteur updatePrescripteur(TPmsPrescripteur prescripteur) throws CustomConstraintViolationException{
        try{
         return entityManager.merge(prescripteur);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deletePrescripteur(int id) throws CustomConstraintViolationException {
         TPmsPrescripteur prescripteur = getByIdPrescripteur(id);
         Date dateDel = new Date();
         prescripteur.setDateDeletion(dateDel);
         try{
         entityManager.merge(prescripteur);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    //Societe
     public List<TMmcSociete> getAllSociete() {
        List<TMmcSociete> societe = entityManager.createQuery("FROM TMmcSociete s where s.dateDeletion = null").getResultList();
        return societe;
    }
    public TMmcSociete getByIdSociete(int id) {
        TMmcSociete customer = entityManager.find(TMmcSociete.class, id);
        return customer;
    }
    public void setSociete(TMmcSociete societe) throws CustomConstraintViolationException {
        try{
        entityManager.persist(societe);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void updateSociete(TMmcSociete societe) throws CustomConstraintViolationException {
        try{
          entityManager.merge(societe); 
          } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    public void deleteSociete(int id) throws CustomConstraintViolationException { 
         TMmcSociete societe = getByIdSociete(id);
         Date dateDel = new Date();
         societe.setDateDeletion(dateDel);
         try{
         entityManager.merge(societe);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    
    
    
    
}
