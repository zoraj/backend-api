/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsTarifGrille;
import cloud.multimicro.mmc.Entity.VPmsTarifGrille;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
public class RateGridDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<VPmsTarifGrille> getRateGrid(String season, String subSeason) {
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsTarifGrille ");

        if (!Objects.isNull(season)) {
            stringBuilder.append(" WHERE saisonId = '" + Integer.parseInt(season) + "'");
            isExist = true;
        }

        if (!Objects.isNull(subSeason)) {
            if (isExist == true) {
                stringBuilder.append(" AND sousSaisonId = '" + Integer.parseInt(subSeason) + "'");
            } else {
                stringBuilder.append(" WHERE sousSaisonId = '" + Integer.parseInt(subSeason) + "'");
            }
        }

        List<VPmsTarifGrille> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }

    //SET TPmsTarifGrille
    public void set(TPmsTarifGrille pmsTarifGrille) throws CustomConstraintViolationException {
        try {
            entityManager.persist(pmsTarifGrille);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPmsTarifGrille getById(Integer id) {
        TPmsTarifGrille pmsTarifGrille = entityManager.find(TPmsTarifGrille.class, id);
        return pmsTarifGrille;
    }

    public void delete(Integer id) {
        entityManager.createNativeQuery("UPDATE t_pms_tarif_grille SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
