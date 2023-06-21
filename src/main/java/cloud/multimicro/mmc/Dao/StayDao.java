/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.jboss.logging.Logger;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsSejour;
import cloud.multimicro.mmc.Entity.TPmsSejourTarif;
import cloud.multimicro.mmc.Entity.TPmsSejourTarifDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import javax.inject.Inject;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class StayDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    private static final Logger LOGGER = Logger.getLogger(StayDao.class);
    
    @Inject
    SettingDao settingDao;

    // TPmsSejour
    
    // SEASON
    public List<TPmsSejour> getAll(String name) {
        if(!Objects.isNull(name)) {
            return entityManager.createQuery("FROM TPmsSejour s WHERE nom LIKE '%" + name + "%' AND s.dateDeletion is null ").getResultList();
        }
        else {
            return entityManager.createQuery("FROM TPmsSejour s WHERE s.dateDeletion is null ").getResultList();
        }
    }

    public TPmsSejour getStayById(int id) {
        TPmsSejour pmsSejour = entityManager.find(TPmsSejour.class, id);
        return pmsSejour;
    }

    public void add(TPmsSejour pmsSejour) throws CustomConstraintViolationException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalTime currentTime = LocalTime.now();
            // Get the current date
            TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
            LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
            LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);
            pmsSejour.setDateNote(dateTimeLogicielle);
            // Get current brigade
            settingData = entityManager.find(TMmcParametrage.class, "NUM_CURRENT_BRIGADE");
            Integer currentBrigade = Integer.valueOf(settingData.getValeur());
            pmsSejour.setBrigade(currentBrigade);
            
            entityManager.persist(pmsSejour);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsSejour update(TPmsSejour pmsSejour)  throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsSejour);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }        
    }

    public void delete(int id) {        
        entityManager.createNativeQuery("UPDATE t_pms_sejour SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
              .setParameter("id", id)               
              .executeUpdate();
    }

    //SejourTarif
    public List<TPmsSejourTarif> getAllStayRate(){
        List<TPmsSejourTarif> sejourTarif = entityManager.createQuery("FROM TPmsSejourTarif").getResultList();
        return sejourTarif;
    }

    public TPmsSejourTarif getStayRateById(int id) {
        TPmsSejourTarif sejourTarif = entityManager.find(TPmsSejourTarif.class, id);
        return sejourTarif;
    }

    public void addStayRate(JsonObject pmsSejourTarif) throws CustomConstraintViolationException {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date daten = null;
            String dateSejour;
            List<Map> pmsSejourTarifList = (List) pmsSejourTarif.get("tarif");
            for (Map room : pmsSejourTarifList) {
                TPmsSejourTarif object = new TPmsSejourTarif();
                dateSejour = room.get("dateSejour").toString();
                try {
                    daten = format.parse(dateSejour.replaceAll("\"", ""));
                    object.setDateSejour(daten);
                } catch (ParseException ex) {
                  
                }

                object.setPmsSejourId(Integer.parseInt(room.get("pmsSejourId").toString()));
                object.setPmsTarifGrilleDetailId(Integer.parseInt(room.get("pmsTarifGrilleDetailId").toString()));
                object.setBase(Integer.parseInt(room.get("base").toString()));
                try {
                    entityManager.persist(object);
                } catch (ConstraintViolationException ex) {
                    throw new CustomConstraintViolationException(ex);
                }
            }
    }

    public TPmsSejourTarif updateStayRate(TPmsSejourTarif pmsSejourTarif)  throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsSejourTarif);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }        
    }

    public void deleteStayRate(int id) {        
        entityManager.createNativeQuery(" DELETE FROM `t_pms_sejour_tarif` WHERE id=:id")              
              .setParameter("id", id)               
              .executeUpdate();
    }


       //SejourTarif
       public List<TPmsSejourTarifDetail> getAllStayRateDetailed(){
        List<TPmsSejourTarifDetail> pmsSejourTarifDetailed = entityManager.createQuery("FROM TPmsSejourTarifDetail").getResultList();
        return pmsSejourTarifDetailed;
    }

    public TPmsSejourTarifDetail getStayRateDetailedById(int id) {
        TPmsSejourTarifDetail pmsSejourTarifDetailed = entityManager.find(TPmsSejourTarifDetail.class, id);
        return pmsSejourTarifDetailed;
    }

    public void addStayRateDetailed(JsonObject pmsSejourTarifDetailed) throws CustomConstraintViolationException {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date daten = null;
            String dateSejour;
            List<Map> pmsSejourTarifList = (List) pmsSejourTarifDetailed.get("tarifDetail");
            for (Map room : pmsSejourTarifList) {
                TPmsSejourTarifDetail object = new TPmsSejourTarifDetail();

                object.setPmsSejourTarifId(Integer.parseInt(room.get("pmsSejourTarifId").toString()));
                object.setPmsPrestationId(Integer.parseInt(room.get("pmsPrestationId").toString()));
                object.setQte(Integer.parseInt(room.get("qte").toString()));
                object.setPu(new BigDecimal(room.get("pu").toString().replaceAll("\"","")));
                object.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
                object.setTauxRemise(new BigDecimal(room.get("tauxRemise").toString().replaceAll("\"","")));
                object.setTauxCommission(new BigDecimal(room.get("tauxCommission").toString().replaceAll("\"","")));
                object.setTauxTva(new BigDecimal(room.get("tauxTva").toString().replaceAll("\"","")));
                object.setIsONG(String.valueOf(room.get("isONG")).replaceAll("\"",""));
                object.setIsExtra(Integer.parseInt(String.valueOf(room.get("isExtra")).replaceAll("\"","")));
                object.setIsRecouche(Integer.parseInt(String.valueOf(room.get("isRecouche")).replaceAll("\"","")));
                try {
                    entityManager.persist(object);
                } catch (ConstraintViolationException ex) {
                    throw new CustomConstraintViolationException(ex);
                }
            }
    }

    public TPmsSejourTarifDetail updateStayRateDetailed(TPmsSejourTarifDetail pmsSejourTarif)  throws CustomConstraintViolationException {
        try {
            pmsSejourTarif.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            return entityManager.merge(pmsSejourTarif);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }        
    }

    public void deleteStayRateDetailed(int id) {        
        entityManager.createNativeQuery(" DELETE FROM `t_pms_sejour_tarif_detail` WHERE id=:id")              
              .setParameter("id", id)               
              .executeUpdate();
    }
    
    public List<Long> getEnNotesByTypechambre(int id, String dateLogiciel) {
        String sql = "select id from TPmsSejour " +
                    "where dateArrivee <= :dateLogiciel and dateDepart >= :dateLogiciel " +
                    "and statut = 'NOTE' and pmsTypeChambreId = :pmsTypeChambreId and dateDeletion is null";
        List<Long> ret = entityManager.createQuery(sql)
                                .setParameter("dateLogiciel", LocalDate.parse(dateLogiciel))
                                .setParameter("pmsTypeChambreId", id)
                                .getResultList();
        return ret;
    }
    
    public List<Long> getSoldeesByTypechambre(int id, String dateLogiciel) {
        String sql = "select id from TPmsSejour " +
                    "where dateArrivee <= :dateLogiciel and dateDepart >= :dateLogiciel " +
                    "and statut = 'SOLDE' and pmsTypeChambreId = :pmsTypeChambreId and dateDeletion is null";
        List<Long> ret = entityManager.createQuery(sql)
                                .setParameter("dateLogiciel", LocalDate.parse(dateLogiciel))
                                .setParameter("pmsTypeChambreId", id)
                                .getResultList();
        return ret;
    }
    
    public List<Long> getEnAttentesByTypechambre(int id, String dateLogiciel) {
        String sql = "select id from TPmsSejour " +
                    "where dateArrivee <= :dateLogiciel and dateDepart >= :dateLogiciel " +
                    "and statut = 'ATTENTE' and pmsTypeChambreId = :pmsTypeChambreId and dateDeletion is null";
        List<Long> ret = entityManager.createQuery(sql)
                                .setParameter("dateLogiciel", LocalDate.parse(dateLogiciel))
                                .setParameter("pmsTypeChambreId", id)
                                .getResultList();
        return ret;
    }
    
    public List<Long> getDepartsByTypechambre(int id, String dateLogiciel) {
        String sql = "select id from TPmsSejour " +
                    "where dateDepart = :dateDepart and pmsTypeChambreId = :pmsTypeChambreId and dateDeletion is null";
        List<Long> ret = entityManager.createQuery(sql)
                                .setParameter("dateDepart", LocalDate.parse(dateLogiciel))
                                .setParameter("pmsTypeChambreId", id)
                                .getResultList();
        return ret;
    }
    
}
