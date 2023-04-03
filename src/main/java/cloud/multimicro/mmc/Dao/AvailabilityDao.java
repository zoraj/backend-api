/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TotalRoomByType;
import cloud.multimicro.mmc.Entity.TotalRoomCountAvailable;
import cloud.multimicro.mmc.Entity.TotalRoomCountUnavailable;
import cloud.multimicro.mmc.Entity.TotalRoomOutOfOrderByType;
import cloud.multimicro.mmc.Entity.VPmsChambreDisponibiliteEtat;
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
        List<Object[]> roomsCount  = entityManager.createNativeQuery("SELECT tch.id, COALESCE(COUNT(ch.id), 0) FROM t_pms_type_chambre tch "
                + "LEFT JOIN t_pms_chambre ch ON tch.id = ch.pms_type_chambre_id"
                + " GROUP BY tch.id ").getResultList();
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
    
    public List<TotalRoomOutOfOrderByType> getRoomCountByTypeOutOfOrder(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(13);
        List<Object[]> roomsCountOut  = entityManager.createNativeQuery("SELECT tc.id, chs.date_debut, chs.date_fin, COALESCE(COUNT(c.id), 0) "
                + " FROM t_pms_type_chambre tc LEFT JOIN t_pms_chambre c ON tc.id = c.pms_type_chambre_id "
                + " LEFT JOIN t_pms_chambre_hors_service chs ON chs.pms_chambre_id = c.id "
                + " WHERE chs.is_soustraire_dispo = true AND (chs.date_debut >= '" + startDate + "' OR chs.date_debut < '" + startDate + "') AND (chs.date_fin <= '" + endDate + "' OR chs.date_fin > '" + endDate + "') "
                + " GROUP BY tc.id, chs.date_debut, chs.date_fin ").getResultList();

        List<TotalRoomOutOfOrderByType> result = new ArrayList<TotalRoomOutOfOrderByType>();
        
        LocalDate date = startDate;

        while (date.compareTo(endDate) <= 0) {
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
            date = date.plus(Period.ofDays(1));
        }
        return result;
    }
    
    public List<TotalRoomCountUnavailable> getRoomCountUnavailable(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(13);
        List<Object[]> roomsCountUnavailable  = entityManager.createNativeQuery("SELECT pms_type_chambre_id, resa.date_arrivee, resa.date_depart "
                + " FROM t_pms_reservation_ventilation v LEFT JOIN t_pms_reservation resa ON v.pms_reservation_id = resa.id "
                + " WHERE (resa.date_arrivee >= '" + startDate + "' OR resa.date_arrivee < '" + startDate + "') AND (resa.date_depart <= '" + endDate + "' OR resa.date_depart > '" + endDate + "') "
                + " GROUP BY v.pms_type_chambre_id, resa.date_arrivee, resa.date_depart ").getResultList();

        List<TotalRoomCountUnavailable> result = new ArrayList<TotalRoomCountUnavailable>();
        
        LocalDate dateOccupation = startDate;

        while (dateOccupation.compareTo(endDate) <= 0) {
            for (Object[] n : roomsCountUnavailable ) {
               String dateDepart = n[1].toString();
               String dateD = dateDepart.substring(0, 10);
               LocalDate dateDebut = LocalDate.parse(dateD);
               LocalDate dateEnd = LocalDate.parse(n[2].toString());
               
               if ((dateOccupation.equals(dateDebut) || dateDebut.isBefore(dateOccupation)) && (dateOccupation.equals(dateEnd) || dateOccupation.isBefore(dateEnd))) {
                   var room = new TotalRoomCountUnavailable();
                   room.setTypeChambreId(Integer.parseInt(n[0].toString()));
                   room.setRoomCountUnavailable(1);
                   room.setDateUnavailable(dateOccupation);
                   result.add(room);
               }
            }           
            dateOccupation = dateOccupation.plus(Period.ofDays(1));
        }
        return result;
    }
    
    public List<TotalRoomCountAvailable> getRoomCountAvailables(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(13);
        List<TotalRoomCountAvailable> result = new ArrayList<>();
        List<TotalRoomByType> totalRooms = getRoomCountByType();        
        List<TotalRoomOutOfOrderByType> totalRoomsOut = getRoomCountByTypeOutOfOrder(startDate);
        List<TotalRoomCountUnavailable> totalRoomsUnavailable = getRoomCountUnavailable(startDate);
        Integer qteTotalRoomsOutNotAvailable = 0;
        Integer qteTotalRoomsBookedNotAvailable = 0;
        Integer qteTotalRoomsExisted = 0;
        Integer qteTotalRoomsAvailable = 0;
        
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            var roomsCountAvailable = new TotalRoomCountAvailable();           
            List<TotalRoomByType> totalRoomsAvailableByType = new ArrayList<>();
            qteTotalRoomsAvailable = 0;
            
            for (TotalRoomByType totalRoom: totalRooms) { 
                qteTotalRoomsOutNotAvailable = 0;
                qteTotalRoomsBookedNotAvailable = 0;
                
                for (TotalRoomOutOfOrderByType roomOut: totalRoomsOut) {
                    if (totalRoom.getTypeChambreId() == roomOut.getTypeChambreId() && roomOut.getDateOutOfOrder().isEqual(date)){
                        qteTotalRoomsOutNotAvailable += roomOut.getRoomCountOutOfOrder();
                    }
                }
                
                for (TotalRoomCountUnavailable roomBooked: totalRoomsUnavailable){
                    if (totalRoom.getTypeChambreId() == roomBooked.getTypeChambreId() && roomBooked.getDateUnavailable().isEqual(date)){
                        qteTotalRoomsBookedNotAvailable += roomBooked.getRoomCountUnavailable();
                    }
                }
                
                qteTotalRoomsExisted = totalRoom.getQteTotalRoom();
                
                qteTotalRoomsExisted = totalRoom.getQteTotalRoom() - qteTotalRoomsOutNotAvailable - qteTotalRoomsBookedNotAvailable;
                
                if(qteTotalRoomsExisted > 0 || qteTotalRoomsExisted == 0){
                    var totalRoomByType = new TotalRoomByType();
                    
                    totalRoomByType.setQteTotalRoom(qteTotalRoomsExisted);
                    totalRoomByType.setTypeChambreId(totalRoom.getTypeChambreId());
                    
                    totalRoomsAvailableByType.add(totalRoomByType);
                }
                qteTotalRoomsAvailable += qteTotalRoomsExisted;
            }
            
            roomsCountAvailable.setDateAvailable(date);
            roomsCountAvailable.setTotalRoomsByType(totalRoomsAvailableByType);
            roomsCountAvailable.setQteTotalRoomsAvailable(qteTotalRoomsAvailable);
            
            result.add(roomsCountAvailable);
        }
        return result;
    }
    
    public List<VPmsChambreDisponibiliteEtat> getRoomsAvailabilityStatus() {
        List<VPmsChambreDisponibiliteEtat> result = entityManager
                .createQuery("FROM VPmsChambreDisponibiliteEtat ORDER BY idTypeChambre ").getResultList();
        return result;
    }
      
}
