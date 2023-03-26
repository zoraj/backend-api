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
import javax.inject.Inject;
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
    
    @Inject
    SettingDao settingDao;

    public void setDeposit(TPmsArrhe pmsArrhe) throws CustomConstraintViolationException{
       try {
           pmsArrhe.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
           entityManager.persist(pmsArrhe);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void setDepositBooking(JsonObject request) throws CustomConstraintViolationException{
       try {
           TPmsArrhe addDeposit = new TPmsArrhe();
           addDeposit.setMmcModeEncaissementId(request.getInt("mmcModeEncaissementId"));
           addDeposit.setMmcClientId(request.getInt("mmcClientId"));
           addDeposit.setMontant(new BigDecimal(request.get("montant").toString()));
           addDeposit.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
           LocalDate dateReglement = LocalDate.parse(request.getString("dateReglement"));
           addDeposit.setDateReglement(dateReglement);
           if(!request.containsKey("pmsReservationId")){
                addDeposit.setPmsReservationId(getLastIdReservation());
            }else{
                addDeposit.setPmsReservationId(request.getInt("pmsReservationId"));
            }
            entityManager.persist(addDeposit);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public Integer getLastIdReservation() {
        return (Integer) entityManager.createNativeQuery("select max(id) from t_pms_reservation")
                .getSingleResult();
    }

    public TPmsArrhe putDeposit(JsonObject request) throws CustomConstraintViolationException{
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TPmsArrhe pmsArrheToModify = getByIdDeposit(Integer.parseInt(request.get("id").toString()));
        TPmsArrhe depositToModify = new TPmsArrhe();
        LocalDate dateReglement = LocalDate.parse(request.getString("dateReglement"));
        depositToModify.setDateReglement(dateReglement);
        depositToModify.setMmcClientId(pmsArrheToModify.getMmcClientId());
        depositToModify.setMontant((pmsArrheToModify.getMontant()).multiply(new BigDecimal(-1.00)));
        depositToModify.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
        depositToModify.setMmcModeEncaissementId(Integer.parseInt(request.get("mmcModeEncaissementId").toString()));
        depositToModify.setPmsReservationId(pmsArrheToModify.getPmsReservationId());

        try {
        entityManager.persist(depositToModify);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        TPmsArrhe newDeposit = new TPmsArrhe();
        newDeposit.setDateReglement(dateReglement);
        newDeposit.setMmcClientId(pmsArrheToModify.getMmcClientId());
        newDeposit.setMontant(new BigDecimal(request.get("montant").toString()));
        newDeposit.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
        newDeposit.setMmcModeEncaissementId(Integer.parseInt(request.get("mmcModeEncaissementId").toString()));
        if(pmsArrheToModify.getPmsReservationId() != null){
            newDeposit.setPmsReservationId(pmsArrheToModify.getPmsReservationId());
        }else{
            if(!request.containsKey("pmsReservationId")){
               newDeposit.setPmsReservationId(pmsArrheToModify.getPmsReservationId());
            }else{
               newDeposit.setPmsReservationId(Integer.parseInt(request.get("pmsReservationId").toString()));
            }
        }
        
        if(!request.containsKey("observation")){
            newDeposit.setObservation(pmsArrheToModify.getObservation());
        }else{
            newDeposit.setObservation(request.getString("observation"));
        }
        
        if(!request.containsKey("dateRemboursement")){
            newDeposit.setDateRemboursement(pmsArrheToModify.getDateRemboursement());
        }else{
            LocalDate datRemboursement = LocalDate.parse(request.getString("dateRemboursement"));
            newDeposit.setDateRemboursement(datRemboursement);
        }
        
        if(!request.containsKey("isConsomme")){
            newDeposit.setIsConsomme(pmsArrheToModify.getIsConsomme());
        }else{
            newDeposit.setIsConsomme(Integer.parseInt(request.get("isConsomme").toString()));
        }
       
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
    
    public BigDecimal totalDepositBalance() {
        return (BigDecimal) entityManager.createNativeQuery("SELECT SUM(montant) FROM t_pms_arrhes WHERE is_consomme = 0 OR is_consomme is null ").getSingleResult(); 
    }
}
