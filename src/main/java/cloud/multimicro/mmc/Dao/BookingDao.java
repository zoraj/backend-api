/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.RoomByType;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsReservation;
import cloud.multimicro.mmc.Entity.TPmsSejour;
import cloud.multimicro.mmc.Entity.TPmsTypeChambrePhoto;
import cloud.multimicro.mmc.Entity.TarifApplicable;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.NotFoundException;

/**
 * BookingDao
 */
@Stateless
@SuppressWarnings("unchecked")
public class BookingDao {

    @PersistenceContext
    EntityManager entityManager;

    // liste des qtes des chambres par type des chambre
    public List<RoomByType> getAllRoomByType() {
        List<Object[]> roomList = entityManager.createNativeQuery("CALL p_nb_chambre_par_type() ").getResultList();
        List<RoomByType> result = new ArrayList<RoomByType>();
        for (Object[] n : roomList) {
            if (n.length > 1) {
                RoomByType room = new RoomByType();
                room.setQteDispo(Integer.parseInt(n[0].toString()));
                room.setPmsTypeChambreId(Integer.parseInt(n[1].toString()));
                room.setNbChild(Integer.parseInt(n[2].toString()));
                room.setPersMin(Integer.parseInt(n[3].toString()));
                room.setPersMax(Integer.parseInt(n[4].toString()));
                room.setTypeChambre(n[5].toString());
                result.add(room);
            }
        }
        return result;
    }

    // liste des qtes des chambres occupées par type des chambre
    public List<RoomByType> getOccupiedRoomByType(Date dateArrivee, Date dateDepart) {
        List<Object[]> roomList = entityManager
                .createNativeQuery("CALL p_nb_chambre_indispo_par_type(:dateArrivee,:dateDepart)")
                .setParameter("dateArrivee", dateArrivee).setParameter("dateDepart", dateDepart).getResultList();
        List<RoomByType> result = new ArrayList<RoomByType>();
        for (Object[] n : roomList) {
            if (n.length > 1) {
                RoomByType room = new RoomByType();
                if (n[0] != null) {
                    room.setQteDispo(Integer.parseInt(n[0].toString()));
                }
                if (n[1] != null) {
                    room.setPmsTypeChambreId(Integer.parseInt(n[1].toString()));
                }
                if (n[2] != null) {
                    room.setNbChild(Integer.parseInt(n[2].toString()));
                }
                if (n[3] != null) {
                    room.setPersMin(Integer.parseInt(n[3].toString()));
                }
                if (n[4] != null) {
                    room.setPersMax(Integer.parseInt(n[4].toString()));
                }
                if (n[5] != null) {
                    room.setTypeChambre(n[5].toString());
                }
                result.add(room);
            }
        }
        return result;
    }
    
    public List<TPmsTypeChambrePhoto> getRoomTypesImageByRoomType(Integer pmsTypeChambreId) {
        List<TPmsTypeChambrePhoto> result = entityManager
                .createQuery("FROM TPmsTypeChambrePhoto where pmsTypeChambreId=:pmsTypeChambreId")
                .setParameter("pmsTypeChambreId", pmsTypeChambreId).getResultList();
        return result;
    }

    // liste des chambres disponibles
    public List<RoomByType> getAvailableRoomsByType(Date dateArrivee, Date dateDepart, JsonArray requests)
            throws DataException {
        List<RoomByType> roomList = getAllRoomByType();
        List<RoomByType> occupiedRoomList = getOccupiedRoomByType(dateArrivee, dateDepart);
        List<RoomByType> availableRoom = new ArrayList<RoomByType>();
        List<TarifApplicable> tarifApplicables = new ArrayList<TarifApplicable>();
        List<TPmsTypeChambrePhoto> roomPhotoType = new ArrayList<TPmsTypeChambrePhoto>();
        int pmsTypeChambreId = 0;
        int qte = 0;
        int nbPersMax = 0;
        int nbPax = 0;
        boolean validPax = false;
        boolean validChild = false;
        List<Integer> pmsTypeChambreIdList = new ArrayList<Integer>();
        Date dateLogicielle = null;
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");

        try {
            dateLogicielle = format.parse(parametrageDateLogicielle.getValeur());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if ((dateLogicielle.before(dateArrivee) || dateLogicielle.equals(dateArrivee)) == true
                && dateArrivee.before(dateDepart) == true) {
            for (int i = 0; i < roomList.size(); i++) {
                qte = 0;
                for (int j = 0; j < occupiedRoomList.size(); j++) {
                    if (occupiedRoomList.get(j).getPmsTypeChambreId() == roomList.get(i).getPmsTypeChambreId()) {
                        qte = qte + occupiedRoomList.get(j).getQteDispo();
                    }
                }
                RoomByType room = new RoomByType();
                pmsTypeChambreId = roomList.get(i).getPmsTypeChambreId();
                room.setPmsTypeChambreId(pmsTypeChambreId);
                qte = roomList.get(i).getQteDispo() - qte;
                room.setQteDispo(qte);
                room.setQteTotal(roomList.get(i).getQteDispo());
                room.setNbChild(roomList.get(i).getNbChild());
                room.setPersMin(roomList.get(i).getPersMin());
                room.setPersMax(roomList.get(i).getPersMax());
                room.setTypeChambre(roomList.get(i).getTypeChambre());
                tarifApplicables = getApplicableRateRoomType(pmsTypeChambreId, dateArrivee);
                room.setTarif(tarifApplicables);
                roomPhotoType = getRoomTypesImageByRoomType(pmsTypeChambreId);
                room.setRoomPhoto(roomPhotoType);

                for (int j = 0; j < requests.size(); j++) {
                    JsonObject request = requests.getJsonObject(j);
                    qte = request.getInt("qteChb");
                    nbPersMax = request.getInt("nbAdulte") - qte + 1;
                    nbPax = request.getInt("nbAdulte");
                    if (qte == 1) {
                        validPax = ((1 <= room.getPersMin()) && (nbPax >= room.getPersMin())
                                && (nbPax == room.getPersMax()));
                    } else {
                        validPax = ((1 <= room.getPersMin()) && (nbPersMax >= room.getPersMin())
                                && (1 <= room.getPersMax()) && (room.getPersMax() <= nbPersMax));
                    }
                    validChild = ((0 <= room.getNbChild()) && (room.getNbChild()) >= (request.getInt("nbEnfant")));
                    if (validPax == true && validChild == true && room.getQteDispo() >= qte) {
                        if (!pmsTypeChambreIdList.contains(pmsTypeChambreId)) {
                            availableRoom.add(room);
                            pmsTypeChambreIdList.add(pmsTypeChambreId);
                        }
                    }
                }
            }
        } else if (dateArrivee.equals(dateDepart)) {
            throw new DataException("arrival date must be before departure");
        }

        return availableRoom;
    }

    // recuperation des tarif applicable par types des chambres
    public List<TarifApplicable> getApplicableRateRoomType(Integer pmsTypeChambreId, Date arrival) {
        TMmcParametrage bookingRateGridParameterId = entityManager.find(TMmcParametrage.class, "BOOKING_GRILLE_TARIF");
        List<Object[]> tarifApplicableList = entityManager
                .createNativeQuery(
                        "CALL p_pms_model_tarif_par_type_chambre(:pmsTarifGrilleId,:pmsTypeChambreId,:arrival)")
                .setParameter("pmsTypeChambreId", pmsTypeChambreId).setParameter("arrival", arrival)
                .setParameter("pmsTarifGrilleId", Integer.parseInt(bookingRateGridParameterId.getValeur()))
                .getResultList();
        List<TarifApplicable> result = new ArrayList<TarifApplicable>();
        for (Object[] n : tarifApplicableList) {
            if (n.length > 1) {
                if (n[2] != null) {
                    TarifApplicable tarifApplicable = new TarifApplicable();
                    tarifApplicable.setTypeTarifId(Integer.parseInt(n[0].toString()));
                    tarifApplicable.setTypeTarifLibelle(n[1].toString());
                    tarifApplicable.setAmount(new BigDecimal(n[2].toString()));
                    tarifApplicable.setPmsTarifGrilleDetailId(Integer.parseInt(n[3].toString()));
                    result.add(tarifApplicable);
                }
            }
        }
        return result;
    }

    public void checking(String reservationNumber) throws CustomConstraintViolationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime currentTime = LocalTime.now();

        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
        LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);

        try {
            TPmsReservation reservation = (TPmsReservation) entityManager
                    .createQuery("FROM TPmsReservation  WHERE numeroReservation=:reservationNumber")
                    .setParameter("reservationNumber", reservationNumber).getSingleResult();
            TPmsSejour stay = new TPmsSejour();
            String description = ("Réservation à partir du booking: N°").concat(reservation.getNumeroReservation());
            stay.setDescription(description);
            stay.setAdresse(reservation.getAdresse1());
            stay.setAdresseComp(reservation.getAdresse2());
            stay.setCartePaiementCVV(reservation.getCbCvv());
            stay.setCartePaiementExpiration(reservation.getCbExp());
            stay.setCartePaiementNumero(reservation.getCbNumero());
            stay.setCartePaiementTitulaire(reservation.getCbTitulaire());
            stay.setCartePaiementType(reservation.getCbType());
            stay.setCivilite(reservation.getCivilite());
            stay.setCodePostal(reservation.getCp());
            stay.setDateArrivee(reservation.getDateArrivee());
            stay.setDateDepart(reservation.getDateDepart());
            stay.setEmail(reservation.getEmail());
            stay.setMmcClientId(reservation.getMmcClientId());
            stay.setMmcSegmentClientId(reservation.getMmcSegmentClientId());
            stay.setMmcTypeClientId(reservation.getMmcTypeClientId());
            stay.setNbEnfant(reservation.getNbEnfant());
            stay.setNbPax(reservation.getNbPax());
            stay.setNom(reservation.getNom());
            stay.setPrenom(reservation.getPrenom());
            stay.setPays(reservation.getPays());
            stay.setTelMobile(reservation.getTel());
            stay.setVille(reservation.getVille());
            stay.setPmsPrescripteurId(reservation.getPmsPrescripteurId());
            stay.setPmsTarifGrilleId(reservation.getPmsTarifGrilleId());
            stay.setPosteUuid(reservation.getPosteUuid());
            stay.setDateNote(dateTimeLogicielle);
            stay.setBrigade(reservation.getBrigade());
            stay.setStatut("NOTE");

            entityManager.persist(stay);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }
}
