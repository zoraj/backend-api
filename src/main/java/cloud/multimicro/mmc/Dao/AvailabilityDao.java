/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.RoomByType;
import cloud.multimicro.mmc.Entity.TPmsChambreHorsService;
import cloud.multimicro.mmc.Entity.TPmsReservationVentilation;
import cloud.multimicro.mmc.Entity.TotalRoomByType;
import cloud.multimicro.mmc.Entity.TotalRoomOutOfOrderByType;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class AvailabilityDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public List<TotalRoomByType> getRoomCountByType() {
        List<Object[]> roomsCount  = entityManager.createNativeQuery("SELECT pms_type_chambre_id, COUNT(*) FROM t_pms_chambre "
                + " GROUP BY pms_type_chambre_id ").getResultList();
        List<TotalRoomByType> result = new ArrayList<TotalRoomByType>();
        for (Object[] n : roomsCount ) {
            if (n.length > 1) {
                TotalRoomByType room = new TotalRoomByType();
                room.setTypeChambreId(Integer.parseInt(n[0].toString()));
                room.setQteTotalRoom(Integer.parseInt(n[1].toString()));
                result.add(room);
            }
        }
        return result;
    }
    
    public List<TotalRoomOutOfOrderByType> getRoomCountByTypeOutOfOrder(String startDate, String endDate) {
        List<Object[]> roomsCountOut  = entityManager.createNativeQuery("SELECT pms_type_chambre_id, chs.date_debut, chs.date_fin, COUNT(*) "
                + " FROM t_pms_chambre c LEFT JOIN t_pms_chambre_hors_service chs ON chs.pms_chambre_id = c.id "
                + " WHERE c.etat_chambre = 'OUT' AND chs.date_debut >= '" + startDate + "' AND chs.date_fin <= '" + endDate + "' "
                + " GROUP BY c.pms_type_chambre_id, chs.date_debut, chs.date_fin ").getResultList();

        List<TotalRoomOutOfOrderByType> result = new ArrayList<TotalRoomOutOfOrderByType>();
        
        LocalDate sDate = LocalDate.parse(startDate);
        LocalDate eDate = LocalDate.parse(endDate);

        for (LocalDate date = sDate; date.isBefore(eDate); date = date.plusDays(1)) {
            for (Object[] n : roomsCountOut ) {
               LocalDate dateDebut = LocalDate.parse(n[1].toString());
               LocalDate dateEnd = LocalDate.parse(n[2].toString());
               
               if ((date.equals(dateDebut) || dateDebut.isBefore(date)) && (date.equals(dateEnd) || date.isBefore(dateEnd))) {
                   var room = new TotalRoomOutOfOrderByType();
                   room.setTypeChambreId(Integer.parseInt(n[0].toString()));
                   room.setRoomCountOutOfOrder(Integer.parseInt(n[3].toString()));
                   room.setDateOutOfOrder(date);
                   result.add(room);
               }
            }           
        }
        return result;
    }
      
}
