/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsModelTarif;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPmsTarifGrille;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetail;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetailDetail;
import cloud.multimicro.mmc.Entity.VPmsTarifGrilleDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
public class RateGridDetailDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<TPmsTarifGrilleDetail> getAll() {
        List<TPmsTarifGrilleDetail> result = entityManager
                .createQuery("FROM TPmsTarifGrilleDetail where dateDeletion = null ").getResultList();
        return result;
    }

    public JsonObject getByRateDetail(Integer pmsTarifGrilleDetailId) {
        List<TPmsTarifGrilleDetailDetail> detailDetaillist = entityManager
                .createQuery("FROM TPmsTarifGrilleDetailDetail where pmsTarifGrilleDetailId=:pmsTarifGrilleDetailId ")
                .setParameter("pmsTarifGrilleDetailId", pmsTarifGrilleDetailId).getResultList();
        TPmsTarifGrilleDetail pmsTarifGrilleDetail = getById(pmsTarifGrilleDetailId);
        JsonArrayBuilder arrayBuilderValue = Json.createArrayBuilder();
        TPmsPrestation pmsPrestation = null;
        for (TPmsTarifGrilleDetailDetail detailDetail : detailDetaillist) {
            JsonObjectBuilder detailDetailObject = Json.createObjectBuilder();
            pmsPrestation = entityManager.find(TPmsPrestation.class, detailDetail.getPmsPrestationId());
            arrayBuilderValue.add(detailDetailObject.add("pmsPrestationId", detailDetail.getPmsPrestationId())
                    .add("libellePrestation", pmsPrestation.getLibelle()).add("pu", detailDetail.getPu())
                    .add("remise", detailDetail.isRemise()).add("promotion", detailDetail.isPromotion())
                    .add("commission", detailDetail.isCommission()).add("recouche", detailDetail.isRecouche())
                    .add("open", detailDetail.isOpen()).build());
        }
        TPmsModelTarif pmsModelTarif = entityManager.find(TPmsModelTarif.class,
                pmsTarifGrilleDetail.getPmsModelTarifId());
        JsonArray valueArray = arrayBuilderValue.build();
        JsonObject value = Json.createObjectBuilder().add("libelle", pmsModelTarif.getLibelle())
                .add("base", pmsTarifGrilleDetail.getBase())
                .add("pmsModelTarifId", pmsTarifGrilleDetail.getPmsModelTarifId())
                .add("pmsTarifGrilleDetailDetail", valueArray).build();
        return value;
    }

    public List<VPmsTarifGrilleDetail> getDetailByRateGrid(String dateStart, String dateEnd, Integer rateGridId) {
        List<VPmsTarifGrilleDetail> lists = entityManager.createQuery(
                
                "FROM VPmsTarifGrilleDetail WHERE pmsTarifGrilleId =:rateGridId AND dateTarif BETWEEN :dateStart AND :dateEnd")
                .setParameter("dateStart", LocalDate.parse(dateStart)).setParameter("dateEnd", LocalDate.parse(dateEnd))
                .setParameter("rateGridId", rateGridId).getResultList();
        return lists;
    }

    // Update
    public void put(JsonObject request) throws CustomConstraintViolationException {
        List<Map> rateGridDetailList = (List) request.get("pmsTarifGrilleDetail");
        String value = "";
        for (Map rateGridDetail : rateGridDetailList) {
            TPmsTarifGrilleDetail pmsTarifGrilleDetail = new TPmsTarifGrilleDetail();
            pmsTarifGrilleDetail.setId(Integer.parseInt(rateGridDetail.get("id").toString()));
            pmsTarifGrilleDetail.setBase(Integer.parseInt(rateGridDetail.get("base").toString()));
            value = rateGridDetail.get("dateTarif").toString();
            pmsTarifGrilleDetail.setDateTarif(LocalDate.parse(value.substring(1, value.length() - 1)));
            pmsTarifGrilleDetail.setPmsModelTarifId(Integer.parseInt(rateGridDetail.get("pmsModelTarifId").toString()));
            pmsTarifGrilleDetail
                    .setPmsTarifGrilleId(Integer.parseInt(rateGridDetail.get("pmsTarifGrilleId").toString()));
            try {
                entityManager.merge(pmsTarifGrilleDetail);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }
    }

    public TPmsTarifGrilleDetail getById(Integer id) {
        TPmsTarifGrilleDetail pmsTarifGrilleDetail = entityManager.find(TPmsTarifGrilleDetail.class, id);
        return pmsTarifGrilleDetail;
    }

    public List<Integer> set(JsonObject request) throws CustomConstraintViolationException {
        List<Map> rateGridDetailList = (List) request.get("pmsTarifGrilleDetail");
        String value = "";
        List<Integer> rateGridDetailIdList = new ArrayList<Integer>();

        for (Map rateGridDetail : rateGridDetailList) {
            TPmsTarifGrilleDetail pmsTarifGrilleDetail = new TPmsTarifGrilleDetail();
            pmsTarifGrilleDetail.setBase(Integer.parseInt(rateGridDetail.get("base").toString()));
            value = rateGridDetail.get("dateTarif").toString();
            pmsTarifGrilleDetail.setDateTarif(LocalDate.parse(value.substring(1, value.length() - 1)));
            pmsTarifGrilleDetail.setPmsModelTarifId(Integer.parseInt(rateGridDetail.get("pmsModelTarifId").toString()));
            pmsTarifGrilleDetail.setPmsTypeChambreId(Integer.parseInt(rateGridDetail.get("pmsTypeChambreId").toString()));
            pmsTarifGrilleDetail
                    .setPmsTarifGrilleId(Integer.parseInt(rateGridDetail.get("pmsTarifGrilleId").toString()));

            try {
                entityManager.persist(pmsTarifGrilleDetail);
                rateGridDetailIdList.add(pmsTarifGrilleDetail.getId());
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }

        }

        return rateGridDetailIdList;
    }

    public void delete(int id) {
        entityManager
                .createNativeQuery(
                        "UPDATE t_pms_tarif_grille_detail SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id).executeUpdate();
    }
}
