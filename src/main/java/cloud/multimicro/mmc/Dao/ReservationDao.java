/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.json.JsonArray;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.jboss.logging.Logger;

import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsReservation;
import cloud.multimicro.mmc.Entity.TPmsReservationTarif;
import cloud.multimicro.mmc.Entity.TPmsReservationTarifPrestation;
import cloud.multimicro.mmc.Entity.TPmsReservationVentilation;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Util.NullAwareBeanUtilsBean;


/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class ReservationDao {
    @PersistenceContext
    EntityManager entityManager;

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
    public List<TPmsReservation> getAll(String arrival, String departure, String name, String statut, String client,
            String origin, String numbooking, String reservationType) {
        List<TPmsReservation> reservationList = new ArrayList<>();
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TPmsReservation WHERE ");
        if (!Objects.isNull(name)) {
            stringBuilder.append(" nomNote LIKE '" + name.charAt(0) + "%'");
            isExist = true;
        }

        if (!Objects.isNull(arrival)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateArrivee >= '" + arrival + "'");
            } else {
                stringBuilder.append(" dateArrivee >= '" + arrival + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(departure)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateDepart >= '" + departure + "'");
            } else {
                stringBuilder.append(" AND dateDepart >= '" + departure + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(statut)) {
            if (isExist == true) {
                stringBuilder.append(" AND statut = '" + statut + "'");
            } else {
                stringBuilder.append(" statut ='" + statut + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(client)) {
            if (isExist == true) {
                stringBuilder.append(" AND mmcClientId =" + client);
            } else {
                stringBuilder.append(" mmcClientId =" + client);
                isExist = true;
            }
        }

        if (!Objects.isNull(origin)) {
            if (isExist == true) {
                stringBuilder.append(" AND origine = '" + origin + "'");
            } else {
                stringBuilder.append(" origine = '" + origin + "'");
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

        if (!Objects.isNull(reservationType)) {
            if (isExist == true) {
                stringBuilder.append(" AND reservationType = '" + reservationType + "'");
            } else {
                stringBuilder.append(" reservationType = '" + reservationType + "'");
                isExist = true;
            }
        }

        if (isExist == true) {
            stringBuilder.append(" AND dateDeletion IS null ");
        } else {
            stringBuilder.append(" dateDeletion IS null ");
        }

        reservationList = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return reservationList;
    }

    public TPmsReservation getResaById(int id) {
        TPmsReservation pmsReservation = entityManager.find(TPmsReservation.class, id);
        return pmsReservation;
    }

    public TPmsReservation add(JsonObject object) throws CustomConstraintViolationException {

        try (Jsonb jsonb = JsonbBuilder.create()) { // Object mapping
            TPmsReservation reservation = jsonb.fromJson(object.toString(), TPmsReservation.class);
            // Global setting - Get the actual date
            TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
            LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur());

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

    public void delete(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_reservation SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
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

    public TPmsReservationVentilation getReservationVentilationById(int id) {
        TPmsReservationVentilation reservationVentilation = entityManager.find(TPmsReservationVentilation.class,
                id);
        return reservationVentilation;
    }

    public void addReservationVentilation(JsonObject object) throws CustomConstraintViolationException {
        Integer pmsReservationId = object.getInt("pmsReservationId");
        JsonArray jsonArray = object.getJsonArray("ventilation");
        for (int i = 0; i < jsonArray.size(); i++) {
            var reservationVentilation = new TPmsReservationVentilation();
            JsonObject rowObject = jsonArray.getJsonObject(i);
            reservationVentilation.setPmsReservationId(pmsReservationId);
            reservationVentilation.setPmsTypeChambreId(rowObject.getInt("pmsTypeChambreId"));
            if(!object.containsKey("pmsChambreId")){
                reservationVentilation.setPmsChambreId(reservationVentilation.getPmsChambreId());
            }else{
                reservationVentilation.setPmsChambreId(rowObject.getInt("pmsChambreId"));
            }
            // 1 par defaut
            reservationVentilation.setQteChb(1);

            try {
                entityManager.persist(reservationVentilation);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
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

    public void addReservationRate(List<Map> pmsReservationTarifList ,String nom, Date dateStart, Date dateEnd, Integer pmsReservationId) throws CustomConstraintViolationException {
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
            entityManager.persist(reservationTarifPresta);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsReservationTarifPrestation updateReservationTarifPrestation(TPmsReservationTarifPrestation reservationTarifPresta) throws CustomConstraintViolationException {
        try {
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

}
