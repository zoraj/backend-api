/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsCategorieChambre;
import cloud.multimicro.mmc.Entity.TPmsChambre;
import cloud.multimicro.mmc.Entity.TPmsChambreHorsService;
import cloud.multimicro.mmc.Entity.TPmsModelTarif;
import cloud.multimicro.mmc.Entity.TPmsTypeChambre;
import cloud.multimicro.mmc.Entity.TPmsTypeChambrePhoto;
import cloud.multimicro.mmc.Entity.TPmsTypeChambreTarifApplicable;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.ConstraintViolationException;

/**
 * RoomDao
 */
@Stateless
@SuppressWarnings("unchecked")
public class RoomDao {

    @PersistenceContext
    EntityManager entityManager;

    // ROOM CATEGORIES
    public List<TPmsCategorieChambre> getRoomCategories() {
        List<TPmsCategorieChambre> roomCategories = entityManager
                .createQuery("FROM TPmsCategorieChambre cc where cc.dateDeletion = null  ORDER BY libelle")
                .getResultList();
        return roomCategories;
    }

    public TPmsCategorieChambre updateRoomCategories(TPmsCategorieChambre pmsTypeChambre)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsTypeChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsCategorieChambre getRoomCategoriesById(int id) {
        TPmsCategorieChambre roomCategories = entityManager.find(TPmsCategorieChambre.class, id);
        return roomCategories;
    }

    public void setRoomCategories(TPmsCategorieChambre categories) throws CustomConstraintViolationException {
        try {
            entityManager.persist(categories);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteRoomCategories(int id) {
        entityManager
                .createNativeQuery("UPDATE t_pms_categorie_chambre SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // ROOM TYPES
    public List<TPmsTypeChambre> getRoomTypes() {
        List<Object[]> roomTypes = entityManager.createQuery(
                "FROM TPmsTypeChambre tc LEFT JOIN TPmsCategorieChambre cc ON tc.pmsCategorieChambreId = cc.id  where tc.dateDeletion = null ")
                .getResultList();

        List<TPmsTypeChambre> result = new ArrayList<TPmsTypeChambre>();

        // Retrieve the corresponding sub-family
        for (Object[] roomtype : roomTypes) {
            if (roomtype.length > 1) {
                TPmsTypeChambre tc = (TPmsTypeChambre) roomtype[0];
                TPmsCategorieChambre cc = (TPmsCategorieChambre) roomtype[1];
                tc.setPmsCategorieChambreLibelle(cc.getLibelle());
                result.add(tc);
            }
        }
        return result;
    }

    public TPmsTypeChambre getRoomTypesById(int id) {
        TPmsTypeChambre roomTypes = entityManager.find(TPmsTypeChambre.class, id);
        return roomTypes;
    }

    public TPmsTypeChambre updateRoomTypes(TPmsTypeChambre pmsTypeChambre) throws CustomConstraintViolationException {
        try {  
                        if(pmsTypeChambre.getIsParDefaut().equals(true)){
                entityManager.createNativeQuery(
                "UPDATE t_pms_type_chambre SET is_par_defaut =null where id != '"+pmsTypeChambre.getId()+"' " )
                .executeUpdate();              
            }    
            return entityManager.merge(pmsTypeChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void setRoomTypes(TPmsTypeChambre typeChambre) throws CustomConstraintViolationException {

        try {
            entityManager.persist(typeChambre);
            if(typeChambre.getIsParDefaut().equals(true)){
                entityManager.createNativeQuery(
                "UPDATE t_pms_type_chambre SET is_par_defaut =null where id <> " + typeChambre.getId())
                .executeUpdate();              
            }                                                        
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteRoomTypes(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_type_chambre SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // ************* TYPE IMAGE *****************
    public TPmsTypeChambrePhoto getRoomTypesImageById(int id) {
        TPmsTypeChambrePhoto roomTypesImage = entityManager.find(TPmsTypeChambrePhoto.class, id);
        return roomTypesImage;
    }

    public void setRoomTypesImage(JsonObject typeImage) throws CustomConstraintViolationException {
        var pmsTypeChambreId = Integer.parseInt(typeImage.get("pmsTypeChambreId").toString());
        var nomStr = typeImage.get("nom").toString();
        String[] stringArray = nomStr.split(",");

        entityManager
                .createNativeQuery("DELETE FROM t_pms_type_chambre_photo WHERE pms_type_chambre_id=:pmsTypeChambreId")
                .setParameter("pmsTypeChambreId", pmsTypeChambreId).executeUpdate();
        var lengthName = nomStr.replaceAll("\"", "").length();
        if (lengthName > 0) {
            try {
                for (var i = 0; stringArray.length > i; i++) {
                    TPmsTypeChambrePhoto roomPicture = new TPmsTypeChambrePhoto();
                    roomPicture.setPmsTypeChambreId(pmsTypeChambreId);
                    roomPicture.setNom(stringArray[i].replaceAll("\"", ""));
                    entityManager.persist(roomPicture);
                }
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }
    
    public void setRoomTypesImages(TPmsTypeChambrePhoto typeChambrePhoto) throws CustomConstraintViolationException {
        try {
            entityManager.persist(typeChambrePhoto);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public List<TPmsTypeChambrePhoto> getRoomTypesImageByRoomType(Integer pmsTypeChambreId) {
        List<TPmsTypeChambrePhoto> result = entityManager
                .createQuery("FROM TPmsTypeChambrePhoto where pmsTypeChambreId=:pmsTypeChambreId")
                .setParameter("pmsTypeChambreId", pmsTypeChambreId).getResultList();
        return result;
    }

    public void deleteRoomTypesImage(int id) {
        TPmsTypeChambrePhoto roomType = getRoomTypesImageById(id);
        entityManager.remove(roomType);
    }
    
    public void deleteImageByRoomTypes(int id) {
        entityManager
                .createNativeQuery("DELETE FROM t_pms_type_chambre_photo WHERE pms_type_chambre_id=:pmsTypeChambreId")
                .setParameter("pmsTypeChambreId", id).executeUpdate();
    }

    // ROOMS
    public List<TPmsChambre> getRooms() {
        List<Object[]> products = entityManager.createQuery(
                "FROM TPmsChambre c LEFT JOIN TPmsTypeChambre tc ON  c.pmsTypeChambreId = tc.id where c.dateDeletion = null")
                .getResultList();
        List<TPmsChambre> result = new ArrayList<TPmsChambre>();

        // Retrieve the corresponding sub-family
        for (Object[] product : products) {
            if (product.length > 1) {
                TPmsChambre c = (TPmsChambre) product[0];
                TPmsTypeChambre tc = (TPmsTypeChambre) product[1];
                c.setPmsTypeChambreLibelle(tc.getLibelle());
                result.add(c);
            }
        }
        return result;
    }

    public TPmsChambre updateRoom(TPmsChambre pmsChambre) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsChambre getRoomsById(int id) {
        TPmsChambre rooms = entityManager.find(TPmsChambre.class, id);
        return rooms;
    }

    public void setRooms(TPmsChambre rooms) throws CustomConstraintViolationException {
        try {
            entityManager.persist(rooms);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteRoom(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_chambre SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // model tarif et option de tarif
    public List<TPmsModelTarif> getModelTarifByTypeChambre(Integer pmsTypeChambreId) {
        List<Integer> rateModelIdList = getRateModelByRoomType(pmsTypeChambreId);
        List<TPmsModelTarif> pmsModelList = getPmsFareModel();
        List<TPmsModelTarif> result = new ArrayList<TPmsModelTarif>();
        boolean isExist = false;
        for (int i = 0; i < pmsModelList.size(); i++) {
            isExist = false;
            for (Integer rate : rateModelIdList) {
                if (Objects.equals(pmsModelList.get(i).getId(), rate)) {
                    isExist = true;
                    pmsModelList.get(i).setChecked(true);
                    result.add(pmsModelList.get(i));
                    break;
                }
            }
            if (isExist == false) {
                pmsModelList.get(i).setChecked(false);
                result.add(pmsModelList.get(i));
            }
        }

        return result;
    }

    // model tarif et pmsTypeChambreId
    public List<Integer> getRateModelByRoomType(Integer pmsTypeChambreId) {
        List<Integer> rateModelList = entityManager.createQuery(
                "SELECT pmsModelTarifId FROM TPmsTypeChambreTarifApplicable  WHERE pmsTypeChambreId =:pmsTypeChambreId")
                .setParameter("pmsTypeChambreId", pmsTypeChambreId).getResultList();
        return rateModelList;
    }
    
    public List<Integer> getRoomTypeByTariffApplicable(Integer pmsModelTarifId) {
        List<Integer> rateModelList = entityManager.createQuery(
                "SELECT pmsTypeChambreId FROM TPmsTypeChambreTarifApplicable  WHERE pmsModelTarifId =:pmsModelTarifId")
                .setParameter("pmsModelTarifId", pmsModelTarifId).getResultList();
        return rateModelList;
    }

    public List<TPmsModelTarif> getPmsFareModel() {
        List<TPmsModelTarif> rateModelList = entityManager
                .createQuery("FROM TPmsModelTarif rate WHERE rate.dateDeletion = null ").getResultList();
        return rateModelList;
    }

    // pmsModelTarifId; pmsTypeChambreId
    public void addRoomTypeApplicableRate(JsonObject object) throws CustomConstraintViolationException {
        Integer pmsTypeChambreId = Integer.parseInt(object.get("pmsTypeChambreId").toString());
        List<Integer> rateToDeleteList = getRateModelByRoomType(pmsTypeChambreId);
        String modelsString = object.get("models").toString();

        if (rateToDeleteList.size() > 0) {
            // supprimer tous les model de tarif de tarif de pmstype chambre Id
            for (Integer rateModelToDelete : rateToDeleteList) {
                deletionApplicableRoomRateModel(rateModelToDelete, pmsTypeChambreId);
            }
        }

        if (!"[]".equals(modelsString)) {
            List<Integer> models = stringToList(modelsString.substring(1, modelsString.length() - 1));
            if (models.size() > 0) {
                // ajouter tous les nouvelles options de tarif de pmsModelTarifId
                models.forEach((Integer model) -> {
                    try {
                        createApplicableRoomRateModel(model, pmsTypeChambreId);
                    } catch (CustomConstraintViolationException ex) {
                        Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
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

    public void createApplicableRoomRateModel(Integer pmsModelTarifId, Integer pmsTypeChambreId)
            throws CustomConstraintViolationException {
        TPmsTypeChambreTarifApplicable pmsModelTarifOption = new TPmsTypeChambreTarifApplicable(pmsModelTarifId,
                pmsTypeChambreId);
        try {
            entityManager.persist(pmsModelTarifOption);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // DELETE
    public void deletionApplicableRoomRateModel(Integer pmsModelTarifId, Integer pmsTypeChambreId) {
        TPmsTypeChambreTarifApplicable pmsTypeChambreTarifApplicable = new TPmsTypeChambreTarifApplicable(
                pmsModelTarifId, pmsTypeChambreId);
        entityManager.remove(entityManager.contains(pmsTypeChambreTarifApplicable) ? pmsTypeChambreTarifApplicable
                : entityManager.merge(pmsTypeChambreTarifApplicable));
    }

    // TPmsChambreHorsService
    public List<TPmsChambreHorsService> getAllTPmsChambreHorsService() {
        List<TPmsChambreHorsService> dataList = entityManager
                .createQuery("FROM TPmsChambreHorsService WHERE dateDeletion = null ").getResultList();
        return dataList;
    }

    public TPmsChambreHorsService getTPmsChambreHorsServiceById(int id) {
        TPmsChambreHorsService data = entityManager.find(TPmsChambreHorsService.class, id);
        return data;
    }

    public void addTPmsChambreHorsService(TPmsChambreHorsService pmsChambreHorsService)
            throws CustomConstraintViolationException, ParseException {
        try {
            entityManager.persist(pmsChambreHorsService);
            putOutService(pmsChambreHorsService);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void putOutService(TPmsChambreHorsService pmsChambreHorsService)
            throws ParseException, CustomConstraintViolationException{
        LocalDate dateLogicielle = null;
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        //final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLogicielle = LocalDate.parse(parametrageDateLogicielle.getValeur(), formatter);
        if (pmsChambreHorsService.getDateDebut().equals(dateLogicielle) 
                || dateLogicielle.isBefore(pmsChambreHorsService.getDateDebut())
                && pmsChambreHorsService.getDateFin().equals(dateLogicielle)
                || dateLogicielle.isBefore(pmsChambreHorsService.getDateFin())) {
            TPmsChambre room = getRoomsById(pmsChambreHorsService.getPmsChambreId());
            room.setEtatChambre("OUT");
            try {
                entityManager.merge(room);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }

    public TPmsChambreHorsService updateTPmsChambreHorsService(TPmsChambreHorsService pmsChambreHorsService)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(pmsChambreHorsService);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteTPmsChambreHorsService(int id) {
        entityManager
                .createNativeQuery(
                        "UPDATE t_pms_chambre_hors_service SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }
    
    //Etat remplissage
    public BigInteger getNbrRoom() {
        return (BigInteger) entityManager.createNativeQuery(
                "select count(*) from t_pms_chambre").getSingleResult();
    }
    
    public BigDecimal getRoomNbReservationIndivClosed(LocalDate dateEffectif) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateEffectif AND date_depart>:dateEffectif AND reservation_type = 'INDIV' ")
                .setParameter("dateEffectif", dateEffectif).getSingleResult();
    }
    
    public BigDecimal getRoomNbReservationIndivOpt(LocalDate dateEffectif) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateEffectif AND date_depart>:dateEffectif AND reservation_type = 'INDIV' AND date_option is not null ")
                .setParameter("dateEffectif", dateEffectif).getSingleResult();
    }
    
    public BigDecimal getRoomNbReservationGroupClosed(LocalDate dateEffectif) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateEffectif AND date_depart>:dateEffectif AND reservation_type = 'GROUPE' ")
                .setParameter("dateEffectif", dateEffectif).getSingleResult();
    }
    
    public BigDecimal getRoomNbReservationGroupOpt(LocalDate dateEffectif) {
        return (BigDecimal) entityManager.createNativeQuery(
                "select sum(nb_chambre) from t_pms_reservation WHERE  date_arrivee<=:dateEffectif AND date_depart>:dateEffectif AND reservation_type = 'GROUPE' AND date_option is not null ")
                .setParameter("dateEffectif", dateEffectif).getSingleResult();
    }
    
    public JsonObject getRoomAvailability(String dateStart, String dateEnd) {
        JsonArrayBuilder resultArrayBuilder = Json.createArrayBuilder();
        LocalDate dateEffectif = null;
        LocalDate dateEndLocalDate = null;
        if (Objects.isNull(dateStart)) {
            dateEffectif = (LocalDate) entityManager.createQuery("select MIN(dateArrivee) from TPmsReservation")
                    .getSingleResult();
        } else {
            dateEffectif = LocalDate.parse(dateStart);
        }
        if (Objects.isNull(dateEnd)) {
            dateEndLocalDate = (LocalDate) entityManager.createQuery("select MAX(dateDepart) from TPmsReservation")
                    .getSingleResult();
        } else {
            dateEndLocalDate = LocalDate.parse(dateEnd);
        }
        
        var stockTotal =  0;
        var indivFermeTotal =  0;
        var indivOptionTotal =  0;
        var groupFermeTotal =  0;
        var groupOptionTotal =  0;
        var totalFermeTotal =  0;
        var totalOptionTotal =  0;
        var totalOccupTotal =  0;
        var tauxOccupFermeTotal =  0;
        var tauxOccupOptTotal =  0;
        var occupationPourcentageTotal =  0;     
        
        while (dateEffectif.compareTo(dateEndLocalDate) <= 0) {         
            BigDecimal nbReservationIndivClosed = getRoomNbReservationIndivClosed(dateEffectif);
            BigDecimal nbReservationIndivOption = getRoomNbReservationIndivOpt(dateEffectif);
            BigDecimal nbReservationGroupClosed = getRoomNbReservationGroupClosed(dateEffectif);
            BigDecimal nbReservationGroupOption = getRoomNbReservationGroupOpt(dateEffectif);
            
            int nbrRoom = getNbrRoom().intValue();
            
            var fermeIndiv = (nbReservationIndivClosed == null) ? 0 : nbReservationIndivClosed.intValue();
            var optionIndiv = (nbReservationIndivOption == null) ? 0 : nbReservationIndivOption.intValue();
            var fermeGroup = (nbReservationGroupClosed == null) ? 0 : nbReservationGroupClosed.intValue();
            var optionGroup = (nbReservationGroupOption == null) ? 0 : nbReservationGroupOption.intValue();
            
            var totalFerme = fermeIndiv + fermeGroup;
            var totalOption = optionIndiv + optionGroup;
            var totalOccup = totalFerme + totalOption;

            var tauxOccupFerme = totalFerme*100/(nbrRoom);
            var tauxOccupOpt = totalOption*100/(nbrRoom);
            var occupationPourcentage = tauxOccupFerme + tauxOccupOpt;
            
            JsonObject value = Json.createObjectBuilder()
                    .add("dateEffectif", dateEffectif.toString())
                    .add("totalStock", nbrRoom)
                    .add("indivFerme", fermeIndiv)
                    .add("indivOption", optionIndiv)
                    .add("groupFerme", fermeGroup)
                    .add("groupOption", optionGroup)
                    .add("totalFerme", totalFerme)
                    .add("totalOption", totalOption)
                    .add("totalOccup", totalOccup)
                    .add("tauxOccupFerme", tauxOccupFerme)
                    .add("tauxOccupOpt", tauxOccupOpt)
                    .add("occupationPourcentage", occupationPourcentage).build();
            stockTotal += nbrRoom;
            indivFermeTotal +=  fermeIndiv;
            indivOptionTotal +=  optionIndiv;
            groupFermeTotal +=  fermeGroup;
            groupOptionTotal +=  optionGroup;
            totalFermeTotal +=  totalFerme;
            totalOptionTotal +=  totalOption;
            totalOccupTotal +=  totalOccup;
            tauxOccupFermeTotal +=  tauxOccupFerme;
            tauxOccupOptTotal +=  tauxOccupOpt;
            occupationPourcentageTotal +=  occupationPourcentage; 
            resultArrayBuilder.add(value);
            dateEffectif = dateEffectif.plus(Period.ofDays(1));
        }
        JsonObject resultJson = Json.createObjectBuilder()
                .add("pmsEditionEtatRemplissage", resultArrayBuilder.build())
                .add("stockTotal", stockTotal)
                .add("indivFermeTotal", indivFermeTotal)
                .add("indivOptionTotal", indivOptionTotal)
                .add("groupFermeTotal", groupFermeTotal)
                .add("groupOptionTotal", groupOptionTotal)
                .add("totalFermeTotal", totalFermeTotal)
                .add("totalOptionTotal", totalOptionTotal)
                .add("totalOccupTotal", totalOccupTotal)
                .add("tauxOccupFermeTotal", tauxOccupFermeTotal)
                .add("tauxOccupOptTotal", tauxOccupOptTotal)
                .add("occupationPourcentageTotal", occupationPourcentageTotal)
                .build();
        return resultJson;
    }
}