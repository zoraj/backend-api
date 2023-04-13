/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;
import cloud.multimicro.mmc.Entity.TPosTablePlan;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class PlanningTableDao {
    
    @PersistenceContext
    EntityManager entityManager;

    public List<TPosTablePlan> getSeatingPlan() {
        List<TPosTablePlan> planTable = entityManager
                .createQuery("FROM TPosTablePlan WHERE dateDeletion = null").getResultList();
        return planTable;
    }

    public TPosTablePlan getSeatingPlanById(int id) {
        TPosTablePlan planTable = entityManager.find(TPosTablePlan.class, id);
        return planTable;
    }

    public void setSeatingPlan(TPosTablePlan tablePlan) throws CustomConstraintViolationException {
        try {
            entityManager.persist(tablePlan);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TPosTablePlan updateSeatingPlan(TPosTablePlan tablePlan) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(tablePlan);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void deleteSeatingPlan(int id) throws CustomConstraintViolationException {
        TPosTablePlan planTable = getSeatingPlanById(id);
        LocalDateTime dateDel = LocalDateTime.now();
        planTable.setDateDeletion(dateDel);
        try{
         entityManager.merge(planTable);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
}
