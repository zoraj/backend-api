/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPosAccompagnement;
import cloud.multimicro.mmc.Entity.TPosActivite;
import cloud.multimicro.mmc.Entity.TPosActivitePrestation;
import cloud.multimicro.mmc.Entity.TPosCuisson;
import cloud.multimicro.mmc.Entity.TPosHappyHours;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosPrestationGroupe;
import cloud.multimicro.mmc.Entity.TPosSiropParfum;
import cloud.multimicro.mmc.Entity.TPosSiropParfumCategorie;
import cloud.multimicro.mmc.Entity.TPosTarif;
import cloud.multimicro.mmc.Entity.TPosTarifPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.math.BigDecimal;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

/**
 * ProductPosDao
 */
@Stateless
@SuppressWarnings("unchecked")
public class ProductDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Inject
    SettingDao settingDao;

    public List<TPosPrestation> getPosProduct() {
        List<TPosPrestation> products = entityManager
                .createQuery("FROM TPosPrestation  WHERE dateDeletion = null ORDER BY libelle").getResultList();
        return products;
    }

    public List<TPosPrestation> getPosProductByIdGroup(int idGroup) {
        List<TPosPrestation> products = entityManager.createQuery("FROM TPosPrestation "
                + "WHERE dateDeletion = null and posPrestationGroupe.id =:idGroup  " + "ORDER BY libelle")
                .setParameter("idGroup", idGroup).getResultList();
        return products;
    }

    public List<TPosPrestation> getPosProductByIdGroupVae(int idGroup) {
        List<TPosPrestation> products = entityManager.createQuery("FROM TPosPrestation "
                + "WHERE dateDeletion = null and posPrestationGroupe.id =:idGroup  and autoriseVAE = 1 "
                + "ORDER BY libelle").setParameter("idGroup", idGroup).getResultList();
        return products;
    }

    public List<TPosPrestation> getProductOrderVae() {
        List<TPosPrestation> products = entityManager
                .createQuery("FROM TPosPrestation  WHERE autoriseVAE = 1 and dateDeletion is null ORDER BY libelle")
                .getResultList();
        return products;

    }

    public TPosPrestation getPosProductById(int id) {
        TPosPrestation product = entityManager.find(TPosPrestation.class, id);
        return product;
    }

    public void setPosPrestation(TPosPrestation prestation) throws CustomConstraintViolationException {
        try {
            entityManager.persist(prestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void gestionTypeNonFacture(int id, String gestion) throws CustomConstraintViolationException {
        TPosPrestation nonFacture = entityManager.find(TPosPrestation.class, id);
        nonFacture.setGestionType(gestion);
        try {
            entityManager.merge(nonFacture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPosPrestation updatePosPrestation(TPosPrestation prestation) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(prestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePosPrestation(Integer id) throws DataException {
        Query query = entityManager
                .createQuery("SELECT COUNT(T) FROM TPosTarifPrestation T WHERE posPrestationId =:prestationId")
                .setParameter("prestationId", id);
        long productCount = (Long) query.getSingleResult();
        if (productCount > 0) {
            throw new DataException("Data still in use");
        } else {
            entityManager
                    .createNativeQuery(
                            "UPDATE t_pos_prestation SET date_deletion = CURRENT_TIMESTAMP WHERE id=:product")
                    .setParameter("product", id).executeUpdate();
        }
    }

    public List<TPosPrestation> getByNature(String admission, String subvention, int client) {
        List<Object[]> natureProducts = entityManager
                .createQuery("FROM TPosPrestation p "
                        + "LEFT JOIN TMmcClient c ON  (p.code = c.codeSubvention or p.code = c.codeAdmission) "
                        + "WHERE (p.nature =:admission OR p.nature =:subvention) and c.id =:client ")
                .setParameter("admission", admission).setParameter("subvention", subvention)
                .setParameter("client", client).getResultList();

        List<TPosPrestation> result = new ArrayList<TPosPrestation>();

        // Retrieve the corresponding sub-family
        for (Object[] product : natureProducts) {
            if (product.length > 1) {
                TPosPrestation p = (TPosPrestation) product[0];
                result.add(p);
            }
        }
        return result;
    }

    public void setByNature(TPosPrestation prestation) throws CustomConstraintViolationException {
        try {
            entityManager.persist(prestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosPrestation updateByNature(TPosPrestation prestation) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(prestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    

    // Groupe prestation
    public List<TPosPrestationGroupe> getProductGroup() {
        List<TPosPrestationGroupe> productsGroups = entityManager
                .createQuery("FROM TPosPrestationGroupe g WHERE g.dateDeletion = null ORDER BY libelle")
                .getResultList();
        return productsGroups;
    }

    public TPosPrestationGroupe getProductGroupById(int id) {
        TPosPrestationGroupe productGroupe = entityManager.find(TPosPrestationGroupe.class, id);
        return productGroupe;
    }

    public void setProductGroup(TPosPrestationGroupe prestationGroupe) throws CustomConstraintViolationException {
        try {
            entityManager.persist(prestationGroupe);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosPrestationGroupe updateProductGroup(TPosPrestationGroupe groupPrestation)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(groupPrestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteProductGroup(Integer id) throws DataException {
        Query query = entityManager.createQuery(
                "SELECT COUNT(P) FROM TPosPrestation P WHERE posPrestationGroupeId =:posPrestationGroupeId and dateDeletion = null")
                .setParameter("posPrestationGroupeId", id);
        long productsCount = (Long) query.getSingleResult();
        if (productsCount > 0) {
            throw new DataException("Data still in use");
        } else {
            entityManager.createNativeQuery(
                    "UPDATE t_pos_prestation_groupe SET date_deletion = CURRENT_TIMESTAMP WHERE id=:productGroupe")
                    .setParameter("productGroupe", id).executeUpdate();
        }
    }

    // PMS
    public List<TPmsPrestation> getPmsProduct() {
        List<TPmsPrestation> products = entityManager
                .createQuery("FROM TPmsPrestation p where p.dateDeletion = null ORDER BY libelle").getResultList();
        return products;
    }

    // PUT PMS
    public TPmsPrestation updateProduct(TPmsPrestation pmsPrestation) throws CustomConstraintViolationException {
        try {
            pmsPrestation.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            return entityManager.merge(pmsPrestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsPrestation updateProductStop(Integer id) throws CustomConstraintViolationException {
        TPmsPrestation prestationStop = getPmsProductById(id);
        prestationStop.setStatut("STOP");
        try {
            return entityManager.merge(prestationStop);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPmsPrestation updateProductAvail(Integer id) throws CustomConstraintViolationException {
        TPmsPrestation prestationStop = getPmsProductById(id);
        prestationStop.setStatut("AVAIL");
        try {
            return entityManager.merge(prestationStop);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPmsPrestation getPmsProductById(int id) {
        TPmsPrestation product = entityManager.find(TPmsPrestation.class, id);
        return product;
    }

    public void deletePmsProduct(int id) {
        entityManager
                .createNativeQuery("UPDATE t_pms_prestation SET date_deletion = CURRENT_TIMESTAMP WHERE id=:product")
                .setParameter("product", id).executeUpdate();

    }

    public void setProduct(TPmsPrestation prestation) throws CustomConstraintViolationException {
        try {
            entityManager.persist(prestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // TPosSiropParfumCategorie
    public List<TPosSiropParfumCategorie> getSpCategorie() {
        List<TPosSiropParfumCategorie> categorie = entityManager
                .createQuery("FROM TPosSiropParfumCategorie c WHERE c.dateDeletion = null ORDER BY libelle")
                .getResultList();
        return categorie;
    }

    public TPosSiropParfumCategorie getPosSiropParfumCategorieById(int id) {
        TPosSiropParfumCategorie sirupParfume = entityManager.find(TPosSiropParfumCategorie.class, id);
        return sirupParfume;
    }

    public void setSpCategorie(TPosSiropParfumCategorie categorie) throws CustomConstraintViolationException {
        try {
            entityManager.persist(categorie);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosSiropParfumCategorie updateSpCategorie(TPosSiropParfumCategorie categorie)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(categorie);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteSpCategorie(int id) {
        entityManager
                .createNativeQuery(
                        "UPDATE t_pos_sirop_parfum_categorie SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Sirop parfum
    public List<TPosSiropParfum> getSiropParfum() {
        List<Object[]> siropParfum = entityManager.createQuery("FROM TPosSiropParfum s "
                + "LEFT JOIN TPosSiropParfumCategorie c ON  s.posSiropParfumCategorieId = c.id  "
                + "WHERE s.dateDeletion = null " + "ORDER BY s.libelle").getResultList();
        List<TPosSiropParfum> result = new ArrayList<TPosSiropParfum>();

        // Retrieve the corresponding sub-family
        for (Object[] sParfum : siropParfum) {
            if (sParfum.length > 1) {
                TPosSiropParfum sP = (TPosSiropParfum) sParfum[0];
                TPosSiropParfumCategorie c = (TPosSiropParfumCategorie) sParfum[1];
                sP.setPosSiropParfumCategorieLibelle(c.getLibelle());
                result.add(sP);
            }
        }
        return result;
    }

    public TPosSiropParfum getPosSiropParfumById(int id) {
        TPosSiropParfum sirupParfum = entityManager.find(TPosSiropParfum.class, id);
        return sirupParfum;
    }

    public void setSiropParfum(TPosSiropParfum siropParfum) throws CustomConstraintViolationException {
        try {
            entityManager.persist(siropParfum);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosSiropParfum updateSiropParfum(TPosSiropParfum siropParfum) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(siropParfum);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteSiropParfum(int id) {
        TPosSiropParfum sirupParfum = getPosSiropParfumById(id);
        Date dateDel = new Date();
        sirupParfum.setDateDeletion(dateDel);
        entityManager.merge(sirupParfum);
    }

    // Accompagnement
    public List<TPosAccompagnement> getAccompagnement() {
        List<TPosAccompagnement> accompagnement = entityManager
                .createQuery("FROM TPosAccompagnement a WHERE a.dateDeletion = null ORDER BY libelle").getResultList();
        return accompagnement;
    }

    public TPosAccompagnement getAccompagnementById(int id) {
        TPosAccompagnement accompagnement = entityManager.find(TPosAccompagnement.class, id);
        return accompagnement;
    }

    public void setAccompagnement(TPosAccompagnement accompagnement) throws CustomConstraintViolationException {
        try {
            entityManager.persist(accompagnement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosAccompagnement updateAccompagnement(TPosAccompagnement accompagnement)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(accompagnement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteAccompagnement(int id) {
        entityManager
                .createNativeQuery("UPDATE t_pos_accompagnement SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Cuisson
    public List<TPosCuisson> getCuisson() {
        List<TPosCuisson> cuisson = entityManager
                .createQuery("FROM TPosCuisson cs WHERE cs.dateDeletion = null ORDER BY libelle").getResultList();
        return cuisson;
    }

    public TPosCuisson getCuissonById(int id) {
        TPosCuisson cuisson = entityManager.find(TPosCuisson.class, id);
        return cuisson;
    }

    public void setCuisson(TPosCuisson cuisson) throws CustomConstraintViolationException {
        try {
            entityManager.persist(cuisson);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPosCuisson updateCuisson(TPosCuisson cuisson) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(cuisson);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public void deleteCuisson(int id) {
        entityManager.createNativeQuery("UPDATE t_pos_cuisson SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Tarif
    public List<TPosTarif> getPosTarif() {
        List<TPosTarif> tarif = entityManager
                .createQuery("FROM TPosTarif t WHERE t.dateDeletion = null ORDER BY libelle").getResultList();
        return tarif;
    }

    public TPosTarif getPosTarifById(int id) {
        TPosTarif tarif = entityManager.find(TPosTarif.class, id);
        return tarif;
    }

    public Integer getPosTarifIdByLibelle(String libelle) {
        return (Integer) entityManager.createQuery("SELECT id FROM TPosTarif WHERE libelle =:libelle")
                .setParameter("libelle", libelle).getSingleResult();
    }

    public void setPosTarif(TPosTarif tarif) throws CustomConstraintViolationException {
        try {
            entityManager.persist(tarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPosTarif updatePosTarif(TPosTarif tarif) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(tarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePosTarif(int id) {
        entityManager.createNativeQuery("UPDATE t_pos_tarif SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Happy Hours
    public List<TPosHappyHours> getHappyHours() {
        List<Object[]> happyHours = entityManager.createQuery(
                "FROM TPosHappyHours h LEFT JOIN TPosActivite a ON  h.posActiviteId = a.id WHERE h.dateDeletion = null")
                .getResultList();
        List<TPosHappyHours> result = new ArrayList<TPosHappyHours>();

        // Retrieve the corresponding sub-family
        for (Object[] hHours : happyHours) {
            if (hHours.length > 1) {
                TPosHappyHours hH = (TPosHappyHours) hHours[0];

                hH.setFrequence(getFrequency(hH));
                TPosActivite a = (TPosActivite) hHours[1];
                hH.setPosActiviteLibelle(a.getLibelle());
                result.add(hH);
            }
        }
        return result;
    }

    public String getFrequency(TPosHappyHours happyHours) {
        String frequency = "";

        if (happyHours.getLundi() == null) {
            happyHours.setLundi(Boolean.FALSE);
        }
        if (happyHours.getMardi() == null) {
            happyHours.setMardi(Boolean.FALSE);
        }
        if (happyHours.getMercredi() == null) {
            happyHours.setMercredi(Boolean.FALSE);
        }
        if (happyHours.getJeudi() == null) {
            happyHours.setJeudi(Boolean.FALSE);
        }
        if (happyHours.getVendredi() == null) {
            happyHours.setVendredi(Boolean.FALSE);
        }
        if (happyHours.getSamedi() == null) {
            happyHours.setSamedi(Boolean.FALSE);
        }
        if (happyHours.getDimanche() == null) {
            happyHours.setDimanche(Boolean.FALSE);
        }

        if (happyHours.getLundi() == true && happyHours.getMardi() == true && happyHours.getMercredi() == true
                && happyHours.getJeudi() == true && happyHours.getVendredi() == true && happyHours.getSamedi() == true
                && happyHours.getDimanche() == true) {
            frequency += "Tous les jours";
        } else {
            if (happyHours.getLundi() == true) {
                if (frequency == "") {
                    frequency += "Lun";
                } else {
                    frequency += "|Lun";
                }
            }
            if (happyHours.getMardi() == true) {
                if (frequency == "") {
                    frequency += "Mar";
                } else {
                    frequency += "|Mar";
                }
            }
            if (happyHours.getMercredi() == true) {
                if (frequency == "") {
                    frequency += "Mer";
                } else {
                    frequency += "|Mer";
                }
            }
            if (happyHours.getJeudi() == true) {
                if (frequency == "") {
                    frequency += "Jeu";
                } else {
                    frequency += "|Jeu";
                }
            }
            if (happyHours.getVendredi() == true) {
                if (frequency == "") {
                    frequency += "Ven";
                } else {
                    frequency += "|Ven";
                }
            }
            if (happyHours.getSamedi() == true) {
                if (frequency == "") {
                    frequency += "Sam";
                } else {
                    frequency += "|Sam";
                }
            }
            if (happyHours.getDimanche() == true) {
                if (frequency == "") {
                    frequency += "Dim";
                } else {
                    frequency += "|Dim";
                }
            }
        }

        return frequency;
    }

    public void activeHappyHours(int id, boolean active) throws CustomConstraintViolationException {
        TPosHappyHours happyHours = entityManager.find(TPosHappyHours.class, id);
        happyHours.setActive(active);
        try {
            entityManager.merge(happyHours);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

    }

    public TPosHappyHours getHappyHoursById(int id) {
        TPosHappyHours happyHours = entityManager.find(TPosHappyHours.class, id);
        return happyHours;
    }

    public void setHappyHours(TPosHappyHours happyHours) throws CustomConstraintViolationException {
        try {
            entityManager.persist(happyHours);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosHappyHours updateHappyHours(TPosHappyHours happyHours) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(happyHours);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // Ajout
    public void setTarifPrestation(TPosTarifPrestation posTarifPrestation) throws CustomConstraintViolationException {
        try {
            entityManager.persist(posTarifPrestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // GET pos_prestation
    public List<TPosTarifPrestation> getAllTarifPrestation() {
        List<TPosTarifPrestation> results = entityManager.createQuery(
                "FROM TPosTarifPrestation where posPrestation.dateDeletion is null and posTarif.dateDeletion is null ")
                .getResultList();
        return results;
    }

    public void deleteHappyHours(int id) {
        entityManager.createNativeQuery("UPDATE t_pos_happy_hours SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();

    }

    public List<TPosTarifPrestation> getTarifPrestationById(int id) {
        List<TPosTarifPrestation> posTarifPrestationsList = entityManager.createQuery(
                "FROM TPosTarifPrestation tarifPrest WHERE posPrestationId =:idPrestation and posPrestation.dateDeletion is null and posTarif.dateDeletion is null ")
                .setParameter("idPrestation", id).getResultList();

        return posTarifPrestationsList;
    }

    public void updateTarifPrestation(int productId, int tarifId, int newProductId, int newTarifId, BigDecimal montant,
            BigDecimal prixMinimum) {
        int updatedEntities = entityManager.createQuery(
                "update TPosTarifPrestation SET posPrestationId =:newProductId, posTarifId =:newTarifId, prixminimum =:prixMinimum, montant=:montant WHERE posTarifId=:tarifId and posPrestationId =:productId ")
                .setParameter("newProductId", newProductId).setParameter("newTarifId", newTarifId)
                .setParameter("tarifId", tarifId).setParameter("productId", productId).setParameter("montant", montant)
                .setParameter("prixMinimum", prixMinimum).executeUpdate();
    }

    // DELETE
    public void deleteTarifPrestation(int idTarif, int idProduct) {
        TPosTarifPrestation tarifPresta = new TPosTarifPrestation(idTarif, idProduct);
        entityManager.remove(entityManager.contains(tarifPresta) ? tarifPresta : entityManager.merge(tarifPresta));

    }

    public List<TPosPrestation> getProductOrderKitchen() {
        List<TPosPrestation> products = entityManager
                .createQuery("FROM TPosPrestation  WHERE cdeCuisine = 1 and dateDeletion is null ORDER BY libelle")
                .getResultList();
        return products;

    }

    // Recuperation de la liste des prestations par nature et par société
    public List<TPosPrestation> getProductByNatureBySociety(String nature, int mmcSocieteId) {
        List<TPosPrestation> posPrestationList = entityManager.createQuery(
                "FROM TPosPrestation posPrestation WHERE posPrestation.mmcSousFamilleCa.nature =:nature AND posPrestation.mmcSocieteId =:mmcSocieteId  ")
                .setParameter("mmcSocieteId", mmcSocieteId).setParameter("nature", nature).getResultList();
        return posPrestationList;

    }

    public boolean getNatureByIdSousFamille(int id) {
        String nature = entityManager.createQuery("SELECT nature FROM TMmcSousFamilleCa  WHERE id =:id")
                .setParameter("id", id).getSingleResult().toString();
        boolean isSubventionCotisationAdmission = false;
        if ("SUBVENTION".equals(nature) || "COTISATION".equals(nature) || "ADMISSION".equals(nature)) {
            isSubventionCotisationAdmission = true;
        }
        return isSubventionCotisationAdmission;
    }

    public List<TPosActivite> getPosActivite() {
        List<TPosActivite> products = entityManager
                .createQuery("FROM TPosActivite  WHERE dateDeletion = null ORDER BY libelle").getResultList();
        return products;
    }

    public List<TPosActivitePrestation> getActiviteProductByIdProduct(Integer posPrestationId) {
        List<Integer> activiteIdList = entityManager
                .createQuery("SELECT posActiviteId FROM TPosActivitePrestation  WHERE posPrestationId=:posPrestationId")
                .setParameter("posPrestationId", posPrestationId).getResultList();
        List<TPosActivite> activiteList = getPosActivite();
        List<TPosActivitePrestation> result = new ArrayList<TPosActivitePrestation>();
        boolean isExist = false;
        for (int i = 0; i < activiteList.size(); i++) {
            isExist = false;
            for (Integer activite : activiteIdList) {
                if (Objects.equals(activiteList.get(i).getId(), activite)) {
                    isExist = true;
                    TPosActivitePrestation activitePrestation = new TPosActivitePrestation(posPrestationId, activite);
                    activitePrestation.setChecked(true);
                    activitePrestation.setLibelleActivite(activiteList.get(i).getLibelle());
                    result.add(activitePrestation);
                    break;
                }
            }
            if (isExist == false) {
                TPosActivitePrestation activitePrestation = new TPosActivitePrestation(posPrestationId,
                        activiteList.get(i).getId());
                activitePrestation.setChecked(false);
                activitePrestation.setLibelleActivite(activiteList.get(i).getLibelle());
                result.add(activitePrestation);
            }
        }

        return result;
    }

    // pmsModelTarifId; pmsTypeChambreId
    public void addActiviteProduct(JsonObject object) throws CustomConstraintViolationException {
        Integer posPrestationId = Integer.parseInt(object.get("posPrestationId").toString());
        List<Integer> activiteToDeleteList = entityManager
                .createQuery("SELECT posActiviteId FROM TPosActivitePrestation  WHERE posPrestationId=:posPrestationId")
                .setParameter("posPrestationId", posPrestationId).getResultList();
        String activitesString = object.get("activites").toString();

        if (activiteToDeleteList.size() > 0) {
            // supprimer tous les model de tarif de tarif de pmstype chambre Id
            for (Integer activiteToDelete : activiteToDeleteList) {
                deleteActivityProduct(posPrestationId, activiteToDelete);
            }
        }

        if (!"[]".equals(activitesString)) {
            List<Integer> activites = stringToList(activitesString.substring(1, activitesString.length() - 1));
            if (activites.size() > 0) {
                // ajouter tous les nouvelles options de tarif de pmsModelTarifId
                activites.forEach((Integer row) -> {
                    try {
                        createActiviteProduct(posPrestationId, row);
                    } catch (CustomConstraintViolationException ex) {
                        // Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    public void createActiviteProduct(Integer posPrestationId, Integer posActiviteId)
            throws CustomConstraintViolationException {
        TPosActivitePrestation posActivitePrestation = new TPosActivitePrestation(posPrestationId, posActiviteId);
        try {
            entityManager.persist(posActivitePrestation);
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

    // UPDATE PRINTER
    public void updateActivityProduct(int idProduct, int activity, int newProductId, int newActiviteId) {
        int updatedEntities = entityManager.createQuery(
                "update TPosActivitePrestation SET posPrestationId=:newProductId, posActiviteId =:newActiviteId WHERE posActiviteId=:posActiviteId and posPrestationId=:idProduct")
                .setParameter("activity", activity).setParameter("newProductId", newProductId)
                .setParameter("newActiviteId", newActiviteId).setParameter("idProduct", idProduct).executeUpdate();
    }

    // DELETE PRINTER
    public void deleteActivityProduct(int idProduct, int posActiviteId) {
        TPosActivitePrestation printerProduct = new TPosActivitePrestation(idProduct, posActiviteId);
        entityManager
                .remove(entityManager.contains(printerProduct) ? printerProduct : entityManager.merge(printerProduct));
    }

}
