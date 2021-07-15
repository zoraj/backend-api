/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.VPmsEditionPlanningMensuelChambre;
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
public class PlanningDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    //Planning room month
    public List<VPmsEditionPlanningMensuelChambre> getAllPlanningRoomMonth(String dateDebut) {
       // LocalDate dateStart = LocalDate.parse(dateDebut);
       // LocalDate dateEnd = dateStart.plusDays(30);
        
        List<VPmsEditionPlanningMensuelChambre> planningRoom = entityManager.createQuery("FROM VPmsEditionPlanningMensuelChambre ")
                .getResultList();
        
        return  planningRoom;
    }
}
