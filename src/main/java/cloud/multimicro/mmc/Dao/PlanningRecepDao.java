/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.VPmsEditionPlanningJour;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class PlanningRecepDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    //Planning day
    public List<VPmsEditionPlanningJour> getAllPlanningDay(String dateDebut) {
        
        List<VPmsEditionPlanningJour> planningDay = entityManager.createQuery("FROM VPmsEditionPlanningJour ")
                .getResultList();
        
        return  planningDay;
    }
}
