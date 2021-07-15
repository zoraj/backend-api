/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsCategorieTarif;
import cloud.multimicro.mmc.Entity.TPmsModelTarif;
import cloud.multimicro.mmc.Entity.TPmsModelTarifDetail;
import cloud.multimicro.mmc.Entity.TPmsModelTarifOption;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPmsTarifOption;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class RateDao {

    @PersistenceContext
    private EntityManager entityManager;

    // Fare category
    public List<TPmsCategorieTarif> getPmsFareCategory() {
        List<TPmsCategorieTarif> categorieTarif = entityManager
                .createQuery("FROM TPmsCategorieTarif cat WHERE cat.dateDeletion = null ORDER BY libelle")
                .getResultList();
        return categorieTarif;
    }

    // Update
    public TPmsCategorieTarif updateFareCategory(TPmsCategorieTarif categorieTarif)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(categorieTarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsCategorieTarif getPmsFareCategoryById(int id) {
        TPmsCategorieTarif categorieTarif = entityManager.find(TPmsCategorieTarif.class, id);
        return categorieTarif;
    }

    public void setPmsFareCategory(TPmsCategorieTarif tarif) throws CustomConstraintViolationException {
        try {
            entityManager.persist(tarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePmsFareCategory(int id) {
        entityManager
                .createNativeQuery("UPDATE t_pms_categorie_tarif SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // Fare Model
    public List<TPmsModelTarif> getPmsFareModel() {
        List<Object[]> products = entityManager.createQuery("FROM TPmsModelTarif mt "
                + "LEFT JOIN TPmsCategorieTarif ct ON  mt.pmsCategorieTarifId = ct.id where mt.dateDeletion = null "
                + "ORDER BY mt.libelle").getResultList();
        List<TPmsModelTarif> result = new ArrayList<TPmsModelTarif>();

        // Retrieve the corresponding sub-family
        for (Object[] product : products) {
            if (product.length > 1) {
                TPmsModelTarif mt = (TPmsModelTarif) product[0];
                TPmsCategorieTarif ct = (TPmsCategorieTarif) product[1];
                mt.setPmsCategorieTarifLibelle(ct.getLibelle());

                result.add(mt);
            }
        }
        return result;

    }

    public TPmsModelTarif getPmsFareModelById(int id) {
        TPmsModelTarif modele = entityManager.find(TPmsModelTarif.class, id);
        return modele;
    }

    // Update
    public TPmsModelTarif updatePmsFareModel(TPmsModelTarif categorieTarif) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(categorieTarif);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void setPmsFareModel(TPmsModelTarif modele) throws CustomConstraintViolationException {
        try {
            entityManager.persist(modele);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePmsFareModel(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_model_tarif SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    public TPmsModelTarifDetail getPmsFareModelDetailedById(int id) {
        TPmsModelTarifDetail modeleDetail = entityManager.find(TPmsModelTarifDetail.class, id);
        return modeleDetail;
    }

    public void setPmsFareModelDetailed(TPmsModelTarifDetail modeleDetail) throws CustomConstraintViolationException {
        try {
            entityManager.persist(modeleDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deletePmsModelTarifDetail(Integer id) {
        entityManager
                .createNativeQuery("UPDATE t_pms_model_tarif_detail SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }

    // model tarif Detail par model de tarif
    public List<TPmsModelTarifDetail> getPmsFareModelDetailedByModelId(Integer modelTarifId) {
        List<TPmsModelTarifDetail> tarifDetailList = entityManager
                .createQuery("FROM TPmsModelTarifDetail  WHERE pmsModelTarifId =:modelTarifId and dateDeletion = null")
                .setParameter("modelTarifId", modelTarifId).getResultList();

        List<TPmsModelTarifDetail> result = new ArrayList<TPmsModelTarifDetail>();
        for (TPmsModelTarifDetail detail : tarifDetailList) {
            TPmsPrestation prestation = entityManager.find(TPmsPrestation.class, detail.getPmsPrestationId());
            detail.setCode(prestation.getCode());
            detail.setPmsPrestationLibelle(prestation.getLibelle());
            result.add(detail);
        }

        return tarifDetailList;
    }

    // ajout des details de tarif de model de tarif
    public void addPmsModelTarifDetail(JsonObject object) throws CustomConstraintViolationException {
        List<Map> detailToAddList = (List) object.get("details");
        Integer pmsModelTarifId = Integer.parseInt(object.get("pmsModelTarifId").toString());
        List<TPmsModelTarifDetail> detailToDeleteList = getPmsFareModelDetailedByModelId(pmsModelTarifId);

        if (detailToDeleteList.size() > 0) {
            // supprimer tous les details de tarif de pmsModelTarifId
            for (TPmsModelTarifDetail detailToDelete : detailToDeleteList) {
                deletePmsModelTarifDetail(detailToDelete.getId());
            }
        }

        if (detailToAddList.size() > 0) {
            // ajouter tous les details de tarif de pmsModelTarifId
            for (Map detailToAdd : detailToAddList) {
                TPmsModelTarifDetail pmsModelTarifDetail = new TPmsModelTarifDetail();
                pmsModelTarifDetail.setCommission(Boolean.parseBoolean(detailToAdd.get("commission").toString()));
                pmsModelTarifDetail.setOpen(Boolean.parseBoolean(detailToAdd.get("open").toString()));
                pmsModelTarifDetail.setPmsModelTarifId(pmsModelTarifId);
                pmsModelTarifDetail.setPmsPrestationId(Integer.parseInt(detailToAdd.get("pmsPrestationId").toString()));
                pmsModelTarifDetail.setPromotion(Boolean.parseBoolean(detailToAdd.get("promotion").toString()));
                pmsModelTarifDetail.setPu(new BigDecimal(detailToAdd.get("pu").toString()));
                pmsModelTarifDetail.setRecouche(Boolean.parseBoolean(detailToAdd.get("recouche").toString()));
                pmsModelTarifDetail.setRemise(Boolean.parseBoolean(detailToAdd.get("remise").toString()));
                pmsModelTarifDetail.setUnite(detailToAdd.get("unite").toString().replaceAll("\"", ""));
                pmsModelTarifDetail.setIsOng(detailToAdd.get("isOng").toString().replaceAll("\"", ""));

                setPmsFareModelDetailed(pmsModelTarifDetail);
            }
        }
    }

    // CRUD Tarif Option
    public List<TPmsTarifOption> getAllTarifOption() {
        List<TPmsTarifOption> tarifOption = entityManager
                .createQuery("FROM TPmsTarifOption cat WHERE cat.dateDeletion = null ORDER BY libelle").getResultList();
        return tarifOption;
    }

    public TPmsTarifOption getTarifOptionById(int id) {
        TPmsTarifOption modele = entityManager.find(TPmsTarifOption.class, id);
        return modele;
    }

    // Update
    public TPmsTarifOption updateTarifOption(TPmsTarifOption tarifOption) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(tarifOption);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void setTarifOption(TPmsTarifOption tarifOption) throws CustomConstraintViolationException {
        try {
            entityManager.persist(tarifOption);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public boolean tarifOptionUsed(int id) {
        List<TPmsModelTarifOption> modelTarifOptionList = entityManager
                .createQuery("FROM TPmsModelTarifOption WHERE pmsTarifOptionId =:pmsTarifOptionId")
                .setParameter("pmsTarifOptionId", id).getResultList();
        if (modelTarifOptionList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteTarifOption(int id) {
        boolean isExist = tarifOptionUsed(id);
        if (isExist == false) {
            entityManager
                    .createNativeQuery("UPDATE t_pms_tarif_option SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                    .setParameter("id", id).executeUpdate();
        }
        return isExist;
    }

    // model tarif et option de tarif
    public List<TPmsTarifOption> getModelTarifOptionByIdModelTarif(Integer modelTarifId) {
        List<Integer> tarifOptionIdList = getTarifOptionByIdModelTarif(modelTarifId);
        List<TPmsTarifOption> tarifOptionList = getAllTarifOption();
        List<TPmsTarifOption> result = new ArrayList<TPmsTarifOption>();
        boolean isExist = false;
        for (int i = 0; i < tarifOptionList.size(); i++) {
            isExist = false;
            for (Integer tarifOptionIdList1 : tarifOptionIdList) {
                if (Objects.equals(tarifOptionList.get(i).getId(), tarifOptionIdList1)) {
                    isExist = true;
                    tarifOptionList.get(i).setChecked(true);
                    result.add(tarifOptionList.get(i));
                    break;
                }
            }
            if (isExist == false) {
                tarifOptionList.get(i).setChecked(false);
                result.add(tarifOptionList.get(i));
            }
        }

        return result;
    }

    // model tarif et option de tarif
    public List<Integer> getTarifOptionByIdModelTarif(Integer modelTarifId) {
        List<Integer> tarifOptionId = entityManager
                .createQuery("SELECT pmsTarifOptionId FROM TPmsModelTarifOption  WHERE pmsModelTarifId =:modelTarifId")
                .setParameter("modelTarifId", modelTarifId).getResultList();
        return tarifOptionId;
    }

    public void addModelTarifOption(JsonObject object) throws CustomConstraintViolationException {
        Integer pmsModelTarifId = Integer.parseInt(object.get("pmsModelTarifId").toString());
        List<Integer> optionToDeleteList = getTarifOptionByIdModelTarif(pmsModelTarifId);
        String optionsString = object.get("options").toString();

        if (optionToDeleteList.size() > 0) {
            // supprimer tous les options de tarif de pmsModelTarifId
            for (Integer optionToDelete : optionToDeleteList) {
                deleteModelTarifOption(pmsModelTarifId, optionToDelete);
            }
        }

        if (!"[]".equals(optionsString)) {
            List<Integer> options = stringToList(optionsString.substring(1, optionsString.length() - 1));
            if (options.size() > 0) {
                // ajouter tous les nouvelles options de tarif de pmsModelTarifId
                options.forEach((Integer option) -> {
                    try {
                        createModelTarifOption(pmsModelTarifId, option);
                    } catch (CustomConstraintViolationException ex) {
                        Logger.getLogger(RateDao.class.getName()).log(Level.SEVERE, null, ex);
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

    public void createModelTarifOption(Integer pmsModelTarifId, Integer pmsTarifOptionId)
            throws CustomConstraintViolationException {
        TPmsModelTarifOption pmsModelTarifOption = new TPmsModelTarifOption(pmsModelTarifId, pmsTarifOptionId);
        try {
            entityManager.persist(pmsModelTarifOption);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // DELETE
    public void deleteModelTarifOption(Integer pmsModelTarifId, Integer pmsTarifOptionId) {
        TPmsModelTarifOption pmsModelTarifOption = new TPmsModelTarifOption(pmsModelTarifId, pmsTarifOptionId);
        entityManager.remove(entityManager.contains(pmsModelTarifOption) ? pmsModelTarifOption
                : entityManager.merge(pmsModelTarifOption));
    }

}
