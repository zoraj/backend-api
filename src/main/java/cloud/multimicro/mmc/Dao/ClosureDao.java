/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcDeviceCloture;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
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
import java.math.BigDecimal;
import java.util.List;
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

    public TMmcDeviceCloture update(TMmcDeviceCloture cloture) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(cloture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
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
    
    public JsonArray getAllListCaByActivity() {
        TMmcParametrage valueDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(valueDateLogicielle.getValeur());
        List<VPosCa> listCa = entityManager
                .createQuery("FROM VPosCa WHERE dateCa =:dateLogicielle ORDER BY posActiviteId, idSousFamille ")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();

        var caResults = Json.createArrayBuilder();
        var activityResults = Json.createArrayBuilder();
        
        if (listCa.size() > 0) {
            VPosCa valueListCa = listCa.get(0);
            Integer idActiviteInitial = valueListCa.getPosActiviteId();
            Integer idSousfamilleInitial = valueListCa.getIdSousFamille();

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

            for (VPosCa caList : listCa) {
                if (idActiviteInitial.equals(caList.getPosActiviteId())) {                              
                    totalMontantCaSF = totalMontantCaSF.add(caList.getMontantCa());
                    totalCaHtSF = totalCaHtSF.add(caList.getCaHt());
                    totalTvaSF = totalTvaSF.add(caList.getTva());
                    totalRemiseSF = totalRemiseSF.add(caList.getRemise());
                    totalOffertSF = totalOffertSF.add(caList.getOffert());
                    
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
                            .add("totalOffertSF", totalOffertSF).build();

                    caResults.add(object);
                   
                    activityResults = Json.createArrayBuilder();
                    
                    totalMontantCaSF = new BigDecimal("0");
                    totalCaHtSF = new BigDecimal("0");
                    totalTvaSF = new BigDecimal("0");
                    totalRemiseSF = new BigDecimal("0");
                    totalOffertSF = new BigDecimal("0");
                    
                    idActiviteInitial       = caList.getPosActiviteId();
                    libelleActivite         = caList.getLibelleActivite();                   
                    idSousfamilleInitial    = caList.getIdSousFamille();
                    libelleSousFamille      = caList.getLibelleSousFamille();
                    
                    totalMontantCaSF = totalMontantCaSF.add(caList.getMontantCa());
                    totalCaHtSF = totalCaHtSF.add(caList.getCaHt());
                    totalTvaSF = totalTvaSF.add(caList.getTva());
                    totalRemiseSF = totalRemiseSF.add(caList.getRemise());
                    totalOffertSF = totalOffertSF.add(caList.getOffert());
                    
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
                        .add("totalOffertSF", totalOffertSF).build();
            
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

}
