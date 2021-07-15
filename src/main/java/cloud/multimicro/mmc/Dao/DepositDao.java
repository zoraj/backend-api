/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcFamilleCa;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.VPmsArrhesClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class DepositDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void setDeposit(TPmsArrhe pmsArrhe) throws CustomConstraintViolationException{
       try {
        entityManager.persist(pmsArrhe);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsArrhe putDeposit(JsonObject request) throws CustomConstraintViolationException{
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TPmsArrhe pmsArrheToModify = getByIdDeposit(Integer.parseInt(request.get("id").toString()));
        TPmsArrhe depositToModify = new TPmsArrhe();
        LocalDate daten = LocalDate.parse(request.getString("dateReglement"));
        depositToModify.setPmsReservationId(Integer.parseInt(request.get("pmsReservationId").toString()));
        depositToModify.setDateReglement(daten);
        depositToModify.setMmcClientId(pmsArrheToModify.getMmcClientId());
        depositToModify.setMontant((pmsArrheToModify.getMontant()).multiply(new BigDecimal(-1.00)));
        depositToModify.setMmcModeEncaissementId(Integer.parseInt(request.get("mmcModeEncaissementId").toString()));
        depositToModify.setPmsReservationId(pmsArrheToModify.getPmsReservationId());

        try {
        entityManager.persist(depositToModify);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        TPmsArrhe newDeposit = new TPmsArrhe();

        newDeposit.setPmsReservationId(Integer.parseInt(request.get("pmsReservationId").toString()));
        newDeposit.setDateReglement(daten);
        newDeposit.setMmcClientId(pmsArrheToModify.getMmcClientId());
        newDeposit.setMontant(new BigDecimal(request.get("montant").toString()));
        newDeposit.setMmcModeEncaissementId(Integer.parseInt(request.get("mmcModeEncaissementId").toString()));
        newDeposit.setPmsReservationId(pmsArrheToModify.getPmsReservationId());

        try {
        entityManager.persist(newDeposit);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

        return newDeposit;
    }

    public TPmsArrhe getByIdDeposit(int id) {
        TPmsArrhe pmsArrhe = entityManager.find(TPmsArrhe.class, id);
        return pmsArrhe;
    }
    
        
    public List<VPmsArrhesClient> getAll() {
        List<VPmsArrhesClient> depositsClients = entityManager.createQuery("FROM VPmsArrhesClient").getResultList();
        return depositsClients;
    }
}
