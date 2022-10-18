/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcDeviceCloture;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsClotureDefinitive;
import cloud.multimicro.mmc.Entity.TPmsClotureProvisoire;
import cloud.multimicro.mmc.Entity.TPmsEncaissement;
import cloud.multimicro.mmc.Entity.TPosClotureDefinitive;
import cloud.multimicro.mmc.Entity.TPosClotureProvisoire;
import cloud.multimicro.mmc.Entity.TPosNoteEntete;
import cloud.multimicro.mmc.Entity.VPmsCa;
import cloud.multimicro.mmc.Entity.VPmsEncaissement;
import cloud.multimicro.mmc.Entity.VPosCa;
import cloud.multimicro.mmc.Entity.VPosEncaissement;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly
 */
@Stateless
public class ClosureDao {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOGGER = Logger.getLogger(ClosureDao.class);

    public TMmcParametrage getDateLogicielleIncrement() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        LocalDate futureDateLogicielle = dateLogicielle.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        valueDateLogicielle.setValeur(futureDateLogicielle.format(formatter));
        entityManager.merge(valueDateLogicielle);

        this.saveNewDeviceCloture(dateLogicielle, futureDateLogicielle);

        return valueDateLogicielle;
    }
    
    public TMmcParametrage getDateLogicielleIncrementPms() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        LocalDate futureDateLogicielle = dateLogicielle.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        valueDateLogicielle.setValeur(futureDateLogicielle.format(formatter));
        entityManager.merge(valueDateLogicielle);
        this.saveNewDeviceCloture(dateLogicielle, futureDateLogicielle);

        return valueDateLogicielle;
    }
    
    public TMmcParametrage getDateLogicielleIncrementPmsHotel() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        LocalDate futureDateLogicielle = dateLogicielle.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        valueDateLogicielle.setValeur(futureDateLogicielle.format(formatter));
        entityManager.merge(valueDateLogicielle);

        return valueDateLogicielle;
    }

    public void saveNewDeviceCloture(LocalDate dateLogicielle, LocalDate futureDateLogicielle) {
        List<TMmcDeviceCloture> mmcDeviceClotureList = entityManager
                .createQuery("FROM TMmcDeviceCloture WHERE dateStatus=:parameter")
                .setParameter("parameter", dateLogicielle).getResultList();
        String status = "";
        for (TMmcDeviceCloture mmcDeviceCloture : mmcDeviceClotureList) {
            status = (!mmcDeviceCloture.getStatus().equals("OUT")) ? "ENCOURS" : mmcDeviceCloture.getStatus();
            TMmcDeviceCloture object = new TMmcDeviceCloture(mmcDeviceCloture.getDeviceUuid(), futureDateLogicielle,
                    status);
            entityManager.persist(object);
        }
    }

    // SEASON
    public List<TMmcDeviceCloture> getAll() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<TMmcDeviceCloture> pmsSaisonList = entityManager
                .createQuery("FROM TMmcDeviceCloture s WHERE s.dateDeletion = null AND dateStatus =:dateLogicielle")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();
        return pmsSaisonList;
    }

    public TMmcDeviceCloture getById(int id) {
        TMmcDeviceCloture pmsSaison = entityManager.find(TMmcDeviceCloture.class, id);
        return pmsSaison;
    }

    public void add(TMmcDeviceCloture cloture) throws CustomConstraintViolationException {
        try {
            entityManager.persist(cloture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void addDeviceClotureList(List<TMmcDeviceCloture> clotureList) throws CustomConstraintViolationException {
        try {
            for (TMmcDeviceCloture cloture : clotureList) {
                entityManager.persist(cloture);
            }
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcDeviceCloture update(TMmcDeviceCloture cloture) throws DataException {
        List<TPosNoteEntete> openNote = entityManager.createQuery("FROM TPosNoteEntete WHERE etat = 'ENCOURS' ").getResultList();
        if (openNote.size() > 0) {
            throw new DataException("Lecture de caisse impossible, il reste des notes ouvertes.");
        }
        return entityManager.merge(cloture);
    }

    public void delete(int id) {
        entityManager
                .createNativeQuery("UPDATE t_mmc_device_cloture SET date_deletion = CURRENT_TIMESTAMP WHERE id=:param")
                .setParameter("param", id).executeUpdate();
    }

    public BigInteger countPostNotClosed() {
        return (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM t_mmc_device_cloture WHERE status = 'ENCOURS' ")
                .getSingleResult();
    }
    
    public BigInteger countNoteInProgress() {
        return (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM t_pms_note_entete WHERE etat = 'ENCOURS' ")
                .getSingleResult();
    }
    
    public BigInteger countNoteInClosureDefinitive() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        return (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM t_pos_cloture_definitive WHERE date_cloture =:dateLogicielle")
                .setParameter("dateLogicielle", dateLogicielle).getSingleResult();
    }
        
    public JsonArray getAllListCaByActivity() {      
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<VPosCa> listCa = entityManager
                .createQuery("FROM VPosCa WHERE dateCa =:dateLogicielle ORDER BY posActiviteId, idSousFamille ")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();

        var caResults = Json.createArrayBuilder();
        var activityResults = Json.createArrayBuilder();
        System.out.println("listCa"+listCa);
        if (listCa.size() > 0) {
            VPosCa valueListCa = listCa.get(0);
            System.out.println("valueListCa"+valueListCa);
            Integer idActiviteInitial = valueListCa.getPosActiviteId();
            Integer idSousfamilleInitial = valueListCa.getIdSousFamille();
            String serviceInitial = valueListCa.getService();
            System.out.println("idActiviteInitial"+idActiviteInitial);
            System.out.println("idSousfamilleInitial"+idSousfamilleInitial);
            System.out.println("serviceInitial"+serviceInitial);
            var libelleSousFamille = "";
            var libelleActivite = "";
            var txTva = new BigDecimal("0");
            var service = "";
            
            
            
            libelleActivite = valueListCa.getLibelleActivite();
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCaSF = new BigDecimal("0");
            var caHtSF = new BigDecimal("0");
            var tvaSF = new BigDecimal("0");
            var remiseSF = new BigDecimal("0");
            var offertSF = new BigDecimal("0");
            
            var totalMontantCaSF = new BigDecimal("0");
            var totalCaHtSF = new BigDecimal("0");
            var totalTvaSF = new BigDecimal("0");
            var totalRemiseSF = new BigDecimal("0");
            var totalOffertSF = new BigDecimal("0");
            var nbcouvertmidi = new BigDecimal("0");
            var nbcouvertsoir = new BigDecimal("0");
            //var nbcouvert = new BigDecimal("0");
            var service1 = "";
            

            for (VPosCa caList : listCa) {
                if (idActiviteInitial.equals(caList.getPosActiviteId())) {                              
                    totalMontantCaSF = totalMontantCaSF.add(caList.getMontantCa());
                    totalCaHtSF = totalCaHtSF.add(caList.getCaHt());
                    totalTvaSF = totalTvaSF.add(caList.getTva());
                    totalRemiseSF = totalRemiseSF.add(caList.getRemise());
                    totalOffertSF = totalOffertSF.add(caList.getOffert());
                    service1 = caList.getService();
                    if(service1.equals("M")){
                        nbcouvertmidi = nbcouvertmidi.add(caList.getNbCouvert());                           
                    }else if(service1.equals("S")){                           
                        nbcouvertsoir = nbcouvertsoir.add(caList.getNbCouvert());
                    }
                    
                    if(idSousfamilleInitial.equals(caList.getIdSousFamille())){                        
                        //cumulena ny CA par sous famille
                        txTva = caList.getTxTva();
                        service = caList.getService();
                        montantCaSF =  montantCaSF.add(caList.getMontantCa());
                        caHtSF = caHtSF.add(caList.getCaHt());
                        tvaSF = tvaSF.add(caList.getTva());
                        remiseSF = remiseSF.add(caList.getRemise());
                        offertSF = offertSF.add(caList.getOffert());

                    }
                    
                    else{
                        var caJson = Json.createObjectBuilder()
                            .add("libelleSousFamille", libelleSousFamille)
                            .add("montantCa", montantCaSF)
                            .add("caHt", caHtSF)
                            .add("txTva", txTva)
                            .add("tva", tvaSF)
                            .add("remise", remiseSF)
                            .add("offert", offertSF)
                            .add("service", service).build();
                        activityResults.add(caJson);
                           
                        //remise a zero cumul CA par sous famille
                        txTva = caList.getTxTva();
                        service = caList.getService();
                        montantCaSF = caList.getMontantCa();
                        caHtSF = caList.getCaHt();
                        tvaSF = caList.getTva();
                        remiseSF = caList.getRemise();
                        offertSF = caList.getOffert();
                        idSousfamilleInitial = caList.getIdSousFamille();
                        libelleSousFamille = caList.getLibelleSousFamille();
                    }
                   
                } else {                    
                    var caJson = Json.createObjectBuilder()
                            .add("libelleSousFamille", libelleSousFamille)
                            .add("montantCa", montantCaSF)
                            .add("caHt", caHtSF)
                            .add("txTva", txTva)
                            .add("tva", tvaSF)
                            .add("remise", remiseSF)
                            .add("offert", offertSF)
                            .add("service", service).build();
                           activityResults.add(caJson);
                           
                    var object = Json.createObjectBuilder()
                            .add("libelleActivite", libelleActivite)
                            .add("listByActivity", activityResults)
                            .add("totalMontantCaSF", totalMontantCaSF)
                            .add("totalCaHtSF", totalCaHtSF)
                            .add("totalTvaSF", totalTvaSF)
                            .add("totalRemiseSF", totalRemiseSF)
                            .add("totalOffertSF", totalOffertSF)
                            .add("nbcouvertmidi", nbcouvertmidi)
                            .add("nbcouvertsoir", nbcouvertsoir)
                            
                            .build();

                    caResults.add(object);
                   
                    activityResults = Json.createArrayBuilder();
                    
                    totalMontantCaSF = new BigDecimal("0");
                    totalCaHtSF = new BigDecimal("0");
                    totalTvaSF = new BigDecimal("0");
                    totalRemiseSF = new BigDecimal("0");
                    totalOffertSF = new BigDecimal("0");
                    nbcouvertmidi = new BigDecimal("0");
                    nbcouvertsoir = new BigDecimal("0");
                    
                    
                    idActiviteInitial       = caList.getPosActiviteId();
                    libelleActivite         = caList.getLibelleActivite();                   
                    idSousfamilleInitial    = caList.getIdSousFamille();
                    libelleSousFamille      = caList.getLibelleSousFamille();
                    
                    totalMontantCaSF = totalMontantCaSF.add(caList.getMontantCa());
                    totalCaHtSF = totalCaHtSF.add(caList.getCaHt());
                    totalTvaSF = totalTvaSF.add(caList.getTva());
                    totalRemiseSF = totalRemiseSF.add(caList.getRemise());
                    totalOffertSF = totalOffertSF.add(caList.getOffert());
                    service1 = caList.getService();
                    if(service1.equals("M")){
                        System.out.println("service1"+service1);
                        nbcouvertmidi = nbcouvertmidi.add(caList.getNbCouvert());                           
                    }else if(service1.equals("S")){                           
                        nbcouvertsoir = nbcouvertsoir.add(caList.getNbCouvert());
                    }
                    
                    txTva           = caList.getTxTva();
                    service         = caList.getService();                    
                    montantCaSF     = caList.getMontantCa();
                    tvaSF           = caList.getTva();
                    caHtSF          = caList.getCaHt(); 
                    remiseSF        = caList.getRemise();
                    offertSF        = caList.getOffert();
                }
                
                
            }
            
            var caJson = Json.createObjectBuilder()
                        .add("libelleSousFamille", libelleSousFamille)
                        .add("montantCa", montantCaSF)
                        .add("caHt", caHtSF)    
                        .add("txTva", txTva)
                        .add("tva", tvaSF)
                        .add("remise", remiseSF)
                        .add("offert", offertSF)
                        .add("service", service).build();
                       activityResults.add(caJson);

            var object = Json.createObjectBuilder()
                        .add("libelleActivite", libelleActivite)
                        .add("listByActivity", activityResults)
                        .add("totalMontantCaSF", totalMontantCaSF)
                        .add("totalCaHtSF", totalCaHtSF)
                        .add("totalTvaSF", totalTvaSF)
                        .add("totalRemiseSF", totalRemiseSF)
                        .add("totalOffertSF", totalOffertSF)
                        .add("nbcouvertmidi", nbcouvertmidi)
                        .add("nbcouvertsoir", nbcouvertsoir)
                        .add("nbcouvertmidi", nbcouvertmidi)
                    .add("nbcouvertsoir", nbcouvertsoir)
                    .build();
            
            caResults.add(object);
        }
        return caResults.build();
    }
    
    public JsonArray getAllListCa() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<VPosCa> listCa = entityManager
                .createQuery("FROM VPosCa WHERE dateCa =:dateLogicielle ORDER BY idSousFamille ")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();

        var caResults = Json.createArrayBuilder();
        
        if (listCa.size() > 0) {
            VPosCa valueListCa = listCa.get(0);
            Integer idSousFamilleInit = valueListCa.getIdSousFamille();
            var libelleSousFamille = "";
            var txTva = new BigDecimal("0");
            var service = "";
            
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCaSF = new BigDecimal("0");
            var caHtSF = new BigDecimal("0");
            var tvaSF = new BigDecimal("0");
            var remiseSF = new BigDecimal("0");
            var offertSF = new BigDecimal("0");
            
            for(VPosCa caList : listCa){
                if(idSousFamilleInit.equals(caList.getIdSousFamille())){
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCaSF = montantCaSF.add(caList.getMontantCa());
                    caHtSF = caHtSF.add(caList.getCaHt());
                    tvaSF = tvaSF.add(caList.getTva());
                    remiseSF = remiseSF.add(caList.getRemise());
                    offertSF = offertSF.add(caList.getOffert());
                }else{
                    var caJson = Json.createObjectBuilder()
                        .add("libelleSousFamille", libelleSousFamille)
                        .add("montantCa", montantCaSF)
                        .add("caHt", caHtSF)    
                        .add("txTva", txTva)
                        .add("tva", tvaSF)
                        .add("remise", remiseSF)
                        .add("offert", offertSF)
                        .add("service", service).build();
                    caResults.add(caJson);
                    
                    montantCaSF = new BigDecimal("0");
                    caHtSF = new BigDecimal("0");
                    tvaSF = new BigDecimal("0");
                    remiseSF = new BigDecimal("0");
                    offertSF = new BigDecimal("0");
                    
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCaSF = montantCaSF.add(caList.getMontantCa());
                    caHtSF = caHtSF.add(caList.getCaHt());
                    tvaSF = tvaSF.add(caList.getTva());
                    remiseSF = remiseSF.add(caList.getRemise());
                    offertSF = offertSF.add(caList.getOffert());
                    
                    idSousFamilleInit = caList.getIdSousFamille();
                }
            }
            var caJson = Json.createObjectBuilder()
                        .add("libelleSousFamille", libelleSousFamille)
                        .add("montantCa", montantCaSF)
                        .add("caHt", caHtSF)    
                        .add("txTva", txTva)
                        .add("tva", tvaSF)
                        .add("remise", remiseSF)
                        .add("offert", offertSF)
                        .add("service", service).build();
                    caResults.add(caJson);
        }
        return caResults.build();
    }
    
    public JsonArray getAllListCashingByActivity() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<VPosEncaissement> listCashing = entityManager
                .createQuery("FROM VPosEncaissement WHERE dateEncaissement =:dateLogicielle ORDER BY posActiviteId, mmcModeEncaissementId")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();
        
        var cashingResults = Json.createArrayBuilder();
        var activityResults = Json.createArrayBuilder();
        
        if (listCashing.size() > 0) {
            VPosEncaissement valueListCashing = listCashing.get(0);
            Integer idActiviteInitial = valueListCashing.getPosActiviteId();
            Integer idModeEncInitial = valueListCashing.getMmcModeEncaissementId();

            var libelleModeCashing = "";
            var libelleActivite = "";
            var service = "";
            
            
            libelleActivite = valueListCashing.getLibelleActivite();
            libelleModeCashing = valueListCashing.getLibelleModeEncaissement();
            
            var montantCashingMidi = BigDecimal.ZERO;
            var montantCashingSoir = BigDecimal.ZERO;
            var totalMontantCashing = BigDecimal.ZERO;
            
            var totalMontantCashingMidi = BigDecimal.ZERO;
            var totalMontantCashingSoir = BigDecimal.ZERO;
            var totalGeneralTotalMontantCashing = BigDecimal.ZERO;

            for (VPosEncaissement cashingList : listCashing) {
                if (idActiviteInitial.equals(cashingList.getPosActiviteId())) {
                    service = cashingList.getService();
                    if(service.equals("M")){
                        totalMontantCashingMidi = totalMontantCashingMidi.add(cashingList.getMontantTtc());                           
                    }else if(service.equals("S")){                           
                        totalMontantCashingSoir = totalMontantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalGeneralTotalMontantCashing = totalMontantCashingMidi.add(totalMontantCashingSoir);
                    
                    if(idModeEncInitial.equals(cashingList.getMmcModeEncaissementId())){                        
                        //cummul
                        service = cashingList.getService();
                        if(service.equals("M")){
                            montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());                           
                        }else if(service.equals("S")){                           
                            montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                        }
                        totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                        
                    }else{
                        var caJson = Json.createObjectBuilder()
                            .add("libelleModeEncaissement", libelleModeCashing)
                            .add("montantCashingMidi", montantCashingMidi)
                            .add("montantCashingSoir", montantCashingSoir)
                            .add("total", totalMontantCashing).build();
                           activityResults.add(caJson);
                           
                        //remise a zero cumul
                        montantCashingMidi = new BigDecimal("0");
                        montantCashingSoir = new BigDecimal("0");
                        totalMontantCashing = new BigDecimal("0");               
                        
                        if(cashingList.getService().equals("M")){
                            montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());
                            montantCashingSoir = new BigDecimal("0");
                        }else if(cashingList.getService().equals("S")){
                            montantCashingMidi = new BigDecimal("0");
                            montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                        }
                        totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                        idModeEncInitial = cashingList.getMmcModeEncaissementId();
                        libelleModeCashing = cashingList.getLibelleModeEncaissement();
                        
                    }
                   
                } else {
                    
                    var caJson = Json.createObjectBuilder()
                            .add("libelleModeEncaissement", libelleModeCashing)
                            .add("montantCashingMidi", montantCashingMidi)
                            .add("montantCashingSoir", montantCashingSoir)
                            .add("total", totalMontantCashing).build();
                           activityResults.add(caJson);
                           
                    var object = Json.createObjectBuilder().add("libelleActivite", libelleActivite)
                            .add("listByActivity", activityResults)
                            .add("totalMontantCashingMidi", totalMontantCashingMidi)
                            .add("totalMontantCashingSoir", totalMontantCashingSoir)
                            .add("totalGeneralTotalMontantCashing", totalGeneralTotalMontantCashing).build();

                    cashingResults.add(object);
                   
                    activityResults = Json.createArrayBuilder();
                    
                    totalMontantCashingMidi = new BigDecimal("0");
                    totalMontantCashingSoir = new BigDecimal("0");
                    totalGeneralTotalMontantCashing = new BigDecimal("0");
                    
                    if(cashingList.getService().equals("M")){
                        totalMontantCashingMidi = totalMontantCashingMidi.add(cashingList.getMontantTtc());                           
                    }else if(cashingList.getService().equals("S")){                           
                        totalMontantCashingSoir = totalMontantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalGeneralTotalMontantCashing = totalMontantCashingMidi.add(totalMontantCashingSoir);
                    
                    idActiviteInitial       = cashingList.getPosActiviteId();
                    libelleActivite         = cashingList.getLibelleActivite();                   
                    idModeEncInitial        = cashingList.getMmcModeEncaissementId();
                    libelleModeCashing      = cashingList.getLibelleModeEncaissement();
                                       
                    montantCashingMidi = new BigDecimal("0");
                    montantCashingSoir = new BigDecimal("0");
                    totalMontantCashing = new BigDecimal("0");
                    if(cashingList.getService().equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());
                        montantCashingSoir = new BigDecimal("0");
                    }else if(cashingList.getService().equals("S")){
                        montantCashingMidi = new BigDecimal("0");
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                }
            }
            
            var caJson = Json.createObjectBuilder()
                        .add("libelleModeEncaissement", libelleModeCashing)
                        .add("montantCashingMidi", montantCashingMidi)
                        .add("montantCashingSoir", montantCashingSoir)
                        .add("total", totalMontantCashing).build();
                       activityResults.add(caJson);

            var object = Json.createObjectBuilder().add("libelleActivite", libelleActivite)
                        .add("listByActivity", activityResults)
                        .add("totalMontantCashingMidi", totalMontantCashingMidi)
                        .add("totalMontantCashingSoir", totalMontantCashingSoir)
                        .add("totalGeneralTotalMontantCashing", totalGeneralTotalMontantCashing).build();
            
            cashingResults.add(object);
        }
        return cashingResults.build();
    }
    
    public JsonArray getAllListCashing() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<VPosEncaissement> listCashing = entityManager
                .createQuery("FROM VPosEncaissement WHERE dateEncaissement =:dateLogicielle ORDER BY mmcModeEncaissementId")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();
        
        var cashingResults = Json.createArrayBuilder();
        
        if (listCashing.size() > 0) {
            VPosEncaissement valueListCashing = listCashing.get(0);
            Integer idModeCashingInit = valueListCashing.getMmcModeEncaissementId();
            var libelleModeCashing = "";
            var service = "";
           
            libelleModeCashing = valueListCashing.getLibelleModeEncaissement();
            
            var montantCashingMidi = BigDecimal.ZERO;
            var montantCashingSoir = BigDecimal.ZERO;
            var totalMontantCashing = BigDecimal.ZERO;
            
            for(VPosEncaissement cashingList : listCashing){
                if(idModeCashingInit.equals(cashingList.getMmcModeEncaissementId())){
                    service = cashingList.getService();
                    if(service.equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());                           
                    }else if(service.equals("S")){                           
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                }else{
                    var cashingJson = Json.createObjectBuilder()
                            .add("libelleModeEncaissement", libelleModeCashing)
                            .add("montantCashingMidi", montantCashingMidi)
                            .add("montantCashingSoir", montantCashingSoir)
                            .add("total", totalMontantCashing).build();
                           cashingResults.add(cashingJson);
                           
                    montantCashingMidi = new BigDecimal("0");
                    montantCashingSoir = new BigDecimal("0");
                    totalMontantCashing = new BigDecimal("0");
                    if(cashingList.getService().equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());
                        montantCashingSoir = new BigDecimal("0");
                    }else if(cashingList.getService().equals("S")){
                        montantCashingMidi = new BigDecimal("0");
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                    idModeCashingInit = cashingList.getMmcModeEncaissementId();
                    libelleModeCashing = cashingList.getLibelleModeEncaissement();
                }
            }
            var cashingJson = Json.createObjectBuilder()
                            .add("libelleModeEncaissement", libelleModeCashing)
                            .add("montantCashingMidi", montantCashingMidi)
                            .add("montantCashingSoir", montantCashingSoir)
                            .add("total", totalMontantCashing).build();
                           cashingResults.add(cashingJson);
        }
        return cashingResults.build();
    }
    
    public JsonObject getAllEditionClosure() {
        var closureCaByActivity = getAllListCaByActivity();
        var closureCashingByActivity = getAllListCashingByActivity();
        var closureCa = getAllListCa();
        var closureCashing = getAllListCashing();

        var closureJsonObject = Json.createObjectBuilder()
                .add("closureCaByActivity", closureCaByActivity)
                .add("closureCashingByActivity", closureCashingByActivity)
                .add("closureCa", closureCa)
                .add("closureCashing", closureCashing).build();

        return closureJsonObject;
    }
    
    //CLOSURE PROVISOIRE
    public void deletePmsClotureProvisoire(String dateReference) {
        entityManager.createNativeQuery(" DELETE FROM `t_pms_cloture_provisoire` WHERE date_cloture=:dateReference").setParameter("dateReference", dateReference)
                .executeUpdate();
    }
    
    public BigDecimal sumCaDayBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) =:dateReference  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ORDER BY id_famille, id_sous_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ORDER BY id_famille, id_sous_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public BigDecimal totalSumCaDayByFamily(Integer familleId, String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) =:dateReference ORDER BY id_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal totalSumCaMonthByFamily(Integer familleId, String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ORDER BY id_famille  ")      
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalSumCaYearByFamily(Integer familleId, String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ORDER BY id_famille  ")      
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public BigDecimal sumCashingDay(Integer modeEncaissementId, String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE id_mode_encaissement =:modeEncaissementId AND Date(date_encaissement) =:dateReference  ")
                .setParameter("modeEncaissementId", modeEncaissementId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCashingMonth(Integer modeEncaissementId, String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE id_mode_encaissement =:modeEncaissementId "
                + "AND Date(date_encaissement) <=:dateReference AND date_encaissement_mois =:dateMois ORDER BY id_mode_encaissement  ")
                .setParameter("modeEncaissementId", modeEncaissementId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCashingYear(Integer modeEncaissementId, String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE id_mode_encaissement =:modeEncaissementId "
                + "AND Date(date_encaissement) <=:dateReference AND date_encaissement_annee =:dateAnnee ORDER BY id_mode_encaissement  ")
                .setParameter("modeEncaissementId", modeEncaissementId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public BigDecimal totalSumCashingDay(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE Date(date_encaissement) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal totalSumCashingMonth(String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE Date(date_encaissement) <=:dateReference AND date_encaissement_mois =:dateMois ORDER BY id_mode_encaissement  ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalSumCashingYear(String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM v_pms_encaissement "
                + "WHERE Date(date_encaissement) <=:dateReference AND date_encaissement_annee =:dateAnnee ORDER BY id_mode_encaissement  ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public void setPmsClotureProvisoire(JsonObject request)
            throws CustomConstraintViolationException, ParseException {
        String dateCloture = request.getString("dateReference");
        deletePmsClotureProvisoire(dateCloture);
        //LIST CA
        StringBuilder stringBuilderCa = new StringBuilder();
        stringBuilderCa.append("FROM VPmsCa  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        var dateMonthEntry = "";
        var dateYearEntry = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilderCa.append(" WHERE Date(dateCa) <= '" + dateCloture + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateCloture.substring(0, 4);
            dateMonthEntry = dateCloture.substring(0, 7);
            dateEntry = dateCloture;
        } else {
            stringBuilderCa.append(" WHERE Date(dateCa) <= '" + dateLog + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateLog.substring(0, 4);
            dateMonthEntry = dateLog.substring(0, 7);
            dateEntry = dateLog;
        }       
        List<VPmsCa> listCa = entityManager.createQuery(stringBuilderCa.toString()).getResultList();
        
        if (listCa.size() > 0) {
            VPmsCa valueListCa = listCa.get(0);
            Integer idFamilyInitial = valueListCa.getIdFamille();
            Integer idSousfamilleInitial = valueListCa.getIdSousFamille();

            var libelleSousFamille = "";
            var libelleFamille = "";
            
            libelleFamille = valueListCa.getLibelleFamille();
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCaJour   = BigDecimal.ZERO;
            var montantCaMois   = BigDecimal.ZERO;
            var montantCaAnnee  = BigDecimal.ZERO;
            
            var totalMontantCaJour      = BigDecimal.ZERO;
            var totalMontantCaMois      = BigDecimal.ZERO;
            var totalMontantCaAnnee     = BigDecimal.ZERO;
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPmsClotureProvisoire family = new TPmsClotureProvisoire();
            TPmsClotureProvisoire subFamily = new TPmsClotureProvisoire();
            
            for (VPmsCa caList : listCa) {
                if (idFamilyInitial.equals(caList.getIdFamille())) {
                    totalMontantCaJour = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    totalMontantCaMois = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);                              
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                    libelleFamille = caList.getLibelleFamille();
                    
                    family.setLibelle("TOTAL "+libelleFamille);
                    family.setMontantJour(totalMontantCaJour);
                    family.setMontantPeriode(totalMontantCaJour);
                    family.setMontantMois(totalMontantCaMois);
                    family.setMontantAnnee(totalMontantCaAnnee);
                    
                    if(idSousfamilleInitial.equals(caList.getIdSousFamille())){                        
                        //cumul CA par sous famille
                        montantCaJour   = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois   = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee  = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                        libelleSousFamille = caList.getLibelleSousFamille();
                        
                        subFamily.setLibelle(libelleSousFamille);
                        subFamily.setMontantJour(montantCaJour);
                        subFamily.setMontantPeriode(montantCaJour);
                        subFamily.setMontantMois(montantCaMois);
                        subFamily.setMontantAnnee(montantCaAnnee);
                        
                    } else {                    
                        try {
                            subFamily.setDateCloture(dateRef);
                            subFamily.setLibelle(libelleSousFamille);
                            subFamily.setMontantJour(montantCaJour);
                            subFamily.setMontantPeriode(montantCaJour);
                            subFamily.setMontantMois(montantCaMois);
                            subFamily.setMontantAnnee(montantCaAnnee);
                            entityManager.persist(subFamily);
                        } catch (ConstraintViolationException ex) {
                            throw new CustomConstraintViolationException(ex);
                        }
                        subFamily = new TPmsClotureProvisoire();
                        subFamily.setLibelle(caList.getLibelleSousFamille());
                        subFamily.setMontantJour(sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry));
                        subFamily.setMontantPeriode(sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry));
                        subFamily.setMontantMois(sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry));
                        subFamily.setMontantAnnee(sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry));
                        
                        idSousfamilleInitial    = caList.getIdSousFamille();
                        libelleSousFamille      = caList.getLibelleSousFamille();                       
                        //remise a zero cumul CA par sous famille
                        montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                    }

                }else{
                    try {
                        subFamily.setDateCloture(dateRef);
                        entityManager.persist(subFamily);
                        family.setDateCloture(dateRef);
                        entityManager.persist(family);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    
                    family = new TPmsClotureProvisoire();
                    subFamily = new TPmsClotureProvisoire();
                    
                    idFamilyInitial         = caList.getIdFamille();
                    libelleFamille          = caList.getLibelleFamille();                   
                    idSousfamilleInitial    = caList.getIdSousFamille();
                    libelleSousFamille      = caList.getLibelleSousFamille();
                    
                    montantCaJour           = BigDecimal.ZERO;
                    montantCaMois           = BigDecimal.ZERO;
                    montantCaAnnee          = BigDecimal.ZERO;
                    
                    montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                    montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                    montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                    
                    subFamily.setLibelle(libelleSousFamille);
                    subFamily.setMontantJour(montantCaJour);
                    subFamily.setMontantPeriode(montantCaJour);
                    subFamily.setMontantMois(montantCaMois);
                    subFamily.setMontantAnnee(montantCaAnnee);
                            
                    totalMontantCaJour      = BigDecimal.ZERO;
                    totalMontantCaMois      = BigDecimal.ZERO;
                    totalMontantCaAnnee     = BigDecimal.ZERO;
                     
                    totalMontantCaJour  = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    totalMontantCaMois  = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                    
                    family.setLibelle("TOTAL "+libelleFamille);
                    family.setMontantJour(totalMontantCaJour);
                    family.setMontantPeriode(totalMontantCaJour);
                    family.setMontantMois(totalMontantCaMois);
                    family.setMontantAnnee(totalMontantCaAnnee);
                }
            }
            try {
                subFamily.setDateCloture(dateRef);
                entityManager.persist(subFamily);
                family.setDateCloture(dateRef);
                entityManager.persist(family);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        
        //LISTE CASHING
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEncaissement  ");
        var dateEntryCashing = "";
        var dateMonthEntryCashing = "";
        var dateYearEntryCashing = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateCloture + "' ORDER BY idModeEncaissement ");
            dateYearEntryCashing = dateCloture.substring(0, 4);
            dateMonthEntryCashing = dateCloture.substring(0, 7);
            dateEntryCashing = dateCloture;
        } else {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateLog + "' ORDER BY idModeEncaissement ");
            dateYearEntryCashing = dateLog.substring(0, 4);
            dateMonthEntryCashing = dateLog.substring(0, 7);
            dateEntryCashing = dateLog;
        }       
        List<VPmsEncaissement> listCashing = entityManager.createQuery(stringBuilder.toString()).getResultList();
        
        if (listCashing.size() > 0) {
            VPmsEncaissement valueListCashing = listCashing.get(0);
            Integer modeCashingIdInitial = valueListCashing.getIdModeEncaissement();
            var libelleModeEncaissement = valueListCashing.getLibelleEncaissement();
            
            var montantCashingJour   = BigDecimal.ZERO;
            var montantCashingMois   = BigDecimal.ZERO;
            var montantCashingAnnee  = BigDecimal.ZERO;
            
            var totalMontantCashingJour      = BigDecimal.ZERO;
            var totalMontantCashingMois      = BigDecimal.ZERO;
            var totalMontantCashingAnnee     = BigDecimal.ZERO; 
     
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPmsClotureProvisoire cashing = new TPmsClotureProvisoire();
            TPmsClotureProvisoire totalCashing = new TPmsClotureProvisoire();
            
            for (VPmsEncaissement cashingList : listCashing) {
                if (modeCashingIdInitial.equals(cashingList.getIdModeEncaissement())) {
                    libelleModeEncaissement = cashingList.getLibelleEncaissement();
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing);
                    
                    cashing.setLibelle(libelleModeEncaissement);
                    cashing.setMontantJour(montantCashingJour);
                    cashing.setMontantPeriode(montantCashingJour);
                    cashing.setMontantMois(montantCashingMois);
                    cashing.setMontantAnnee(montantCashingAnnee);
                } else {
                    try {
                        cashing.setDateCloture(dateRef);
                        entityManager.persist(cashing);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    cashing = new TPmsClotureProvisoire();
                    cashing.setLibelle(cashingList.getLibelleEncaissement());
                    cashing.setMontantJour(sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing));
                    cashing.setMontantPeriode(sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing));
                    cashing.setMontantMois(sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing));
                    cashing.setMontantAnnee(sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing));
                    
                    montantCashingJour   = BigDecimal.ZERO;
                    montantCashingMois   = BigDecimal.ZERO;
                    montantCashingAnnee  = BigDecimal.ZERO;
                    
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing);

                    modeCashingIdInitial = cashingList.getIdModeEncaissement();
                }
            }
            totalMontantCashingJour      = totalSumCashingDay(dateEntryCashing);
            totalMontantCashingMois      = totalSumCashingMonth(dateEntryCashing, dateMonthEntryCashing);
            totalMontantCashingAnnee     = totalSumCashingYear(dateEntryCashing, dateYearEntryCashing);
            
            totalCashing.setLibelle("TOTAL ENCAISSEMENT");
            totalCashing.setMontantJour(totalMontantCashingJour);
            totalCashing.setMontantPeriode(totalMontantCashingJour);
            totalCashing.setMontantMois(totalMontantCashingMois);
            totalCashing.setMontantAnnee(totalMontantCashingAnnee);
            
            try {
                cashing.setDateCloture(dateRef);
                entityManager.persist(cashing);
                totalCashing.setDateCloture(dateRef);
                entityManager.persist(totalCashing);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
    
        }
   
    }  
    
    public List<TPmsClotureProvisoire> getPmsClotureProvisoire(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPmsClotureProvisoire  ");
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE dateCloture  = '" + dateReference + "'");
        }

        List<TPmsClotureProvisoire> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }
    
    //CLOSURE DEFINITIVE
    public void deletePmsClotureDefinitive(String dateReference) {
        entityManager.createNativeQuery(" DELETE FROM `t_pms_cloture_definitive` WHERE date_cloture=:dateReference").setParameter("dateReference", dateReference)
                .executeUpdate();
    }
    
    public void setPmsClotureDefinitive(JsonObject request)
            throws CustomConstraintViolationException, ParseException {
        String dateCloture = request.getString("dateReference");
        deletePmsClotureDefinitive(dateCloture);
        //LIST CA
        StringBuilder stringBuilderCa = new StringBuilder();
        stringBuilderCa.append("FROM VPmsCa  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        var dateMonthEntry = "";
        var dateYearEntry = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilderCa.append(" WHERE Date(dateCa) <= '" + dateCloture + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateCloture.substring(0, 4);
            dateMonthEntry = dateCloture.substring(0, 7);
            dateEntry = dateCloture;
        } else {
            stringBuilderCa.append(" WHERE Date(dateCa) <= '" + dateLog + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateLog.substring(0, 4);
            dateMonthEntry = dateLog.substring(0, 7);
            dateEntry = dateLog;
        }       
        List<VPmsCa> listCa = entityManager.createQuery(stringBuilderCa.toString()).getResultList();
        
        if (listCa.size() > 0) {
            VPmsCa valueListCa = listCa.get(0);
            Integer idFamilyInitial = valueListCa.getIdFamille();
            Integer idSousfamilleInitial = valueListCa.getIdSousFamille();

            var libelleSousFamille = "";
            var libelleFamille = "";
            
            libelleFamille = valueListCa.getLibelleFamille();
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCaJour   = BigDecimal.ZERO;
            var montantCaMois   = BigDecimal.ZERO;
            var montantCaAnnee  = BigDecimal.ZERO;
            
            var totalMontantCaJour      = BigDecimal.ZERO;
            var totalMontantCaMois      = BigDecimal.ZERO;
            var totalMontantCaAnnee     = BigDecimal.ZERO;
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPmsClotureDefinitive family = new TPmsClotureDefinitive();
            TPmsClotureDefinitive subFamily = new TPmsClotureDefinitive();
            
            for (VPmsCa caList : listCa) {
                if (idFamilyInitial.equals(caList.getIdFamille())) {
                    totalMontantCaJour = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    totalMontantCaMois = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);                              
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                    libelleFamille = caList.getLibelleFamille();
                    
                    family.setLibelle("TOTAL "+libelleFamille);
                    family.setMontantJour(totalMontantCaJour);
                    family.setMontantPeriode(totalMontantCaJour);
                    family.setMontantMois(totalMontantCaMois);
                    family.setMontantAnnee(totalMontantCaAnnee);
                    
                    if(idSousfamilleInitial.equals(caList.getIdSousFamille())){                        
                        //cumul CA par sous famille
                        montantCaJour   = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois   = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee  = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                        libelleSousFamille = caList.getLibelleSousFamille();
                        
                        subFamily.setLibelle(libelleSousFamille);
                        subFamily.setMontantJour(montantCaJour);
                        subFamily.setMontantPeriode(montantCaJour);
                        subFamily.setMontantMois(montantCaMois);
                        subFamily.setMontantAnnee(montantCaAnnee);
                        
                    } else {                    
                        try {
                            subFamily.setDateCloture(dateRef);
                            subFamily.setLibelle(libelleSousFamille);
                            subFamily.setMontantJour(montantCaJour);
                            subFamily.setMontantPeriode(montantCaJour);
                            subFamily.setMontantMois(montantCaMois);
                            subFamily.setMontantAnnee(montantCaAnnee);
                            entityManager.persist(subFamily);
                        } catch (ConstraintViolationException ex) {
                            throw new CustomConstraintViolationException(ex);
                        }
                        subFamily = new TPmsClotureDefinitive();
                        subFamily.setLibelle(caList.getLibelleSousFamille());
                        subFamily.setMontantJour(sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry));
                        subFamily.setMontantPeriode(sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry));
                        subFamily.setMontantMois(sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry));
                        subFamily.setMontantAnnee(sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry));
                        
                        idSousfamilleInitial    = caList.getIdSousFamille();
                        libelleSousFamille      = caList.getLibelleSousFamille();                       
                        //remise a zero cumul CA par sous famille
                        montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                    }

                }else{
                    try {
                        subFamily.setDateCloture(dateRef);
                        entityManager.persist(subFamily);
                        family.setDateCloture(dateRef);
                        entityManager.persist(family);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    
                    family = new TPmsClotureDefinitive();
                    subFamily = new TPmsClotureDefinitive();
                    
                    idFamilyInitial         = caList.getIdFamille();
                    libelleFamille          = caList.getLibelleFamille();                   
                    idSousfamilleInitial    = caList.getIdSousFamille();
                    libelleSousFamille      = caList.getLibelleSousFamille();
                    
                    montantCaJour           = BigDecimal.ZERO;
                    montantCaMois           = BigDecimal.ZERO;
                    montantCaAnnee          = BigDecimal.ZERO;
                    
                    montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                    montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                    montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                    
                    subFamily.setLibelle(libelleSousFamille);
                    subFamily.setMontantJour(montantCaJour);
                    subFamily.setMontantPeriode(montantCaJour);
                    subFamily.setMontantMois(montantCaMois);
                    subFamily.setMontantAnnee(montantCaAnnee);
                    
                    totalMontantCaJour      = BigDecimal.ZERO;
                    totalMontantCaMois      = BigDecimal.ZERO;
                    totalMontantCaAnnee     = BigDecimal.ZERO;
                     
                    totalMontantCaJour  = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    totalMontantCaMois  = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                    
                    family.setLibelle("TOTAL "+libelleFamille);
                    family.setMontantJour(totalMontantCaJour);
                    family.setMontantPeriode(totalMontantCaJour);
                    family.setMontantMois(totalMontantCaMois);
                    family.setMontantAnnee(totalMontantCaAnnee);                    
                }
            }
            try {
                subFamily.setDateCloture(dateRef);
                entityManager.persist(subFamily);
                family.setDateCloture(dateRef);
                entityManager.persist(family);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        
        //LISTE CASHING
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEncaissement  ");
        var dateEntryCashing = "";
        var dateMonthEntryCashing = "";
        var dateYearEntryCashing = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateCloture + "' ORDER BY idModeEncaissement ");
            dateYearEntryCashing = dateCloture.substring(0, 4);
            dateMonthEntryCashing = dateCloture.substring(0, 7);
            dateEntryCashing = dateCloture;
        } else {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateLog + "' ORDER BY idModeEncaissement ");
            dateYearEntryCashing = dateLog.substring(0, 4);
            dateMonthEntryCashing = dateLog.substring(0, 7);
            dateEntryCashing = dateLog;
        }       
        List<VPmsEncaissement> listCashing = entityManager.createQuery(stringBuilder.toString()).getResultList();
        
        if (listCashing.size() > 0) {
            VPmsEncaissement valueListCashing = listCashing.get(0);
            Integer modeCashingIdInitial = valueListCashing.getIdModeEncaissement();
            var libelleModeEncaissement = valueListCashing.getLibelleEncaissement();
            
            var montantCashingJour   = BigDecimal.ZERO;
            var montantCashingMois   = BigDecimal.ZERO;
            var montantCashingAnnee  = BigDecimal.ZERO;
            
            var totalMontantCashingJour      = BigDecimal.ZERO;
            var totalMontantCashingMois      = BigDecimal.ZERO;
            var totalMontantCashingAnnee     = BigDecimal.ZERO; 
     
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPmsClotureDefinitive cashing = new TPmsClotureDefinitive();
            TPmsClotureDefinitive totalCashing = new TPmsClotureDefinitive();
            
            for (VPmsEncaissement cashingList : listCashing) {
                if (modeCashingIdInitial.equals(cashingList.getIdModeEncaissement())) {
                    libelleModeEncaissement = cashingList.getLibelleEncaissement();
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing);
                    
                    cashing.setLibelle(libelleModeEncaissement);
                    cashing.setMontantJour(montantCashingJour);
                    cashing.setMontantPeriode(montantCashingJour);
                    cashing.setMontantMois(montantCashingMois);
                    cashing.setMontantAnnee(montantCashingAnnee);
                } else {
                    try {
                        cashing.setDateCloture(dateRef);
                        entityManager.persist(cashing);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    cashing = new TPmsClotureDefinitive();
                    cashing.setLibelle(cashingList.getLibelleEncaissement());
                    cashing.setMontantJour(sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing));
                    cashing.setMontantPeriode(sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing));
                    cashing.setMontantMois(sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing));
                    cashing.setMontantAnnee(sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing));
                    
                    montantCashingJour   = BigDecimal.ZERO;
                    montantCashingMois   = BigDecimal.ZERO;
                    montantCashingAnnee  = BigDecimal.ZERO;
                    
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntryCashing);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntryCashing, dateMonthEntryCashing);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntryCashing, dateYearEntryCashing);

                    modeCashingIdInitial = cashingList.getIdModeEncaissement();
                }
            }
            totalMontantCashingJour      = totalSumCashingDay(dateEntryCashing);
            totalMontantCashingMois      = totalSumCashingMonth(dateEntryCashing, dateMonthEntryCashing);
            totalMontantCashingAnnee     = totalSumCashingYear(dateEntryCashing, dateYearEntryCashing);
            
            totalCashing.setLibelle("TOTAL ENCAISSEMENT");
            totalCashing.setMontantJour(totalMontantCashingJour);
            totalCashing.setMontantPeriode(totalMontantCashingJour);
            totalCashing.setMontantMois(totalMontantCashingMois);
            totalCashing.setMontantAnnee(totalMontantCashingAnnee);
            
            try {
                cashing.setDateCloture(dateRef);
                entityManager.persist(cashing);
                totalCashing.setDateCloture(dateRef);
                entityManager.persist(totalCashing);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
    
        }
   
    }
    
    public List<TPmsClotureDefinitive> getPmsClotureDefinitive(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPmsClotureDefinitive  ");
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE dateCloture  = '" + dateReference + "'");
        }

        List<TPmsClotureDefinitive> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }
    
    //CLOSURE PROVISOIRE POS
    public void deletePosClotureProvisoire(String dateReference) {
        entityManager.createNativeQuery(" DELETE FROM `t_pos_cloture_provisoire` WHERE date_cloture=:dateReference").setParameter("dateReference", dateReference)
                .executeUpdate();
    }
    
    public List<TPosClotureProvisoire> getPosClotureProvisoire(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPosClotureProvisoire  ");
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE dateCloture  = '" + dateReference + "'");
        }

        List<TPosClotureProvisoire> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }
    
    public BigDecimal totalMontantCa(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal totalTva(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(tva), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal totalCaHt(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(ca_ht), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();     
    }
    
    public BigDecimal totalRemise(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(remise), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();     
    }
    
    public BigDecimal totalOffert(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(offert), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();     
    }
    
    public BigDecimal totalNbCouvert(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE Date(date_ca) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();
    }
    
    public BigDecimal totalMontantEncaissement(String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ttc), 0) FROM v_pos_encaissement "
                + "WHERE Date(date_encaissement) =:dateReference  ")
                .setParameter("dateReference", dateReference).getSingleResult();     
    }
            
    public void setPosClotureProvisoire(JsonObject request)
            throws CustomConstraintViolationException, ParseException {
        String dateCloture = request.getString("dateReference");
        deletePosClotureProvisoire(dateCloture);
        
        //LIST  CA
        StringBuilder stringBuilderCa = new StringBuilder();
        stringBuilderCa.append("FROM VPosCa  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilderCa.append(" WHERE Date(dateCa) = '" + dateCloture + "' ORDER BY idSousFamille ");
            dateEntry = dateCloture;
        } else {
            stringBuilderCa.append(" WHERE Date(dateCa) = '" + dateLog + "' ORDER BY idSousFamille ");
            dateEntry = dateLog;
        }       
        List<VPosCa> listCa = entityManager.createQuery(stringBuilderCa.toString()).getResultList();
        
        if (listCa.size() > 0) {
            VPosCa valueListCa = listCa.get(0);
            Integer idSousFamilleInit = valueListCa.getIdSousFamille();
            var libelleSousFamille = "";
            var txTva = BigDecimal.ZERO;
            var service = "";
            
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCa = BigDecimal.ZERO;
            var caHt = BigDecimal.ZERO;
            var tva = BigDecimal.ZERO;
            var remise = BigDecimal.ZERO;
            var offert = BigDecimal.ZERO;
            var nbCouvert = BigDecimal.ZERO;
            
            var totalMontantCa = BigDecimal.ZERO;
            var totalCaHt = BigDecimal.ZERO;
            var totalTva = BigDecimal.ZERO;
            var totalRemise = BigDecimal.ZERO;
            var totalOffert = BigDecimal.ZERO;
            var totalNbCouvert = 0;
            
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPosClotureProvisoire caSubFamily = new TPosClotureProvisoire();
            TPosClotureProvisoire totalCaSubFamily = new TPosClotureProvisoire();
            for(VPosCa caList : listCa){
                if(idSousFamilleInit.equals(caList.getIdSousFamille())){
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCa = montantCa.add(caList.getMontantCa());
                    caHt = caHt.add(caList.getCaHt());
                    tva = tva.add(caList.getTva());
                    remise = remise.add(caList.getRemise());
                    offert = offert.add(caList.getOffert());
                    nbCouvert = caList.getNbCouvert();
                    
                    caSubFamily.setLibelle(libelleSousFamille);
                    caSubFamily.setTauxTva(txTva);
                    caSubFamily.setMontantHt(caHt);
                    caSubFamily.setMontantTva(tva);
                    caSubFamily.setMontantTtc(montantCa);
                    caSubFamily.setMontantRemise(remise);
                    caSubFamily.setMontantOffert(offert);
                    caSubFamily.setNbCouvert(nbCouvert.intValue());
                    caSubFamily.setService(service);
                }else{
                    try {
                        caSubFamily.setDateCloture(dateRef);
                        entityManager.persist(caSubFamily);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    caSubFamily = new TPosClotureProvisoire();
                    caSubFamily.setLibelle(caList.getLibelleSousFamille());
                    caSubFamily.setTauxTva(caList.getTxTva());
                    caSubFamily.setMontantHt(caList.getCaHt());
                    caSubFamily.setMontantTva(caList.getTva());
                    caSubFamily.setMontantTtc(caList.getMontantCa());
                    caSubFamily.setMontantRemise(caList.getRemise());
                    caSubFamily.setMontantOffert(caList.getOffert());
                    caSubFamily.setNbCouvert(caList.getNbCouvert().intValue());
                    caSubFamily.setService(caList.getService());
                    
                    montantCa = BigDecimal.ZERO;
                    caHt = BigDecimal.ZERO;
                    tva = BigDecimal.ZERO;
                    remise = BigDecimal.ZERO;
                    offert = BigDecimal.ZERO;
                    
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCa = montantCa.add(caList.getMontantCa());
                    caHt = caHt.add(caList.getCaHt());
                    tva = tva.add(caList.getTva());
                    remise = remise.add(caList.getRemise());
                    offert = offert.add(caList.getOffert());
                    
                    idSousFamilleInit = caList.getIdSousFamille();
                }
            }   
            totalMontantCa = totalMontantCa(dateEntry);
            totalCaHt = totalCaHt(dateEntry);
            totalTva = totalTva(dateEntry);
            totalRemise = totalRemise(dateEntry);
            totalOffert = totalOffert(dateEntry);
            totalNbCouvert = totalNbCouvert(dateEntry).intValue();
            
            totalCaSubFamily.setLibelle("TOTAL");
            totalCaSubFamily.setTauxTva(new BigDecimal("0"));
            totalCaSubFamily.setMontantHt(totalCaHt);
            totalCaSubFamily.setMontantTva(totalTva);
            totalCaSubFamily.setMontantTtc(totalMontantCa);
            totalCaSubFamily.setMontantRemise(totalRemise);
            totalCaSubFamily.setMontantOffert(totalOffert);
            totalCaSubFamily.setNbCouvert(totalNbCouvert);
            totalCaSubFamily.setService("0");
            
            try {
                caSubFamily.setDateCloture(dateRef);
                entityManager.persist(caSubFamily);
                totalCaSubFamily.setDateCloture(dateRef);
                entityManager.persist(totalCaSubFamily);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        
        //LIST  CASHING    
        List<VPosEncaissement> listCashing = entityManager.createQuery("FROM VPosEncaissement WHERE Date(dateEncaissement) = '" + dateCloture + "' ORDER BY mmcModeEncaissementId  ").getResultList();
        if (listCashing.size() > 0) {
            VPosEncaissement valueListCashing = listCashing.get(0);
            Integer idModeCashingInit = valueListCashing.getMmcModeEncaissementId();
            var libelleModeCashing = "";
            var service = "";
           
            libelleModeCashing = valueListCashing.getLibelleModeEncaissement();
            
            var montantCashingMidi = BigDecimal.ZERO;
            var montantCashingSoir = BigDecimal.ZERO;
            var totalMontantCashing = BigDecimal.ZERO;
            var totalMontantEncaissement = BigDecimal.ZERO;
            
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPosClotureProvisoire cashing = new TPosClotureProvisoire();
            TPosClotureProvisoire totalCashing = new TPosClotureProvisoire();
            
            for(VPosEncaissement cashingList : listCashing){
                if(idModeCashingInit.equals(cashingList.getMmcModeEncaissementId())){
                    service = cashingList.getService();
                    if(service.equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());                           
                    }else if(service.equals("S")){                           
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                    
                    cashing.setLibelle(cashingList.getLibelleModeEncaissement());
                    cashing.setMontantEncaissement(totalMontantCashing);
                }else{
                    try {
                        cashing.setDateCloture(dateRef);
                        entityManager.persist(cashing);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    cashing = new TPosClotureProvisoire(); 
                    cashing.setLibelle(cashingList.getLibelleModeEncaissement());
                    
                    montantCashingMidi = new BigDecimal("0");
                    montantCashingSoir = new BigDecimal("0");
                    totalMontantCashing = new BigDecimal("0");
                    if(cashingList.getService().equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());
                        montantCashingSoir = new BigDecimal("0");
                    }else if(cashingList.getService().equals("S")){
                        montantCashingMidi = new BigDecimal("0");
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                    cashing.setMontantEncaissement(totalMontantCashing);
                    idModeCashingInit = cashingList.getMmcModeEncaissementId();
                    libelleModeCashing = cashingList.getLibelleModeEncaissement();
                }
            }
            totalMontantEncaissement = totalMontantEncaissement(dateCloture);
            
            totalCashing.setLibelle("TOTAL ENCAISSEMENT");
            totalCashing.setMontantEncaissement(totalMontantEncaissement);
            
            try {
                cashing.setDateCloture(dateRef);
                entityManager.persist(cashing);
                totalCashing.setDateCloture(dateRef);
                entityManager.persist(totalCashing);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }
    
    
    //CLOSURE DEFINITIVE POS
    public void deletePosClotureDefinitive(String dateReference) {
        entityManager.createNativeQuery(" DELETE FROM `t_pos_cloture_definitive` WHERE date_cloture=:dateReference").setParameter("dateReference", dateReference)
                .executeUpdate();
    } 
    
    public List<TPosClotureDefinitive> getPosClotureDefinitive(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPosClotureDefinitive  ");
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE dateCloture  = '" + dateReference + "'");
        }

        List<TPosClotureDefinitive> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }
    
    public void setPosClotureDefinitive(JsonObject request)
            throws CustomConstraintViolationException, ParseException {
        String dateCloture = request.getString("dateReference");
        deletePosClotureDefinitive(dateCloture);
        
        //LIST  CA
        StringBuilder stringBuilderCa = new StringBuilder();
        stringBuilderCa.append("FROM VPosCa  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        
        if (!Objects.isNull(dateCloture)) {
            stringBuilderCa.append(" WHERE Date(dateCa) = '" + dateCloture + "' ORDER BY idSousFamille ");
            dateEntry = dateCloture;
        }       
        List<VPosCa> listCa = entityManager.createQuery(stringBuilderCa.toString()).getResultList();
        
        if (listCa.size() > 0) {
            VPosCa valueListCa = listCa.get(0);
            Integer idSousFamilleInit = valueListCa.getIdSousFamille();
            var libelleSousFamille = "";
            var txTva = BigDecimal.ZERO;
            var service = "";
            
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCa = BigDecimal.ZERO;
            var caHt = BigDecimal.ZERO;
            var tva = BigDecimal.ZERO;
            var remise = BigDecimal.ZERO;
            var offert = BigDecimal.ZERO;
            var nbCouvert = BigDecimal.ZERO;
            
            var totalMontantCa = BigDecimal.ZERO;
            var totalCaHt = BigDecimal.ZERO;
            var totalTva = BigDecimal.ZERO;
            var totalRemise = BigDecimal.ZERO;
            var totalOffert = BigDecimal.ZERO;
            var totalNbCouvert = 0;
            
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPosClotureDefinitive caSubFamily = new TPosClotureDefinitive();
            TPosClotureDefinitive totalCaSubFamily = new TPosClotureDefinitive();
            for(VPosCa caList : listCa){
                if(idSousFamilleInit.equals(caList.getIdSousFamille())){
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCa = montantCa.add(caList.getMontantCa());
                    caHt = caHt.add(caList.getCaHt());
                    tva = tva.add(caList.getTva());
                    remise = remise.add(caList.getRemise());
                    offert = offert.add(caList.getOffert());
                    nbCouvert = caList.getNbCouvert();
                    
                    caSubFamily.setLibelle(libelleSousFamille);
                    caSubFamily.setTauxTva(txTva);
                    caSubFamily.setMontantHt(caHt);
                    caSubFamily.setMontantTva(tva);
                    caSubFamily.setMontantTtc(montantCa);
                    caSubFamily.setMontantRemise(remise);
                    caSubFamily.setMontantOffert(offert);
                    caSubFamily.setNbCouvert(nbCouvert.intValue());
                    caSubFamily.setService(service);
                }else{
                    try {
                        caSubFamily.setDateCloture(dateRef);
                        entityManager.persist(caSubFamily);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    caSubFamily = new TPosClotureDefinitive();
                    caSubFamily.setLibelle(caList.getLibelleSousFamille());
                    caSubFamily.setTauxTva(caList.getTxTva());
                    caSubFamily.setMontantHt(caList.getCaHt());
                    caSubFamily.setMontantTva(caList.getTva());
                    caSubFamily.setMontantTtc(caList.getMontantCa());
                    caSubFamily.setMontantRemise(caList.getRemise());
                    caSubFamily.setMontantOffert(caList.getOffert());
                    caSubFamily.setNbCouvert(caList.getNbCouvert().intValue());
                    caSubFamily.setService(caList.getService());
                    
                    montantCa = BigDecimal.ZERO;
                    caHt = BigDecimal.ZERO;
                    tva = BigDecimal.ZERO;
                    remise = BigDecimal.ZERO;
                    offert = BigDecimal.ZERO;
                    
                    libelleSousFamille = caList.getLibelleSousFamille();
                    service = caList.getService();
                    txTva = caList.getTxTva();
                    montantCa = montantCa.add(caList.getMontantCa());
                    caHt = caHt.add(caList.getCaHt());
                    tva = tva.add(caList.getTva());
                    remise = remise.add(caList.getRemise());
                    offert = offert.add(caList.getOffert());
                    
                    idSousFamilleInit = caList.getIdSousFamille();
                }
            }   
            totalMontantCa = totalMontantCa(dateEntry);
            totalCaHt = totalCaHt(dateEntry);
            totalTva = totalTva(dateEntry);
            totalRemise = totalRemise(dateEntry);
            totalOffert = totalOffert(dateEntry);
            totalNbCouvert = totalNbCouvert(dateEntry).intValue();
            
            totalCaSubFamily.setLibelle("TOTAL");
            totalCaSubFamily.setTauxTva(new BigDecimal("0"));
            totalCaSubFamily.setMontantHt(totalCaHt);
            totalCaSubFamily.setMontantTva(totalTva);
            totalCaSubFamily.setMontantTtc(totalMontantCa);
            totalCaSubFamily.setMontantRemise(totalRemise);
            totalCaSubFamily.setMontantOffert(totalOffert);
            totalCaSubFamily.setNbCouvert(totalNbCouvert);
            totalCaSubFamily.setService("0");
            
            try {
                caSubFamily.setDateCloture(dateRef);
                entityManager.persist(caSubFamily);
                totalCaSubFamily.setDateCloture(dateRef);
                entityManager.persist(totalCaSubFamily);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        
        //LIST  CASHING       
        List<VPosEncaissement> listCashing = entityManager.createQuery("FROM VPosEncaissement WHERE Date(dateEncaissement) = '" + dateCloture + "' ORDER BY mmcModeEncaissementId  ").getResultList();

        if (listCashing.size() > 0) {
            VPosEncaissement valueListCashing = listCashing.get(0);
            Integer idModeCashingInit = valueListCashing.getMmcModeEncaissementId();
            var libelleModeCashing = "";
            var service = "";
           
            libelleModeCashing = valueListCashing.getLibelleModeEncaissement();
            
            var montantCashingMidi = BigDecimal.ZERO;
            var montantCashingSoir = BigDecimal.ZERO;
            var totalMontantCashing = BigDecimal.ZERO;
            var totalMontantEncaissement = BigDecimal.ZERO;
            
            LocalDate dateRef = LocalDate.parse(dateCloture);
            TPosClotureDefinitive cashing = new TPosClotureDefinitive();
            TPosClotureDefinitive totalCashing = new TPosClotureDefinitive();
            
            for(VPosEncaissement cashingList : listCashing){
                if(idModeCashingInit.equals(cashingList.getMmcModeEncaissementId())){
                    service = cashingList.getService();
                    if(service.equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());                           
                    }else if(service.equals("S")){                           
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                    
                    cashing.setLibelle(cashingList.getLibelleModeEncaissement());
                    cashing.setMontantEncaissement(totalMontantCashing);
                }else{
                    try {
                        cashing.setDateCloture(dateRef);
                        entityManager.persist(cashing);
                    } catch (ConstraintViolationException ex) {
                        throw new CustomConstraintViolationException(ex);
                    }
                    cashing = new TPosClotureDefinitive(); 
                    cashing.setLibelle(cashingList.getLibelleModeEncaissement());
                    
                    montantCashingMidi = new BigDecimal("0");
                    montantCashingSoir = new BigDecimal("0");
                    totalMontantCashing = new BigDecimal("0");
                    if(cashingList.getService().equals("M")){
                        montantCashingMidi = montantCashingMidi.add(cashingList.getMontantTtc());
                        montantCashingSoir = new BigDecimal("0");
                    }else if(cashingList.getService().equals("S")){
                        montantCashingMidi = new BigDecimal("0");
                        montantCashingSoir = montantCashingSoir.add(cashingList.getMontantTtc());
                    }
                    totalMontantCashing = montantCashingMidi.add(montantCashingSoir);
                    cashing.setMontantEncaissement(totalMontantCashing);
                    idModeCashingInit = cashingList.getMmcModeEncaissementId();
                    libelleModeCashing = cashingList.getLibelleModeEncaissement();
                }
            }
            totalMontantEncaissement = totalMontantEncaissement(dateCloture);
            
            totalCashing.setLibelle("TOTAL ENCAISSEMENT");
            totalCashing.setMontantEncaissement(totalMontantEncaissement);
            
            try {
                cashing.setDateCloture(dateRef);
                entityManager.persist(cashing);
                totalCashing.setDateCloture(dateRef);
                entityManager.persist(totalCashing);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }
}
