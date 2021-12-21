/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.RemittanceInBank;
import cloud.multimicro.mmc.Entity.RemittanceInBankDetail;
import cloud.multimicro.mmc.Entity.TMmcDeviceCloture;
import cloud.multimicro.mmc.Entity.TMmcJournalOperation;
import cloud.multimicro.mmc.Entity.TMmcModeEncaissement;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.TPmsEncaissement;
import cloud.multimicro.mmc.Entity.TPmsFacture;
import cloud.multimicro.mmc.Entity.TPmsFactureDetail;
import cloud.multimicro.mmc.Entity.TPmsNoteDetail;
import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPosEncaissement;
import cloud.multimicro.mmc.Entity.TPosFacture;
import cloud.multimicro.mmc.Entity.TPosFactureDetail;
import cloud.multimicro.mmc.Entity.TPosNoteDetail;
import cloud.multimicro.mmc.Entity.TPosNoteEntete;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.VPmsEncaissement;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class CashingDao {

    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    private EntityManager entityManagerEstablishement;

    // CASHING MODE
    public List<TMmcModeEncaissement> getAll() {
        List<TMmcModeEncaissement> collectionMode = entityManager
                .createQuery("FROM TMmcModeEncaissement m WHERE m.dateDeletion = null  ORDER BY libelle")
                .getResultList();
        return collectionMode;
    }

    public TMmcModeEncaissement getModeEncaissementById(int id) {
        TMmcModeEncaissement collectionMode = entityManager.find(TMmcModeEncaissement.class, id);
        return collectionMode;
    }

    public void setModeEncaissement(TMmcModeEncaissement modeEncaissement) throws CustomConstraintViolationException {
        try {
            entityManager.persist(modeEncaissement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TMmcModeEncaissement updateModeEncaissement(TMmcModeEncaissement modeEncaissement)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(modeEncaissement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteModeEncaissement(int id) throws CustomConstraintViolationException {
        TMmcModeEncaissement collectionMode = getModeEncaissementById(id);
        Date dateDel = new Date();
        collectionMode.setDateDeletion(dateDel);
        try {
            entityManager.merge(collectionMode);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // CASHING PMS
    public List<TPmsEncaissement> getPmsEncaissement() {
        List<TPmsEncaissement> collection = entityManager.createQuery("FROM TPmsEncaissement").getResultList();
        return collection;
    }

    public TPmsEncaissement getPmsEncaissementById(int id) {
        TPmsEncaissement collection = entityManager.find(TPmsEncaissement.class, id);
        return collection;
    }

    public void setPmsEncaissement(TPmsEncaissement encaissement)
            throws CustomConstraintViolationException, ParseException {
        String action = "PMS-ADD-CASHING";
        LocalTime currentTime = LocalTime.now();      
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        TMmcParametrage invoiceHeader1 = entityManager.find(TMmcParametrage.class, "INVOICE_HEADER_1");
        TMmcParametrage invoiceHeader2 = entityManager.find(TMmcParametrage.class, "INVOICE_HEADER_2");
        TMmcParametrage invoiceHeader3 = entityManager.find(TMmcParametrage.class, "INVOICE_HEADER_3");
        TMmcParametrage invoiceFooter1 = entityManager.find(TMmcParametrage.class, "INVOICE_FOOTER_1");
        TMmcParametrage invoiceFooter2 = entityManager.find(TMmcParametrage.class, "INVOICE_FOOTER_2");
        TMmcParametrage invoiceFooter3 = entityManager.find(TMmcParametrage.class, "INVOICE_FOOTER_3");
        LocalDate dateLogiciel = LocalDate.parse(parametrageDateLogicielle.getValeur());
        LocalDateTime dateEtatSolde = currentTime.atDate(dateLogiciel);       
        BigDecimal amountCashed = encaissement.getMontant();
        BigDecimal totalNote = totalMontantByNoteHeaderId(encaissement.getPmsNoteEnteteId());
        BigDecimal amountAlreadyCashed = totalMontantExistingCashed(encaissement.getPmsNoteEnteteId());
        BigDecimal totalAmount = amountCashed.add(amountAlreadyCashed);
        List<TPmsNoteDetail> noteDetail = entityManager.createQuery("FROM TPmsNoteDetail WHERE dateDeletion = null ").getResultList();
        List<TPmsPrestation> Prestation = entityManager.createQuery("FROM TPmsPrestation WHERE dateDeletion = null ").getResultList();
        List<TMmcDeviceCloture> mmcDeviceClotureList = entityManager
                .createQuery("SELECT deviceUuid FROM TMmcDeviceCloture WHERE dateStatus=:parameter")
                .setParameter("parameter", dateLogiciel).getResultList();      
        String DeviceUuid = "";
        DeviceUuid = mmcDeviceClotureList.toString().replace("[","");
        DeviceUuid = DeviceUuid.replace("]","");
        
        TPmsFacture facture = new TPmsFacture();
        TPmsFactureDetail factureDetail = new TPmsFactureDetail();

        if (encaissement.getPmsNoteEnteteId() != null) {
            TPmsNoteEntete noteHeader = entityManager.find(TPmsNoteEntete.class, encaissement.getPmsNoteEnteteId());
            for (TPmsNoteDetail noteDetailList : noteDetail) {
                if(noteDetailList.getPmsNoteEnteteId().equals(encaissement.getPmsNoteEnteteId())){
                    //BigDecimal oneHundred = new BigDecimal(100);
                    BigDecimal noteDetailPu = noteDetailList.getPu();
                    BigDecimal noteDetailqte = new BigDecimal(noteDetailList.getQte());
                    BigDecimal montantHt = noteDetailqte.multiply(noteDetailPu);
                    facture.setMontantHt(montantHt);
                    factureDetail.setMontantHt(montantHt);
                    /*BigDecimal tauxTva = montantHt.divide(oneHundred).multiply(noteDetailList.getTauxTva());
                    BigDecimal tauxCommission = montantHt.divide(oneHundred).multiply(noteDetailList.getTauxCommission());
                    BigDecimal tauxRemise = montantHt.divide(oneHundred).multiply(noteDetailList.getTauxRemise());*/
                    BigDecimal montantTtc =  new BigDecimal(0);
                    montantTtc = montantTtc.add(noteDetailList.getTauxTva());
                    montantTtc = montantTtc.add(noteDetailList.getTauxCommission());
                    montantTtc = montantTtc.add(noteDetailList.getTauxRemise());
                    montantTtc = montantTtc.add(montantHt);                   
                    facture.setMontantTtc(montantTtc);
                    factureDetail.setMontantTtc(montantTtc);
                    //tauxRemise = tauxRemise.add(montantHt);
                    //BigDecimal montantRemise = montantHt.multiply(noteDetailList.getTauxRemise());
                    facture.setMontantRemise(noteDetailList.getTauxRemise());
                }
                for(TPmsPrestation PrestationList : Prestation ){
                    if(noteDetailList.getPmsPrestationId().equals(PrestationList.getId())){
                       factureDetail.setLibellePrestation(PrestationList.getLibelle()); 
                    }
                }
            } 
            String dateLogicielle = parametrageDateLogicielle.getValeur();
            String[] data = dateLogicielle.split("-", 2);
            String numFact = getInvoiceNumber("INVOICE_INDEX", data[0]);
            noteHeader.setNumFacture(numFact);
            noteHeader.setDateFacture(dateLogiciel);
            //
            facture.setNumero(numFact);
            facture.setDateFacture(dateLogiciel);
            facture.setDateEcheance(dateLogiciel);
            facture.setEntete1(invoiceHeader1.getValeur());         
            facture.setEntete2(invoiceHeader2.getValeur());
            facture.setEntete3(invoiceHeader3.getValeur());
            facture.setBas1(invoiceFooter1.getValeur());
            facture.setBas2(invoiceFooter2.getValeur());           
            facture.setBas3(invoiceFooter3.getValeur());             
            facture.setDeviceUuid(DeviceUuid);
            facture.setUtilisateur("herizo");
            //
            factureDetail.setPmsFactureNumero(numFact);
            factureDetail.setQteCde(2);           
            if(amountCashed.equals(totalNote) || amountCashed.compareTo(totalNote) == 1 || totalAmount.equals(totalNote) || totalAmount.compareTo(totalNote) == 1){
                noteHeader.setEtat("SOLDE");
                noteHeader.setDateEtatSolde(dateEtatSolde);
            }else{
                noteHeader.setEtat("ENCOURS");
            }
            try {
                entityManager.merge(noteHeader);
                entityManager.merge(facture);
                entityManager.merge(factureDetail);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        try {
            encaissement.setDateEncaissement(dateLogiciel);
            entityManager.persist(encaissement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        /*
         * JsonObject value; value = Json.createObjectBuilder()
         * .add("mmcModeEncaissementId", encaissement.getMmcModeEncaissementId())
         * .add("pmsNoteEnteteId", encaissement.getPmsNoteEnteteId() != null ?
         * encaissement.getPmsNoteEnteteId() : 0) .add("mmcUserId",
         * encaissement.getMmcUserId()) .add("dateEncaissement",
         * parametrageDateLogicielle.getValeur()) .add("posteUuid",
         * encaissement.getPosteUuid()) .add("montant", (encaissement.getMontant() ==
         * null) ? new BigDecimal(0.0) : encaissement.getMontant()) .build();
         * setJournalOperation(action, value);
         */
    }

    public TPmsEncaissement updatePmsEncaissement(TPmsEncaissement cashing) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(cashing);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // CASHING POS
    public List<TPosEncaissement> getPosEncaissement() {
        List<TPosEncaissement> collection = entityManager.createQuery("FROM TPosEncaissement").getResultList();
        return collection;
    }

    // CASHING POS
    public List<RemittanceInBankDetail> getRemittanceInBanksDetailed(String begin, String end, String cashingMode)
            throws ParseException {
        List<RemittanceInBankDetail> cashingList = new ArrayList<>();

        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "SELECT enc.date_encaissement, cl.code, cl.nom, ent.num_facture, mo.libelle, enc.montant FROM t_pms_encaissement enc LEFT JOIN t_mmc_mode_encaissement mo ON mo.id = enc.mmc_mode_encaissement_id LEFT JOIN t_pms_note_entete ent ON ent.id = enc.pms_note_entete_id LEFT JOIN t_pms_sejour sej ON sej.id = ent.pms_sejour_id LEFT JOIN t_mmc_client cl ON cl.id = sej.mmc_client_id ");

        if (!Objects.isNull(begin)) {
            stringBuilder.append(" WHERE enc.date_encaissement >='" + begin + "'");
            isExist = true;
        }

        if (!Objects.isNull(end)) {
            if (isExist == true) {
                stringBuilder.append(" AND enc.date_encaissement <= '" + end + "'");
            } else {
                stringBuilder.append(" WHERE enc.date_encaissement <= '" + end + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(cashingMode)) {
            if (isExist == true) {
                stringBuilder.append(" AND mo.libelle = '" + cashingMode + "'");
            } else {
                stringBuilder.append(" WHERE mo.libelle = '" + cashingMode + "'");
                isExist = true;
            }
        }

        List<Object[]> objectList = entityManager.createNativeQuery(stringBuilder.toString()).getResultList();

        final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

        for (Object[] n : objectList) {
            if (n.length > 1) {
                RemittanceInBankDetail remittanceInBank = new RemittanceInBankDetail();
                remittanceInBank.setCashingDate(format.parse(n[0].toString()));
                remittanceInBank.setClientAccount(n[1].toString());
                remittanceInBank.setClientName(n[2].toString());
                remittanceInBank.setInformation(n[3].toString());
                remittanceInBank.setCashingMode(n[4].toString());
                remittanceInBank.setAmount(new BigDecimal(n[5].toString()));
                cashingList.add(remittanceInBank);
            }
        }
        return cashingList;
    }

    // CASHING POS
    public List<RemittanceInBank> getRemittanceInBanks(String begin, String end, String activity) {
        List<RemittanceInBank> cashingList = new ArrayList<>();

        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "Select SUM(enc.montant) montant, modeEnc.libelle modeEncaissement From t_pms_encaissement  enc LEFT JOIN t_mmc_mode_encaissement modeEnc ON modeEnc.id = enc.mmc_mode_encaissement_id ");

        if (!Objects.isNull(begin)) {
            stringBuilder.append(" WHERE enc.date_encaissement >='" + begin + "'");
            isExist = true;
        }

        if (!Objects.isNull(end)) {
            if (isExist == true) {
                stringBuilder.append(" AND enc.date_encaissement <= '" + end + "'");
            } else {
                stringBuilder.append(" WHERE enc.date_encaissement <= '" + end + "'");
                isExist = true;
            }
        }

        stringBuilder.append(" group by enc.mmc_mode_encaissement_id;");

        List<Object[]> objectList = entityManager.createNativeQuery(stringBuilder.toString()).getResultList();

        for (Object[] n : objectList) {
            if (n.length > 1) {
                RemittanceInBank remittanceInBank = new RemittanceInBank();
                remittanceInBank.setAmount(new BigDecimal(n[0].toString()));
                remittanceInBank.setCashingMode(n[1].toString());
                cashingList.add(remittanceInBank);
            }
        }
        return cashingList;
    }

    public TPosEncaissement getPosEncaissementById(int id) {
        TPosEncaissement collection = entityManager.find(TPosEncaissement.class, id);
        return collection;
    }

    public void setPosEncaissement(TPosEncaissement encaissement) throws CustomConstraintViolationException {
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate daten = LocalDate.parse(parametrageDateLogicielle.getValeur());
        
        //
        List<TPosNoteDetail> noteDetail = entityManager.createQuery("FROM TPosNoteDetail WHERE dateDeletion = null ").getResultList();
        List<TPosPrestation> Prestation = entityManager.createQuery("FROM TPosPrestation WHERE dateDeletion = null ").getResultList();
        List<TMmcDeviceCloture> mmcDeviceClotureList = entityManager
                .createQuery("SELECT deviceUuid FROM TMmcDeviceCloture WHERE dateStatus=:parameter")
                .setParameter("parameter", daten).getResultList();
        String DeviceUuid = "";
        DeviceUuid = mmcDeviceClotureList.toString().replace("[","");
        DeviceUuid = DeviceUuid.replace("]","");
        TPosFacture facturePos = new TPosFacture();
        TPosFactureDetail factureDetailPos = new TPosFactureDetail();
        if (encaissement.getPosNoteEnteteId() != null) {
            TPosNoteEntete noteHeader = entityManager.find(TPosNoteEntete.class, encaissement.getPosNoteEnteteId()); 
            for (TPosNoteDetail noteDetailList : noteDetail) {
                if(noteDetailList.getPosNoteEnteteId().equals(encaissement.getPosNoteEnteteId())){
                    BigDecimal noteDetailPu = noteDetailList.getPosPrestationPrix();
                    BigDecimal noteDetailqte = new BigDecimal(noteDetailList.getQte());
                    BigDecimal montantHt = noteDetailqte.multiply(noteDetailPu);
                    facturePos.setMontantHt(montantHt);
                    factureDetailPos.setMontantHt(montantHt);
                    facturePos.setMontantTtc(montantHt);
                    factureDetailPos.setQteCde(noteDetailList.getQteCdeMarche());
                }
                for(TPosPrestation PrestationList : Prestation ){
                    if(noteDetailList.getPosPrestationId().equals(PrestationList.getId())){
                       factureDetailPos.setLibellePrestation(PrestationList.getLibelle()); 
                    }
                }
            } 
            String numFact = getInvoiceNumberPos(noteHeader.getPosteUuid());
            noteHeader.setNumFacture(numFact);
            facturePos.setNumero(numFact);
            facturePos.setDateFacture(daten);
            facturePos.setDateEcheance(daten);
            facturePos.setEntete1("Adresse: "+ "<br>" +"CP Ville:"+ "<br>" +"Téléphone"+ "<br>" +"Références Internet: ");
            facturePos.setEntete2(" Référence: "+ "<br>" +"Date"+ "<br>" +"N°client");
            facturePos.setEntete3("Société et/ou Nom du client "+ "<br>" +" Adresse  "+ "<br>" +" CP Ville");
            facturePos.setBas1("En votre aimable règlement,"+ "<br>" +"Cordialement,");
            facturePos.setBas2("Conditions de paiement : paiement à réception de facture, à 30 jours..."+ "<br>" +" Aucun escomptè consenti pour le règlement anticipé"+ "<br>" +
            "Tout incident de paiement est passible d'intérêt de retard. Le montant des pénalités résulte de l'application"+ "<br>" +
            "aux sommes restant dues d'un taux d'intérêt légal en vigueur au moment de l'incident.Indemnité forfaitaire pour frais de recouvrement due au créancier en cas de retard de "+ "<br>" +"paiement: 40 €");
            facturePos.setBas3("N° Siret 210.890.764 00015 RCS Monpelier "+ "<br>" +"Code APE 947A-N°TVA intracom FR 77825696764000     ");                     
            facturePos.setMontantRemise(new BigDecimal(0));
            facturePos.setDeviceUuid(DeviceUuid);
            facturePos.setUtilisateur("herizo");
             //
            factureDetailPos.setPosFactureNumero(numFact);
                    
            
            factureDetailPos.setMontantTtc(new BigDecimal(0));
            try {
                entityManager.merge(noteHeader);
                entityManager.persist(facturePos);
                entityManager.persist(factureDetailPos);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
        
        try {
            entityManager.persist(encaissement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        } 
    }

    public String getInvoiceNumber(String key, String dateLogicielle) {
        TMmcParametrage parametrage = entityManager.find(TMmcParametrage.class, key);
        String numero = "";
        int value = Integer.parseInt(parametrage.getValeur()) + 1;
        parametrage.setValeur(Integer.toString(value));
        entityManager.merge(parametrage);
        numero = "FAC-"+dateLogicielle +"-"+ String.format("%011d", value);
        return numero;
    }
    
    public String getInvoiceNumberPos(String posteUuid){
        String invoice = entityManagerEstablishement.createNativeQuery("SELECT invoice_current_num FROM t_device WHERE uuid = '"+ posteUuid +"' ")
                .getSingleResult().toString();
        
        String invoicePrefix = entityManagerEstablishement.createNativeQuery("SELECT invoice_prefix FROM t_device WHERE uuid = '"+ posteUuid +"' ")
                .getSingleResult().toString();
        
        String numInvoice = "";
        int invoiceIncrement = Integer.parseInt(invoice) + 1;
        numInvoice = Integer.toString(invoiceIncrement);
        entityManagerEstablishement.createNativeQuery("UPDATE t_device SET invoice_current_num = '"+numInvoice+"' WHERE uuid = '"+ posteUuid +"' ").executeUpdate();
        String numberInvoice = invoicePrefix + String.format("%011d", invoiceIncrement);
        return numberInvoice;
    }

    public List<TPmsEncaissement> getCashingByNoteHeaderId(Integer pmsNoteEnteteId) {
        List<TPmsEncaissement> cashingList = entityManager
                .createQuery("FROM TPmsEncaissement WHERE pmsNoteEnteteId =:pmsNoteEnteteId")
                .setParameter("pmsNoteEnteteId", pmsNoteEnteteId).getResultList();

        return cashingList;
    }
    
    private BigDecimal totalMontantByNoteHeaderId(Integer pmsNoteEnteteId) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT SUM(pu*qte) FROM t_pms_note_detail "
                + "WHERE pms_note_entete_id =:pmsNoteEnteteId  ")
                .setParameter("pmsNoteEnteteId", pmsNoteEnteteId).getSingleResult();       
    }
    
    private BigDecimal totalMontantExistingCashed(Integer pmsNoteEnteteId) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant), 0) FROM t_pms_encaissement "
                + "WHERE pms_note_entete_id =:pmsNoteEnteteId  ")
                .setParameter("pmsNoteEnteteId", pmsNoteEnteteId).getSingleResult();       
    }

    public void setJournalOperation(String action, JsonObject detail) throws CustomConstraintViolationException {
        TMmcJournalOperation mmcJournalOperation = new TMmcJournalOperation();
        mmcJournalOperation.setAction(action);
        mmcJournalOperation.setDetail(detail.toString());
        try {
            entityManager.persist(mmcJournalOperation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void refundArrhe(JsonObject object)
            throws DataException, ParseException, CustomConstraintViolationException {
        String amountStr = object.getString("montant");
        Integer mmcUserId = Integer.parseInt(object.get("mmcUserId").toString());
        Integer arrheId = Integer.parseInt(object.get("id").toString());
        TPmsArrhe pmsArrhe = entityManager.find(TPmsArrhe.class, arrheId);
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate softwareDate = LocalDate.parse(parametrageDateLogicielle.getValeur());
        List<TMmcModeEncaissement> mmcModeEncaissementList = entityManager
                .createQuery("FROM TMmcModeEncaissement WHERE nature = 'REMBTARR'").getResultList();
        if (mmcModeEncaissementList.size() > 0) {
            TPmsEncaissement pmsEncaissement = new TPmsEncaissement();
            pmsEncaissement.setDateEncaissement(softwareDate);
            BigDecimal amount = new BigDecimal(amountStr);
            pmsEncaissement.setMontant(amount.multiply(new BigDecimal("-1")));
            pmsEncaissement.setPosteUuid(object.getString("posteUuid"));
            pmsEncaissement.setMmcUserId(mmcUserId);
            pmsEncaissement.setPmsNoteEnteteId(Integer.parseInt(object.get("pmsNoteEnteteId").toString()));
            pmsArrhe.setDateRemboursement(softwareDate);

            try {
                entityManager.merge(pmsArrhe);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }

            try {
                entityManager.persist(pmsEncaissement);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        } else {
            throw new DataException("No payment method configured");
        }
    }
    
    //Cloture provisoire PMS
    public JsonArray getAllListCashing(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEncaissement  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        var dateMonthEntry = "";
        var dateYearEntry = "";
        
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateReference + "' ORDER BY idModeEncaissement ");
            dateYearEntry = dateReference.substring(0, 4);
            dateMonthEntry = dateReference.substring(0, 7);
            dateEntry = dateReference;
        } else {
            stringBuilder.append(" WHERE Date(dateEncaissement) <= '" + dateLog + "' ORDER BY idModeEncaissement ");
            dateYearEntry = dateLog.substring(0, 4);
            dateMonthEntry = dateLog.substring(0, 7);
            dateEntry = dateLog;
        }       
        List<VPmsEncaissement> listCashing = entityManager.createQuery(stringBuilder.toString()).getResultList();
        
        var cashingResults = Json.createArrayBuilder();
        var cashingResultsAll = Json.createArrayBuilder();
        
        if (listCashing.size() > 0) {
            VPmsEncaissement valueListCashing = listCashing.get(0);
            Integer modeCashingIdInitial = valueListCashing.getIdModeEncaissement();
            var libelleModeEncaissement = valueListCashing.getLibelleEncaissement();
            
            var montantCashingJour   = BigDecimal.ZERO;
            //var montantCaPeriode = new BigDecimal("0");
            var montantCashingMois   = BigDecimal.ZERO;
            var montantCashingAnnee  = BigDecimal.ZERO;
            
            var totalMontantCashingJour      = BigDecimal.ZERO;
            //var totalMontantCaPeriode = BigDecimal.ZERO;
            var totalMontantCashingMois      = BigDecimal.ZERO;
            var totalMontantCashingAnnee     = BigDecimal.ZERO;            

            for (VPmsEncaissement cashingList : listCashing) {
                if (modeCashingIdInitial.equals(cashingList.getIdModeEncaissement())) {
                    libelleModeEncaissement = cashingList.getLibelleEncaissement();
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntry);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntry, dateMonthEntry);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntry, dateYearEntry);
                    
                } else {
                    var object = Json.createObjectBuilder()
                            .add("libelleEncaissement", libelleModeEncaissement)
                            .add("montantCashingJour", montantCashingJour)
                            .add("montantCashingPeriode", montantCashingJour)
                            .add("montantCashingMois", montantCashingMois)
                            .add("montantCashingAnnee", montantCashingAnnee).build();

                    cashingResults.add(object);

                    montantCashingJour   = BigDecimal.ZERO;
                    //var montantCaPeriode = new BigDecimal("0");
                    montantCashingMois   = BigDecimal.ZERO;
                    montantCashingAnnee  = BigDecimal.ZERO;
                    
                    montantCashingJour  = sumCashingDay(cashingList.getIdModeEncaissement(), dateEntry);
                    montantCashingMois  = sumCashingMonth(cashingList.getIdModeEncaissement(), dateEntry, dateMonthEntry);
                    montantCashingAnnee = sumCashingYear(cashingList.getIdModeEncaissement(), dateEntry, dateYearEntry);

                    modeCashingIdInitial = cashingList.getIdModeEncaissement();
                }
            }
            
            var object = Json.createObjectBuilder()
                            .add("libelleEncaissement", libelleModeEncaissement)
                            .add("montantCashingJour", montantCashingJour)
                            .add("montantCashingPeriode", montantCashingJour)
                            .add("montantCashingMois", montantCashingMois)
                            .add("montantCashingAnnee", montantCashingAnnee).build();
                totalMontantCashingJour      = totalSumCashingDay(dateEntry);
                //var totalMontantCaPeriode = BigDecimal.ZERO;
                totalMontantCashingMois      = totalSumCashingMonth(dateEntry, dateMonthEntry);
                totalMontantCashingAnnee     = totalSumCashingYear(dateEntry, dateYearEntry);
                cashingResults.add(object);
                
                var resultJson = Json.createObjectBuilder()
                        .add("listCashing", cashingResults.build())
                        .add("totalMontantCashingJour", totalMontantCashingJour)
                        .add("totalMontantCashingPeriode", totalMontantCashingJour)
                        .add("totalMontantCashingMois", totalMontantCashingMois)
                        .add("totalMontantCashingAnnee", totalMontantCashingAnnee).build();
                
                cashingResultsAll.add(resultJson);
        }
               
        return cashingResultsAll.build();
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
    
    public BigDecimal totalDebitBalance(String dateReference) {
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLogicielle = settingData.getValeur();
        BigDecimal valueDebitBalance = new BigDecimal("0");
        if (!Objects.isNull(dateReference)) {
            valueDebitBalance = (BigDecimal) entityManager.createNativeQuery("SELECT SUM(montant) FROM t_pms_encaissement "
                + "WHERE Date(date_encaissement) =:dateReference AND is_reglmt_debiteur = 1 ")
                .setParameter("dateReference", dateReference).getSingleResult();  
        }else{
            valueDebitBalance = (BigDecimal) entityManager.createNativeQuery("SELECT SUM(montant) FROM t_pms_encaissement "
                + "WHERE Date(date_encaissement) ='"+dateLogicielle+"' AND is_reglmt_debiteur = 1 ").getSingleResult();
        }
        return valueDebitBalance;
    }
    
}
