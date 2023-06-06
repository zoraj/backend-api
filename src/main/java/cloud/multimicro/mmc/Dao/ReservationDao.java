/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.json.JsonArray;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.jboss.logging.Logger;

import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsReservation;
import cloud.multimicro.mmc.Entity.TPmsReservationTarif;
import cloud.multimicro.mmc.Entity.TPmsReservationTarifPrestation;
import cloud.multimicro.mmc.Entity.TPmsReservationVentilation;
import cloud.multimicro.mmc.Entity.VPmsReservationVentilation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Util.NullAwareBeanUtilsBean;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;


/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class ReservationDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Inject
    SettingDao settingDao;

    private static final Logger LOGGER = Logger.getLogger(ReservationDao.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private String getNextReservationNumber() {
        // Get the current reservation index
        TMmcParametrage parametrage = entityManager.find(TMmcParametrage.class, "ACTUAL_RESERVATION_INDEX");
        int value = Integer.parseInt(parametrage.getValeur()) + 1;

        // Get the current date
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        return settingData.getValeur().replace("-", "") + String.format("%011d", value);
    }

    // RESERVATION
    public List<TPmsReservation> getAll(String arrival, String departure, String name, String numbooking, Integer canceled) {
        List<TPmsReservation> reservationList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPmsReservation WHERE ");
        if (!Objects.isNull(name)) {
            stringBuilder.append(" nomReservation LIKE '%" + name + "%'");
            isExist = true;
        }

        if (!Objects.isNull(arrival)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateArrivee = '" + arrival + "'");
            } else {
                stringBuilder.append(" dateArrivee = '" + arrival + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(departure)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart = '" + departure + "'");
            } else {
                stringBuilder.append(" dateDepart = '" + departure + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(numbooking)) {
            if (isExist == true) {
                stringBuilder.append(" AND numeroReservation = '" + numbooking + "'");
            } else {
                stringBuilder.append(" numeroReservation = '" + numbooking + "'");
                isExist = true;
            }
        }
        
        if (!Objects.isNull(canceled)) {
            if (canceled != 0) {
                if (isExist == true) {
                    stringBuilder.append(" AND (dateArrivee >= '" + dateLogicielle + "' OR dateArrivee < '" + dateLogicielle + "') AND dateDepart >= '" + dateLogicielle + "' AND (dateDeletion IS not null OR dateDeletion IS null) ");
                } else {
                    stringBuilder.append(" (dateArrivee >= '" + dateLogicielle + "' OR dateArrivee < '" + dateLogicielle + "') AND dateDepart >= '" + dateLogicielle + "' AND (dateDeletion IS not null OR dateDeletion IS null)");
                    isExist = true;
                }
            }
        }
        
        if(Objects.isNull(name) && Objects.isNull(arrival) && Objects.isNull(departure) && Objects.isNull(numbooking) && Objects.isNull(canceled)){
            stringBuilder.append(" (dateArrivee >= '" + dateLogicielle + "' OR dateArrivee < '" + dateLogicielle + "') AND dateDepart >= '" + dateLogicielle + "' ");
            isExist = true;
        }
        
        if (Objects.isNull(canceled)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDeletion IS null ");
            } else {
                stringBuilder.append(" dateDeletion IS null ");
            }
        }

        reservationList = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return reservationList;
    }
    
    public List<TPmsReservation> getReservationPlanning(String arrival) {
        List<TPmsReservation> result = entityManager.createQuery("FROM TPmsReservation WHERE (dateArrivee >= '" + arrival + "' OR dateArrivee < '" + arrival + "') AND dateDepart >= '" + arrival + "' AND dateDeletion IS null ").getResultList();
        return result;
    }

    public TPmsReservation getResaById(int id) {
        TPmsReservation pmsReservation = entityManager.find(TPmsReservation.class, id);
        return pmsReservation;
    }

    public TPmsReservation add(JsonObject object) throws CustomConstraintViolationException {

        try (Jsonb jsonb = JsonbBuilder.create()) { // Object mapping
            TPmsReservation reservation = jsonb.fromJson(object.toString(), TPmsReservation.class);
            // Global setting - Get the actual date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalTime currentTime = LocalTime.now();
            TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
            LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
            LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);

            if(dateLogicielle.isBefore(reservation.getDateArrivee()) && reservation.getDateArrivee().isBefore(reservation.getDateDepart()) || dateLogicielle.equals(reservation.getDateArrivee())) {
                // Get the next reservation number
                String numeroReservation = getNextReservationNumber();
                reservation.setNumeroReservation(numeroReservation);

                // Global setting - Get nb current brigade
                settingData = entityManager.find(TMmcParametrage.class, "NB_BRIGADE");
                Integer nbBrigade = Integer.valueOf(settingData.getValeur());
                if (nbBrigade > 1) {
                    settingData = entityManager.find(TMmcParametrage.class, "NUM_CURRENT_BRIGADE");
                    Integer currentBrigade = Integer.valueOf(settingData.getValeur());
                    reservation.setBrigade(currentBrigade);
                }
                else {
                    reservation.setBrigade(1); // Default Brigade is one, if none is set
                }

                // If from booking engine
                if (reservation.getOrigine().equals("BOOKING")) {
                    settingData = entityManager.find(TMmcParametrage.class, "BOOKING_CLIENT_ID");
                    reservation.setMmcClientId(Integer.valueOf(settingData.getValeur()));
                    // Retrieve informations about the client
                    TMmcClient client = entityManager.find(TMmcClient.class, Integer.parseInt(settingData.getValeur()));
                    reservation.setMmcSegmentClientId(client.getMmcSegmentClientId());
                    reservation.setMmcTypeClientId(client.getMmcTypeClientId());
                    reservation.setPmsPrescripteurId(client.getPmsPrescripteurId());
                }
                
                reservation.setDateSaisie(dateTimeLogicielle);
                entityManager.persist(reservation);

                // We need to increment current reservation index after persisting the new reservation
                TMmcParametrage parametrage = entityManager.find(TMmcParametrage.class, "ACTUAL_RESERVATION_INDEX");
                int value = Integer.parseInt(parametrage.getValeur()) + 1;
                parametrage.setValeur(Integer.toString(value));
                entityManager.merge(parametrage);
            }
            else{
                throw new CustomConstraintViolationException("arrival date must be before departure");
            }
            return reservation;
        }
        catch(Exception ex){
            throw new CustomConstraintViolationException(ex.getMessage());
        }

        /*
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Date dateOption = null;
        try {

            if (dateLogicielle.before(dateArrivee) && dateArrivee.before(dateDepart) && !Objects.isNull(numeroNewResa)) {
                try {
                    TMmcClient client = entityManager.find(TMmcClient.class, Integer.parseInt(settingData.getValeur()));
                    List<Map> reservationTarif = (List) object.get("reservationTarif");
                    List<Map> ventilationList = (List) object.get("ventilation");

                    TMmcParametrage bookingRateGridParameterId = entityManager.find(TMmcParametrage.class, "BOOKING_GRILLE_TARIF");
                    reservation.setPmsTarifGrilleId(Integer.parseInt(bookingRateGridParameterId.getValeur()));
                    reservation.setDateArrivee(dateArrivee);
                    reservation.setDateDepart(dateDepart);
                    reservation.setDateOption(dateLogicielle);
                    reservation.setNomReservation(object.getString("nomReservation"));
                    Integer roomNumber = Integer.parseInt(object.getString("nbChambre"));
                    reservation.setNbChambre(roomNumber);
                    Integer paxNumber = Integer.parseInt(object.getString("nbPax"));
                    reservation.setNbPax(paxNumber);
                    Integer nbEnfant = object.containsKey("nbEnfant") ? Integer.parseInt(object.getString("nbEnfant")) : 0;
                    reservation.setNbEnfant(nbEnfant);
                    reservation.setReservationType(object.getString("reservationType"));
                    reservation.setNationalite(object.getString("nationalite"));
                    reservation.setCivilite(object.getString("civilite"));
                    reservation.setNom(object.getString("nom"));
                    reservation.setPrenom(object.getString("prenom"));
                    reservation.setAdresse1(object.getString("adresse1"));
                    reservation.setAdresse2(object.getString("adresse2"));
                    reservation.setTel(object.getString("tel"));
                    reservation.setEmail(object.getString("email"));
                    reservation.setCp(object.getString("cp"));
                    reservation.setVille(object.getString("ville"));

                    // Payment card infos
                    reservation.setCbType(object.getString("cbType"));
                    reservation.setCbNumero(object.getString("cbNumero"));
                    reservation.setCbTitulaire(object.getString("cbTitulaire"));
                    reservation.setCbCvv(object.getString("cbCvv"));
                    Date cbExpiration = format.parse(object.getString("cbExp"));
                    reservation.setCbExp(cbExpiration);

                    reservation.setOrigine(object.getString("origine"));
                    reservation.setMmcSegmentClientId(client.getMmcSegmentClientId());
                    reservation.setPmsPrescripteurId(client.getPmsPrescripteurId());
                    reservation.setMmcTypeClientId(client.getMmcTypeClientId());
                    reservation.setMmcClientId(Integer.parseInt(settingData.getValeur()));
                    reservation.setNumeroReservation(numeroNewResa);
                    reservation.setPays(object.getString("pays"));
                    reservation.setObservation(object.getString("observation"));
                    reservation.setPosteUuid(object.getString("posteUuid"));

                    entityManager.persist(reservation);
                    //addReservationVentilation(ventilationList, reservation.getId());
                    //addReservationRate(reservationTarif, reservation.getNom(), dateArrivee, dateDepart, reservation.getId());
                } catch (ConstraintViolationException ex) {
                    throw new CustomConstraintViolationException(ex);
                }
            } else {
                throw new DataException("Software date must be before arrival date and departure date");
            }
        } catch (ParseException ex) {

        }
        */
    }

    public TPmsReservation update(TPmsReservation newReservationData) throws CustomConstraintViolationException {
        try {
            TPmsReservation oldReservation = entityManager.find(TPmsReservation.class, newReservationData.getId());
            BeanUtilsBean reservation = new NullAwareBeanUtilsBean();
            reservation.copyProperties(oldReservation, newReservationData);
            return entityManager.merge(oldReservation);
        }
        catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void delete(int id,String motifAnnulation) {
        entityManager.createNativeQuery("UPDATE t_pms_reservation SET motif_annulation =:motifAnnulation, date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id)
                .setParameter("motifAnnulation", motifAnnulation).executeUpdate();
    }
    
    public void reopenReservation(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_reservation SET date_deletion = null WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // CRUD RESERVATION VENTILATION
    public List<TPmsReservationVentilation> getAllReservationVentilation() {
        List<TPmsReservationVentilation> pmsReservationVentilation = entityManager
                .createQuery("FROM TPmsReservationVentilation  WHERE dateDeletion = null").getResultList();
        return pmsReservationVentilation;
    }

    public List<TPmsReservationVentilation> getReservationVentilationByReservation(Integer pmsReservationId) {
        List<TPmsReservationVentilation> reservationVentilation = entityManager
                .createQuery("FROM TPmsReservationVentilation  WHERE pmsReservationId=:pmsReservationId")
                .setParameter("pmsReservationId", pmsReservationId).getResultList();
        return reservationVentilation;
    }
    
    public List<VPmsReservationVentilation> getVentilationByReservation(Integer pmsReservationId) {
        List<VPmsReservationVentilation> reservationVentilation = entityManager
                .createQuery("FROM VPmsReservationVentilation  WHERE idReservation=:pmsReservationId")
                .setParameter("pmsReservationId", pmsReservationId).getResultList();
        return reservationVentilation;
    }

    public TPmsReservationVentilation getReservationVentilationById(int id) {
        TPmsReservationVentilation reservationVentilation = entityManager.find(TPmsReservationVentilation.class,
                id);
        return reservationVentilation;
    }

    public void addReservationVentilation(JsonObject object) throws CustomConstraintViolationException {
        Integer pmsReservationId = 0;
        if(object.containsKey("pmsReservationId")){
            pmsReservationId = object.getInt("pmsReservationId");
        }
        JsonArray jsonArray = object.getJsonArray("ventilation");
        for (int i = 0; i < jsonArray.size(); i++) {
            var reservationVentilation = new TPmsReservationVentilation();
            JsonObject rowObject = jsonArray.getJsonObject(i);
            if(!object.containsKey("pmsReservationId")){
                reservationVentilation.setPmsReservationId(getLastIdReservation());
            }else{
                reservationVentilation.setPmsReservationId(pmsReservationId);
            }
            reservationVentilation.setPmsTypeChambreId(rowObject.getInt("pmsTypeChambreId"));
            if(!rowObject.containsKey("pmsChambreId")){
                reservationVentilation.setPmsChambreId(reservationVentilation.getPmsChambreId());
            }else{
                reservationVentilation.setPmsChambreId(rowObject.getInt("pmsChambreId"));
            }

            try {
                entityManager.persist(reservationVentilation);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }
    
    public Integer getLastIdReservation() {
        return (Integer) entityManager.createNativeQuery("select max(id) from t_pms_reservation")
                .getSingleResult();
    }

    public TPmsReservationVentilation updateReservationVentilation(
            TPmsReservationVentilation reservationVentilation) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(reservationVentilation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteReservationVentilation(int id) {
        entityManager
                .createNativeQuery(
                        "UPDATE t_pms_reservation_ventilation SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // ReservationTarif
    public List<TPmsReservationTarif> getAllReservationRate() {
        List<TPmsReservationTarif> reservationTarif = entityManager
                .createQuery("FROM TPmsReservationTarif  WHERE dateDeletion = null").getResultList();
        return reservationTarif;
    }

    public TPmsReservationTarif getReservationRateById(int id) {
        TPmsReservationTarif reservationTarif = entityManager.find(TPmsReservationTarif.class, id);
        return reservationTarif;
    }

    /*public void addReservationRate(List<Map> pmsReservationTarifList ,String nom, Date dateStart, Date dateEnd, Integer pmsReservationId) throws CustomConstraintViolationException {
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        for (Map room : pmsReservationTarifList) {
            TPmsReservationTarif object = new TPmsReservationTarif();
            Integer pmsTarifGrilleDetailId = Integer.parseInt(room.get("pmsTarifGrilleDetailId").toString());
            TPmsTarifGrilleDetail pmsGrilleTarifDetail = entityManager.find(TPmsTarifGrilleDetail.class, pmsTarifGrilleDetailId);
            object.setDateDebut(dateStart);
            object.setDateFin(dateEnd);
            object.setPmsReservationId(pmsReservationId);
            object.setNbAdult(Integer.parseInt(room.get("nbAdult").toString()));
            object.setNbEnf(Integer.parseInt(room.get("nbEnf").toString()));
            object.setNom(nom);
            object.setBase(pmsGrilleTarifDetail.getBase());
            object.setPmsTypeChambreId(Integer.parseInt(room.get("pmsTypeChambreId").toString()));
            object.setPmsModelTarifId(pmsGrilleTarifDetail.getPmsModelTarifId());
            try {
                entityManager.persist(object);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }*/
    
    public void addRateReservation(JsonObject object) throws CustomConstraintViolationException {
        JsonArray jsonArray = object.getJsonArray("reservationTarif");
        for (int i = 0; i < jsonArray.size(); i++) {
            var reservationRate = new TPmsReservationTarif();
            JsonObject rowObject = jsonArray.getJsonObject(i);
            if(!rowObject.containsKey("pmsReservationId")){
                reservationRate.setPmsReservationId(getLastIdReservation());
            }else{
                reservationRate.setPmsReservationId(rowObject.getInt("pmsReservationId"));
            }
            reservationRate.setPmsTypeChambreId(rowObject.getInt("pmsTypeChambreId"));
            reservationRate.setNbAdult(rowObject.getInt("nbAdult"));
            reservationRate.setNbEnf(rowObject.getInt("nbEnf"));
            LocalDate dateDebut = LocalDate.parse(rowObject.getString("dateDebut"));
            LocalDate dateFin = LocalDate.parse(rowObject.getString("dateFin"));
            reservationRate.setDateDebut(dateDebut);
            reservationRate.setDateFin(dateFin);
            reservationRate.setBase(rowObject.getInt("base"));
            reservationRate.setPmsTarifGrilleDetailId(rowObject.getInt("pmsTarifGrilleDetailId"));
            if(!rowObject.containsKey("pmsChambreId")){
                reservationRate.setPmsChambreId(reservationRate.getPmsChambreId());
            }else{
                reservationRate.setPmsChambreId(rowObject.getInt("pmsChambreId"));
            }
            if(!rowObject.containsKey("nom")){
                reservationRate.setNom(reservationRate.getNom());
            }else{
                reservationRate.setNom(rowObject.getString("nom"));
            }

            try {
                entityManager.persist(reservationRate);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }

    public TPmsReservationTarif updateReservationRate(TPmsReservationTarif pmsReservationTarif)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsReservationTarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteReservationRate(int id) {
        entityManager
                .createNativeQuery("UPDATE t_pms_reservation_tarif SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }
    
    //ReservationTarifPrestation
    public List<TPmsReservationTarifPrestation> getReservationTarifPrestation() {
        List<TPmsReservationTarifPrestation> reservationTarifPresta = entityManager.createQuery("FROM TPmsReservationTarifPrestation")
                .getResultList();
        return reservationTarifPresta;
    }
    
    public TPmsReservationTarifPrestation getReservationTarifPrestationById(int id) {
        TPmsReservationTarifPrestation reservationTarifPresta = entityManager.find(TPmsReservationTarifPrestation.class, id);
        return reservationTarifPresta;
    }
    
    public void setReservationTarifPrestation(TPmsReservationTarifPrestation reservationTarifPresta) throws CustomConstraintViolationException {
        try {
            reservationTarifPresta.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(reservationTarifPresta);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsReservationTarifPrestation updateReservationTarifPrestation(TPmsReservationTarifPrestation reservationTarifPresta) throws CustomConstraintViolationException {
        try {
            reservationTarifPresta.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            return entityManager.merge(reservationTarifPresta);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
     public List<TPmsReservation> getAllReservationCanceled() {
        List<TPmsReservation> pmsReservationCanceled = entityManager
                .createQuery("FROM TPmsReservation  WHERE dateDeletion IS not null").getResultList();
        return pmsReservationCanceled;
    }
     
    public List<Long> getPreaffectedByTypeChambre(int id, String dateLogiciel) {
        String sql = "select v.id from TPmsReservationVentilation v " +
                     "join TPmsReservation r ON v.pmsReservationId = r.id " +
                     "where v.pmsChambreId is not null and v.pmsTypeChambreId = :id and " +
                     "r.dateArrivee = :dateLogiciel";
        List<Long> ret = entityManager.createQuery(sql)
                .setParameter("id", id)
                .setParameter("dateLogiciel", LocalDate.parse(dateLogiciel))
                .getResultList();
        return ret;
    }

}
