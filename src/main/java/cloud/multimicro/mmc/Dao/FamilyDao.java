/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcFamilleCa;
import cloud.multimicro.mmc.Entity.TMmcSousFamilleCa;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
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
@SuppressWarnings("unchecked")
public class FamilyDao {
    @PersistenceContext
    EntityManager entityManager;

    // GET FAMILY
    public List<TMmcFamilleCa> getFamily() {
        List<TMmcFamilleCa> mmcFamilies = entityManager
                .createQuery("FROM TMmcFamilleCa f WHERE f.dateDeletion = null ORDER BY libelle").getResultList();
        return mmcFamilies;
    }

    // GET FAMILY BY ID
    public TMmcFamilleCa getFamilyById(int id) {
        TMmcFamilleCa mmcFamilies = entityManager.find(TMmcFamilleCa.class, id);
        return mmcFamilies;
    }

    // SET FAMILY
    public void setFamily(TMmcFamilleCa family) throws CustomConstraintViolationException {
        try {
            entityManager.persist(family);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TMmcFamilleCa updateFamily(TMmcFamilleCa family) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(family);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteFamily(int id) throws CustomConstraintViolationException {
        TMmcFamilleCa mmcFamilies = getFamilyById(id);
        Date dateDel = new Date();
        mmcFamilies.setDateDeletion(dateDel);
        try{
         entityManager.merge(mmcFamilies);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // GET SUBFAMILY
    public List<TMmcSousFamilleCa> getSubFamily() {
        List<Object[]> mmcSFamilies = entityManager.createQuery(
                "FROM TMmcSousFamilleCa sf LEFT JOIN TMmcFamilleCa f ON  sf.mmcFamilleCaId = f.id WHERE sf.dateDeletion = null ORDER BY sf.libelle")
                .getResultList();
        List<TMmcSousFamilleCa> result = new ArrayList<TMmcSousFamilleCa>();
        // Retrieve the corresponding sub-family
        for (Object[] mmcSFamilie : mmcSFamilies) {
            if (mmcSFamilie.length > 1) {
                TMmcSousFamilleCa sf = (TMmcSousFamilleCa) mmcSFamilie[0];
                TMmcFamilleCa f = (TMmcFamilleCa) mmcSFamilie[1];
                sf.setMmcFamilleCaLibelle(f.getLibelle());
                result.add(sf);
            }
        }
        return result;
    }

    // GET SUBFAMILY
    public List<TMmcSousFamilleCa> getAllSubFamily(String nature) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM TMmcSousFamilleCa sf ");
        if (!Objects.isNull(nature)) {
            if (nature.equals("PRESTATION")) {
                stringBuilder.append(
                        "  WHERE sf.nature <> 'SUBVENTION' and sf.nature <> 'ADMISSION' and sf.nature <> 'SUBVENTION MODULAIRE' and sf.nature <> 'COTISATION'");
            } else {
                stringBuilder.append(" WHERE sf.nature = '" + nature + "'");
            }

        }
        List<TMmcSousFamilleCa> mmcSFamilies = entityManager.createQuery(stringBuilder.toString()).getResultList();

        return mmcSFamilies;
    }

    // GET SUBFAMILY
    public List<TMmcSousFamilleCa> getSubFamilyByFamily(Integer mmcFamilleCaId) {
        List<TMmcSousFamilleCa> mmcSFamilies = entityManager
                .createQuery("FROM TMmcSousFamilleCa sf WHERE sf.mmcFamilleCaId =:mmcFamilleCaId ")
                .setParameter("mmcFamilleCaId", mmcFamilleCaId).getResultList();

        return mmcSFamilies;
    }

    // GET SUBFAMILY BY ID
    public TMmcSousFamilleCa getSubFamilyById(int id) {
        TMmcSousFamilleCa mmcFamilies = entityManager.find(TMmcSousFamilleCa.class, id);
        return mmcFamilies;
    }

    // SET SUBFAMILY
    public void setSubFamily(TMmcSousFamilleCa sfamily) throws CustomConstraintViolationException {
        try {
            entityManager.persist(sfamily);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TMmcSousFamilleCa updateSubFamily(TMmcSousFamilleCa sfamily) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(sfamily);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteSubFamily(int id) throws CustomConstraintViolationException {
        TMmcSousFamilleCa subFamilies = getSubFamilyById(id);
        Date dateDel = new Date();
        subFamilies.setDateDeletion(dateDel);
        try {
            entityManager.merge(subFamilies);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
}
