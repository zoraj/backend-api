/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetailDetail;
import cloud.multimicro.mmc.Entity.VPmsTarifGrilleDetailDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
public class RateGridDetailDetailDao {

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(RateGridDetailDetailDao.class);

    public List<TPmsTarifGrilleDetailDetail> getAll() {
        List<TPmsTarifGrilleDetailDetail> result = entityManager
                .createQuery("FROM TPmsTarifGrilleDetailDetail where dateDeletion = null ").getResultList();
        return result;
    }

    public List<TPmsTarifGrilleDetailDetail> getByRateDetail(Integer pmsTarifGrilleDetailId) {
        List<TPmsTarifGrilleDetailDetail> detailDetaillist = entityManager.createQuery(
                "FROM TPmsTarifGrilleDetailDetail where pmsTarifGrilleDetailId=:pmsTarifGrilleDetailId and dateDeletion = null ")
                .setParameter("pmsTarifGrilleDetailId", pmsTarifGrilleDetailId).getResultList();

        List<TPmsTarifGrilleDetailDetail> result = new ArrayList<TPmsTarifGrilleDetailDetail>();
        for (TPmsTarifGrilleDetailDetail detail : detailDetaillist) {
            TPmsPrestation prestation = entityManager.find(TPmsPrestation.class, detail.getPmsPrestationId());
            detail.setCode(prestation.getCode());
            detail.setPmsPrestationLibelle(prestation.getLibelle());
            result.add(detail);
        }

        return result;
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

    // Update
    public void put(JsonObject request) throws CustomConstraintViolationException {
        List<Map> pmsTarifGrilleDetailDetailList = (List) request.get("pmsTarifGrilleDetailDetail");
        String stringList = request.get("pmsTarifGrilleDetailDetailIdList").toString();
        stringList = stringList.substring(1, stringList.length() - 1);
        try {
            for (Map rateGridDetailDetail : pmsTarifGrilleDetailDetailList) {
                TPmsTarifGrilleDetailDetail pmsTarifGrilleDetailDetail = new TPmsTarifGrilleDetailDetail();

                pmsTarifGrilleDetailDetail.setPmsTarifGrilleDetailId(
                        Integer.parseInt(rateGridDetailDetail.get("pmsTarifGrilleDetailId").toString()));
                pmsTarifGrilleDetailDetail
                        .setPmsPrestationId(Integer.parseInt(rateGridDetailDetail.get("pmsPrestationId").toString()));
                pmsTarifGrilleDetailDetail.setPu(new BigDecimal(rateGridDetailDetail.get("pu").toString()));
                pmsTarifGrilleDetailDetail
                        .setRemise(Boolean.parseBoolean(rateGridDetailDetail.get("remise").toString()));
                pmsTarifGrilleDetailDetail
                        .setPromotion(Boolean.parseBoolean(rateGridDetailDetail.get("promotion").toString()));
                pmsTarifGrilleDetailDetail
                        .setCommission(Boolean.parseBoolean(rateGridDetailDetail.get("commission").toString()));
                pmsTarifGrilleDetailDetail
                        .setRecouche(Boolean.parseBoolean(rateGridDetailDetail.get("recouche").toString()));
                pmsTarifGrilleDetailDetail.setOpen(Boolean.parseBoolean(rateGridDetailDetail.get("open").toString()));
                pmsTarifGrilleDetailDetail.setUnite(rateGridDetailDetail.get("unite").toString().replaceAll("\"", ""));
                pmsTarifGrilleDetailDetail.setIsOng(rateGridDetailDetail.get("isOng").toString().replaceAll("\"", ""));

                if ("0".equals(rateGridDetailDetail.get("id").toString())) {
                    entityManager.persist(pmsTarifGrilleDetailDetail);
                } else {
                    pmsTarifGrilleDetailDetail.setId(Integer.parseInt(rateGridDetailDetail.get("id").toString()));
                    entityManager.merge(pmsTarifGrilleDetailDetail);
                }

            }
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }

        if (!"".equals(stringList)) {
            List<Integer> pmsTarifGrilleDetailDetailIdList = stringToList(stringList);
            for (Integer obj : pmsTarifGrilleDetailDetailIdList) {
                delete(obj);
            }
        }
    }

    public TPmsTarifGrilleDetailDetail getById(int id) {
        TPmsTarifGrilleDetailDetail pmsTarifGrilleDetailDetail = entityManager.find(TPmsTarifGrilleDetailDetail.class,
                id);
        return pmsTarifGrilleDetailDetail;
    }

    public void set(JsonObject request) throws CustomConstraintViolationException {
        List<Map> pmsTarifGrilleDetailDetailList = (List) request.get("pmsTarifGrilleDetailDetail");
        try {
            for (Map rateGridDetailDetail : pmsTarifGrilleDetailDetailList) {
                TPmsTarifGrilleDetailDetail pmsTarifGrilleDetailDetail = new TPmsTarifGrilleDetailDetail();
                pmsTarifGrilleDetailDetail.setPmsTarifGrilleDetailId(
                        Integer.parseInt(rateGridDetailDetail.get("pmsTarifGrilleDetailId").toString()));
                pmsTarifGrilleDetailDetail
                        .setPmsPrestationId(Integer.parseInt(rateGridDetailDetail.get("pmsPrestationId").toString()));
                pmsTarifGrilleDetailDetail.setPu(new BigDecimal(rateGridDetailDetail.get("pu").toString()));
                pmsTarifGrilleDetailDetail
                        .setRemise(Boolean.parseBoolean(rateGridDetailDetail.get("remise").toString()));
                pmsTarifGrilleDetailDetail
                        .setPromotion(Boolean.parseBoolean(rateGridDetailDetail.get("promotion").toString()));
                pmsTarifGrilleDetailDetail
                        .setCommission(Boolean.parseBoolean(rateGridDetailDetail.get("commission").toString()));
                pmsTarifGrilleDetailDetail
                        .setRecouche(Boolean.parseBoolean(rateGridDetailDetail.get("recouche").toString()));
                pmsTarifGrilleDetailDetail.setOpen(Boolean.parseBoolean(rateGridDetailDetail.get("open").toString()));
                pmsTarifGrilleDetailDetail.setUnite(rateGridDetailDetail.get("unite").toString().replaceAll("\"", ""));
                pmsTarifGrilleDetailDetail.setIsOng(rateGridDetailDetail.get("isOng").toString().replaceAll("\"", ""));

                entityManager.persist(pmsTarifGrilleDetailDetail);
            }
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void delete(Integer id) {
        entityManager
                .createNativeQuery(
                        "UPDATE t_pms_tarif_grille_detail_detail SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }
    
    public void deleteRateGridDetailDetail(String dateStart, String dateFin, Integer modelTarifId, Integer base) {
        entityManager
                .createNativeQuery(
                        "DELETE FROM t_pms_tarif_grille_detail "
                                + "WHERE date_tarif >= '" + dateStart + "' AND date_tarif <= '" + dateFin + "' "
                                + "AND pms_model_tarif_id = '" + modelTarifId + "' AND base = '" + base + "'").executeUpdate();
    }
    
    public List<VPmsTarifGrilleDetailDetail> getAllTarifGrilleDetailDetail(String dateTarif, Integer mmcClientId, Integer tarifGrilleId) {
        List<VPmsTarifGrilleDetailDetail> result = entityManager
                .createQuery("FROM VPmsTarifGrilleDetailDetail "
                            + "WHERE dateTarif = '" + dateTarif + "' AND mmcClientId = '"+mmcClientId+"' AND "
                            + "pmsTarifGrilleId = '" + tarifGrilleId + "'").getResultList();
        return result;
    }
}
