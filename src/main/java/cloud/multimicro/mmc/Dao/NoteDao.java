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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.json.JsonArray;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import cloud.multimicro.mmc.Entity.MonthlyTurnover;
import cloud.multimicro.mmc.Entity.PassageHistory;
import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcJournalOperation;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsNoteDetail;
import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.TPosNoteApprovision;
import cloud.multimicro.mmc.Entity.TPosNoteDetail;
import cloud.multimicro.mmc.Entity.TPosNoteDetailCommande;
import cloud.multimicro.mmc.Entity.TPosNoteEntete;
import cloud.multimicro.mmc.Entity.VPosNoteDetailVenteEmportee;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class NoteDao {

    @PersistenceContext
    EntityManager entityManager;
    private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(NoteDao.class);
    
    public void setPosNoteEntete(TPosNoteEntete headerNote) throws CustomConstraintViolationException {
        String action = "POS-ADD-NOTE-HEADER";

        try {
            entityManager.persist(headerNote);

        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        /*
         * JsonObject value = Json.createObjectBuilder() .add("dateReservation",
         * (headerNote.getDateReservation() == null) ? "" :
         * headerNote.getDateReservation().toString()) .add("service",
         * headerNote.getService()).add("posteUuid", headerNote.getPosteUuid())
         * .add("pourcentageRemise", (headerNote.getPourcentageRemise() == null) ? 0.0 :
         * headerNote.getPourcentageRemise()) .add("montantRemise",
         * (headerNote.getMontantRemise() == null) ? new BigDecimal(0.0) :
         * headerNote.getMontantRemise()) .add("dateNote",
         * headerNote.getDateNote().toString()) .add("nbCouvert",
         * (headerNote.getNbCouvert() == null) ? 0 : headerNote.getNbCouvert())
         * .add("numTable", (headerNote.getNumTable() == null) ? 0 :
         * headerNote.getNumTable()) .add("numFacture", (headerNote.getNumFacture() ==
         * null) ? "" : headerNote.getNumFacture()) .add("mmcClientId",
         * (headerNote.getMmcClientId() == null) ? 0 : headerNote.getMmcClientId())
         * .add("mmcClientId", (headerNote.getMmcClientId() == null) ? 0 :
         * headerNote.getMmcClientId()) .add("mmcClientId", (headerNote.getMmcClientId()
         * == null) ? 0 : headerNote.getMmcClientId()) .add("adresseLivraison",
         * headerNote.getAdresseLivraison()) .add("telephoneLivraison",
         * headerNote.getTelephoneLivraison()
         */

    }

    public void setPosNoteDetail(TPosNoteDetail detailNote)
            throws CustomConstraintViolationException, DataException, ParseException {
        String action = "POS-ADD-NOTE-DETAIL";

        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur());

        LocalDate dateEmp = detailNote.getDateEmportee();
        if (detailNote.getVenteType().equals("NORMAL")) {
            detailNote.setDateEmportee(null);
        }
        try {
            if ((detailNote.getVenteType().equals("EMPORTE") || detailNote.getVenteType().equals("EMPORTE+OFFERT"))
                    && (dateEmp == null || dateLogicielle.isAfter(dateEmp))) {
                throw new DataException("the date should not be null");
            }

            entityManager.persist(detailNote);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

        JsonObject value = (JsonObject) Json.createObjectBuilder()
                .add("posNoteEnteteId", detailNote.getPosNoteEnteteId())
                .add("posPrestationId", detailNote.getPosPrestationId())
                .add("posPrestationPrix", detailNote.getPosPrestationPrix())
                .add("qteCdeMarche", (detailNote.getQteCdeMarche() == null) ? 0 : detailNote.getQteCdeMarche())
                .add("qte", detailNote.getQte()).add("venteType", detailNote.getVenteType())
                .add("dateEmportee", dateEmp == null ? "" : dateEmp.toString()).build();

    }

    public void setPosNoteDetailCommande(TPosNoteDetailCommande detailNoteCommande)
            throws CustomConstraintViolationException {
        String action = "POS-ADD-NOTE-DETAIL-COMMANDE";
        try {
            entityManager.persist(detailNoteCommande);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

        JsonObject value = Json.createObjectBuilder().add("posNoteDetailId", detailNoteCommande.getPosNoteDetailId())
                .add("posCuissonId", detailNoteCommande.getPosCuissonId())
                .add("posAccompagnementId", detailNoteCommande.getPosAccompagnementId())
                .add("posSiropParfumId", detailNoteCommande.getPosSiropParfumId())
                .add("qteCde", detailNoteCommande.getQteCde()).build();

    }
    
    
    public void addTakeAway(JsonObject object) throws CustomConstraintViolationException {
        JsonObject jsonObject = object.getJsonObject("notedetail");
        JsonArray jsonArray = object.getJsonArray("detailcommandes");
        TPosNoteDetail noteDetail = null;
   
    try (Jsonb jsonb = JsonbBuilder.create()) { // Object mapping

        noteDetail = jsonb.fromJson(jsonObject.toString(), TPosNoteDetail.class);

        try {
            entityManager.persist(noteDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    catch(Exception ex){
        throw new CustomConstraintViolationException(ex.getMessage());
    }  

        for (int i = 0; i < jsonArray.size(); i++) {
            try (Jsonb jsonb = JsonbBuilder.create()) { 
            TPosNoteDetailCommande detailcommandes = jsonb.fromJson(jsonArray.getJsonObject(i).toString(), TPosNoteDetailCommande.class);
            detailcommandes.setPosNoteDetailId(noteDetail.getId());
            setPosNoteDetailCommande(detailcommandes);
        }
        catch(Exception ex){
            throw new CustomConstraintViolationException(ex.getMessage());
        }  
    
        } 
        
    }

    public List<TPosNoteEntete> getPosNoteHeader() {
        List<Object[]> noteEntete = entityManager
                .createQuery("FROM TPosNoteEntete head " + "LEFT JOIN TMmcClient c ON  head.mmcClientId = c.id  ")
                .getResultList();
        List<TPosNoteEntete> result = new ArrayList<TPosNoteEntete>();
        // Retrieve the corresponding sub-family
        for (Object[] note : noteEntete) {
            if (note.length > 1) {
                TPosNoteEntete head = (TPosNoteEntete) note[0];
                TMmcClient c = (TMmcClient) note[1];
                if (!Objects.isNull(c)) {
                    head.setMmcClient(c.getNom() + " " + c.getPrenom());
                }
                head.setMontant(getMontantTot(getPosNoteDetailByNoteHeaderId(head.getId())));
                result.add(head);
            }
        }

        return result;
    }

    public List<TPosNoteEntete> getPosOpenNote() {
        List<TPosNoteEntete> noteEntete = entityManager.createQuery("FROM TPosNoteEntete head WHERE etat<>:status")
                .setParameter("status", "SOLDE").getResultList();
        return noteEntete;

    }

    public List<TPosNoteEntete> getPosVaeNote() {       
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLogiciel = parametrageDateLogicielle.getValeur();
        LOGGER.info(" dateLogiciel " + dateLogiciel);
        
        List<TPosNoteEntete> noteEntete = entityManager.createQuery("FROM TPosNoteEntete WHERE poste_uuid = '_VAE_' AND date_note >= :dateLogiciel ")
                .setParameter("dateLogiciel", dateLogiciel).getResultList();      
        return noteEntete;   
    }

    public List<TPosNoteDetail> getPosNoteDetailByNoteHeaderId(Integer noteHeaderId) {
        return entityManager.createQuery("FROM TPosNoteDetail WHERE posNoteEnteteId=:noteHeaderId")
                .setParameter("noteHeaderId", noteHeaderId).getResultList();
    }
    
    public JsonArray getPosNoteDetailVaeByNoteHeaderId(Integer noteHeaderId) {
        List<VPosNoteDetailVenteEmportee> listeNoteDetailVae = entityManager.createQuery("FROM VPosNoteDetailVenteEmportee WHERE noteEnteteId=:noteHeaderId")
                .setParameter("noteHeaderId", noteHeaderId).getResultList();
        
        var noteDetailVaeResults = Json.createArrayBuilder();
        
        if(listeNoteDetailVae.size() > 0){
            VPosNoteDetailVenteEmportee valueNoteDetail = listeNoteDetailVae.get(0);
            Integer noteDetailIdInitial = valueNoteDetail.getNoteDetailId();
            Integer prestationIdInitial = valueNoteDetail.getPrestationId();
            
            var qte = 0;
            var libelleProduct = "";
            var libelleCuisson = "";
            String libelleAccompagnement = "";
            
            for(VPosNoteDetailVenteEmportee noteDetailVae : listeNoteDetailVae){
                if(noteDetailIdInitial.equals(noteDetailVae.getNoteDetailId()) && prestationIdInitial.equals(noteDetailVae.getPrestationId())){
                    qte = noteDetailVae.getQte();
                    libelleProduct = noteDetailVae.getLibellePrestation();
                    libelleCuisson = noteDetailVae.getLibelleCuisson();
                    libelleAccompagnement += (" - " + noteDetailVae.getLibelleAccompagnement());
                }else{
                    var object = Json.createObjectBuilder()
                            .add("qte", qte)
                            .add("libellePrestation", libelleProduct)
                            .add("libelleCuisson", libelleCuisson)
                            .add("libelleAccompagnement", libelleAccompagnement).build();
                        noteDetailVaeResults.add(object);
                        
                    qte = noteDetailVae.getQte();
                    libelleProduct = noteDetailVae.getLibellePrestation();
                    libelleCuisson = noteDetailVae.getLibelleCuisson();
                    libelleAccompagnement = noteDetailVae.getLibelleAccompagnement();
                    
                    noteDetailIdInitial = noteDetailVae.getNoteDetailId();
                    prestationIdInitial = noteDetailVae.getPrestationId();
                }
            }
            
            var object = Json.createObjectBuilder()
                            .add("qte", qte)
                            .add("libellePrestation", libelleProduct)
                            .add("libelleCuisson", libelleCuisson)
                            .add("libelleAccompagnement", libelleAccompagnement).build();
                        noteDetailVaeResults.add(object);
        }
        return noteDetailVaeResults.build();
    }
    
    public Long getPosNoteHeaderNbCouvert(String dateNote) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long nbCouvert = 0L;
        final Date daten;
        try {
            daten = format.parse(dateNote);
            final Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.setTime(daten);
            calendarBefore.add(Calendar.DAY_OF_YEAR, 1);
            String dateAfter = format.format(calendarBefore.getTime());

            nbCouvert = (Long) entityManager.createQuery(
                    " SELECT SUM(nbCouvert) FROM TPosNoteEntete WHERE dateNote BETWEEN :dateBefore and :dateAfter ")
                    .setParameter("dateBefore", format.parse(dateNote))
                    .setParameter("dateAfter", format.parse(dateAfter)).getSingleResult();
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nbCouvert;

    }

    // filtre dateReservation
    public List<TPosNoteEntete> getPosReservation(String dateBegin, String dateEnd) {

        Date dateE = null;
        Date dateB = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateB = simpleDateFormat.parse(dateBegin);
            dateE = simpleDateFormat.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Object[]> noteEntetes = entityManager.createQuery("FROM TPosNoteEntete head "
                + "LEFT JOIN TMmcClient c ON  head.mmcClientId = c.id where head.dateReservation BETWEEN :dateBegin AND :dateEnd ")
                .setParameter("dateBegin", dateB).setParameter("dateEnd", dateE).getResultList();

        List<TPosNoteEntete> result = new ArrayList<TPosNoteEntete>();

        for (Object[] note : noteEntetes) {
            TPosNoteEntete noteEntete = (TPosNoteEntete) note[0];
            TMmcClient client = (TMmcClient) note[1];
            noteEntete.setMmcClient(client.getNom());
            noteEntete.setNbPassage(getNbPassage(getPosNoteDetailByNoteHeaderId(noteEntete.getId())));
            noteEntete.setMontant(getMontantTot(getPosNoteDetailByNoteHeaderId(noteEntete.getId())));
            result.add(noteEntete);
        }
        return result;
    }

    public List<TPosNoteEntete> getAllPosReservation() {
        List<Object[]> noteEntetes = entityManager
                .createQuery("FROM TPosNoteEntete head "
                        + "LEFT JOIN TMmcClient c ON  head.mmcClientId = c.id where head.dateReservation <> null")
                .getResultList();

        List<TPosNoteEntete> result = new ArrayList<TPosNoteEntete>();

        for (Object[] note : noteEntetes) {
            TPosNoteEntete noteEntete = (TPosNoteEntete) note[0];
            TMmcClient client = (TMmcClient) note[1];
            noteEntete.setMmcClient(client.getNom());
            noteEntete.setNbPassage(getNbPassage(getPosNoteDetailByNoteHeaderId(noteEntete.getId())));
            noteEntete.setMontant(getMontantTot(getPosNoteDetailByNoteHeaderId(noteEntete.getId())));
            result.add(noteEntete);
        }
        return result;
    }

    public int getNbPassage(List<TPosNoteDetail> noteDetailList) {
        int nbPassage = 0;
        for (TPosNoteDetail noteDet : noteDetailList) {
            if (noteDet.getPosPrestation().getMmcSousFamilleCa().getNature().equals("SUBVENTION")) {
                nbPassage++;
            }
        }

        return nbPassage;
    }

    public BigDecimal getMontantTot(List<TPosNoteDetail> noteDetailList) {
        BigDecimal montant = new BigDecimal("0.0");
        for (TPosNoteDetail noteDet : noteDetailList) {
            montant = montant.add(noteDet.getPosPrestationPrix().multiply(new BigDecimal(noteDet.getQte())));

        }
        return montant;
    }

    public List<TPosNoteDetail> getPosNoteDetail() {
        List<TPosNoteDetail> notes = entityManager.createQuery("FROM TPosNoteDetail ORDER BY id").getResultList();
        return notes;
    }

    public TPosNoteDetail getPosNoteDetailById(int id) {
        TPosNoteDetail details = entityManager.find(TPosNoteDetail.class, id);
        return details;
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

    public List<Integer> stringToList(String value) {
        List<Integer> array = new ArrayList<Integer>();
        List<String> arrayString = new ArrayList<String>();
        String str[] = value.split(",");
        arrayString = Arrays.asList(str);
        for (String s : arrayString) {
            array.add(Integer.parseInt(s));
        }
        return array;
    }

    // NOTE APPROVISION POS
    public List<TPosNoteApprovision> getPosNoteApprovision() {
        List<TPosNoteApprovision> approvision = entityManager.createQuery("FROM TPosNoteApprovision").getResultList();
        return approvision;
    }

    public TPosNoteApprovision getPosNoteApprovisionById(int id) {
        TPosNoteApprovision approvision = entityManager.find(TPosNoteApprovision.class, id);
        return approvision;
    }

    public void setPosNoteApprovision(TPosNoteApprovision approvision) throws CustomConstraintViolationException {
        try {
            entityManager.persist(approvision);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public List<PassageHistory> getPassageHistory(int id) {
        List<Object[]> historyList = new ArrayList<Object[]>();

        // recuperation de la liste des notes details
        for (Object[] detail : getDetailsByClient(id)) {
            historyList.add(detail);
        }

        // recuperation de la liste des approvisions
        for (Object[] approv : getApprovisionByClient(id)) {
            historyList.add(approv);
        }

        List<PassageHistory> result = new ArrayList<PassageHistory>();
        boolean isPassage = false;
        boolean headerNoteExist;
        BigDecimal creditInList = new BigDecimal("0.0"), newCredit = new BigDecimal("0.0"),
                admissionInList = new BigDecimal("0.0"), newAdmission = new BigDecimal("0.0"),
                subInList = new BigDecimal("0.0"), newSub = new BigDecimal("0.0"),
                zoneSupInList = new BigDecimal("0.0"), newZoneSup = new BigDecimal("0.0"),
                solidInList = new BigDecimal("0.0"), newSolid = new BigDecimal("0.0"),
                liquidInList = new BigDecimal("0.0"), newLiquid = new BigDecimal("0.0"),
                zeroBig = new BigDecimal("0.0"), solde = new BigDecimal("0.0");
        int nbPassage = 0;
        // set passage history
        for (Object[] history : historyList) {
            if (history.length > 1) {
                PassageHistory histo = new PassageHistory();
                headerNoteExist = false;
                if (result.size() > 0) {
                    for (PassageHistory res : result) {
                        if (res.getIdNoteHeader() == (int) history[5]) {
                            headerNoteExist = true;
                            histo = res;
                            String nature = (String) history[1];
                            // histo.setCaseName(admission);
                            solde = histo.getBalance();
                            switch (nature) {
                            case "ADMISSION":
                                newAdmission = (BigDecimal) history[4];
                                if (histo.getAdmission() != null) {
                                    admissionInList = histo.getAdmission();
                                }
                                histo.setAdmission(admissionInList.add(newAdmission));
                                solde = addBalanceByCustomer(solde, histo.getAdmission(), false);
                                break;
                            case "SUBVENTION":
                                newSub = (BigDecimal) history[4];
                                if (histo.getSubvention() != null) {
                                    subInList = histo.getSubvention();
                                }
                                histo.setSubvention(subInList.add(newSub));
                                nbPassage = histo.getNbPassage();
                                histo.setNbPassage(nbPassage + 1);
                                solde = addBalanceByCustomer(solde, histo.getSubvention(), true);
                                break;
                            case "SOLIDE":
                                newSolid = (BigDecimal) history[4];
                                if (histo.getSolidDebit() != null) {
                                    solidInList = histo.getSolidDebit();
                                }
                                histo.setSolidDebit(solidInList.add(newSolid));
                                solde = addBalanceByCustomer(solde, histo.getSolidDebit(), false);
                                break;
                            case "LIQUIDE":
                                newLiquid = (BigDecimal) history[4];
                                if (histo.getLiquidDebit() != null) {
                                    liquidInList = histo.getLiquidDebit();
                                }
                                histo.setLiquidDebit(liquidInList.add(newLiquid));
                                solde = addBalanceByCustomer(solde, histo.getLiquidDebit(), false);
                                break;
                            case "ZONE SUPPLEMENTAIRE":
                                newZoneSup = new BigDecimal((String) history[4]);
                                if (histo.getZoneSup() != null) {
                                    zoneSupInList = histo.getZoneSup();
                                }
                                histo.setZoneSup(zoneSupInList.add(newZoneSup));
                                break;
                            default:
                                newCredit = (BigDecimal) history[4];
                                if (histo.getCredit() != null) {
                                    creditInList = histo.getCredit();
                                }
                                histo.setCredit(creditInList.add(newCredit));
                                solde = addBalanceByCustomer(solde, histo.getCredit(), true);
                                break;
                            }
                            histo.setBalance(solde);
                            result.set(result.indexOf(res), histo);
                            break;
                        }
                    }
                }

                if (headerNoteExist == false) {
                    histo.setIdNoteHeader((int) history[5]);
                    histo.setName((String) history[0]);
                    String nature = (String) history[1];
                    solde = zeroBig;
                    switch (nature) {
                    case "ADMISSION":
                        histo.setAdmission((BigDecimal) history[4]);
                        solde = addBalanceByCustomer(solde, histo.getAdmission(), false);
                        break;
                    case "SUBVENTION":
                        histo.setSubvention((BigDecimal) history[4]);
                        isPassage = true;
                        solde = addBalanceByCustomer(solde, histo.getSubvention(), true);
                        break;
                    case "SOLIDE":
                        histo.setSolidDebit((BigDecimal) history[4]);
                        solde = addBalanceByCustomer(solde, histo.getSolidDebit(), false);
                        break;
                    case "LIQUIDE":
                        histo.setLiquidDebit((BigDecimal) history[4]);
                        solde = addBalanceByCustomer(solde, histo.getLiquidDebit(), false);
                        break;
                    case "ZONE SUPPLEMENTAIRE":
                        histo.setZoneSup((BigDecimal) history[4]);
                        break;
                    default:
                        histo.setCredit((BigDecimal) history[4]);
                        solde = addBalanceByCustomer(solde, histo.getCredit(), true);
                        break;
                    }
                    if (isPassage == true) {
                        histo.setNbPassage(1);
                        isPassage = false;
                    } else {
                        histo.setNbPassage(0);
                    }
                    histo.setTicketNumber((String) history[2]);
                    ;
                    histo.setDatePassage((Date) history[3]);
                    histo.setBalance(solde);
                    histo.setCaseName((String) history[6]);
                    result.add(histo);
                }
            }
        }

        return setCrediteurDebiteur(result);
    }

    public List<Object[]> getDetailsByClient(int id) {
        return entityManager.createQuery(
                "SELECT noteDetail.posNoteEntete.mmcClientObject.nom, noteDetail.posPrestation.mmcSousFamilleCa.nature, noteDetail.posNoteEntete.numFacture, noteDetail.posNoteEntete.dateNote, SUM(noteDetail.posPrestationPrix),  noteDetail.posNoteEnteteId,noteDetail.posNoteEntete.posteUuid "
                        + " FROM TPosNoteDetail noteDetail "
                        + " WHERE noteDetail.posNoteEntete.mmcClientId  =:clientId "
                        + " GROUP BY noteDetail.posPrestation.mmcSousFamilleCa.nature, noteDetail.posNoteEnteteId  ")
                .setParameter("clientId", id).getResultList();
    }

    public List<Object[]> getApprovisionByClient(int id) {
        return entityManager.createQuery(
                " SELECT appro.posNoteEntete.mmcClientObject.nom, appro.mmcModeEncaissement.encaissementType, appro.posNoteEntete.numFacture, appro.posNoteEntete.dateNote, SUM(appro.montant), appro.posNoteEnteteId,appro.posNoteEntete.posteUuid "
                        + " FROM TPosNoteApprovision appro " + " WHERE appro.posNoteEntete.mmcClientId =:clientId "
                        + " GROUP BY appro.mmcModeEncaissement.encaissementType, appro.posNoteEnteteId ")
                .setParameter("clientId", id).getResultList();
    }

    public BigDecimal addBalanceByCustomer(BigDecimal solde, BigDecimal value, boolean isToAdd) {
        if (value != null) {
            if (isToAdd == true) {
                solde = solde.add(value);
            } else {
                solde = solde.subtract(value);
            }
        }
        return solde;
    }

    public List<PassageHistory> setCrediteurDebiteur(List<PassageHistory> result) {
        BigDecimal zeroBig = new BigDecimal("0.0");
        List<PassageHistory> passageHistoryList = new ArrayList<PassageHistory>();
        PassageHistory passageHistory = new PassageHistory();
        BigDecimal soldeTotal = new BigDecimal("0.0");
        for (PassageHistory res : result) {
            passageHistory = res;
            soldeTotal = soldeTotal.add(res.getBalance());
            if (soldeTotal.compareTo(zeroBig) == -1) { // soldeTotal < 0
                passageHistory.setDebitBalance(soldeTotal);
                passageHistory.setCreditBalance(zeroBig);
            } else { // soldeTotal >= 0
                passageHistory.setCreditBalance(soldeTotal);
                passageHistory.setDebitBalance(zeroBig);
            }
            passageHistoryList.add(passageHistory);
        }
        return passageHistoryList;
    }

    public BigDecimal getDayTurnover(String dateLogicielle) {

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<BigDecimal> dayTurnoverList = new ArrayList<BigDecimal>();
        final Date daten;
        try {
            daten = format.parse(dateLogicielle);
            final Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.setTime(daten);
            calendarBefore.add(Calendar.DAY_OF_YEAR, 1);
            String dateAfter = format.format(calendarBefore.getTime());

            dayTurnoverList = entityManager.createQuery(
                    " SELECT SUM(montant) FROM TPosEncaissement WHERE dateEncaissement BETWEEN :dateBefore and :dateAfter ")
                    .setParameter("dateBefore", format.parse(dateLogicielle))
                    .setParameter("dateAfter", format.parse(dateAfter)).getResultList();
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dayTurnoverList.get(0);
    }

    public List<MonthlyTurnover> getMonthlyTurnover() {
        List<Object[]> encaissements = entityManager.createQuery(
                " SELECT SUM(montant),SUM(posNoteEntete.nbCouvert), posNoteEntete.service, SUM(montantTva),MONTH(dateEncaissement)  FROM TPosEncaissement  where YEAR(dateEncaissement) = YEAR(NOW()) GROUP BY MONTH(dateEncaissement), posNoteEntete.service ")
                .getResultList();

        List<MonthlyTurnover> result = new ArrayList<MonthlyTurnover>();
        for (Object[] encaissement : encaissements) {
            MonthlyTurnover turn = new MonthlyTurnover();
            turn.setCouvert((Long) encaissement[1]);
            turn.setService((String) encaissement[2]);
            turn.setMontant((BigDecimal) encaissement[0]);
            turn.setMontantTva((BigDecimal) encaissement[3]);
            turn.setMonthCashing((Integer) encaissement[4]);

            result.add(turn);
        }
        return getTurnover(result);
    }

    public List<MonthlyTurnover> getTurnover(List<MonthlyTurnover> turnovers) {
        List<MonthlyTurnover> result = new ArrayList<MonthlyTurnover>();
        MonthlyTurnover turn = new MonthlyTurnover();
        for (int i = 1; i < 13; i++) {
            BigDecimal montantTot = new BigDecimal("0.0");
            for (MonthlyTurnover turnover : turnovers) {
                if (i == turnover.getMonthCashing()) {
                    montantTot = montantTot.add(turnover.getMontant());
                }
            }

            for (MonthlyTurnover turnover : turnovers) {
                if (i == turnover.getMonthCashing()) {
                    turn = turnover;
                    turn.setMontantTotByMonth(montantTot);
                    result.add(turn);
                }
            }
        }
        return result;
    }

    public Long getNumberPassesDay(String dateLogicielle) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long numberPasseDay = 0L;
        final Date daten;
        try {
            daten = format.parse(dateLogicielle);
            final Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.setTime(daten);
            calendarBefore.add(Calendar.DAY_OF_YEAR, 1);
            String dateAfter = format.format(calendarBefore.getTime());

            List<TPosNoteEntete> noteEntetes = entityManager
                    .createQuery("FROM TPosNoteEntete WHERE dateNote BETWEEN :dateBefore and :dateAfter  ")
                    .setParameter("dateBefore", format.parse(dateLogicielle))
                    .setParameter("dateAfter", format.parse(dateAfter)).getResultList();

            for (TPosNoteEntete note : noteEntetes) {
                numberPasseDay = Long.sum(numberPasseDay,
                        new Long(getNbPassage(getPosNoteDetailByNoteHeaderId(note.getId()))));
            }

        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return numberPasseDay;

    }

    public BigDecimal getCashingDayCollectivity(String dateLogicielle) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<BigDecimal> dayCashingList = new ArrayList<BigDecimal>();
        final Date daten;
        try {
            daten = format.parse(dateLogicielle);
            final Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.setTime(daten);
            calendarBefore.add(Calendar.DAY_OF_YEAR, 1);
            String dateAfter = format.format(calendarBefore.getTime());

            dayCashingList = (List<BigDecimal>) entityManager.createQuery(
                    " SELECT SUM(montant) FROM TPosNoteApprovision WHERE posNoteEntete.dateNote BETWEEN :dateBefore and :dateAfter ")
                    .setParameter("dateBefore", format.parse(dateLogicielle))
                    .setParameter("dateAfter", format.parse(dateAfter)).getResultList();
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (dayCashingList.isEmpty()) ? new BigDecimal(0.0) : dayCashingList.get(0);
    }

    public BigDecimal getDayTurnoverCollectivity(String dateLogicielle) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        BigDecimal turnoverDay = new BigDecimal(0.0);
        final Date daten;
        try {
            daten = format.parse(dateLogicielle);
            final Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.setTime(daten);
            calendarBefore.add(Calendar.DAY_OF_YEAR, 1);
            String dateAfter = format.format(calendarBefore.getTime());

            List<TPosNoteEntete> noteEntetes = entityManager
                    .createQuery("FROM TPosNoteEntete WHERE dateNote BETWEEN :dateBefore and :dateAfter  ")
                    .setParameter("dateBefore", format.parse(dateLogicielle))
                    .setParameter("dateAfter", format.parse(dateAfter)).getResultList();

            for (TPosNoteEntete note : noteEntetes) {
                turnoverDay = turnoverDay.add(getMontantTot(getPosNoteDetailByNoteHeaderId(note.getId())));
            }

        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return turnoverDay;

    }

    public BigDecimal getAnnualTurnover(String dateLogicielle) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        BigDecimal turnoverDay = new BigDecimal(0.0);
        final Date daten;
        try {
            daten = format.parse(dateLogicielle);
            List<TPosNoteEntete> noteEntetes = entityManager
                    .createQuery("FROM TPosNoteEntete WHERE dateNote BETWEEN :dateBefore and :dateAfter  ")
                    .setParameter("dateBefore", getDate(Integer.valueOf(formatYear.format(daten)), 1, 1))
                    .setParameter("dateAfter", getDate(Integer.valueOf(formatYear.format(daten)), 12, 31))
                    .getResultList();

            for (TPosNoteEntete note : noteEntetes) {
                turnoverDay = turnoverDay.add(getMontantTot(getPosNoteDetailByNoteHeaderId(note.getId())));

            }
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(NoteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return turnoverDay;
    }

    public List<MonthlyTurnover> getMonthlyTurnoverCollectivity() {
        List<TPosNoteEntete> turnovers = entityManager
                .createQuery(" FROM TPosNoteEntete  WHERE YEAR(dateNote) = YEAR(NOW()) ORDER BY dateNote  ")
                .getResultList();
        return createMonthlyTurnover(turnovers);
    }

    public List<MonthlyTurnover> createMonthlyTurnover(List<TPosNoteEntete> turnovers) {
        List<MonthlyTurnover> monthlyTurnover = new ArrayList();
        BigDecimal value = new BigDecimal(0.0);

        final SimpleDateFormat formatYear = new SimpleDateFormat("MM");
        boolean isExist = false;
        for (int i = 1; i < 13; i++) {
            isExist = false;
            value = new BigDecimal(0.0);
            for (TPosNoteEntete tur : turnovers) {
                if (i == Integer.valueOf(formatYear.format(tur.getDateNote()))) {
                    isExist = true;
                    value = value.add(getMontantTot(getPosNoteDetailByNoteHeaderId(tur.getId())));
                }
            }
            if (isExist == true) {
                MonthlyTurnover turnover = new MonthlyTurnover();
                turnover.setMontant(value);
                turnover.setMonthCashing(i);
                monthlyTurnover.add(turnover);
            }
        }
        return monthlyTurnover;
    }

    public Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        return calendar.getTime();
    }

    public String getReservationNumber(String key, String dateLogicielle) throws CustomConstraintViolationException {
        TMmcParametrage parametrage = entityManager.find(TMmcParametrage.class, key);
        String numero = "";
        int value = Integer.parseInt(parametrage.getValeur()) + 1;
        parametrage.setValeur(Integer.toString(value));

        try {
            entityManager.merge(parametrage);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

        numero = dateLogicielle.replace("-", "") + String.format("%011d", value);
        return numero;
    }

    // note header
    public List<TPmsNoteEntete> getAllPmsNoteHeader() {
        List<TPmsNoteEntete> note = entityManager.createQuery("FROM TPmsNoteEntete s WHERE s.dateDeletion is null ")
                .getResultList();
        return note;
    }

    public TPmsNoteEntete getPmsNoteHeaderById(int id) {
        TPmsNoteEntete pmsSejour = entityManager.find(TPmsNoteEntete.class, id);
        return pmsSejour;
    }

    public void addPmsNoteHeader(TPmsNoteEntete note) throws CustomConstraintViolationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime currentTime = LocalTime.now();
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
        LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);
        try {
            note.setDateNote(dateTimeLogicielle);
            entityManager.persist(note);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsNoteEntete updatePmsNoteHeader(TPmsNoteEntete note) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(note);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePmsNoteHeader(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_note_entete SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Note Detail
    public List<TPmsNoteDetail> getAllPmsNoteDetailed() {
        List<TPmsNoteDetail> detailNote = entityManager.createQuery("FROM TPmsNoteDetail").getResultList();
        return detailNote;
    }

    public TPmsNoteDetail getPmsNoteDetailedById(int id) {
        TPmsNoteDetail detailNote = entityManager.find(TPmsNoteDetail.class, id);
        return detailNote;
    }

    public void addPmsNoteDetailed(JsonObject pmsSejourTarif) throws CustomConstraintViolationException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateNote;
        List<Map> pmsSejourTarifList = (List) pmsSejourTarif.get("noteDetailed");
        for (Map row : pmsSejourTarifList) {
            TPmsNoteDetail object = new TPmsNoteDetail();
            dateNote = row.get("dateNote").toString();
            LocalDate daten = LocalDate.parse(dateNote.replaceAll("\"", ""));
            object.setDateNote(daten);

            object.setPmsNoteEnteteId(Integer.parseInt(row.get("pmsNoteEnteteId").toString()));
            object.setPmsPrestationId(Integer.parseInt(row.get("pmsPrestationId").toString()));
            object.setQte(Integer.parseInt(row.get("qte").toString()));
            object.setPu(new BigDecimal(row.get("pu").toString().replaceAll("\"", "")));
            object.setTauxRemise(new BigDecimal(row.get("tauxRemise").toString().replaceAll("\"", "")));
            object.setTauxCommission(new BigDecimal(row.get("tauxCommission").toString().replaceAll("\"", "")));
            object.setTauxTva(new BigDecimal(row.get("tauxTva").toString().replaceAll("\"", "")));
            try {
                entityManager.persist(object);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }

    public TPmsNoteDetail updatePmsNoteDetailed(TPmsNoteDetail pmsNoteDetail)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsNoteDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePmsNoteDetailed(int id) {
        entityManager.createNativeQuery(" DELETE FROM `t_pms_note_detail` WHERE id=:id").setParameter("id", id)
                .executeUpdate();
    }
   
    public TPosNoteEntete getPosNoteHeaderId(int id) {
        TPosNoteEntete product = entityManager.find(TPosNoteEntete.class, id);
        return product;
    }
    public TPosNoteEntete updatePosNoteVaeStatutDelivre(Integer id) throws CustomConstraintViolationException {
        TPosNoteEntete prestationStop = getPosNoteHeaderId(id);
        prestationStop.setStatutVae("DELIVRE");
        try {
            return entityManager.merge(prestationStop);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    public TPosNoteEntete updatePosNoteVaeStatutAnnule(Integer id) throws CustomConstraintViolationException {
        TPosNoteEntete prestationStop = getPosNoteHeaderId(id);
        prestationStop.setStatutVae("ANNULE");
        try {
            return entityManager.merge(prestationStop);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
   
    public BigDecimal openNotesBalances(String dateReference) {
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLogicielle = settingData.getValeur();
        BigDecimal valueOpenNotes = new BigDecimal("0");
        if (!Objects.isNull(dateReference)) {
            valueOpenNotes = (BigDecimal) entityManager.createNativeQuery("SELECT SUM(det.pu * det.qte) FROM t_pms_note_entete ent "
                + "INNER JOIN t_pms_note_detail det ON det.pms_note_entete_id = ent.id "
                + "WHERE ent.date_note =:dateReference AND ent.date_etat_solde >:dateReference OR ent.date_etat_solde is null ")
                .setParameter("dateReference", dateReference).getSingleResult(); 
        }else{
            valueOpenNotes = (BigDecimal) entityManager.createNativeQuery("SELECT SUM(det.pu * det.qte) FROM t_pms_note_entete ent "
                + "INNER JOIN t_pms_note_detail det ON det.pms_note_entete_id = ent.id "
                + "WHERE ent.date_note ='"+dateLogicielle+"' AND ent.date_etat_solde >'"+dateLogicielle+"' OR ent.date_etat_solde is null ")
                .getSingleResult();
        }
        return valueOpenNotes;
    }
  
}
