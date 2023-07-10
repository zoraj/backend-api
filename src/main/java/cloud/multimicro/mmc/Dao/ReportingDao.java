/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.VCollectiviteLectureFamille;
import cloud.multimicro.mmc.Entity.VCollectiviteLectureSousFamille;
import cloud.multimicro.mmc.Entity.VCollectiviteLecturePrestation;
import cloud.multimicro.mmc.Entity.VCollectiviteValorisationBac;
import cloud.multimicro.mmc.Entity.VCollectiviteAdmissionSubvention;
import cloud.multimicro.mmc.Entity.VComEditionAgeeArrhe;
import cloud.multimicro.mmc.Entity.VComEditionAgeeFacture;
import cloud.multimicro.mmc.Entity.VComEditionDetailCompte;
import cloud.multimicro.mmc.Entity.VComEditionExtraitCompte;
import cloud.multimicro.mmc.Entity.VComEditionFacture;
import cloud.multimicro.mmc.Entity.VComEditionReleveDetaileFacture;
import cloud.multimicro.mmc.Entity.VComEditionReleveSimpleFacture;
import cloud.multimicro.mmc.Entity.VComEditionSituationDebiteur;
import cloud.multimicro.mmc.Entity.VPmsCa;
import cloud.multimicro.mmc.Entity.VPmsEditionArriveePrevue;
import cloud.multimicro.mmc.Entity.VPmsEditionBalanceAppartement;
import cloud.multimicro.mmc.Entity.VPmsEditionClientPresent;
import cloud.multimicro.mmc.Entity.VPmsEditionClotureDefinitive;
import cloud.multimicro.mmc.Entity.VPmsEditionClotureProvisoire;
import cloud.multimicro.mmc.Entity.VPmsEditionDepartPrevu;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatControl;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatDebit;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatDebitChambre;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatPrevRealMois;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatRemplissage;
import cloud.multimicro.mmc.Entity.VPmsEditionListeArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionListeGlobalArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionPrestationVendue;
import cloud.multimicro.mmc.Entity.VPmsEditionRapportArriveeDepart;
import cloud.multimicro.mmc.Entity.VPmsEditionRapportEtage;
import cloud.multimicro.mmc.Entity.VPmsEditionOccupation;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupation;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationReel;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationReelRecep;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationResa;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOption;
import cloud.multimicro.mmc.Entity.VPmsEditionListePrestationArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionListeVerifArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionPlanningMensuelChambre;
import cloud.multimicro.mmc.Entity.VPmsEditionSoldeNoteOuverte;
import cloud.multimicro.mmc.Entity.VPosCa;
import cloud.multimicro.mmc.Entity.VPosEditionCaActivite;
import cloud.multimicro.mmc.Entity.VPosEditionJournalOffert;
import cloud.multimicro.mmc.Entity.VPosEditionNoteSoldeJour;
import cloud.multimicro.mmc.Entity.VPosEditionPrestationVendue;
import cloud.multimicro.mmc.Entity.VPosEditionVisualisationModeEncaissement;
import cloud.multimicro.mmc.Util.Util;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
public class ReportingDao {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOGGER = Logger.getLogger(ReportingDao.class);

    public JsonArray getEditionPrestationVendue(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionPrestationVendue  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateNote >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateNote <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateNote <= '" + dateEnd + "'");
            }
        }

        List<VPmsEditionPrestationVendue> products = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var productSoldResults = Json.createArrayBuilder();
        //var productResults = Json.createArrayBuilder();
        if (products.size() > 0) {
            VPmsEditionPrestationVendue valueListProduct = products.get(0);
            String productInitial = valueListProduct.getLibelle();
            var libProduct = "";
            var code = "";
            var qte = 0;
            var ca = new BigDecimal("0");
            var totalQte = 0;
            var totalCa = new BigDecimal("0");

            for (VPmsEditionPrestationVendue productSold : products) {
                if (productInitial.equals(productSold.getLibelle())) {
                    libProduct = productSold.getLibelle();
                    code = productSold.getCode();
                    qte = productSold.getQte();
                    ca = productSold.getCa();
                    totalQte += productSold.getQte();
                    totalCa = totalCa.add(productSold.getCa());
                } else {
                    var object = Json.createObjectBuilder()
                            .add("code", code)
                            .add("libelle", libProduct)
                            .add("totalQte", totalQte)
                            .add("totalCa", totalCa).build();

                    productSoldResults.add(object);

                    totalQte = 0;
                    totalCa = new BigDecimal("0");

                    libProduct = productSold.getLibelle();
                    code = productSold.getCode();
                    qte = productSold.getQte();
                    ca = productSold.getCa();
                    totalQte += productSold.getQte();
                    totalCa = totalCa.add(productSold.getCa());

                    productInitial = libProduct;
                }
            }
            var object = Json.createObjectBuilder()
                    .add("code", code)
                    .add("libelle", libProduct)
                    .add("totalQte", totalQte)
                    .add("totalCa", totalCa).build();

            productSoldResults.add(object);
        }
        return productSoldResults.build();
    }

    /*
     * public List<VPmsEditionDepartPrevu> getAllEditionDepartPrevu(String
     * dateStart, String dateEnd) { Boolean isExist = false; StringBuilder
     * stringBuilder = new StringBuilder();
     * stringBuilder.append("FROM VPmsEditionDepartPrevu  "); if
     * (!Objects.isNull(dateStart)) { stringBuilder.append(" WHERE sejour >= '" +
     * dateStart + "'"); isExist = true; }
     * 
     * if (!Objects.isNull(dateEnd)) { if (isExist == true) {
     * stringBuilder.append(" AND sejour <= '" + dateEnd + "'"); } else {
     * stringBuilder.append(" WHERE sejour <= '" + dateEnd + "'"); } }
     * 
     * List<VPmsEditionDepartPrevu> result =
     * entityManager.createQuery(stringBuilder.toString()).getResultList(); return
     * result; }
     */
    public JsonArray getAllEditionDepartPrevu(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionDepartPrevu  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateDepart >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionDepartPrevu> listeDepartPrevu = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var departPrevuResults = Json.createArrayBuilder();
        var departResults = Json.createArrayBuilder();
        if (listeDepartPrevu.size() > 0) {
            VPmsEditionDepartPrevu valueListDepartPrevu = listeDepartPrevu.get(0);
            LocalDate dateDepInitial = valueListDepartPrevu.getDateDepart();

            var numReservation = "";
            var master = "";
            var numChambre = "";
            var typeChambre = "";
            var nomReservation = "";
            var adultEnfant = "";
            var sejour = "";
            var qte = 0;
            var nationalite = "";
            var segment = "";
            var totalNote = new BigDecimal(0);
            var idSejour = 0;
            var adult = 0;
            var enfant = 0;
            var totalNbPax = 0;
            var nbHorsChambre = 0;
            var totalNbHorsChambre = 0;
            var nbChambre = 0;
            var totalNbChambre = 0;

            for (VPmsEditionDepartPrevu listeDepart : listeDepartPrevu) {
                if (dateDepInitial.equals(listeDepart.getDateDepart())) {
                    dateDepInitial = listeDepart.getDateDepart();
                    numReservation = listeDepart.getNumeroReservation();
                    master = listeDepart.getMaster();
                    numChambre = listeDepart.getNumChambre();
                    typeChambre = listeDepart.getTypeChambre();
                    nomReservation = listeDepart.getNom();
                    adultEnfant = listeDepart.getAdultEnfant();
                    sejour = listeDepart.getSejour();
                    qte = listeDepart.getQuantite();
                    nationalite = listeDepart.getNationalite();
                    segment = listeDepart.getSegment();
                    idSejour = listeDepart.getIdSejour() == null ? 0 : listeDepart.getIdSejour();
                    totalNote = listeDepart.getTotal();
                    adult += listeDepart.getAdult();
                    enfant += listeDepart.getEnfant();
                    totalNbPax = adult + enfant;
                    nbHorsChambre = listeDepart.getIdChambreSejour() == 0 ? 1 : 0;
                    totalNbHorsChambre += nbHorsChambre;
                    nbChambre = listeDepart.getIdChambreSejour() > 0 ? 1 : 0;
                    totalNbChambre += nbChambre;

                    var departList = Json.createObjectBuilder()
                            .add("dateDepart", dateDepInitial.toString())
                            .add("numReservation", numReservation)
                            .add("master", master)
                            .add("numChambre", (numChambre == null) ? "" : numChambre)
                            .add("typeChambre", typeChambre)
                            .add("nomReservation", nomReservation)
                            .add("adultEnfant", adultEnfant)
                            .add("sejour", sejour)
                            .add("qte", qte)
                            .add("nationalite", nationalite)
                            .add("segment", segment)
                            .add("totalNote", totalNote)
                            .add("pmsSejourId", idSejour).build();
                    departResults.add(departList);
                } else {
                    var object = Json.createObjectBuilder()
                            .add("dateDepartPrevu", dateDepInitial.toString())
                            .add("totalNbPax", totalNbPax)
                            .add("totalNbChambre", totalNbChambre)
                            .add("totalNbHorsChambre", totalNbHorsChambre)
                            .add("listDepartPrevu", departResults).build();

                    departPrevuResults.add(object);
                    adult = 0;
                    enfant = 0;
                    totalNbPax = 0;
                    totalNbHorsChambre = 0;
                    totalNbChambre = 0;

                    dateDepInitial = listeDepart.getDateDepart();
                    numReservation = listeDepart.getNumeroReservation();
                    master = listeDepart.getMaster();
                    numChambre = listeDepart.getNumChambre();
                    typeChambre = listeDepart.getTypeChambre();
                    nomReservation = listeDepart.getNom();
                    adultEnfant = listeDepart.getAdultEnfant();
                    sejour = listeDepart.getSejour();
                    qte = listeDepart.getQuantite();
                    nationalite = listeDepart.getNationalite();
                    segment = listeDepart.getSegment();
                    idSejour = listeDepart.getIdSejour() == null ? 0 : listeDepart.getIdSejour();
                    totalNote = listeDepart.getTotal();
                    adult += listeDepart.getAdult();
                    enfant += listeDepart.getEnfant();
                    totalNbPax = adult + enfant;
                    nbHorsChambre = listeDepart.getIdChambreSejour() == 0 ? 1 : 0;
                    totalNbHorsChambre += nbHorsChambre;
                    nbChambre = listeDepart.getIdChambreSejour() > 0 ? 1 : 0;
                    totalNbChambre += nbChambre;

                    var departList = Json.createObjectBuilder()
                            .add("dateDepart", dateDepInitial.toString())
                            .add("numReservation", numReservation)
                            .add("master", master)
                            .add("numChambre", (numChambre == null) ? "" : numChambre)
                            .add("typeChambre", typeChambre)
                            .add("nomReservation", nomReservation)
                            .add("adultEnfant", adultEnfant)
                            .add("sejour", sejour)
                            .add("qte", qte)
                            .add("nationalite", nationalite)
                            .add("segment", segment)
                            .add("totalNote", totalNote)
                            .add("pmsSejourId", idSejour).build();

                    departResults = Json.createArrayBuilder();
                    departResults.add(departList);
                    dateDepInitial = dateDepInitial;
                }
            }
            var object = Json.createObjectBuilder()
                    .add("dateDepartPrevu", dateDepInitial.toString())
                    .add("totalNbPax", totalNbPax)
                    .add("totalNbChambre", totalNbChambre)
                    .add("totalNbHorsChambre", totalNbHorsChambre)
                    .add("listDepartPrevu", departResults).build();

            departPrevuResults.add(object);
        }
        return departPrevuResults.build();
    }

    public JsonArray getAllEditionArrivalPrevu(String dateStart, String dateEnd, Integer notArrival) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionArriveePrevue  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateArrivee <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateArrivee <= '" + dateEnd + "'");
            }
        }
        if (notArrival != 0) {
            stringBuilder.append(" AND idSejour = null");
        }
        List<VPmsEditionArriveePrevue> listeArriveePrevu = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var arriveePrevuResults = Json.createArrayBuilder();
        var arriveeResults = Json.createArrayBuilder();
        if (listeArriveePrevu.size() > 0) {
            VPmsEditionArriveePrevue valueListArriveePrevu = listeArriveePrevu.get(0);
            LocalDate dateArrInitial = valueListArriveePrevu.getDateArrivee();

            var numReservation = "";
            var master = "";
            var numChambre = "";
            var typeChambre = "";
            var nomReservation = "";
            var adultEnfant = "";
            var sejour = "";
            var qte = 0;
            var nationalite = "";
            var segment = "";
            var totalNote = new BigDecimal(0);
            var idSejour = 0;
            var adult = 0;
            var enfant = 0;
            var totalNbPax = 0;
            var nbHorsChambre = 0;
            var totalNbHorsChambre = 0;
            var nbChambre = 0;
            var totalNbChambre = 0;

            for (VPmsEditionArriveePrevue listeArrivee : listeArriveePrevu) {
                if (dateArrInitial.equals(listeArrivee.getDateArrivee())) {
                    dateArrInitial = listeArrivee.getDateArrivee();
                    numReservation = listeArrivee.getNumeroReservation();
                    master = listeArrivee.getMaster();
                    numChambre = listeArrivee.getNumChambre();
                    typeChambre = listeArrivee.getTypeChambre();
                    nomReservation = listeArrivee.getNom();
                    adultEnfant = listeArrivee.getAdultEnfant();
                    sejour = listeArrivee.getSejour();
                    qte = listeArrivee.getQuantite();
                    nationalite = listeArrivee.getNationalite();
                    segment = listeArrivee.getSegment();
                    idSejour = listeArrivee.getIdSejour() == null ? 0 : listeArrivee.getIdSejour();
                    totalNote = listeArrivee.getTotal();
                    adult += listeArrivee.getAdult();
                    enfant += listeArrivee.getEnfant();
                    totalNbPax = adult + enfant;
                    nbHorsChambre = listeArrivee.getIdChambreSejour() == 0 ? 1 : 0;
                    totalNbHorsChambre += nbHorsChambre;
                    nbChambre = listeArrivee.getIdChambreSejour() > 0 ? 1 : 0;
                    totalNbChambre += nbChambre;

                    var arriveeList = Json.createObjectBuilder()
                            .add("dateArrivee", dateArrInitial.toString())
                            .add("numReservation", numReservation)
                            .add("master", master)
                            .add("numChambre", (numChambre == null) ? "" : numChambre)
                            .add("typeChambre", typeChambre)
                            .add("nomReservation", nomReservation)
                            .add("adultEnfant", adultEnfant)
                            .add("sejour", sejour)
                            .add("qte", qte)
                            .add("nationalite", nationalite)
                            .add("segment", segment)
                            .add("totalNote", totalNote)
                            .add("pmsSejourId", idSejour).build();
                    arriveeResults.add(arriveeList);
                } else {
                    var object = Json.createObjectBuilder()
                            .add("dateArriveePrevu", dateArrInitial.toString())
                            .add("totalNbPax", totalNbPax)
                            .add("totalNbChambre", totalNbChambre)
                            .add("totalNbHorsChambre", totalNbHorsChambre)
                            .add("listArriveePrevu", arriveeResults).build();

                    arriveePrevuResults.add(object);
                    adult = 0;
                    enfant = 0;
                    totalNbPax = 0;
                    totalNbHorsChambre = 0;
                    totalNbChambre = 0;

                    dateArrInitial = listeArrivee.getDateArrivee();
                    numReservation = listeArrivee.getNumeroReservation();
                    master = listeArrivee.getMaster();
                    numChambre = listeArrivee.getNumChambre();
                    typeChambre = listeArrivee.getTypeChambre();
                    nomReservation = listeArrivee.getNom();
                    adultEnfant = listeArrivee.getAdultEnfant();
                    sejour = listeArrivee.getSejour();
                    qte = listeArrivee.getQuantite();
                    nationalite = listeArrivee.getNationalite();
                    segment = listeArrivee.getSegment();
                    idSejour = listeArrivee.getIdSejour() == null ? 0 : listeArrivee.getIdSejour();
                    totalNote = listeArrivee.getTotal();
                    adult += listeArrivee.getAdult();
                    enfant += listeArrivee.getEnfant();
                    totalNbPax = adult + enfant;
                    nbHorsChambre = listeArrivee.getIdChambreSejour() == 0 ? 1 : 0;
                    totalNbHorsChambre += nbHorsChambre;
                    nbChambre = listeArrivee.getIdChambreSejour() > 0 ? 1 : 0;
                    totalNbChambre += nbChambre;

                    var arriveeList = Json.createObjectBuilder()
                            .add("dateArrivee", dateArrInitial.toString())
                            .add("numReservation", numReservation)
                            .add("master", master)
                            .add("numChambre", (numChambre == null) ? "" : numChambre)
                            .add("typeChambre", typeChambre)
                            .add("nomReservation", nomReservation)
                            .add("adultEnfant", adultEnfant)
                            .add("sejour", sejour)
                            .add("qte", qte)
                            .add("nationalite", nationalite)
                            .add("segment", segment)
                            .add("totalNote", totalNote)
                            .add("pmsSejourId", idSejour).build();

                    arriveeResults = Json.createArrayBuilder();
                    arriveeResults.add(arriveeList);
                    dateArrInitial = dateArrInitial;
                }
            }
            var object = Json.createObjectBuilder()
                    .add("dateArriveePrevu", dateArrInitial.toString())
                    .add("totalNbPax", totalNbPax)
                    .add("totalNbChambre", totalNbChambre)
                    .add("totalNbHorsChambre", totalNbHorsChambre)
                    .add("listArriveePrevu", arriveeResults).build();

            arriveePrevuResults.add(object);
        }
        return arriveePrevuResults.build();
    }

    /**
     *
     * @param dateStart
     * @param dateEnd
     * @redturn
     */

    /*
     * public List<VPmsEditionClientPresent> getVPmsEditionClientPresent(String
     * dateStart, String dateEnd) { Boolean isExist = false; StringBuilder
     * stringBuilder = new StringBuilder();
     * stringBuilder.append("FROM VPmsEditionClientPresent  "); if
     * (!Objects.isNull(dateStart)) { stringBuilder.append(" WHERE dateDepart >= '"
     * + dateStart + "'"); isExist = true; } if (!Objects.isNull(dateEnd)) { if
     * (isExist == true) { stringBuilder.append(" AND dateDepart <= '" + dateEnd +
     * "'"); } else { stringBuilder.append(" WHERE dateDepart <= '" + dateEnd +
     * "'"); } } List<VPmsEditionClientPresent> result =
     * entityManager.createQuery(stringBuilder.toString()).getResultList(); return
     * result; }
     */
    public List<VPmsEditionClientPresent> getAllClientPresent() {
        List<VPmsEditionClientPresent> clientPresent = entityManager.createQuery("FROM VPmsEditionClientPresent")
                .getResultList();
        return clientPresent;
    }

    /*
     * public List<VPmsEditionRapportArriveeDepart>
     * getEditionRaportArrivalDeparture(String dateReference) { Boolean isExist =
     * false; StringBuilder stringBuilder = new StringBuilder();
     * stringBuilder.append("FROM VPmsEditionRapportArriveeDepart  "); if
     * (!Objects.isNull(dateReference)) {
     * stringBuilder.append(" WHERE dateArrivee = '" + dateReference +
     * "' OR dateDepart = '" + dateReference + "'"); isExist = true; }
     * 
     * List<VPmsEditionRapportArriveeDepart> result =
     * entityManager.createQuery(stringBuilder.toString()).getResultList(); return
     * result; }
     */
    public List<VPmsEditionRapportArriveeDepart> getEditionRaportArrivalDeparture() {
        List<VPmsEditionRapportArriveeDepart> arrivalDeparture = entityManager
                .createQuery("FROM VPmsEditionRapportArriveeDepart").getResultList();
        return arrivalDeparture;
    }

    /**
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    /*
     * public List<VPmsEditionRapportEtage> getAllEditionRapportEtage(String
     * dateReference) { Boolean isExist = false; StringBuilder stringBuilder = new
     * StringBuilder(); stringBuilder.append("FROM VPmsEditionRapportEtage  "); if
     * (!Objects.isNull(dateReference)) {
     * stringBuilder.append(" WHERE date_arrivee  = '" + dateReference +
     * "' OR date_depart  = '" + dateReference + "'"); isExist = true; }
     * 
     * List<VPmsEditionRapportEtage> result =
     * entityManager.createQuery(stringBuilder.toString()).getResultList(); return
     * result; }
     */
    public JsonArray getAllRapportEtage(String dateReference) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionRapportEtage  ");
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE (dateArrivee <= '" + dateReference + "' AND dateDepart >= '" + dateReference + "') OR (dateArrivee is null AND dateDepart is null)");
            isExist = true;
        }
        List<VPmsEditionRapportEtage> listeRapportEtage = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        System.out.println("listeRapportEtage** :" + listeRapportEtage);
        System.out.println("listeRapportEtage size** :" + listeRapportEtage.size());
        var rapportEtageResults = Json.createArrayBuilder();
        var rapportResults = Json.createArrayBuilder();
        if (listeRapportEtage.size() > 0) {
            VPmsEditionRapportEtage valueListRapportEtage = listeRapportEtage.get(0);
            String numEtageInitial = valueListRapportEtage.getNumEtage();

            var typeChambre = "";
            var numChambre = "";
            var nomReservation = "";
            var situation = "";
            var etat = "";
            var pax = 0;
            var sejour = "";
            var paxArrivee = 0;
            var remarque = "";

            for (VPmsEditionRapportEtage listeEtage : listeRapportEtage) {
                if (numEtageInitial.equals(listeEtage.getNumEtage())) {
                    numEtageInitial = listeEtage.getNumEtage();
                    typeChambre = listeEtage.getType();
                    numChambre = listeEtage.getNumeroChambre();
                    nomReservation = listeEtage.getNom();
                    situation = listeEtage.getSituation();
                    etat = listeEtage.getEtat();
                    pax = listeEtage.getPax();
                    sejour = listeEtage.getSejour();
                    paxArrivee = listeEtage.getPaxArrivee();
                    remarque = listeEtage.getRemarque();

                    var rapportList = Json.createObjectBuilder()
                            .add("numEtage", numEtageInitial)
                            .add("typeChambre", typeChambre)
                            .add("numChambre", numChambre)
                            .add("nomReservation", nomReservation)
                            .add("situation", situation)
                            .add("etat", etat)
                            .add("pax", pax)
                            .add("sejour", sejour)
                            .add("paxArrivee", paxArrivee)
                            .add("remarque", remarque).build();
                    rapportResults.add(rapportList);
                } else {
                    var object = Json.createObjectBuilder()
                            .add("numeroEtage", numEtageInitial)
                            .add("listRapportEtage", rapportResults).build();

                    rapportEtageResults.add(object);

                    numEtageInitial = listeEtage.getNumEtage();
                    typeChambre = listeEtage.getType();
                    numChambre = listeEtage.getNumeroChambre();
                    nomReservation = listeEtage.getNom();
                    situation = listeEtage.getSituation();
                    etat = listeEtage.getEtat();
                    pax = listeEtage.getPax();
                    sejour = listeEtage.getSejour();
                    paxArrivee = listeEtage.getPaxArrivee();
                    remarque = listeEtage.getRemarque();

                    var rapportList = Json.createObjectBuilder()
                            .add("numEtage", numEtageInitial)
                            .add("typeChambre", typeChambre)
                            .add("numChambre", numChambre)
                            .add("nomReservation", nomReservation)
                            .add("situation", situation)
                            .add("etat", etat)
                            .add("pax", pax)
                            .add("sejour", sejour)
                            .add("paxArrivee", paxArrivee)
                            .add("remarque", remarque).build();

                    rapportResults = Json.createArrayBuilder();
                    rapportResults.add(rapportList);
                    numEtageInitial = numEtageInitial;
                }
            }
            var object = Json.createObjectBuilder()
                    .add("numeroEtage", numEtageInitial)
                    .add("listRapportEtage", rapportResults).build();

            rapportEtageResults.add(object);
        }
        return rapportEtageResults.build();
    }

    public List<VPmsEditionOccupation> getAllOccupation() {
        List<VPmsEditionOccupation> occupation = entityManager.createQuery("FROM VPmsEditionOccupation")
                .getResultList();
        return occupation;
    }

    /**
     *
     * @param dateStart
     * @param dateEnd
     * @redturn
     */
    /*
     * public List<VPmsEditionListeOccupation> getVPmsListeOccupation(String
     * dateStart, String dateEnd) { Boolean isExist = false; StringBuilder
     * stringBuilder = new StringBuilder();
     * stringBuilder.append("FROM vPmsListeOccupation  "); if
     * (!Objects.isNull(dateStart)) { stringBuilder.append(" WHERE dateArrivee >= '"
     * + dateStart + "'"); isExist = true; } if (!Objects.isNull(dateEnd)) { if
     * (isExist == true) { stringBuilder.append(" AND dateDepart <= '" + dateEnd +
     * "'"); } else { stringBuilder.append(" WHERE dateDepart <= '" + dateEnd +
     * "'"); } } List<VPmsEditionListeOccupation> result =
     * entityManager.createQuery(stringBuilder.toString()).getResultList(); return
     * result; }
     */
    public List<VPmsEditionListeOccupation> getAllListeOccupation() {
        List<VPmsEditionListeOccupation> listeOccupation = entityManager.createQuery("FROM VPmsEditionListeOccupation")
                .getResultList();
        return listeOccupation;
    }

    public List<VPmsEditionSoldeNoteOuverte> getAllSoldeNoteOuverte() {
        List<VPmsEditionSoldeNoteOuverte> soldeNote = entityManager.createQuery("FROM VPmsEditionSoldeNoteOuverte")
                .getResultList();
        return soldeNote;
    }

    public List<VPmsEditionEtatDebit> getAllEtatDebit() {
        List<VPmsEditionEtatDebit> etatDebit = entityManager.createQuery("FROM VPmsEditionEtatDebit").getResultList();
        return etatDebit;
    }

    public List<VPmsEditionEtatDebitChambre> getAllEtatDebitRoom() {
        List<VPmsEditionEtatDebitChambre> etatDebitChbr = entityManager.createQuery("FROM VPmsEditionEtatDebitChambre")
                .getResultList();
        return etatDebitChbr;
    }

    public List<VPmsEditionEtatControl> getAllEtatControl() {
        List<VPmsEditionEtatControl> etatControl = entityManager.createQuery("FROM VPmsEditionEtatControl")
                .getResultList();
        return etatControl;
    }

    /**
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<VPmsEditionListeArrivee> getAllListArrival(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeArrivee  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeArrivee> listeArrival = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        return listeArrival;
    }

    public JsonArray getAllListGlobalArriv() {
        List<VPmsEditionListeGlobalArrivee> listeGlobalArrival = entityManager
                .createQuery("FROM VPmsEditionListeGlobalArrivee").getResultList();

        var soldes = new BigDecimal("0");
        var typeRoomResults = Json.createArrayBuilder();
        var resaResults = Json.createArrayBuilder();
        var productList = Json.createArrayBuilder();
        if (listeGlobalArrival.size() > 0) {
            VPmsEditionListeGlobalArrivee valueListArrival = listeGlobalArrival.get(0);
            String libTypRoomInit = valueListArrival.getTypeChambre();
            String numResa = valueListArrival.getNumReservation();
            var typeChambre = "";
            var typeClient = "";
            var segmentClient = "";
            var compteClient = "";
            var numReservation = "";
            var client = "";
            var sejour = "";
            var adultEnfant = 0;
            var nombre = 0;
            var observation = "";
            var dateArrivee = "";
            var dateDepart = "";
            var dateCreation = "";
            var prestation = "";
            var qte = 0;
            // Remplir liste par type chambre

            for (VPmsEditionListeGlobalArrivee typRoomArr : listeGlobalArrival) {
                if (libTypRoomInit.equals(typRoomArr.getTypeChambre())) {
                    if (numResa.equals(typRoomArr.getNumReservation())) {
                        prestation = typRoomArr.getPrestation();
                        qte = typRoomArr.getQuantite();

                        var product = Json.createObjectBuilder().add("libelle", prestation).add("qte", qte).build();
                        productList.add(product);

                        typeChambre = typRoomArr.getTypeChambre();
                        typeClient = typRoomArr.getTypeClient();
                        segmentClient = typRoomArr.getSegmentClient();
                        compteClient = typRoomArr.getCompteClient();
                        numReservation = typRoomArr.getNumReservation();
                        client = typRoomArr.getClient();
                        sejour = typRoomArr.getSejour();
                        adultEnfant = typRoomArr.getAdultEnfant();
                        nombre = typRoomArr.getNombre();
                        observation = typRoomArr.getObservation();
                        soldes = soldes.add(typRoomArr.getMontant());

                        dateArrivee = typRoomArr.getDateArrivee().toString();
                        dateDepart = typRoomArr.getDateDepart().toString();
                        dateCreation = typRoomArr.getDateCreation().toString();
                    } else {
                        var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                                .add("typeClient", typeClient).add("segmentClient", segmentClient)
                                .add("compteClient", compteClient).add("numReservation", numReservation)
                                .add("client", client).add("sejour", sejour).add("adultEnfant", adultEnfant)
                                .add("nombre", nombre).add("prestations", productList).add("observation", observation)
                                .add("soldes", soldes.setScale(2, RoundingMode.HALF_UP).toString())
                                .add("dateArrivee", dateArrivee).add("dateDepart", dateDepart)
                                .add("dateCreation", dateCreation).build();
                        resaResults.add(resaList);

                        prestation = typRoomArr.getPrestation();
                        qte = typRoomArr.getQuantite();

                        var product = Json.createObjectBuilder().add("libelle", prestation).add("qte", qte).build();

                        productList = Json.createArrayBuilder();
                        productList.add(product);
                        soldes = new BigDecimal("0");
                        typeChambre = typRoomArr.getTypeChambre();
                        typeClient = typRoomArr.getTypeClient();
                        segmentClient = typRoomArr.getSegmentClient();
                        compteClient = typRoomArr.getCompteClient();
                        numReservation = typRoomArr.getNumReservation();
                        client = typRoomArr.getClient();
                        sejour = typRoomArr.getSejour();
                        adultEnfant = typRoomArr.getAdultEnfant();
                        nombre = typRoomArr.getNombre();
                        observation = typRoomArr.getObservation();
                        soldes = soldes.add(typRoomArr.getMontant());
                        dateArrivee = typRoomArr.getDateArrivee().toString();
                        dateDepart = typRoomArr.getDateDepart().toString();
                        dateCreation = typRoomArr.getDateCreation().toString();

                        numResa = typRoomArr.getNumReservation();

                    }
                } else {
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("typeClient", typeClient).add("segmentClient", segmentClient)
                            .add("compteClient", compteClient).add("numReservation", numReservation)
                            .add("client", client).add("sejour", sejour).add("adultEnfant", adultEnfant)
                            .add("nombre", nombre).add("prestations", productList).add("observation", observation)
                            .add("soldes", soldes.setScale(2, RoundingMode.HALF_UP).toString())
                            .add("dateArrivee", dateArrivee).add("dateDepart", dateDepart)
                            .add("dateCreation", dateCreation).build();

                    resaResults.add(resaList);

                    var listeArrivalByTypeChambre = Json.createObjectBuilder()
                            .add("listeArrival-by-typeChambre", resaResults).add("typeChambre", typeChambre).build();
                    typeRoomResults.add(listeArrivalByTypeChambre);

                    resaResults = Json.createArrayBuilder();

                    var product = Json.createObjectBuilder().add("libelle", typRoomArr.getPrestation())
                            .add("qte", typRoomArr.getQuantite()).build();
                    productList = Json.createArrayBuilder();
                    productList.add(product);
                    soldes = new BigDecimal("0");
                    typeChambre = typRoomArr.getTypeChambre();
                    typeClient = typRoomArr.getTypeClient();
                    segmentClient = typRoomArr.getSegmentClient();
                    compteClient = typRoomArr.getCompteClient();
                    numReservation = typRoomArr.getNumReservation();
                    client = typRoomArr.getClient();
                    sejour = typRoomArr.getSejour();
                    adultEnfant = typRoomArr.getAdultEnfant();
                    nombre = typRoomArr.getNombre();
                    observation = typRoomArr.getObservation();
                    soldes = soldes.add(typRoomArr.getMontant());
                    dateArrivee = typRoomArr.getDateArrivee().toString();
                    dateDepart = typRoomArr.getDateDepart().toString();
                    dateCreation = typRoomArr.getDateCreation().toString();

                    numResa = typRoomArr.getNumReservation();
                    libTypRoomInit = typRoomArr.getTypeChambre();
                }
            }

            var listeArrivalByTypeChambre = Json.createObjectBuilder().add("listeArrival-by-typeChambre", resaResults)
                    .add("typeChambre", typeChambre).build();
            typeRoomResults.add(listeArrivalByTypeChambre);
        }
        return typeRoomResults.build();
    }

    public JsonArray getAllListGlobalArrival(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeGlobalArrivee  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeGlobalArrivee> listeGlobalArrival = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var soldes = new BigDecimal("0");
        var typeRoomResults = Json.createArrayBuilder();
        var resaResults = Json.createArrayBuilder();
        var productList = Json.createArrayBuilder();
        if (listeGlobalArrival.size() > 0) {
            VPmsEditionListeGlobalArrivee valueListArrival = listeGlobalArrival.get(0);
            String libTypRoomInit = valueListArrival.getTypeChambre();
            String numResa = valueListArrival.getNumReservation();
            var typeChambre = "";
            var typeClient = "";
            var segmentClient = "";
            var compteClient = "";
            var numReservation = "";
            var client = "";
            var sejour = "";
            var adultEnfant = 0;
            var nombre = 0;
            var observation = "";
            var dateArrivee = "";
            var dateDepart = "";
            var dateCreation = "";
            var prestation = "";
            var qte = 0;
            // Remplir liste par type chambre

            for (VPmsEditionListeGlobalArrivee typRoomArr : listeGlobalArrival) {
                if (libTypRoomInit.equals(typRoomArr.getTypeChambre())) {
                    if (numResa.equals(typRoomArr.getNumReservation())) {
                        prestation = typRoomArr.getPrestation();
                        qte = typRoomArr.getQuantite();

                        var product = Json.createObjectBuilder().add("libelle", prestation).add("qte", qte).build();
                        productList.add(product);

                        typeChambre = typRoomArr.getTypeChambre();
                        typeClient = typRoomArr.getTypeClient();
                        segmentClient = typRoomArr.getSegmentClient();
                        compteClient = typRoomArr.getCompteClient();
                        numReservation = typRoomArr.getNumReservation();
                        client = typRoomArr.getClient();
                        sejour = typRoomArr.getSejour();
                        adultEnfant = typRoomArr.getAdultEnfant();
                        nombre = typRoomArr.getNombre();
                        observation = typRoomArr.getObservation();
                        soldes = soldes.add(typRoomArr.getMontant());
                        dateArrivee = typRoomArr.getDateArrivee().toString();
                        dateDepart = typRoomArr.getDateDepart().toString();
                        dateCreation = typRoomArr.getDateCreation().toString();
                    } else {
                        var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                                .add("typeClient", typeClient).add("segmentClient", segmentClient)
                                .add("compteClient", compteClient).add("numReservation", numReservation)
                                .add("client", client).add("sejour", sejour).add("adultEnfant", adultEnfant)
                                .add("nombre", nombre).add("prestations", productList).add("observation", observation)
                                .add("soldes", soldes.setScale(2, RoundingMode.HALF_UP).toString())
                                .add("dateArrivee", dateArrivee).add("dateDepart", dateDepart)
                                .add("dateCreation", dateCreation).build();

                        resaResults.add(resaList);

                        prestation = typRoomArr.getPrestation();
                        qte = typRoomArr.getQuantite();

                        var product = Json.createObjectBuilder().add("libelle", prestation).add("qte", qte).build();

                        productList = Json.createArrayBuilder();
                        productList.add(product);
                        soldes = new BigDecimal("0");
                        typeChambre = typRoomArr.getTypeChambre();
                        typeClient = typRoomArr.getTypeClient();
                        segmentClient = typRoomArr.getSegmentClient();
                        compteClient = typRoomArr.getCompteClient();
                        numReservation = typRoomArr.getNumReservation();
                        client = typRoomArr.getClient();
                        sejour = typRoomArr.getSejour();
                        adultEnfant = typRoomArr.getAdultEnfant();
                        nombre = typRoomArr.getNombre();
                        observation = typRoomArr.getObservation();
                        soldes = soldes.add(typRoomArr.getMontant());
                        dateArrivee = typRoomArr.getDateArrivee().toString();
                        dateDepart = typRoomArr.getDateDepart().toString();
                        dateCreation = typRoomArr.getDateCreation().toString();

                        numResa = typRoomArr.getNumReservation();

                    }
                } else {
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("typeClient", typeClient).add("segmentClient", segmentClient)
                            .add("compteClient", compteClient).add("numReservation", numReservation)
                            .add("client", client).add("sejour", sejour).add("adultEnfant", adultEnfant)
                            .add("nombre", nombre).add("prestations", productList).add("observation", observation)
                            .add("soldes", soldes.setScale(2, RoundingMode.HALF_UP).toString())
                            .add("dateArrivee", dateArrivee).add("dateDepart", dateDepart)
                            .add("dateCreation", dateCreation).build();

                    resaResults.add(resaList);

                    var listeArrivalByTypeChambre = Json.createObjectBuilder()
                            .add("listeArrival-by-typeChambre", resaResults).add("typeChambre", typeChambre).build();
                    typeRoomResults.add(listeArrivalByTypeChambre);

                    resaResults = Json.createArrayBuilder();

                    var product = Json.createObjectBuilder().add("libelle", typRoomArr.getPrestation())
                            .add("qte", typRoomArr.getQuantite()).build();
                    productList = Json.createArrayBuilder();
                    productList.add(product);
                    soldes = new BigDecimal("0");
                    typeChambre = typRoomArr.getTypeChambre();
                    typeClient = typRoomArr.getTypeClient();
                    segmentClient = typRoomArr.getSegmentClient();
                    compteClient = typRoomArr.getCompteClient();
                    numReservation = typRoomArr.getNumReservation();
                    client = typRoomArr.getClient();
                    sejour = typRoomArr.getSejour();
                    adultEnfant = typRoomArr.getAdultEnfant();
                    nombre = typRoomArr.getNombre();
                    observation = typRoomArr.getObservation();
                    soldes = soldes.add(typRoomArr.getMontant());
                    dateArrivee = typRoomArr.getDateArrivee().toString();
                    dateDepart = typRoomArr.getDateDepart().toString();
                    dateCreation = typRoomArr.getDateCreation().toString();

                    numResa = typRoomArr.getNumReservation();
                    libTypRoomInit = typRoomArr.getTypeChambre();
                }
            }

            var listeArrivalByTypeChambre = Json.createObjectBuilder().add("listeArrival-by-typeChambre", resaResults)
                    .add("typeChambre", typeChambre).build();
            typeRoomResults.add(listeArrivalByTypeChambre);
        }
        return typeRoomResults.build();
    }

    public JsonArray getAllListOccupationResa() {
        List<VPmsEditionListeOccupationResa> listeOccResa = entityManager
                .createQuery("FROM VPmsEditionListeOccupationResa").getResultList();
        var typeRoomResults = Json.createArrayBuilder();
        var resaResults = Json.createArrayBuilder();
        if (listeOccResa.size() > 0) {
            VPmsEditionListeOccupationResa valueListArrival = listeOccResa.get(0);
            String typRoomInitial = valueListArrival.getTypeChambre();
            var typeChambre = "";
            var compteClient = "";
            var numReservation = "";
            var nom = "";
            var sejour = "";
            var qte = 0;
            var numChambre = "";
            var totalQte = 0;

            for (VPmsEditionListeOccupationResa typeRoom : listeOccResa) {
                if (typRoomInitial.equals(typeRoom.getTypeChambre())) {
                    typeChambre = typeRoom.getTypeChambre();
                    compteClient = typeRoom.getCompteClient();
                    numReservation = typeRoom.getNumReservation();
                    nom = typeRoom.getNom();
                    sejour = typeRoom.getSejour();
                    qte = typeRoom.getQte();
                    totalQte += typeRoom.getQte();
                    numChambre = typeRoom.getNumChambre();
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("compteClient", compteClient).add("numReservation", numReservation).add("nom", nom)
                            .add("sejour", sejour).add("qte", qte).add("numChambre", numChambre).build();
                    resaResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("typeChambre", typeChambre).add("qteChambre", totalQte)
                            .add("list-by-typeChambre", resaResults).build();

                    typeRoomResults.add(object);

                    totalQte = 0;

                    typeChambre = typeRoom.getTypeChambre();
                    compteClient = typeRoom.getCompteClient();
                    numReservation = typeRoom.getNumReservation();
                    nom = typeRoom.getNom();
                    sejour = typeRoom.getSejour();
                    qte = typeRoom.getQte();
                    totalQte += typeRoom.getQte();
                    numChambre = typeRoom.getNumChambre();
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("compteClient", compteClient).add("numReservation", numReservation).add("nom", nom)
                            .add("sejour", sejour).add("qte", qte).add("numChambre", numChambre).build();

                    resaResults = Json.createArrayBuilder();
                    resaResults.add(resaList);
                    typRoomInitial = typeChambre;
                }
            }
            var object = Json.createObjectBuilder().add("typeChambre", typeChambre).add("qteChambre", totalQte)
                    .add("list-by-typeChambre", resaResults).build();

            typeRoomResults.add(object);
        }
        return typeRoomResults.build();
    }

    public JsonArray getListOccupationResa(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeOccupationResa  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeOccupationResa> listeOccResa = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        var typeRoomResults = Json.createArrayBuilder();
        var resaResults = Json.createArrayBuilder();
        if (listeOccResa.size() > 0) {
            VPmsEditionListeOccupationResa valueListArrival = listeOccResa.get(0);
            String typRoomInitial = valueListArrival.getTypeChambre();
            var typeChambre = "";
            var compteClient = "";
            var numReservation = "";
            var nom = "";
            var sejour = "";
            var qte = 0;
            var numChambre = "";
            var totalQte = 0;

            for (VPmsEditionListeOccupationResa typeRoom : listeOccResa) {
                if (typRoomInitial.equals(typeRoom.getTypeChambre())) {
                    typeChambre = typeRoom.getTypeChambre();
                    compteClient = typeRoom.getCompteClient();
                    numReservation = typeRoom.getNumReservation();
                    nom = typeRoom.getNom();
                    sejour = typeRoom.getSejour();
                    qte = typeRoom.getQte();
                    totalQte += typeRoom.getQte();
                    numChambre = typeRoom.getNumChambre();
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("compteClient", compteClient).add("numReservation", numReservation).add("nom", nom)
                            .add("sejour", sejour).add("qte", qte).add("numChambre", numChambre).build();
                    resaResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("typeChambre", typeChambre).add("qteChambre", totalQte)
                            .add("list-by-typeChambre", resaResults).build();

                    typeRoomResults.add(object);

                    totalQte = 0;

                    typeChambre = typeRoom.getTypeChambre();
                    compteClient = typeRoom.getCompteClient();
                    numReservation = typeRoom.getNumReservation();
                    nom = typeRoom.getNom();
                    sejour = typeRoom.getSejour();
                    qte = typeRoom.getQte();
                    totalQte += typeRoom.getQte();
                    numChambre = typeRoom.getNumChambre();
                    var resaList = Json.createObjectBuilder().add("typeChambre", typeChambre)
                            .add("compteClient", compteClient).add("numReservation", numReservation).add("nom", nom)
                            .add("sejour", sejour).add("qte", qte).add("numChambre", numChambre).build();

                    resaResults = Json.createArrayBuilder();
                    resaResults.add(resaList);
                    typRoomInitial = typeChambre;

                }

            }
            var object = Json.createObjectBuilder().add("typeChambre", typeChambre).add("qteChambre", totalQte)
                    .add("list-by-typeChambre", resaResults).build();

            typeRoomResults.add(object);
        }
        return typeRoomResults.build();
    }

    public List<VPmsEditionListeOccupationReel> getAllListOccupationReel(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeOccupationReel  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeOccupationReel> listeOccReel = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        return listeOccReel;
    }

    public List<VPmsEditionListeOccupationReelRecep> getAllListOccupationReelRecep(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeOccupationReelRecep  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeOccupationReelRecep> listeOccReel = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        return listeOccReel;
    }

    public List<VPmsEditionListeOption> getAllListOption(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeOption  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeOption> listeOption = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return listeOption;
    }

    public JsonArray getAllListVerifArrival() {
        List<VPmsEditionListeVerifArrivee> listeVerifArrival = entityManager
                .createQuery("FROM VPmsEditionListeVerifArrivee").getResultList();

        var compteClientResults = Json.createArrayBuilder();
        var arrivalResults = Json.createArrayBuilder();
        if (listeVerifArrival.size() > 0) {
            VPmsEditionListeVerifArrivee valueListArrival = listeVerifArrival.get(0);
            Integer idCompteClientInitial = valueListArrival.getIdClient();

            var sejour = "";
            var typeChambre = "";
            var qte = 0;
            var nbrPax = 0;
            var client = "";
            var numReservation = "";
            var indivGroup = "";
            var refDossier = "";
            var signature = "";

            for (VPmsEditionListeVerifArrivee listeArrival : listeVerifArrival) {
                if (idCompteClientInitial.equals(listeArrival.getIdClient())) {
                    idCompteClientInitial = listeArrival.getIdClient();
                    sejour = listeArrival.getSejour();
                    typeChambre = listeArrival.getTypeChambre();
                    qte = listeArrival.getQte();
                    nbrPax = listeArrival.getNbrPax();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    indivGroup = listeArrival.getIndivGroupe();
                    refDossier = listeArrival.getRefDossier();
                    signature = listeArrival.getSignature();

                    var resaList = Json.createObjectBuilder().add("idCompteClient", idCompteClientInitial)
                            .add("sejour", sejour).add("typeChambre", typeChambre).add("qte", qte).add("nbrPax", nbrPax)
                            .add("client", client).add("numReservation", numReservation).add("indivGroup", indivGroup)
                            .add("refDossier", refDossier).add("signature", signature).build();
                    arrivalResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("compteClient", client)
                            .add("listByCompteClient", arrivalResults).build();

                    compteClientResults.add(object);

                    idCompteClientInitial = listeArrival.getIdClient();
                    sejour = listeArrival.getSejour();
                    typeChambre = listeArrival.getTypeChambre();
                    qte = listeArrival.getQte();
                    nbrPax = listeArrival.getNbrPax();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    indivGroup = listeArrival.getIndivGroupe();
                    refDossier = listeArrival.getRefDossier();
                    signature = listeArrival.getSignature();

                    var resaList = Json.createObjectBuilder().add("idCompteClient", idCompteClientInitial)
                            .add("sejour", sejour).add("typeChambre", typeChambre).add("qte", qte).add("nbrPax", nbrPax)
                            .add("client", client).add("numReservation", numReservation).add("indivGroup", indivGroup)
                            .add("refDossier", refDossier).add("signature", signature).build();

                    arrivalResults = Json.createArrayBuilder();
                    arrivalResults.add(resaList);
                    idCompteClientInitial = idCompteClientInitial;
                }
            }
            var object = Json.createObjectBuilder().add("compteClient", client)
                    .add("listByCompteClient", arrivalResults).build();

            compteClientResults.add(object);
        }
        return compteClientResults.build();
    }

    public JsonArray getListVerifArrival(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListeVerifArrivee  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }
        List<VPmsEditionListeVerifArrivee> listeVerifArrival = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var compteClientResults = Json.createArrayBuilder();
        var arrivalResults = Json.createArrayBuilder();
        if (listeVerifArrival.size() > 0) {
            VPmsEditionListeVerifArrivee valueListArrival = listeVerifArrival.get(0);
            Integer idCompteClientInitial = valueListArrival.getIdClient();

            var sejour = "";
            var typeChambre = "";
            var qte = 0;
            var nbrPax = 0;
            var client = "";
            var numReservation = "";
            var indivGroup = "";
            var refDossier = "";
            var signature = "";

            for (VPmsEditionListeVerifArrivee listeArrival : listeVerifArrival) {
                if (idCompteClientInitial.equals(listeArrival.getIdClient())) {
                    idCompteClientInitial = listeArrival.getIdClient();
                    sejour = listeArrival.getSejour();
                    typeChambre = listeArrival.getTypeChambre();
                    qte = listeArrival.getQte();
                    nbrPax = listeArrival.getNbrPax();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    indivGroup = listeArrival.getIndivGroupe();
                    refDossier = listeArrival.getRefDossier();
                    signature = listeArrival.getSignature();

                    var resaList = Json.createObjectBuilder().add("idCompteClient", idCompteClientInitial)
                            .add("sejour", sejour).add("typeChambre", typeChambre).add("qte", qte).add("nbrPax", nbrPax)
                            .add("client", client).add("numReservation", numReservation).add("indivGroup", indivGroup)
                            .add("refDossier", refDossier).add("signature", signature).build();
                    arrivalResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("compteClient", client)
                            .add("listByCompteClient", arrivalResults).build();

                    compteClientResults.add(object);

                    idCompteClientInitial = listeArrival.getIdClient();
                    sejour = listeArrival.getSejour();
                    typeChambre = listeArrival.getTypeChambre();
                    qte = listeArrival.getQte();
                    nbrPax = listeArrival.getNbrPax();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    indivGroup = listeArrival.getIndivGroupe();
                    refDossier = listeArrival.getRefDossier();
                    signature = listeArrival.getSignature();

                    var resaList = Json.createObjectBuilder().add("idCompteClient", idCompteClientInitial)
                            .add("sejour", sejour).add("typeChambre", typeChambre).add("qte", qte).add("nbrPax", nbrPax)
                            .add("client", client).add("numReservation", numReservation).add("indivGroup", indivGroup)
                            .add("refDossier", refDossier).add("signature", signature).build();

                    arrivalResults = Json.createArrayBuilder();
                    arrivalResults.add(resaList);
                    idCompteClientInitial = idCompteClientInitial;
                }
            }
            var object = Json.createObjectBuilder().add("compteClient", client)
                    .add("listByCompteClient", arrivalResults).build();

            compteClientResults.add(object);
        }
        return compteClientResults.build();
    }

    public JsonArray getAllListProductArrivalWithDate(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionListePrestationArrivee ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEnd + "' ORDER BY libellePrestation ");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEnd + "'");
            }
        }

        List<VPmsEditionListePrestationArrivee> listeProductArrival = entityManager
                .createQuery(stringBuilder.toString()).getResultList();
        var prestationResults = Json.createArrayBuilder();
        var arrivalResults = Json.createArrayBuilder();
        if (listeProductArrival.size() > 0) {
            VPmsEditionListePrestationArrivee valueListArrival = listeProductArrival.get(0);
            Integer idProductInitial = valueListArrival.getIdPrestation();

            var libellePrestation = "";
            var sejour = "";
            var nombre = 0;
            var client = "";
            var numReservation = "";
            var reference = "";
            int total = 0;

            for (VPmsEditionListePrestationArrivee listeArrival : listeProductArrival) {
                if (idProductInitial.equals(listeArrival.getIdPrestation())) {
                    idProductInitial = listeArrival.getIdPrestation();
                    libellePrestation = listeArrival.getLibellePrestation();
                    sejour = listeArrival.getSejour();
                    nombre = listeArrival.getNombre();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    reference = listeArrival.getReference();
                    total += listeArrival.getNombre();

                    var resaList = Json.createObjectBuilder().add("idPrestation", idProductInitial)
                            .add("libellePresation", libellePrestation).add("sejour", sejour).add("nombre", nombre)
                            .add("client", client).add("numReservation", numReservation).add("reference", reference)
                            .build();
                    arrivalResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("libellePrestation", libellePrestation)
                            .add("total", total).add("listByPrestation", arrivalResults).build();

                    prestationResults.add(object);
                    total = 0;

                    idProductInitial = listeArrival.getIdPrestation();
                    libellePrestation = listeArrival.getLibellePrestation();
                    sejour = listeArrival.getSejour();
                    nombre = listeArrival.getNombre();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    reference = listeArrival.getReference();
                    total += listeArrival.getNombre();

                    var resaList = Json.createObjectBuilder().add("idPrestation", idProductInitial)
                            .add("libellePresation", libellePrestation).add("sejour", sejour).add("nombre", nombre)
                            .add("client", client).add("numReservation", numReservation).add("reference", reference)
                            .build();

                    arrivalResults = Json.createArrayBuilder();
                    arrivalResults.add(resaList);
                    idProductInitial = idProductInitial;
                }
            }
            var object = Json.createObjectBuilder().add("libellePrestation", libellePrestation).add("total", total)
                    .add("listByPrestation", arrivalResults).build();

            prestationResults.add(object);
        }
        return prestationResults.build();

    }

    public JsonArray getAllListProductArrival() {
        List<VPmsEditionListePrestationArrivee> listeProductArrival = entityManager
                .createQuery("FROM VPmsEditionListePrestationArrivee ORDER BY libellePrestation").getResultList();

        var prestationResults = Json.createArrayBuilder();
        var arrivalResults = Json.createArrayBuilder();
        if (listeProductArrival.size() > 0) {
            VPmsEditionListePrestationArrivee valueListArrival = listeProductArrival.get(0);
            Integer idProductInitial = valueListArrival.getIdPrestation();

            var libellePrestation = "";
            var sejour = "";
            var nombre = 0;
            var client = "";
            var numReservation = "";
            var reference = "";
            int total = 0;

            for (VPmsEditionListePrestationArrivee listeArrival : listeProductArrival) {
                if (idProductInitial.equals(listeArrival.getIdPrestation())) {
                    idProductInitial = listeArrival.getIdPrestation();
                    libellePrestation = listeArrival.getLibellePrestation();
                    sejour = listeArrival.getSejour();
                    nombre = listeArrival.getNombre();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    reference = listeArrival.getReference();
                    total += listeArrival.getNombre();

                    var resaList = Json.createObjectBuilder().add("idPrestation", idProductInitial)
                            .add("libellePresation", libellePrestation).add("sejour", sejour).add("nombre", nombre)
                            .add("client", client).add("numReservation", numReservation).add("reference", reference)
                            .build();
                    arrivalResults.add(resaList);
                } else {
                    var object = Json.createObjectBuilder().add("libellePrestation", libellePrestation)
                            .add("total", total).add("listByPrestation", arrivalResults).build();

                    prestationResults.add(object);
                    total = 0;

                    idProductInitial = listeArrival.getIdPrestation();
                    libellePrestation = listeArrival.getLibellePrestation();
                    sejour = listeArrival.getSejour();
                    nombre = listeArrival.getNombre();
                    client = listeArrival.getClient();
                    numReservation = listeArrival.getNumReservation();
                    reference = listeArrival.getReference();
                    total += listeArrival.getNombre();

                    var resaList = Json.createObjectBuilder().add("idPrestation", idProductInitial)
                            .add("libellePresation", libellePrestation).add("sejour", sejour).add("nombre", nombre)
                            .add("client", client).add("numReservation", numReservation).add("reference", reference)
                            .build();

                    arrivalResults = Json.createArrayBuilder();
                    arrivalResults.add(resaList);
                    idProductInitial = idProductInitial;
                }
            }
            var object = Json.createObjectBuilder().add("libellePrestation", libellePrestation).add("total", total)
                    .add("listByPrestation", arrivalResults).build();

            prestationResults.add(object);
        }
        return prestationResults.build();
    }

    public List<VPmsEditionEtatRemplissage> getAllEtatRemplissage() {
        List<VPmsEditionEtatRemplissage> etatRemplissage = entityManager.createQuery("FROM VPmsEditionEtatRemplissage")
                .getResultList();
        return etatRemplissage;
    }

    public JsonObject getAllRemplissageAnnuel(String dateStart, String dateEnd) {
        JsonArrayBuilder resultArrayBuilder = Json.createArrayBuilder();
        LocalDate dateOccupation = null;
        LocalDate dateEndLocalDate = null;
        if (Objects.isNull(dateStart)) {
            dateOccupation = (LocalDate) entityManager.createQuery("select MIN(dateArrivee) from TPmsReservation")
                    .getSingleResult();
        } else {
            dateOccupation = LocalDate.parse(dateStart);
        }
        if (Objects.isNull(dateEnd)) {
            dateEndLocalDate = (LocalDate) entityManager.createQuery("select MAX(dateDepart) from TPmsReservation")
                    .getSingleResult();
        } else {
            dateEndLocalDate = LocalDate.parse(dateEnd);
        }

        while (dateOccupation.compareTo(dateEndLocalDate) <= 0) {
            BigDecimal nbReservationClosed = getRoomNbReservationClosed(dateOccupation);
            BigDecimal nbReservationOption = getRoomNbReservationOption(dateOccupation);
            int nbrRoom = getNbrRoom().intValue();

            var option = (nbReservationOption == null) ? 0 : nbReservationOption.intValue();
            var ferme = (nbReservationClosed == null) ? 0 : nbReservationClosed.intValue();

            JsonObject value = Json.createObjectBuilder().add("dateOccupation", dateOccupation.toString())
                    .add("option", option).add("ferme", ferme).add("totalRoomStock", nbrRoom).build();
            resultArrayBuilder.add(value);
            dateOccupation = dateOccupation.plus(Period.ofDays(1));
        }
        JsonObject resultJson = Json.createObjectBuilder()
                .add("pmsEditionRemplissageAnnuel", resultArrayBuilder.build()).build();
        return resultJson;
    }

    public BigDecimal getRoomNbReservationClosed(LocalDate dateOccupation) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateOccupation AND date_depart>:dateOccupation ")
                .setParameter("dateOccupation", dateOccupation).getSingleResult();
    }

    public BigDecimal getRoomNbReservationOption(LocalDate dateOccupation) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateOccupation AND date_depart>:dateOccupation AND date_option is not null ")
                .setParameter("dateOccupation", dateOccupation).getSingleResult();
    }

    public BigInteger getNbrRoom() {
        return (BigInteger) entityManager.createNativeQuery("select count(*) from t_pms_chambre").getSingleResult();
    }

    public JsonObject getAllEffectifPrevDebours(String yearMonthStr) {
        JsonArrayBuilder resultArrayBuilder = Json.createArrayBuilder();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);
        YearMonth yearMonth = null;
        LocalDate dateEffective = null;
        LocalDate dateEndLocalDate = null;
        if (Objects.isNull(yearMonthStr)) {
            dateEffective = (LocalDate) entityManager.createQuery("select MIN(dateArrivee) from TPmsReservation")
                    .getSingleResult();
            dateEndLocalDate = (LocalDate) entityManager.createQuery("select MAX(dateDepart) from TPmsReservation")
                    .getSingleResult();
        } else {
            yearMonth = YearMonth.parse(yearMonthStr, formatter);
            dateEffective = yearMonth.atDay(1);
            dateEndLocalDate = yearMonth.atEndOfMonth();
        }

        while (dateEffective.compareTo(dateEndLocalDate) <= 0) {
            BigDecimal nbOccupation = getNbrOccupation(dateEffective);
            var nbOcc = (nbOccupation == null) ? 0 : nbOccupation.intValue();
            int nbArrival = getNbrArrivee(dateEffective).intValue();
            int nbDepart = getNbrDepart(dateEffective).intValue();
            Integer nbRecouche = getNbrRecouche();
            var nbRec = (nbRecouche == null) ? 0 : nbRecouche;
            Integer nbPdj = getNbrPdj();
            var nbPetitDej = (nbPdj == null) ? 0 : nbPdj;

            BigDecimal nbPax = getNbrePax(dateEffective);
            var nbrPax = (nbPax == null) ? 0 : nbPax.intValue();
            String nbAdultEnfant = getNbrAdultEnfant(dateEffective);
            var nbAdltEnf = (nbAdultEnfant == null) ? "" : nbAdultEnfant.toString();
            BigDecimal nbEnfant = getNbrEnfant(dateEffective);
            var nbrEnf = (nbEnfant == null) ? 0 : nbEnfant.intValue();

            JsonObject value = Json.createObjectBuilder().add("dateEffective", dateEffective.toString())
                    .add("nbOccupation", nbOcc).add("nbArrivee", nbArrival).add("nbDepart", nbDepart)
                    .add("nbRec", nbRec).add("nbPdj", nbPetitDej).add("nbPax", nbrPax).add("nbAdultEnfant", nbAdltEnf)
                    .add("nbEnfant", nbrEnf).add("nbCouvertMidi", 0).add("nbCouvertSoir", 0).build();
            resultArrayBuilder.add(value);
            dateEffective = dateEffective.plus(Period.ofDays(1));
        }

        JsonObject resultJson = Json.createObjectBuilder().add("editionEffectifDebours", resultArrayBuilder.build())
                .build();
        return resultJson;
    }

    public BigDecimal getNbrOccupation(LocalDate dateEffective) {
        return (BigDecimal) entityManager.createNativeQuery("select SUM(s.nb_pax) from t_pms_sejour s "
                + "LEFT JOIN t_pms_reservation resa ON s.pms_reservation_id = resa.id "
                + "WHERE resa.date_arrivee<=:dateEffective AND resa.date_depart>:dateEffective AND s.pms_reservation_id = resa.id")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public BigInteger getNbrArrivee(LocalDate dateEffective) {
        return (BigInteger) entityManager
                .createNativeQuery("select count(*) from t_pms_reservation WHERE  date_arrivee =:dateEffective ")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public BigInteger getNbrDepart(LocalDate dateEffective) {
        return (BigInteger) entityManager
                .createNativeQuery("select count(*) from t_pms_reservation WHERE  date_depart =:dateEffective ")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public Integer getNbrRecouche() {
        return (Integer) entityManager.createNativeQuery("select is_recouche from t_pms_reservation_tarif_prestation")
                .getSingleResult();
    }

    public Integer getNbrPdj() {
        return (Integer) entityManager.createNativeQuery("select is_qte_pdj from t_pms_prestation").getSingleResult();
    }

    public BigDecimal getNbrePax(LocalDate dateEffective) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_pax) from t_pms_reservation WHERE date_arrivee<=:dateEffective AND date_depart>:dateEffective")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public String getNbrAdultEnfant(LocalDate dateEffective) {
        return (String) entityManager.createNativeQuery(
                "select concat(sum(nb_pax), '/',sum(nb_enf)) from t_pms_reservation WHERE date_arrivee<=:dateEffective AND date_depart>:dateEffective")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public BigDecimal getNbrEnfant(LocalDate dateEffective) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_enf) from t_pms_reservation WHERE date_arrivee<=:dateEffective AND date_depart>:dateEffective")
                .setParameter("dateEffective", dateEffective).getSingleResult();
    }

    public JsonObject getAllEffectifPrevFamTarif(String yearMonthStr) {
        JsonArrayBuilder resultArrayBuilder = Json.createArrayBuilder();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);
        YearMonth yearMonth = null;
        LocalDate dateEffective = null;
        LocalDate dateEndLocalDate = null;
        if (Objects.isNull(yearMonthStr)) {
            dateEffective = (LocalDate) entityManager.createQuery("select MIN(dateArrivee) from TPmsReservation")
                    .getSingleResult();
            dateEndLocalDate = (LocalDate) entityManager.createQuery("select MAX(dateDepart) from TPmsReservation")
                    .getSingleResult();
        } else {
            yearMonth = YearMonth.parse(yearMonthStr, formatter);
            dateEffective = yearMonth.atDay(1);
            dateEndLocalDate = yearMonth.atEndOfMonth();
        }

        while (dateEffective.compareTo(dateEndLocalDate) <= 0) {
            BigDecimal nbOccupation = getNbrOccupation(dateEffective);
            var nbOcc = (nbOccupation == null) ? 0 : nbOccupation.intValue();
            int nbArrival = getNbrArrivee(dateEffective).intValue();
            int nbDepart = getNbrDepart(dateEffective).intValue();
            Integer nbRecouche = getNbrRecouche();
            var nbRec = (nbRecouche == null) ? 0 : nbRecouche;
            Integer nbPdj = getNbrPdj();
            var nbPetitDej = (nbPdj == null) ? 0 : nbPdj;

            BigDecimal nbPax = getNbrePax(dateEffective);
            var nbrPax = (nbPax == null) ? 0 : nbPax.intValue();
            String nbAdultEnfant = getNbrAdultEnfant(dateEffective);
            var nbAdltEnf = (nbAdultEnfant == null) ? "" : nbAdultEnfant.toString();
            BigDecimal nbEnfant = getNbrEnfant(dateEffective);
            var nbrEnf = (nbEnfant == null) ? 0 : nbEnfant.intValue();

            JsonObject value = Json.createObjectBuilder().add("dateEffective", dateEffective.toString())
                    .add("nbOccupation", nbOcc).add("nbArrivee", nbArrival).add("nbDepart", nbDepart)
                    .add("nbRec", nbRec).add("nbPdj", nbPetitDej).add("nbPax", nbrPax).add("nbAdultEnfant", nbAdltEnf)
                    .add("nbEnfant", nbrEnf).add("nbCouvertMidi", 0).add("nbCouvertSoir", 0).build();
            resultArrayBuilder.add(value);
            dateEffective = dateEffective.plus(Period.ofDays(1));
        }

        JsonObject resultFinal = Json.createObjectBuilder().add("editionEffectifFamille", resultArrayBuilder.build())
                .build();
        return resultFinal;
    }

    public List<VPmsEditionBalanceAppartement> getAllBalanceAppartement() {
        List<VPmsEditionBalanceAppartement> balanceAppartement = entityManager
                .createQuery("FROM VPmsEditionBalanceAppartement").getResultList();
        return balanceAppartement;
    }

    public List<VPmsEditionPlanningMensuelChambre> getAllPlanningMonthRoom() {
        List<VPmsEditionPlanningMensuelChambre> planningMonth = entityManager
                .createQuery("FROM VPmsEditionPlanningMensuelChambre").getResultList();
        return planningMonth;
    }

    public JsonObject createObjectForTypeList(String typeClientInitial, Integer tauxOccTotal, Integer tauxOccConf, Integer nbPax, Integer nbChambre, Integer nbChambreOpt, Integer nbChambreConf, String dateArrivee, String dateDepart) {
        return Json.createObjectBuilder()
                .add("typeClient", typeClientInitial)
                .add("tauxOccTotal", tauxOccTotal)
                .add("tauxOccConf", tauxOccConf)
                .add("ca#", 0)
                .add("caPM", 0)
                .add("caPar#", 0)
                .add("caTot", 0)
                .add("nbPax", nbPax)
                .add("nbChambre", nbChambre)
                .add("nbChambreOpt", nbChambreOpt)
                .add("nbChambreConf", nbChambreConf)
                .add("dateArrivee", dateArrivee)
                .add("dateDepart", dateDepart)
                .build();

    }

    public JsonObject createTotalObjectForType(String typeClient, JsonArray arrivalResults, BigDecimal totalTauxOccTotal, BigDecimal totalTauxOccConf, BigDecimal totalCa, BigDecimal totalCaPM, BigDecimal totalCaPar, BigDecimal totalCaTot, Integer totalNbPax, Integer totalNbChambre, Integer totalNbChambreOpt, Integer totalNbChambreConf) {
        return Json.createObjectBuilder()
                .add("typeClient", typeClient)
                .add("listByTypeClient", arrivalResults)
                .add("totalTauxOccTotal", totalTauxOccTotal)
                .add("totalTauxOccConf", totalTauxOccConf)
                .add("totalCa", totalCa)
                .add("totalCaPM", totalCaPM)
                .add("totalCaPar", totalCaPar)
                .add("totalCaTot", totalCaTot)
                .add("totalNbPax", totalNbPax)
                .add("totalNbChambre", totalNbChambre)
                .add("totalNbChambreOpt", totalNbChambreOpt)
                .add("totalNbChambreConf", totalNbChambreConf)
                .build();

    }

    public JsonObject createObjectForMonthList(LocalDate dateInitial, JsonArray typeClientResults, BigDecimal totalMoisTauxOccTotal, BigDecimal totalMoisTauxOccConf, BigDecimal totalMoisCa, BigDecimal totalMoisCaPM, BigDecimal totalMoisCaPar, BigDecimal totalMoisCaTot, Integer totalMoisNbPax, Integer totalMoisNbChambre, Integer totalMoisNbChambreOpt, Integer totalMoisNbChambreConf) {
        DateTimeFormatter formatterYearMonth = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);

        return Json.createObjectBuilder()
                .add("moisEffective", dateInitial.format(formatterYearMonth))
                .add("listeByDateMonth", typeClientResults)
                .add("totalMoisTauxOccTotal", totalMoisTauxOccTotal)
                .add("totalMoisTauxOccConf", totalMoisTauxOccConf)
                .add("totalMoisCa", totalMoisCa)
                .add("totalMoisCaPM", totalMoisCaPM)
                .add("totalMoisCaPar", totalMoisCaPar)
                .add("totalMoisCaTot", totalMoisCaTot)
                .add("totalMoisNbPax", totalMoisNbPax)
                .add("totalMoisNbChambre", totalMoisNbChambre)
                .add("totalMoisNbChambreOpt", totalMoisNbChambreOpt)
                .add("totalMoisNbChambreConf", totalMoisNbChambreConf)
                .build();

    }

    public JsonObject createTotalGeneralObject(JsonArray listeByDateMonth, BigDecimal totalGeneralTauxOccTotal, BigDecimal totalGeneralTauxOccConf, BigDecimal totalGeneralCa, BigDecimal totalGeneralCaPM, BigDecimal totalGeneralCaPar, BigDecimal totalGeneralCaTot, Integer totalGeneralNbPax, Integer totalGeneralNbChambre, Integer totalGeneralNbChambreOpt, Integer totalGeneralNbChambreConf) {
        return Json.createObjectBuilder()
                .add("reportEtatPrevReal", listeByDateMonth)
                .add("totalGeneralTauxOccTotal", totalGeneralTauxOccTotal)
                .add("totalGeneralTauxOccConf", totalGeneralTauxOccConf)
                .add("totalGeneralCa", totalGeneralCa)
                .add("totalGeneralCaPM", totalGeneralCaPM)
                .add("totalGeneralCaPar", totalGeneralCaPar)
                .add("totalGeneralCaTot", totalGeneralCaTot)
                .add("totalGeneralNbPax", totalGeneralNbPax)
                .add("totalGeneralNbChambre", totalGeneralNbChambre)
                .add("totalGeneralNbChambreOpt", totalGeneralNbChambreOpt)
                .add("totalGeneralNbChambreConf", totalGeneralNbChambreConf)
                .build();

    }

    public List<VPmsEditionEtatPrevRealMois> getVPmsEditionEtatPrevRealMoisByDate(String dateStart, String dateEnd) {
        DateTimeFormatter formatterYearMonth = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM")
                .toFormatter(Locale.ENGLISH);
        YearMonth yearMonthStart = YearMonth.parse(dateStart, formatterYearMonth);
        LocalDate dateStr = yearMonthStart.atDay(1);

        YearMonth yearMonthEnd = YearMonth.parse(dateEnd, formatterYearMonth);
        LocalDate dateEn = yearMonthEnd.atEndOfMonth();

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsEditionEtatPrevRealMois ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateArrivee >= '" + dateStr.format(formatDate) + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart <= '" + dateEn.format(formatDate) + "' ");
            } else {
                stringBuilder.append(" WHERE dateDepart <= '" + dateEn.format(formatDate) + "'");
            }
        }

        stringBuilder.append(" ORDER BY YEAR(dateArrivee), MONTH(dateArrivee), typeClient ");

        List<VPmsEditionEtatPrevRealMois> etatPrevRealMonth = entityManager
                .createQuery(stringBuilder.toString()).getResultList();

        return etatPrevRealMonth;
    }

    public JsonObject getAllEtatPrevRealMonth(String dateStart, String dateEnd) {
        List<VPmsEditionEtatPrevRealMois> etatPrevRealMonth = getVPmsEditionEtatPrevRealMoisByDate(dateStart, dateEnd);
        var typeClientResults = Json.createArrayBuilder();
        var arrivalResults = Json.createArrayBuilder();
        var listeByDateMonth = Json.createArrayBuilder();

        var object = Json.createObjectBuilder().build();
        // Liste des totals  par type
        var totalTauxOccTotal = new BigDecimal(0);
        var totalTauxOccConf = new BigDecimal(0);
        var totalCa = new BigDecimal(0);
        var totalCaPM = new BigDecimal(0);
        var totalCaPar = new BigDecimal(0);
        var totalCaTot = new BigDecimal(0);
        var totalNbPax = 0;
        var totalNbChambre = 0;
        var totalNbChambreOpt = 0;
        var totalNbChambreConf = 0;

        // Liste des totals  mois
        var totalMoisTauxOccTotal = new BigDecimal(0);
        var totalMoisTauxOccConf = new BigDecimal(0);
        var totalMoisCa = new BigDecimal(0);
        var totalMoisCaPM = new BigDecimal(0);
        var totalMoisCaPar = new BigDecimal(0);
        var totalMoisCaTot = new BigDecimal(0);
        var totalMoisNbPax = 0;
        var totalMoisNbChambre = 0;
        var totalMoisNbChambreOpt = 0;
        var totalMoisNbChambreConf = 0;

        // Liste des totals  générals
        var totalGeneralTauxOccTotal = new BigDecimal(0);
        var totalGeneralTauxOccConf = new BigDecimal(0);
        var totalGeneralCa = new BigDecimal(0);
        var totalGeneralCaPM = new BigDecimal(0);
        var totalGeneralCaPar = new BigDecimal(0);
        var totalGeneralCaTot = new BigDecimal(0);
        var totalGeneralNbPax = 0;
        var totalGeneralNbChambre = 0;
        var totalGeneralNbChambreOpt = 0;
        var totalGeneralNbChambreConf = 0;
        var tauxOccTotal = 0;
        var tauxOccConf = 0;

        if (etatPrevRealMonth.size() > 0) {
            VPmsEditionEtatPrevRealMois valueListArrival = etatPrevRealMonth.get(0);
            String typeClientInitial = valueListArrival.getTypeClient();
            LocalDate dateInitial = valueListArrival.getDateArrivee();

            var typeClient = "";
            var nbChambre = 0;
            var nbChambreConf = 0;
            var nbChambreOpt = 0;
            var nbPax = 0;
            LocalDate dateArrivee = null;
            LocalDate dateDepart = null;
            int nbrRoom = getNbrRoom().intValue();

            for (VPmsEditionEtatPrevRealMois listeArrival : etatPrevRealMonth) {
                if (dateInitial.getMonth() == listeArrival.getDateArrivee().getMonth()) {
                    //pour  les objets de même mois
                    if (typeClientInitial.equals(listeArrival.getTypeClient())) {
                        //pour  les objets de même types
                        typeClient = listeArrival.getTypeClient();
                        nbChambre = listeArrival.getNbChambre();
                        nbChambreConf = listeArrival.getNbChambreConf();
                        nbChambreOpt = listeArrival.getNbChambreOpt();
                        nbPax = listeArrival.getNbPax();
                        dateArrivee = listeArrival.getDateArrivee();
                        dateDepart = listeArrival.getDateDepart();

                        tauxOccTotal = (nbChambre * 100) / (nbrRoom);
                        tauxOccConf = (nbChambreConf * 100) / (nbrRoom);
                        totalTauxOccTotal = totalTauxOccTotal.add(new BigDecimal(tauxOccTotal));
                        totalTauxOccConf = totalTauxOccConf.add(new BigDecimal(tauxOccConf));

                        totalCa = new BigDecimal(0);
                        totalCaPM = new BigDecimal(0);
                        totalCaPar = new BigDecimal(0);
                        totalCaTot = new BigDecimal(0);
                        totalNbPax += nbPax;
                        totalNbChambre += nbChambre;
                        totalNbChambreOpt += nbChambreOpt;
                        totalNbChambreConf += nbChambreConf;

                        var resaList = createObjectForTypeList(typeClient, tauxOccTotal, tauxOccConf, nbPax, nbChambre, nbChambreOpt, nbChambreConf, dateArrivee.toString(), dateDepart.toString());
                        //ajout dans la liste par type client
                        arrivalResults.add(resaList);
                    } else {
                        object = createTotalObjectForType(typeClient, arrivalResults.build(), totalTauxOccTotal, totalTauxOccConf, totalCa, totalCaPM, totalCaPar, totalCaTot, totalNbPax, totalNbChambre, totalNbChambreOpt, totalNbChambreConf);
                        //ajout dans la liste des total par type client
                        typeClientResults.add(object);

                        //reinitialisation  des donnée dans la liste des type clients
                        typeClient = listeArrival.getTypeClient();
                        nbChambre = listeArrival.getNbChambre();
                        nbChambreConf = listeArrival.getNbChambreConf();
                        nbChambreOpt = listeArrival.getNbChambreOpt();
                        nbPax = listeArrival.getNbPax();
                        dateArrivee = listeArrival.getDateArrivee();
                        dateDepart = listeArrival.getDateDepart();
                        tauxOccTotal = (nbChambre * 100) / (nbrRoom);
                        tauxOccConf = (nbChambreConf * 100) / (nbrRoom);
                        totalTauxOccTotal = new BigDecimal(tauxOccTotal);
                        totalTauxOccConf = new BigDecimal(tauxOccConf);

                        totalMoisTauxOccTotal = totalMoisTauxOccTotal.add(totalTauxOccTotal);
                        totalMoisTauxOccConf = totalMoisTauxOccConf.add(totalTauxOccConf);
                        totalMoisCa = totalMoisCa.add(totalCa);
                        totalMoisCaPM = totalMoisCaPM.add(totalCaPM);
                        totalMoisCaPar = totalMoisCaPar.add(totalCaPar);
                        totalMoisCaTot = totalMoisCaTot.add(totalCaTot);
                        totalMoisNbPax += totalNbPax;
                        totalMoisNbChambre += totalNbChambre;
                        totalMoisNbChambreOpt += totalNbChambreOpt;
                        totalMoisNbChambreConf += totalNbChambreConf;

                        //reinitialisation des totals par type client
                        totalCa = new BigDecimal(0);
                        totalCaPM = new BigDecimal(0);
                        totalCaPar = new BigDecimal(0);
                        totalCaTot = new BigDecimal(0);
                        totalNbPax = nbPax;
                        totalNbChambre = nbChambre;
                        totalNbChambreOpt = nbChambreOpt;
                        totalNbChambreConf = nbChambreConf;

                        //creation objet pour la liste par type client
                        var resaList = createObjectForTypeList(listeArrival.getTypeClient(), tauxOccTotal, tauxOccConf, nbPax, nbChambre, nbChambreOpt, nbChambreConf, dateArrivee.toString(), dateDepart.toString());

                        arrivalResults = Json.createArrayBuilder().add(resaList);
                        typeClientInitial = typeClient;
                    }
                } else {
                    totalMoisTauxOccTotal = totalMoisTauxOccTotal.add(totalTauxOccTotal);
                    totalMoisTauxOccConf = totalMoisTauxOccConf.add(totalTauxOccConf);
                    totalMoisCa = totalMoisCa.add(totalCa);
                    totalMoisCaPM = totalMoisCaPM.add(totalCaPM);
                    totalMoisCaPar = totalMoisCaPar.add(totalCaPar);
                    totalMoisCaTot = totalMoisCaTot.add(totalCaTot);
                    totalMoisNbPax += totalNbPax;
                    totalMoisNbChambre += totalNbChambre;
                    totalMoisNbChambreOpt += totalNbChambreOpt;
                    totalMoisNbChambreConf += totalNbChambreConf;

                    object = createTotalObjectForType(typeClient, arrivalResults.build(), totalTauxOccTotal, totalTauxOccConf, totalCa, totalCaPM, totalCaPar, totalCaTot, totalNbPax, totalNbChambre, totalNbChambreOpt, totalNbChambreConf);

                    typeClientResults.add(object);

                    object = createObjectForMonthList(dateInitial, typeClientResults.build(), totalMoisTauxOccTotal, totalMoisTauxOccConf, totalMoisCa, totalMoisCaPM, totalMoisCaPar, totalMoisCaTot, totalMoisNbPax, totalMoisNbChambre, totalMoisNbChambreOpt, totalMoisNbChambreConf);

                    listeByDateMonth.add(object);

                    totalGeneralTauxOccTotal = totalGeneralTauxOccTotal.add(totalMoisTauxOccTotal);
                    totalGeneralTauxOccConf = totalGeneralTauxOccConf.add(totalMoisTauxOccConf);
                    totalGeneralCa = totalGeneralCa.add(totalMoisCa);
                    totalGeneralCaPM = totalGeneralCaPM.add(totalMoisCaPM);
                    totalGeneralCaPar = totalGeneralCaPar.add(totalMoisCaPar);
                    totalGeneralCaTot = totalGeneralCaTot.add(totalMoisCaTot);
                    totalGeneralNbPax += totalMoisNbPax;
                    totalGeneralNbChambre += totalMoisNbChambre;
                    totalGeneralNbChambreOpt += totalMoisNbChambreOpt;
                    totalGeneralNbChambreConf += totalMoisNbChambreConf;

                    totalMoisTauxOccTotal = new BigDecimal(0);
                    totalMoisTauxOccConf = new BigDecimal(0);
                    totalMoisCa = new BigDecimal(0);
                    totalMoisCaPM = new BigDecimal(0);
                    totalMoisCaPar = new BigDecimal(0);
                    totalMoisCaTot = new BigDecimal(0);
                    totalMoisNbPax = 0;
                    totalMoisNbChambre = 0;
                    totalMoisNbChambreOpt = 0;
                    totalMoisNbChambreConf = 0;

                    arrivalResults = Json.createArrayBuilder();
                    typeClientResults = Json.createArrayBuilder();
                    dateInitial = listeArrival.getDateArrivee();
                    typeClientInitial = listeArrival.getTypeClient();

                    if (typeClientInitial.equals(listeArrival.getTypeClient())) {
                        typeClient = listeArrival.getTypeClient();
                        nbChambre = listeArrival.getNbChambre();
                        nbChambreConf = listeArrival.getNbChambreConf();
                        nbChambreOpt = listeArrival.getNbChambreOpt();
                        nbPax = listeArrival.getNbPax();
                        dateArrivee = listeArrival.getDateArrivee();
                        dateDepart = listeArrival.getDateDepart();
                        tauxOccTotal = (nbChambre * 100) / (nbrRoom);
                        tauxOccConf = (nbChambreConf * 100) / (nbrRoom);
                        totalTauxOccTotal = new BigDecimal(tauxOccTotal);
                        totalTauxOccConf = new BigDecimal(tauxOccConf);
                        totalCa = new BigDecimal(0);
                        totalCaPM = new BigDecimal(0);
                        totalCaPar = new BigDecimal(0);
                        totalCaTot = new BigDecimal(0);
                        totalNbPax = nbPax;
                        totalNbChambre = nbChambre;
                        totalNbChambreOpt = nbChambreOpt;
                        totalNbChambreConf = nbChambreConf;

                        var resaList = createObjectForTypeList(typeClient, tauxOccTotal, tauxOccConf, nbPax, nbChambre, nbChambreOpt, nbChambreConf, dateArrivee.toString(), dateDepart.toString());
                        arrivalResults.add(resaList);
                    } else {
                        object = createTotalObjectForType(typeClient, arrivalResults.build(), totalTauxOccTotal, totalTauxOccConf, totalCa, totalCaPM, totalCaPar, totalCaTot, totalNbPax, totalNbChambre, totalNbChambreOpt, totalNbChambreConf);
                        typeClientResults.add(object);

                        typeClient = listeArrival.getTypeClient();
                        nbChambre = listeArrival.getNbChambre();
                        nbChambreConf = listeArrival.getNbChambreConf();
                        nbChambreOpt = listeArrival.getNbChambreOpt();
                        nbPax = listeArrival.getNbPax();
                        dateArrivee = listeArrival.getDateArrivee();
                        dateDepart = listeArrival.getDateDepart();
                        tauxOccTotal = (nbChambre * 100) / (nbrRoom);
                        tauxOccConf = (nbChambreConf * 100) / (nbrRoom);
                        totalTauxOccTotal = new BigDecimal(tauxOccTotal);
                        totalTauxOccConf = new BigDecimal(tauxOccConf);

                        totalMoisTauxOccTotal = totalMoisTauxOccTotal.add(totalTauxOccTotal);
                        totalMoisTauxOccConf = totalMoisTauxOccConf.add(totalTauxOccConf);
                        totalMoisCa = totalMoisCa.add(totalCa);
                        totalMoisCaPM = totalMoisCaPM.add(totalCaPM);
                        totalMoisCaPar = totalMoisCaPar.add(totalCaPar);
                        totalMoisCaTot = totalMoisCaTot.add(totalCaTot);
                        totalMoisNbPax += totalNbPax;
                        totalMoisNbChambre += totalNbChambre;
                        totalMoisNbChambreOpt += totalNbChambreOpt;
                        totalMoisNbChambreConf += totalNbChambreConf;

                        totalCa = new BigDecimal(0);
                        totalCaPM = new BigDecimal(0);
                        totalCaPar = new BigDecimal(0);
                        totalCaTot = new BigDecimal(0);
                        totalNbPax = nbPax;
                        totalNbChambre = nbChambre;
                        totalNbChambreOpt = nbChambreOpt;
                        totalNbChambreConf = nbChambreConf;

                        var resaList = createObjectForTypeList(listeArrival.getTypeClient(), tauxOccTotal, tauxOccConf, nbPax, nbChambre, nbChambreOpt, nbChambreConf, dateArrivee.toString(), dateDepart.toString());

                        arrivalResults = Json.createArrayBuilder().add(resaList);
                        typeClientInitial = typeClient;
                    }
                }
            }

            object = createTotalObjectForType(typeClient, arrivalResults.build(), totalTauxOccTotal, totalTauxOccConf, totalCa, totalCaPM, totalCaPar, totalCaTot, totalNbPax, totalNbChambre, totalNbChambreOpt, totalNbChambreConf);
            totalMoisTauxOccTotal = totalMoisTauxOccTotal.add(totalTauxOccTotal);
            totalMoisTauxOccConf = totalMoisTauxOccConf.add(totalTauxOccConf);
            totalMoisCa = totalMoisCa.add(totalCa);
            totalMoisCaPM = totalMoisCaPM.add(totalCaPM);
            totalMoisCaPar = totalMoisCaPar.add(totalCaPar);
            totalMoisCaTot = totalMoisCaTot.add(totalCaTot);
            totalMoisNbPax += totalNbPax;
            totalMoisNbChambre += totalNbChambre;
            totalMoisNbChambreOpt += totalNbChambreOpt;
            totalMoisNbChambreConf += totalNbChambreConf;

            typeClientResults.add(object);
            object = createObjectForMonthList(dateInitial, typeClientResults.build(), totalMoisTauxOccTotal, totalMoisTauxOccConf, totalMoisCa, totalMoisCaPM, totalMoisCaPar, totalMoisCaTot, totalMoisNbPax, totalMoisNbChambre, totalMoisNbChambreOpt, totalMoisNbChambreConf);
            listeByDateMonth.add(object);

        }

        totalGeneralTauxOccTotal = totalGeneralTauxOccTotal.add(totalMoisTauxOccTotal);
        totalGeneralTauxOccConf = totalGeneralTauxOccConf.add(totalMoisTauxOccConf);
        totalGeneralCa = totalGeneralCa.add(totalMoisCa);
        totalGeneralCaPM = totalGeneralCaPM.add(totalMoisCaPM);
        totalGeneralCaPar = totalGeneralCaPar.add(totalMoisCaPar);
        totalGeneralCaTot = totalGeneralCaTot.add(totalMoisCaTot);
        totalGeneralNbPax += totalMoisNbPax;
        totalGeneralNbChambre += totalMoisNbChambre;
        totalGeneralNbChambreOpt += totalMoisNbChambreOpt;
        totalGeneralNbChambreConf += totalMoisNbChambreConf;

        object = createTotalGeneralObject(listeByDateMonth.build(), totalGeneralTauxOccTotal, totalGeneralTauxOccConf, totalGeneralCa, totalGeneralCaPM, totalGeneralCaPar, totalGeneralCaTot, totalGeneralNbPax, totalGeneralNbChambre, totalGeneralNbChambreOpt, totalGeneralNbChambreConf);

        return object;
    }

    public List<VPmsEditionClotureProvisoire> getAllClosureProvisoire() {
        List<VPmsEditionClotureProvisoire> closureProvisoire = entityManager
                .createQuery("FROM VPmsEditionClotureProvisoire").getResultList();
        return closureProvisoire;
    }

    public List<VPmsEditionClotureDefinitive> getAllClosureDefinitive() {
        List<VPmsEditionClotureDefinitive> closureDefinitive = entityManager
                .createQuery("FROM VPmsEditionClotureDefinitive").getResultList();
        return closureDefinitive;
    }

    public List<TPmsNoteEntete> getDataClosure(String dateSaisie) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPmsNoteEntete  ");
        if (!Objects.isNull(dateSaisie)) {
            stringBuilder.append(" WHERE dateFacture  = '" + dateSaisie + "'");
        }

        List<TPmsNoteEntete> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }

    public List<VComEditionDetailCompte> getAllDetailCompte() {
        List<VComEditionDetailCompte> detailCompte = entityManager.createQuery("FROM VComEditionDetailCompte")
                .getResultList();
        return detailCompte;
    }

    public List<VComEditionAgeeFacture> getAllAgeeFacture() {
        List<VComEditionAgeeFacture> ageeFacture = entityManager.createQuery("FROM VComEditionAgeeFacture")
                .getResultList();
        return ageeFacture;
    }

    public List<VComEditionAgeeArrhe> getAllAgeeArrhe() {
        List<VComEditionAgeeArrhe> ageeArrhe = entityManager.createQuery("FROM VComEditionAgeeArrhe").getResultList();
        return ageeArrhe;
    }

    public List<VComEditionExtraitCompte> getAllExtraitCompte() {
        List<VComEditionExtraitCompte> extraitCompte = entityManager.createQuery("FROM VComEditionExtraitCompte")
                .getResultList();
        return extraitCompte;
    }

    public List<VComEditionFacture> getAllFacture() {
        List<VComEditionFacture> facture = entityManager.createQuery("FROM VComEditionFacture").getResultList();
        return facture;
    }

    public List<VComEditionSituationDebiteur> getAllSituationDebtor() {
        List<VComEditionSituationDebiteur> situationDebtor = entityManager
                .createQuery("FROM VComEditionSituationDebiteur").getResultList();
        return situationDebtor;
    }

    public List<VComEditionReleveSimpleFacture> getAllReleveSimpleFacture() {
        List<VComEditionReleveSimpleFacture> releveFacture = entityManager
                .createQuery("FROM VComEditionReleveSimpleFacture").getResultList();
        return releveFacture;
    }

    public List<VComEditionReleveDetaileFacture> getAllReleveDetaileFacture() {
        List<VComEditionReleveDetaileFacture> releveDetailFacture = entityManager
                .createQuery("FROM VComEditionReleveDetaileFacture").getResultList();
        return releveDetailFacture;
    }

    // RESTAURANT
    public List<VPosEditionNoteSoldeJour> getAllNoteSoldeJour(String dateNote) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPosEditionNoteSoldeJour  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");

        if (!Objects.isNull(dateNote)) {
            stringBuilder.append(" WHERE Date(dateNote) = '" + dateNote + "'");
        } else {
            stringBuilder.append(" WHERE Date(dateNote) = '" + settingData.getValeur() + "'");
        }

        List<VPosEditionNoteSoldeJour> noteSoldeJour = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        return noteSoldeJour;
    }

    public JsonArray getAllJournalOffert(String startDate, String endDate, String service) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPosEditionJournalOffert ");

        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            stringBuilder.append(" WHERE dateNote >= '" + startDate + "' and  dateNote <= '" + endDate + "'");
        } else if (Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            stringBuilder.append(" WHERE dateNote <= '" + endDate + "'");
        } else if (!Objects.isNull(startDate) && Objects.isNull(endDate)) {
            stringBuilder.append("WHERE dateNote <= '" + startDate + "'");
        }

        if (Objects.isNull(startDate) && Objects.isNull(endDate) && !Objects.isNull(service)) {
            stringBuilder.append("WHERE service = '" + service + "'");
        } else if (!Objects.isNull(service)) {
            stringBuilder.append(" and service = '" + service + "'");
        }

        service = (Objects.isNull(service)) ? " SERVICE MIDI ET SOIR "
                : ((service == "S") ? " SERVICE SOIR " : " SERVICE MIDI ");

        List<VPosEditionJournalOffert> journalOffert = entityManager.createQuery(stringBuilder.toString())
                .getResultList();
        var resultArray = Json.createArrayBuilder();
        if (!journalOffert.isEmpty()) {

            LocalDate initDateCreation = journalOffert.get(0).getDateNote();

            var rowArray = Json.createArrayBuilder();
            var total = new BigDecimal("0");

            for (var i = 0; i < journalOffert.size(); i++) {
                var rowOfferObject = Json.createObjectBuilder().add("ordre", journalOffert.get(i).getOrdre())
                        .add("heure", journalOffert.get(i).getHeure()).add("note", journalOffert.get(i).getNote())
                        .add("code", journalOffert.get(i).getCode()).add("libelle", journalOffert.get(i).getLibelle())
                        .add("quantite", journalOffert.get(i).getQte())
                        .add("montant", journalOffert.get(i).getMontant().toString())
                        .add("tva", journalOffert.get(i).getTva().toString())
                        .add("taux_remise", journalOffert.get(i).getTauxRemise().toString())
                        .add("taux_service", journalOffert.get(i).getTauxService().toString())
                        .add("serveur", journalOffert.get(i).getServeur())
                        .add("reglement", journalOffert.get(i).getReglement().toString())
                        .add("compte", journalOffert.get(i).getCompte())
                        .add("chambre", journalOffert.get(i).getChambre())
                        .add("commentaire", journalOffert.get(i).getCommentaire()).build();
                if (initDateCreation.equals(journalOffert.get(i).getDateNote())) {
                    total = total.add(journalOffert.get(i).getMontant());
                    rowArray.add(rowOfferObject);
                } else {
                    var row = Json.createObjectBuilder().add("total", total.toString())
                            .add("date", initDateCreation.toString()).add("service", service).add("offerts", rowArray)
                            .build();
                    resultArray.add(row);
                    rowArray = Json.createArrayBuilder();
                    total = new BigDecimal("0");
                    total = total.add(journalOffert.get(i).getMontant());
                    rowArray.add(rowOfferObject);
                    initDateCreation = journalOffert.get(i).getDateNote();
                }
            }
            var row = Json.createObjectBuilder().add("total", total.toString()).add("date", initDateCreation.toString())
                    .add("service", service).add("offerts", rowArray).build();
            resultArray.add(row);

        }

        return resultArray.build();
    }

    public JsonObject getAllStatistiqueCouvert(String dateBegin, String dateEnd, Integer activity) {
        List<Object[]> statistiqueCouvertList = entityManager
                .createNativeQuery("CALL p_pos_edition_statistique_couvert(:dateBegin,:dateEnd,:activity)")
                .setParameter("dateBegin", LocalDate.parse(dateBegin)).setParameter("dateEnd", LocalDate.parse(dateEnd))
                .setParameter("activity", activity).getResultList();

        var jsonArray = Json.createArrayBuilder();
        var totalPmGlobal = new BigDecimal(0);
        var totalPmMidi = new BigDecimal(0);
        var totalPmSoir = new BigDecimal(0);
        DecimalFormat df = new DecimalFormat("0.00");

        for (Object[] n : statistiqueCouvertList) {
            if (n.length > 1) {
                BigDecimal pmSoir = (n[10] == null) ? new BigDecimal(0.0) : new BigDecimal(n[10].toString());
                BigDecimal pmMidi = (n[9] == null) ? new BigDecimal(0.0) : new BigDecimal(n[9].toString());
                BigDecimal pmGlobal = (n[8] == null) ? new BigDecimal(0.0) : new BigDecimal(n[8].toString());

                var rowJson = Json.createObjectBuilder().add("activite", n[1].toString())
                        .add("dateCouvert", n[0].toString()).add("caGlobal", new BigDecimal(n[2].toString()))
                        .add("caMidi", new BigDecimal(n[3].toString())).add("caSoir", new BigDecimal(n[4].toString()))
                        .add("totalCouvert", Integer.parseInt(n[5].toString()))
                        .add("couvertMidi", Integer.parseInt(n[6].toString()))
                        .add("couvertSoir", Integer.parseInt(n[7].toString())).add("pmGlobal", df.format(pmGlobal))
                        .add("pmMidi", df.format(pmMidi)).add("pmSoir", df.format(pmSoir)).build();

                totalPmGlobal = totalPmGlobal.add(pmGlobal);
                totalPmMidi = totalPmMidi.add(pmMidi);
                totalPmSoir = totalPmSoir.add(pmSoir);

                jsonArray.add(rowJson);
            }
        }
        var tarifApplicableJson = Json.createObjectBuilder().add("statisiqueCouvert", jsonArray.build())
                .add("totalPmGlobal", df.format(totalPmGlobal)).add("totalPmMidi", df.format(totalPmMidi))
                .add("totalPmSoir", df.format(totalPmSoir)).build();

        return tarifApplicableJson;
    }

    public JsonObject getTurnoverJsonObject(String dateBegin, String dateEnd, Integer activity, String midiSoir) {
        var jsonArray = Json.createArrayBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        var totalcaHt = new BigDecimal(0);
        var totalcattc = new BigDecimal(0);
        var totaltxtva = new BigDecimal(0);
        var totalremise = new BigDecimal(0);
        var totaltva = new BigDecimal(0);
        var totaloffert = new BigDecimal(0);
        var totalservice = new BigDecimal(0);
        var caHt = new BigDecimal(0.0);
        var cattc = new BigDecimal(0.0);
        var txtva = new BigDecimal(0.0);
        var tva = new BigDecimal(0.0);
        var remise = new BigDecimal(0.0);
        var offert = new BigDecimal(0.0);
        var service = new BigDecimal(0.0);

        List<Object[]> middayTurnoverObject = entityManager
                .createNativeQuery("CALL p_pos_consolidation_ca(:dateBegin,:dateEnd,:activity,:service)")
                .setParameter("dateBegin", LocalDate.parse(dateBegin)).setParameter("dateEnd", LocalDate.parse(dateEnd))
                .setParameter("activity", activity).setParameter("service", midiSoir).getResultList();

        for (Object[] n : middayTurnoverObject) {
            if (n.length > 1) {
                caHt = (n[1] == null) ? new BigDecimal(0.0) : new BigDecimal(n[1].toString());
                cattc = (n[2] == null) ? new BigDecimal(0.0) : new BigDecimal(n[2].toString());
                txtva = (n[3] == null) ? new BigDecimal(0.0) : new BigDecimal(n[3].toString());
                tva = (n[4] == null) ? new BigDecimal(0.0) : new BigDecimal(n[4].toString());
                remise = (n[5] == null) ? new BigDecimal(0.0) : new BigDecimal(n[5].toString());
                offert = (n[6] == null) ? new BigDecimal(0.0) : new BigDecimal(n[6].toString());
                service = (n[7] == null) ? new BigDecimal(0.0) : new BigDecimal(n[7].toString());

                var rowJson = Json.createObjectBuilder().add("sousfamille", n[0].toString())
                        .add("ca_ht", df.format(caHt)).add("ca_ttc", df.format(cattc)).add("tx_tva", df.format(txtva))
                        .add("tva", df.format(tva)).add("remise", df.format(remise)).add("offert", df.format(offert))
                        .add("service", df.format(service)).build();

                totalcaHt = totalcaHt.add(caHt);
                totalcattc = totalcattc.add(cattc);
                totaltxtva = totaltxtva.add(txtva);
                totaltva = totalremise.add(tva);
                totalremise = totalremise.add(remise);
                totaloffert = totaloffert.add(offert);
                totalservice = totalservice.add(service);

                jsonArray.add(rowJson);
            }
        }
        var consolidationJsonObject = Json.createObjectBuilder().add("consolidation", jsonArray.build())
                .add("totalcaHt", df.format(totalcaHt)).add("totalcattc", df.format(totalcattc))
                .add("totaltxtva", df.format(totaltxtva)).add("totaltva", df.format(totaltva))
                .add("totalremise", df.format(totalremise)).add("totaloffert", df.format(totaloffert))
                .add("totalservice", df.format(totalservice)).build();

        return consolidationJsonObject;
    }

    public JsonObject getConsolidationCashingJsonObject(String dateBegin, String dateEnd, Integer activity) {
        var jsonArray = Json.createArrayBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        var totalcaevening = new BigDecimal(0);
        var totalcamidday = new BigDecimal(0);
        var totalCashing = new BigDecimal(0);
        var camidday = new BigDecimal(0);
        var caevening = new BigDecimal(0);
        var total = new BigDecimal(0);

        List<Object[]> cashingObject = entityManager
                .createNativeQuery("CALL p_pos_consolidation_encaissement(:dateBegin,:dateEnd,:activity)")
                .setParameter("dateBegin", LocalDate.parse(dateBegin)).setParameter("dateEnd", LocalDate.parse(dateEnd))
                .setParameter("activity", activity).getResultList();

        for (Object[] n : cashingObject) {
            if (n.length > 1) {
                camidday = (n[0] == null) ? new BigDecimal(0.0) : new BigDecimal(n[0].toString());
                caevening = (n[1] == null) ? new BigDecimal(0.0) : new BigDecimal(n[1].toString());
                total = (n[2] == null) ? new BigDecimal(0.0) : new BigDecimal(n[2].toString());

                var rowJson = Json.createObjectBuilder().add("libelle", n[3].toString())
                        .add("camidi", df.format(camidday)).add("casoir", df.format(caevening))
                        .add("total", df.format(total)).build();

                totalcaevening = totalcaevening.add(caevening);
                totalcamidday = totalcamidday.add(camidday);
                totalCashing = totalCashing.add(total);

                jsonArray.add(rowJson);
            }
        }

        var consolidationCashingJsonObject = Json.createObjectBuilder()
                .add("consolidationEncaissement", jsonArray.build()).add("totalcasoir", df.format(totalcaevening))
                .add("totalcamidi", df.format(totalcamidday)).add("totalGeneral", df.format(totalCashing)).build();

        return consolidationCashingJsonObject;
    }

    public BigDecimal getNumberOfCoversObject(String dateBegin, String dateEnd, Integer activity, String service) {

        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT SUM(nb_couvert) from t_pos_note_entete  ");
        if (!Objects.isNull(dateBegin)) {
            stringBuilder.append(" where DATE(date_note) > '" + dateBegin + "'");
            isExist = true;
        }

        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" and DATE(date_note) < '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE DATE(date_note) < '" + dateEnd + "'");
            }
        }

        if (activity != 0) {
            if (isExist == true) {
                stringBuilder.append(" and pos_activite_id = " + activity + "");
            } else {
                stringBuilder.append(" WHERE pos_activite_id = " + activity + "");
            }
        }

        if (!Objects.isNull(service)) {
            if (isExist == true) {
                stringBuilder.append(" and service = '" + service + "'");
            } else {
                stringBuilder.append(" WHERE service = '" + service + "'");
            }
        }

        BigDecimal eveningNumberOfCoversObject = (BigDecimal) entityManager.createNativeQuery(stringBuilder.toString())
                .getSingleResult();

        return eveningNumberOfCoversObject;
    }

    public JsonObject getAllConsolidation(String dateBegin, String dateEnd, Integer activity) {
        var consolidationEncaissement = getConsolidationCashingJsonObject(dateBegin, dateEnd, activity);
        var consolidationCaMidi = getTurnoverJsonObject(dateBegin, dateEnd, activity, "M");
        var consolidationCaSoir = getTurnoverJsonObject(dateBegin, dateEnd, activity, "S");
        var totalCouvertmidi = getNumberOfCoversObject(dateBegin, dateEnd, activity, "M");
        var totalCouvertSoir = getNumberOfCoversObject(dateBegin, dateEnd, activity, "S");
        totalCouvertmidi = (totalCouvertmidi == null) ? new BigDecimal("0") : totalCouvertmidi;
        totalCouvertSoir = (totalCouvertSoir == null) ? new BigDecimal("0") : totalCouvertSoir;
        var report = "0";

        var consolidationJsonObject = Json.createObjectBuilder()
                .add("consolidationEncaissement", consolidationEncaissement)
                .add("consolidationCaMidi", consolidationCaMidi).add("consolidationCaSoir", consolidationCaSoir)
                .add("totalCouvertmidi", totalCouvertmidi.intValue()).add("report", report)
                .add("totalCouvertSoir", totalCouvertSoir.intValue()).build();

        return consolidationJsonObject;
    }

    public JsonObject getAllVisualisationModeEncaissement(String dateEncaissement, Integer activity) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPosEditionVisualisationModeEncaissement  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");

        if (!Objects.isNull(dateEncaissement)) {
            stringBuilder.append(" WHERE Date(dateEncaissement) = '" + dateEncaissement + "'");
        } else {
            stringBuilder.append(" WHERE Date(dateEncaissement) = '" + settingData.getValeur() + "'");
        }

        if (activity != 0) {
            if (isExist == true) {
                stringBuilder.append(" AND activiteId = " + activity + "");
            } else {
                stringBuilder.append(" AND activiteId = " + activity + "");
            }
        }

        List<VPosEditionVisualisationModeEncaissement> visualisationModeEncaissement = entityManager
                .createQuery(stringBuilder.toString()).getResultList();

        var visualisationResults = Json.createArrayBuilder();
        var totalGeneralDebitJour = new BigDecimal("0");
        var totalGeneralCreditJour = new BigDecimal("0");
        var totalGeneralSoldeJour = new BigDecimal("0");

        if (visualisationModeEncaissement.size() > 0) {
            VPosEditionVisualisationModeEncaissement valueListeVisualisationModeEnc = visualisationModeEncaissement.get(0);
            String libModeEncInitial = valueListeVisualisationModeEnc.getLibelleModeEncaissement();
            var libelleModeEnc = "";
            var debitJour = new BigDecimal("0");
            var creditJour = new BigDecimal("0");
            var soldeJour = new BigDecimal("0");
            var totalDebitJour = new BigDecimal("0");
            var totalCreditJour = new BigDecimal("0");
            var totalSoldeJour = new BigDecimal("0");

            for (VPosEditionVisualisationModeEncaissement visualModeEnc : visualisationModeEncaissement) {
                if (libModeEncInitial.equals(visualModeEnc.getLibelleModeEncaissement())) {
                    libelleModeEnc = visualModeEnc.getLibelleModeEncaissement();
                    debitJour = visualModeEnc.getDebitJour();
                    creditJour = visualModeEnc.getCreditJour();
                    soldeJour = visualModeEnc.getSoldeJour();
                    totalDebitJour = totalDebitJour.add(visualModeEnc.getDebitJour());
                    totalCreditJour = totalCreditJour.add(visualModeEnc.getCreditJour());
                    totalSoldeJour = totalDebitJour.add(totalCreditJour);
                } else {
                    var object = Json.createObjectBuilder()
                            .add("libelleModeEncaissement", libelleModeEnc)
                            .add("totalDebitJour", totalDebitJour)
                            .add("totalCreditJour", totalCreditJour)
                            .add("totalSoldeJour", totalSoldeJour).build();

                    totalGeneralDebitJour = totalGeneralDebitJour.add(totalDebitJour);
                    totalGeneralCreditJour = totalGeneralCreditJour.add(totalCreditJour);
                    totalGeneralSoldeJour = totalGeneralSoldeJour.add(totalSoldeJour);
                    visualisationResults.add(object);

                    totalDebitJour = new BigDecimal("0");
                    totalCreditJour = new BigDecimal("0");
                    totalSoldeJour = new BigDecimal("0");

                    libelleModeEnc = visualModeEnc.getLibelleModeEncaissement();
                    debitJour = visualModeEnc.getDebitJour();
                    creditJour = visualModeEnc.getCreditJour();
                    soldeJour = visualModeEnc.getSoldeJour();
                    totalDebitJour = totalDebitJour.add(visualModeEnc.getDebitJour());
                    totalCreditJour = totalCreditJour.add(visualModeEnc.getCreditJour());
                    totalSoldeJour = totalDebitJour.add(totalCreditJour);

                    libModeEncInitial = libelleModeEnc;
                }
            }

            var object = Json.createObjectBuilder()
                    .add("libelleModeEncaissement", libelleModeEnc)
                    .add("totalDebitJour", totalDebitJour)
                    .add("totalCreditJour", totalCreditJour)
                    .add("totalSoldeJour", totalSoldeJour).build();

            totalGeneralDebitJour = totalGeneralDebitJour.add(totalDebitJour);
            totalGeneralCreditJour = totalGeneralCreditJour.add(totalCreditJour);
            totalGeneralSoldeJour = totalGeneralSoldeJour.add(totalSoldeJour);

            visualisationResults.add(object);
        }
        var resultsVisualisationModeEnc = Json.createObjectBuilder()
                .add("visualisation", visualisationResults.build())
                .add("totalGeneralDebitJour", totalGeneralDebitJour)
                .add("totalGeneralCreditJour", totalGeneralCreditJour)
                .add("totalGeneralSoldeJour", totalGeneralSoldeJour).build();

        return resultsVisualisationModeEnc;
    }

    public JsonObject getAllCaByActivity(String dateStart, String dateEnd, Integer activity) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPosCa  ");

        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateCa >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateCa <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateCa <= '" + dateEnd + "'");
            }
        }

        if (activity != 0) {
            if (isExist == true) {
                stringBuilder.append(" AND posActiviteId = " + activity + "");
            } else {
                stringBuilder.append(" AND posActiviteId = " + activity + "");
            }
        }

        List<VPosCa> caActivity = entityManager.createQuery(stringBuilder.toString()).getResultList();

        var caActivityResults = Json.createArrayBuilder();
        var totalGeneralMontantCa = new BigDecimal("0");

        if (caActivity.size() > 0) {
            VPosCa valueCaActivity = caActivity.get(0);
            String libActivityInit = valueCaActivity.getLibelleActivite();
            var libelleAcitivite = "";
            var montantCa = new BigDecimal("0");
            var totalMontantCa = new BigDecimal("0");

            for (VPosCa activiteCa : caActivity) {
                if (libActivityInit.equals(activiteCa.getLibelleActivite())) {
                    libelleAcitivite = activiteCa.getLibelleActivite();
                    montantCa = activiteCa.getMontantCa();
                    totalMontantCa = totalMontantCa.add(montantCa);
                } else {
                    var object = Json.createObjectBuilder()
                            .add("libelleActivite", libelleAcitivite)
                            .add("totalMontantCa", totalMontantCa).build();

                    totalGeneralMontantCa = totalGeneralMontantCa.add(totalMontantCa);
                    caActivityResults.add(object);

                    totalMontantCa = new BigDecimal("0");

                    libelleAcitivite = activiteCa.getLibelleActivite();
                    montantCa = activiteCa.getMontantCa();
                    totalMontantCa = totalMontantCa.add(montantCa);

                    libActivityInit = libelleAcitivite;
                }
            }

            var object = Json.createObjectBuilder()
                    .add("libelleActivite", libelleAcitivite)
                    .add("totalMontantCa", totalMontantCa).build();

            totalGeneralMontantCa = totalGeneralMontantCa.add(totalMontantCa);

            caActivityResults.add(object);
        }
        var resultsCaActivity = Json.createObjectBuilder()
                .add("caActivity", caActivityResults.build())
                .add("totalGeneralMontantCa", totalGeneralMontantCa).build();

        return resultsCaActivity;
    }

    public List<VPosEditionCaActivite> getAllCaActivite() {
        List<VPosEditionCaActivite> caActivite = entityManager.createQuery("FROM VPosEditionCaActivite")
                .getResultList();
        return caActivite;
    }

    public JsonArray getAllPrestationVendue(String dateStart, String dateEnd, Integer activity) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPosEditionPrestationVendue  ");

        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE datePresta >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND datePresta <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE datePresta <= '" + dateEnd + "'");
            }
        }

        if (activity != 0) {
            if (isExist == true) {
                stringBuilder.append(" AND idActivite = " + activity + "");
            } else {
                stringBuilder.append(" AND idActivite = " + activity + "");
            }
        }

        stringBuilder.append(" ORDER BY idPrestation ");

        List<VPosEditionPrestationVendue> prestaVendue = entityManager.createQuery(stringBuilder.toString()).getResultList();

        var prestaVendueResults = Json.createArrayBuilder();

        if (prestaVendue.size() > 0) {
            VPosEditionPrestationVendue valueListePrestaVendue = prestaVendue.get(0);
            Integer idProductInit = valueListePrestaVendue.getIdPrestation();
            LocalDate datePresta = null;
            var libelleProduct = "";
            var libelleFamille = "";
            var libelleSousFamille = "";
            var totalQte = 0;
            var totalMontantBrut = new BigDecimal("0");
            var remise = new BigDecimal("0");
            var qteOffert = 0;

            for (VPosEditionPrestationVendue productSolde : prestaVendue) {
                if (idProductInit.equals(productSolde.getIdPrestation())) {
                    datePresta = productSolde.getDatePresta();
                    libelleProduct = productSolde.getLibelle();
                    libelleFamille = productSolde.getFamille();
                    libelleSousFamille = productSolde.getSousFamille();
                    totalQte += productSolde.getQte();
                    totalMontantBrut = totalMontantBrut.add(productSolde.getCaTtcBrut());
                    remise = productSolde.getRemise();
                    qteOffert = productSolde.getQteOffert();
                } else {
                    var object = Json.createObjectBuilder()
                            .add("datePresta", datePresta.toString())
                            .add("famille", libelleFamille)
                            .add("sousFamille", libelleSousFamille)
                            .add("libelle", libelleProduct)
                            .add("qte", totalQte)
                            .add("caTtcBrut", totalMontantBrut)
                            .add("remise", remise)
                            .add("qteOffert", qteOffert).build();

                    prestaVendueResults.add(object);

                    totalQte = 0;
                    totalMontantBrut = new BigDecimal("0");
                    remise = new BigDecimal("0");
                    qteOffert = 0;

                    datePresta = productSolde.getDatePresta();
                    libelleProduct = productSolde.getLibelle();
                    libelleFamille = productSolde.getFamille();
                    libelleSousFamille = productSolde.getSousFamille();
                    totalQte += productSolde.getQte();
                    totalMontantBrut = totalMontantBrut.add(productSolde.getCaTtcBrut());
                    remise = productSolde.getRemise();
                    qteOffert = productSolde.getQteOffert();

                    idProductInit = productSolde.getIdPrestation();
                }
            }

            var object = Json.createObjectBuilder()
                    .add("datePresta", datePresta.toString())
                    .add("famille", libelleFamille)
                    .add("sousFamille", libelleSousFamille)
                    .add("libelle", libelleProduct)
                    .add("qte", totalQte)
                    .add("caTtcBrut", totalMontantBrut)
                    .add("remise", remise)
                    .add("qteOffert", qteOffert).build();

            prestaVendueResults.add(object);
        }

        return prestaVendueResults.build();
    }

    //EDITION COLLECTIVITE
    public JsonArray getEditionFamilyReading(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VCollectiviteLectureFamille  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateNote >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateNote <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateNote <= '" + dateEnd + "'");
            }
        }

        List<VCollectiviteLectureFamille> familyRead = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var familyReadingResults = Json.createArrayBuilder();

        if (familyRead.size() > 0) {
            VCollectiviteLectureFamille valueListFamille = familyRead.get(0);
            String libFamilleInitial = valueListFamille.getLibelle();
            var libFam = "";
            var code = "";
            var qte = 0;
            var montantTtc = new BigDecimal("0");
            var montantHt = new BigDecimal("0");
            var totalQte = 0;
            var totalMontantTtc = new BigDecimal("0");
            var totalMontantHt = new BigDecimal("0");
            var totalMontantHtFive = new BigDecimal("0");
            var totalMontantHtTen = new BigDecimal("0");
            var totalMontantHtTwenty = new BigDecimal("0");
            BigDecimal cent = new BigDecimal("100");
            BigDecimal one = new BigDecimal("1");
            BigDecimal tauxFive = new BigDecimal("5.5");
            BigDecimal tauxTen = new BigDecimal("10.5");
            BigDecimal tauxTwenty = new BigDecimal("20");

            for (VCollectiviteLectureFamille lectureFamille : familyRead) {
                if (libFamilleInitial.equals(lectureFamille.getLibelle())) {
                    libFam = lectureFamille.getLibelle();
                    code = lectureFamille.getCode();
                    qte = lectureFamille.getQte();
                    montantTtc = lectureFamille.getMontantTtc();
                    montantHt = lectureFamille.getMontantHt();
                    totalQte += lectureFamille.getQte();
                    totalMontantTtc = totalMontantTtc.add(lectureFamille.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lectureFamille.getMontantHt());
                } else {
                    totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

                    var object = Json.createObjectBuilder()
                            .add("code", code)
                            .add("libelle", libFam)
                            .add("totalQte", totalQte)
                            .add("totalMontantTtc", totalMontantTtc)
                            .add("totalMontantHt", totalMontantHt)
                            .add("totalMontantHtFive", totalMontantHtFive)
                            .add("totalMontantHtTen", totalMontantHtTen)
                            .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

                    familyReadingResults.add(object);

                    totalQte = 0;
                    totalMontantTtc = new BigDecimal("0");
                    totalMontantHt = new BigDecimal("0");

                    libFam = lectureFamille.getLibelle();
                    code = lectureFamille.getCode();
                    qte = lectureFamille.getQte();
                    montantTtc = lectureFamille.getMontantTtc();
                    montantHt = lectureFamille.getMontantHt();
                    totalQte += lectureFamille.getQte();
                    totalMontantTtc = totalMontantTtc.add(lectureFamille.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lectureFamille.getMontantHt());

                    libFamilleInitial = libFam;
                }
            }
            totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

            var object = Json.createObjectBuilder()
                    .add("code", code)
                    .add("libelle", libFam)
                    .add("totalQte", totalQte)
                    .add("totalMontantTtc", totalMontantTtc)
                    .add("totalMontantHt", totalMontantHt)
                    .add("totalMontantHtFive", totalMontantHtFive)
                    .add("totalMontantHtTen", totalMontantHtTen)
                    .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

            familyReadingResults.add(object);
        }
        return familyReadingResults.build();
    }

    public JsonArray getEditionSousFamilyReading(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VCollectiviteLectureSousFamille  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateNote >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateNote <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateNote <= '" + dateEnd + "'");
            }
        }

        List<VCollectiviteLectureSousFamille> sousFamilyRead = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var sousFamilyReadingResults = Json.createArrayBuilder();

        if (sousFamilyRead.size() > 0) {
            VCollectiviteLectureSousFamille valueListSousFamille = sousFamilyRead.get(0);
            String libSousFamilleInitial = valueListSousFamille.getLibelle();
            var libSousFam = "";
            var code = "";
            var qte = 0;
            var montantTtc = new BigDecimal("0");
            var montantHt = new BigDecimal("0");
            var totalQte = 0;
            var totalMontantTtc = new BigDecimal("0");
            var totalMontantHt = new BigDecimal("0");
            var totalMontantHtFive = new BigDecimal("0");
            var totalMontantHtTen = new BigDecimal("0");
            var totalMontantHtTwenty = new BigDecimal("0");
            BigDecimal cent = new BigDecimal("100");
            BigDecimal one = new BigDecimal("1");
            BigDecimal tauxFive = new BigDecimal("5.5");
            BigDecimal tauxTen = new BigDecimal("10.5");
            BigDecimal tauxTwenty = new BigDecimal("20");

            for (VCollectiviteLectureSousFamille lectureSousFamille : sousFamilyRead) {
                if (libSousFamilleInitial.equals(lectureSousFamille.getLibelle())) {
                    libSousFam = lectureSousFamille.getLibelle();
                    code = lectureSousFamille.getCode();
                    qte = lectureSousFamille.getQte();
                    montantTtc = lectureSousFamille.getMontantTtc();
                    montantHt = lectureSousFamille.getMontantHt();
                    totalQte += lectureSousFamille.getQte();
                    totalMontantTtc = totalMontantTtc.add(lectureSousFamille.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lectureSousFamille.getMontantHt());
                } else {
                    totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

                    var object = Json.createObjectBuilder()
                            .add("code", code)
                            .add("libelle", libSousFam)
                            .add("totalQte", totalQte)
                            .add("totalMontantTtc", totalMontantTtc)
                            .add("totalMontantHt", totalMontantHt)
                            .add("totalMontantHtFive", totalMontantHtFive)
                            .add("totalMontantHtTen", totalMontantHtTen)
                            .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

                    sousFamilyReadingResults.add(object);

                    totalQte = 0;
                    totalMontantTtc = new BigDecimal("0");
                    totalMontantHt = new BigDecimal("0");

                    libSousFam = lectureSousFamille.getLibelle();
                    code = lectureSousFamille.getCode();
                    qte = lectureSousFamille.getQte();
                    montantTtc = lectureSousFamille.getMontantTtc();
                    montantHt = lectureSousFamille.getMontantHt();
                    totalQte += lectureSousFamille.getQte();
                    totalMontantTtc = totalMontantTtc.add(lectureSousFamille.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lectureSousFamille.getMontantHt());

                    libSousFamilleInitial = libSousFam;
                }
            }
            totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

            var object = Json.createObjectBuilder()
                    .add("code", code)
                    .add("libelle", libSousFam)
                    .add("totalQte", totalQte)
                    .add("totalMontantTtc", totalMontantTtc)
                    .add("totalMontantHt", totalMontantHt)
                    .add("totalMontantHtFive", totalMontantHtFive)
                    .add("totalMontantHtTen", totalMontantHtTen)
                    .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

            sousFamilyReadingResults.add(object);
        }
        return sousFamilyReadingResults.build();
    }

    public JsonArray getEditionProductReading(String dateStart, String dateEnd) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VCollectiviteLecturePrestation  ");
        if (!Objects.isNull(dateStart)) {
            stringBuilder.append(" WHERE dateNote >= '" + dateStart + "'");
            isExist = true;
        }
        if (!Objects.isNull(dateEnd)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateNote <= '" + dateEnd + "'");
            } else {
                stringBuilder.append(" WHERE dateNote <= '" + dateEnd + "'");
            }
        }
        List<VCollectiviteLecturePrestation> listeProduct = entityManager.createQuery(stringBuilder.toString())
                .getResultList();

        var productResults = Json.createArrayBuilder();

        if (listeProduct.size() > 0) {
            VCollectiviteLecturePrestation valueListSousFamille = listeProduct.get(0);
            String libProductInitial = valueListSousFamille.getLibellePrestation();
            var libProduct = "";
            var code = "";
            var qte = 0;
            var montantTtc = new BigDecimal("0");
            var montantHt = new BigDecimal("0");
            var totalQte = 0;
            var totalMontantTtc = new BigDecimal("0");
            var totalMontantHt = new BigDecimal("0");
            var totalMontantHtFive = new BigDecimal("0");
            var totalMontantHtTen = new BigDecimal("0");
            var totalMontantHtTwenty = new BigDecimal("0");
            BigDecimal cent = new BigDecimal("100");
            BigDecimal one = new BigDecimal("1");
            BigDecimal tauxFive = new BigDecimal("5.5");
            BigDecimal tauxTen = new BigDecimal("10.5");
            BigDecimal tauxTwenty = new BigDecimal("20");

            for (VCollectiviteLecturePrestation lecturePrestation : listeProduct) {
                if (libProductInitial.equals(lecturePrestation.getLibellePrestation())) {
                    libProduct = lecturePrestation.getLibellePrestation();
                    code = lecturePrestation.getCode();
                    qte = lecturePrestation.getQte();
                    montantTtc = lecturePrestation.getMontantTtc();
                    montantHt = lecturePrestation.getMontantHt();
                    totalQte += lecturePrestation.getQte();
                    totalMontantTtc = totalMontantTtc.add(lecturePrestation.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lecturePrestation.getMontantHt());
                } else {
                    totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
                    totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

                    var object = Json.createObjectBuilder()
                            .add("code", code)
                            .add("libellePrestation", libProduct)
                            .add("totalQte", totalQte)
                            .add("totalMontantTtc", totalMontantTtc)
                            .add("totalMontantHt", totalMontantHt)
                            .add("totalMontantHtFive", totalMontantHtFive)
                            .add("totalMontantHtTen", totalMontantHtTen)
                            .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

                    productResults.add(object);

                    totalQte = 0;
                    totalMontantTtc = new BigDecimal("0");
                    totalMontantHt = new BigDecimal("0");

                    libProduct = lecturePrestation.getLibellePrestation();
                    code = lecturePrestation.getCode();
                    qte = lecturePrestation.getQte();
                    montantTtc = lecturePrestation.getMontantTtc();
                    montantHt = lecturePrestation.getMontantHt();
                    totalQte += lecturePrestation.getQte();
                    totalMontantTtc = totalMontantTtc.add(lecturePrestation.getMontantTtc());
                    totalMontantHt = totalMontantHt.add(lecturePrestation.getMontantHt());

                    libProductInitial = libProduct;
                }
            }
            totalMontantHtFive = totalMontantTtc.divide(tauxFive.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTen = totalMontantTtc.divide(tauxTen.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);
            totalMontantHtTwenty = totalMontantTtc.divide(tauxTwenty.divide(cent).add(one), 2, RoundingMode.HALF_EVEN);

            var object = Json.createObjectBuilder()
                    .add("code", code)
                    .add("libellePrestation", libProduct)
                    .add("totalQte", totalQte)
                    .add("totalMontantTtc", totalMontantTtc)
                    .add("totalMontantHt", totalMontantHt)
                    .add("totalMontantHtFive", totalMontantHtFive)
                    .add("totalMontantHtTen", totalMontantHtTen)
                    .add("totalMontantHtTwenty", totalMontantHtTwenty).build();

            productResults.add(object);
        }
        return productResults.build();
    }

    public List<VCollectiviteValorisationBac> getEditionValorisationBac() {
        List<VCollectiviteValorisationBac> valBac = entityManager.createQuery("FROM VCollectiviteValorisationBac").getResultList();
        return valBac;
    }

    public JsonArray getListAdmissionSubvention() {

        List<VCollectiviteAdmissionSubvention> listeAdmSubv = entityManager.createQuery("FROM VCollectiviteAdmissionSubvention ").getResultList();

        var societeResults = Json.createArrayBuilder();
        var clientResults = Json.createArrayBuilder();
        if (listeAdmSubv.size() > 0) {
            VCollectiviteAdmissionSubvention valueListSociete = listeAdmSubv.get(0);
            Integer idSocieteInitial = valueListSociete.getIdSociete();

            var reference = "";
            var nom = "";
            var prenom = "";
            var societe = "";

            for (VCollectiviteAdmissionSubvention listeClient : listeAdmSubv) {
                if (idSocieteInitial.equals(listeClient.getIdSociete())) {
                    idSocieteInitial = listeClient.getIdSociete();
                    reference = listeClient.getReference();
                    nom = listeClient.getNom();
                    prenom = listeClient.getPrenom();
                    societe = listeClient.getNomSociete();

                    var customerList = Json.createObjectBuilder()
                            .add("idSociete", idSocieteInitial)
                            .add("reference", reference)
                            .add("nom", nom)
                            .add("prenom", prenom)
                            .add("societe", societe).build();
                    clientResults.add(customerList);
                } else {
                    var object = Json.createObjectBuilder().add("societe", societe)
                            .add("listBySociete", clientResults).build();

                    societeResults.add(object);

                    idSocieteInitial = listeClient.getIdSociete();
                    reference = listeClient.getReference();
                    nom = listeClient.getNom();
                    prenom = listeClient.getPrenom();
                    societe = listeClient.getNomSociete();

                    var customerList = Json.createObjectBuilder()
                            .add("idSociete", idSocieteInitial)
                            .add("reference", reference)
                            .add("nom", nom)
                            .add("prenom", prenom)
                            .add("societe", societe).build();

                    clientResults = Json.createArrayBuilder();
                    clientResults.add(customerList);
                    idSocieteInitial = idSocieteInitial;
                }
            }
            var object = Json.createObjectBuilder().add("societe", societe)
                    .add("listBySociete", clientResults).build();

            societeResults.add(object);
        }
        return societeResults.build();
    }

    public JsonObject getEditionStatistiqueCAparChambre(String dateBegin, String dateEnd, Locale locale) throws ParseException {

        // construction entête tableau
        String entete = "<thead><tr role=\"row\">";
        int nbMois = 0;
        if (!dateBegin.equals("") && !dateEnd.equals("")) {
            entete += "<th class=\"text-center\">Chambre</th>";
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", locale);
            Date dateFrom = df1.parse(dateBegin);
            Date dateTo = df1.parse(dateEnd);
            DateFormat df2 = new SimpleDateFormat("MMM yyyy", locale);
            Calendar calendar = Calendar.getInstance(locale);
            calendar.setTime(dateFrom);
            String previousMonthYear = df1.format(calendar.getTime());
            String[] tab = previousMonthYear.split("-");
            previousMonthYear = tab[1].concat(tab[0]); // MMyyyy
            String enteteMois = df2.format(calendar.getTime());
            enteteMois = enteteMois.substring(0, 1).toUpperCase() + enteteMois.substring(1).toLowerCase();
            entete = entete.concat("<th class=\"text-center\">" + enteteMois + "</th>");
            nbMois++;
            while ((calendar.getTime()).compareTo(dateTo) < 0) {
                calendar.add(Calendar.DATE, 1);
                String encoursMonthYear = df1.format(calendar.getTime());
                String[] tabs = encoursMonthYear.split("-");
                encoursMonthYear = tabs[1].concat(tabs[0]); // MMyyyy
                if (!previousMonthYear.equals(encoursMonthYear)) {
                    enteteMois = df2.format(calendar.getTime());
                    enteteMois = enteteMois.substring(0, 1).toUpperCase() + enteteMois.substring(1).toLowerCase();
                    entete = entete.concat("<th class=\"text-center\">" + enteteMois + "</th>");
                    previousMonthYear = encoursMonthYear;
                    nbMois++;
                }
            }
        }
        if (nbMois > 1) {
            entete = entete.concat("<th class=\"text-center\">Total</th></tr></thead>");
        } else {
            entete = entete.concat("</tr></thead>");
        }
        
        // construction liste enregistrements
        BigDecimal totals = BigDecimal.ZERO;
        String body = "<tbody>";
        String footer = "";
        StringBuilder hql = new StringBuilder();
        hql.append("from VPmsCa where 1 = 1");
        
        if (!dateBegin.equals("") && !dateEnd.equals("")) {
            
            hql.append(" and dateCa >= '" + dateBegin + "' and dateCa <= '" + dateEnd + "'");
            hql.append(" order by dateCa, numeroChambre");
            List<VPmsCa> listVPmsCa = entityManager.createQuery(hql.toString()).getResultList();
            
            if (!listVPmsCa.isEmpty()) {
                
                footer = "<tfoot><tr><td  style=\"font-weight: bold; text-align: right;\">Total :</td>";
                
                HashMap<String, List> resultat = new HashMap<String, List>(); // groupement par chambre
                TreeMap<String, List> resultatSorted = new TreeMap<>(); // groupement par chambre trié
                
                BigDecimal totalCAchbr = BigDecimal.ZERO;
                
                LocalDate previousDateCA = null;
                String previousChbr = null;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
                
                int moisIndex = 0;

                for (int i = 0; i < listVPmsCa.size(); i++) {

                    VPmsCa pmsCa = listVPmsCa.get(i);
                    LocalDate dateCA = pmsCa.getDateCa();
                    String chbr = pmsCa.getNumeroChambre();

                    if (i == 0) {
                        previousChbr = chbr;
                        previousDateCA = dateCA;
                    }

                    String previousDateCAformatted = previousDateCA.format(dateTimeFormatter);
                    String dateCAformatted = dateCA.format(dateTimeFormatter);
                    boolean sameMonth = previousDateCAformatted.equals(dateCAformatted);
                    boolean sameChbr = previousChbr.equals(chbr);
                    
                    if (!resultat.containsKey(previousChbr)) {
                        resultat.put(previousChbr, new ArrayList<BigDecimal>());
                    }
                    
                    if (sameMonth) {
                        if (sameChbr) {
                            totalCAchbr = totalCAchbr.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
                        } else {
                            List<BigDecimal> listeCAchbr = resultat.get(previousChbr);
                            if (listeCAchbr.isEmpty()) {
                                listeCAchbr.add(totalCAchbr);
                            } else {
                                BigDecimal lastCAchbr = listeCAchbr.get(moisIndex);
                                listeCAchbr.set(moisIndex, lastCAchbr.add(totalCAchbr));
                            }
                            resultat.replace(previousChbr, listeCAchbr);
                            previousChbr = chbr;
                            totalCAchbr = BigDecimal.ZERO;
                            totalCAchbr = totalCAchbr.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
                            if (!resultat.containsKey(previousChbr)) {
                                resultat.put(previousChbr, new ArrayList<BigDecimal>());
                            }
                        }
                    }
                    else {
                        List<BigDecimal> listeCAchbr = resultat.get(previousChbr);
                        BigDecimal lastCAchbr = listeCAchbr.get(moisIndex);
                        listeCAchbr.set(moisIndex, lastCAchbr.add(totalCAchbr));
                        resultat.replace(previousChbr, listeCAchbr);
                        totalCAchbr = BigDecimal.ZERO;
                        totalCAchbr = totalCAchbr.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
                        previousChbr = chbr;
                        previousDateCA = dateCA;
                        moisIndex++;
                        listeCAchbr.set(moisIndex, totalCAchbr);
                    }
                    
                }
                List<BigDecimal> listeCAchbr = resultat.get(previousChbr);
                if (listeCAchbr.isEmpty()) {
                    listeCAchbr.add(totalCAchbr);
                } else {
                    BigDecimal lastCAchbr = listeCAchbr.get(moisIndex);
                    listeCAchbr.set(moisIndex, lastCAchbr.add(totalCAchbr));
                }
                resultat.replace(previousChbr, listeCAchbr);
                resultatSorted.putAll(resultat);
                
                for (Map.Entry<String, List> entry : resultatSorted.entrySet()) {
                    String ligne = "<tr><td>" + entry.getKey() + "</td>";
                    List<BigDecimal> listCAchbr = entry.getValue();
                    for(int c = 0; c < listCAchbr.size(); c++) {
                        ligne += "<td>" + listCAchbr.get(c).toString() + "</td>";
                    }
                    if (listCAchbr.size() < nbMois) {
                        int colonnesManquants = nbMois - listCAchbr.size();
                        for(int cm = 1; cm <= colonnesManquants; cm++) {
                            ligne += "<td></td>";
                        }
                    }
                    if (nbMois > 1) {
                        BigDecimal totalLigne = Util.sumValuesListOfBigDecimals(listCAchbr);
                        ligne += "<td>" + totalLigne.toString() + "</td>";
                        totals = totals.add(totalLigne);
                    }
                    ligne += "</tr>";
                    body += ligne;
                }
                
                for(int m = 0; m <= moisIndex; m++) {
                    BigDecimal total = BigDecimal.ZERO;
                    for (Map.Entry<String, List> entry : resultatSorted.entrySet()) {
                        List<BigDecimal> CAs = entry.getValue();
                        total = total.add(CAs.get(m));
                    }
                    footer += "<td style=\"padding-top: 8px;padding-right: 8px;padding-bottom: 8px;padding-left: 8px;\">"+total.toString()+"</td>";
                }
                if (moisIndex < nbMois && nbMois > 1) {
                    int colonnesManquants = nbMois - moisIndex;
                    for(int cm = 1; cm <= colonnesManquants; cm++) {
                        if (cm == colonnesManquants) {
                            footer += "<td style=\"padding-top: 8px;padding-right: 8px;padding-bottom: 8px;padding-left: 8px;\">" + totals.toString() + "</td>";
                        } else {
                            footer += "<td></td>";
                        }
                    }
                }
                footer += "</tr></tfoot>";
            }
        }
        body += "</tbody>";

        JsonObject results = Json.createObjectBuilder()
                .add("header", entete)
                .add("body", body)
                .add("footer", footer)
                .build();
        return results;
    }
    
    public JsonObject getStatistiqueCaByFamily(String dateBegin, String dateEnd, Locale locale) throws ParseException {
	
	LinkedList<String> moisAparcourir = new LinkedList<>();
	String entete = "<thead><tr role=\"row\">";
	int nbMois = 0;
	if (!dateBegin.equals("") && !dateEnd.equals("")) {
		entete += "<th class=\"text-center\">FAMILLE C.A</th>";
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd", locale);
		Date dateFrom = df1.parse(dateBegin);
		Date dateTo = df1.parse(dateEnd);
		DateFormat df2 = new SimpleDateFormat("MMM yyyy", locale);
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(dateFrom);
		String previousMonthYear = df1.format(calendar.getTime());
		String[] tab = previousMonthYear.split("-");
		previousMonthYear = tab[1].concat("-").concat(tab[0]); // MM-yyyy
		moisAparcourir.add(previousMonthYear);
		String enteteMois = df2.format(calendar.getTime());
		enteteMois = enteteMois.substring(0, 1).toUpperCase() + enteteMois.substring(1).toLowerCase();
		entete = entete.concat("<th class=\"text-center\">" + enteteMois + "</th>");
		nbMois++;
		while ((calendar.getTime()).compareTo(dateTo) < 0) {
			calendar.add(Calendar.DATE, 1);
			String encoursMonthYear = df1.format(calendar.getTime());
			String[] tabs = encoursMonthYear.split("-");
			encoursMonthYear = tabs[1].concat("-").concat(tabs[0]); // MM-yyyy
			if (!previousMonthYear.equals(encoursMonthYear)) {
				enteteMois = df2.format(calendar.getTime());
				enteteMois = enteteMois.substring(0, 1).toUpperCase() + enteteMois.substring(1).toLowerCase();
				entete = entete.concat("<th class=\"text-center\">" + enteteMois + "</th>");
				previousMonthYear = encoursMonthYear;
				moisAparcourir.add(previousMonthYear);
				nbMois++;
			}
		}
	}
	if (nbMois > 1) {
		entete = entete.concat("<th class=\"text-center\">Total</th></tr></thead>");
	} else {
		entete = entete.concat("</tr></thead>");
	}
	
	// construction liste enregistrements
	BigDecimal totals = BigDecimal.ZERO;
	String body = "<tbody>";
	String footer = "";
	StringBuilder hql = new StringBuilder();
	hql.append("from VPmsCa where 1 = 1");
	
	if (!dateBegin.equals("") && !dateEnd.equals("")) {
		
		hql.append(" and dateCa >= '" + dateBegin + "' and dateCa <= '" + dateEnd + "'");
		hql.append(" order by dateCa, idFamille");
		List<VPmsCa> listVPmsCa = entityManager.createQuery(hql.toString()).getResultList();
		
		if (!listVPmsCa.isEmpty()) {
			
			footer = "<tfoot><tr><td  style=\"font-weight: bold; text-align: right;\">Total :</td>";
			
			HashMap<String, TreeMap> resultat = new HashMap<>(); // groupement par nationalite
			TreeMap<String, TreeMap> resultatSorted = new TreeMap<>(); // groupement par nationalite trié
			TreeMap<String, BigDecimal> totalVerticalParMois = new TreeMap<>();
			for(int m = 0 ; m < moisAparcourir.size(); m++) {
				totalVerticalParMois.put(moisAparcourir.get(m), BigDecimal.ZERO);
			}
			
			BigDecimal totalCAFamille = BigDecimal.ZERO;
			
			LocalDate previousDateCA = null;
			String previousDateCAformatted = null;
			String previousFamille = null;
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
			
			int moisIndex = 0;

			for (int i = 0; i < listVPmsCa.size(); i++) {

				VPmsCa pmsCa = listVPmsCa.get(i);
				LocalDate dateCA = pmsCa.getDateCa();
				String famille = pmsCa.getLibelleFamille();

				if (i == 0) {
					previousFamille = famille;
					previousDateCA = dateCA;
				}

				previousDateCAformatted = previousDateCA.format(dateTimeFormatter);
				String dateCAformatted = dateCA.format(dateTimeFormatter);
				boolean sameMonth = previousDateCAformatted.equals(dateCAformatted);
				boolean sameFamille = previousFamille.equals(famille);
				
				if (!resultat.containsKey(previousFamille)) {
					TreeMap<String, BigDecimal> tmp = new TreeMap<>();
					for(int m = 0 ; m < moisAparcourir.size(); m++) {
						tmp.put(moisAparcourir.get(m), BigDecimal.ZERO);
					}
					resultat.put(previousFamille, tmp);
				}
				
				if (sameMonth) {
					if (sameFamille) {
						totalCAFamille = totalCAFamille.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
					} else {
						TreeMap<String, BigDecimal> listeCAFamille = resultat.get(previousFamille);
						BigDecimal lastCAFamille = listeCAFamille.get(previousDateCAformatted);
						listeCAFamille.replace(previousDateCAformatted, lastCAFamille.add(totalCAFamille));
						resultat.replace(previousFamille, listeCAFamille);
						previousFamille = famille;
						totalCAFamille = BigDecimal.ZERO;
						totalCAFamille = totalCAFamille.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
						if (!resultat.containsKey(previousFamille)) {
							TreeMap<String, BigDecimal> tmp = new TreeMap<>();
							for(int m = 0 ; m < moisAparcourir.size(); m++) {
								tmp.put(moisAparcourir.get(m), BigDecimal.ZERO);
							}
							resultat.put(previousFamille, tmp);
						}
					}
				}
				else {
					TreeMap<String, BigDecimal> listeCAFamille = resultat.get(previousFamille);
					BigDecimal lastCAFamille = listeCAFamille.get(previousDateCAformatted);
					listeCAFamille.replace(previousDateCAformatted, lastCAFamille.add(totalCAFamille));
					resultat.replace(previousFamille, listeCAFamille);
					totalCAFamille = BigDecimal.ZERO;
					totalCAFamille = totalCAFamille.add(pmsCa.getMontantCa() == null ? BigDecimal.ZERO : pmsCa.getMontantCa());
					previousFamille = famille;
					previousDateCA = dateCA;
					previousDateCAformatted = previousDateCA.format(dateTimeFormatter);
					moisIndex++;
					listeCAFamille = resultat.get(previousFamille);
					lastCAFamille = listeCAFamille.get(previousDateCAformatted);
					listeCAFamille.replace(previousDateCAformatted, lastCAFamille.add(totalCAFamille));
				}
				
			}
			TreeMap<String, BigDecimal> listeCAFamille = resultat.get(previousFamille);
			BigDecimal lastCAFamille = listeCAFamille.get(previousDateCAformatted);
			listeCAFamille.replace(previousDateCAformatted, lastCAFamille.add(totalCAFamille));
			resultat.replace(previousFamille, listeCAFamille);
			
			resultatSorted.putAll(resultat);
			
			for (Map.Entry<String, TreeMap> entry : resultatSorted.entrySet()) {
				String ligne = "<tr><td class=\"text-center\">" + entry.getKey() + "</td>";
				TreeMap<String, BigDecimal> listCAnationalite = entry.getValue();
				for (Map.Entry<String, BigDecimal> entri : listCAnationalite.entrySet()) {
					ligne += "<td class=\"text-center\">" + entri.getValue().toString() + "</td>";
					totalVerticalParMois.replace(entri.getKey(), totalVerticalParMois.get(entri.getKey()).add(entri.getValue()));
				}
				if (nbMois > 1) {
					BigDecimal totalLigne = Util.sumValuesListOfBigDecimal(listCAnationalite);
					ligne += "<td class=\"text-center\">" + totalLigne.toString() + "</td>";
					totals = totals.add(totalLigne);
				}
				ligne += "</tr>";
				body += ligne;
			}
			
			for(int mois = 0; mois < moisAparcourir.size(); mois++) {
				footer += "<td style=\"padding-top: 8px;padding-right: 8px;padding-bottom: 8px;padding-left: 8px;\" class=\"text-center\">"+totalVerticalParMois.get(moisAparcourir.get(mois)).toString()+"</td>";
			}
			if (nbMois > 1) {
				footer += "<td style=\"padding-top: 8px;padding-right: 8px;padding-bottom: 8px;padding-left: 8px;\" class=\"text-center\">" + totals.toString() + "</td>";
			}
			footer += "</tr></tfoot>";
		}
	}
	body += "</tbody>";

	JsonObject results = Json.createObjectBuilder()
			.add("header", entete)
			.add("body", body)
			.add("footer", footer)
			.build();
	return results;
}

}
