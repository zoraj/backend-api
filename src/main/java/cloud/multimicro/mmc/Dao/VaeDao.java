/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPosClientVae;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosReservation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import cloud.multimicro.mmc.Util.Constant;
import cloud.multimicro.mmc.Util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.NoResultException;

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

    public List<TPosReservation> getAllPosReservation() {
        List<TPosReservation> reservation = entityManager.createQuery(" FROM TPosReservation  where dateDeletion is null ").getResultList();
        return reservation;
    }

    public TPosClientVae checkCredentials(String email, String pass) {

        String hashedPassword = Util.sha256(Constant.MMC_PEPPER + pass);
        try {
            TPosClientVae user = (TPosClientVae) entityManager.createQuery("FROM TPosClientVae WHERE email =:email and  pass =:pass and dateDeletion = null").setParameter("email", email).setParameter("pass", hashedPassword).getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
        //return null;
    }

    public TPosClientVae create(TPosClientVae client) throws CustomConstraintViolationException {
        try {
            String hashedPassword = Util.sha256(Constant.MMC_PEPPER + client.getPass());
            client.setPass(hashedPassword);
            entityManager.persist(client);
            return client;
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void setPosReservation(TPosReservation reservation) throws CustomConstraintViolationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime currentTime = LocalTime.now();

        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
        LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);

        try {
            
            if ((reservation.getDateReservation()).isBefore(dateTimeLogicielle)) {
                 throw new CustomConstraintViolationException("Invalid date reservation");
            }
            else {
               entityManager.persist(reservation);
            }
            
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosClientVae updateClient(TPosClientVae vae) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(vae);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosReservation updatePosReservation(TPosReservation PosReservation) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(PosReservation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosClientVae getByIdCustomer(int id) {
        TPosClientVae customer = entityManager.find(TPosClientVae.class, id);
        return customer;
    }

    public TPosReservation getByIdPosReservation(int id) {
        TPosReservation reservation = entityManager.find(TPosReservation.class, id);
        return reservation;
    }

    public void deleteClient(int id) throws CustomConstraintViolationException {
        TPosClientVae customer = getByIdCustomer(id);
        Date dateDel = new Date();
        customer.setDateDeletion(dateDel);
        try {
            entityManager.merge(customer);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePosReservation(int id) throws CustomConstraintViolationException {
        TPosReservation customer = getByIdPosReservation(id);
        Date dateDel = new Date();
        customer.setDateDeletion(dateDel);
        try {
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

    public LocalDate getDateLogicielle() {
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        return LocalDate.parse(settingData.getValeur());
    }
}
