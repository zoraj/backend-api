/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Naly-PC
 */
@Stateless
public class TurnoverDao {
    @PersistenceContext
    EntityManager entityManager;
    
    private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(TurnoverDao.class);
    
    //SOMME CA BY ACTIVITY
    public BigDecimal sumCaDayByActivity(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId "
                + "AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthByActivity(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearByActivity(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME CA BY ACTIVITY AND SERVICE MIDI
    public BigDecimal sumCaDayByActivityMidi(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'M' "
                + "AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthByActivityMidi(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'M' "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearByActivityMidi(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'M' "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME CA BY ACTIVITY AND SERVICE SOIR
    public BigDecimal sumCaDayByActivitySoir(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'S' "
                + "AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthByActivitySoir(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'S' "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearByActivitySoir(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'S' "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME NOMBRE COUVERT BY ACTIVITY
    public BigDecimal totalNbCouvertDayByActivity(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();
    }
    
    public BigDecimal totalNbCouvertMonthByActivity(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalNbCouvertYearByActivity(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME NOMBRE COUVERT BY ACTIVITY AND SERVICE MIDI
    public BigDecimal totalNbCouvertDayByActivityMidi(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId  AND service = 'M' AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();
    }
    
    public BigDecimal totalNbCouvertMonthByActivityMidi(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'M' "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalNbCouvertYearByActivityMidi(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'M' "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME NOMBRE COUVERT BY ACTIVITY AND SERVICE SOIR
    public BigDecimal totalNbCouvertDayByActivitySoir(Integer activityId, LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId  AND service = 'S' AND Date(date_ca) =:dateReference  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference).getSingleResult();
    }
    
    public BigDecimal totalNbCouvertMonthByActivitySoir(Integer activityId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'S' "
                + "AND Date(date_ca) <=:dateReference AND year_month_note =:dateMois ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalNbCouvertYearByActivitySoir(Integer activityId, LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(nb_couvert), 0) FROM v_pos_ca "
                + "WHERE pos_activite_id =:activityId AND service = 'S' "
                + "AND Date(date_ca) <=:dateReference AND year_note =:dateAnnee ORDER BY pos_activite_id  ")
                .setParameter("activityId", activityId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME CA RECEPTION
    public BigDecimal sumCaDayPdj(LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE Date(date_ca) =:dateReference "
                + "AND ca_pdj = 1 ")
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthPdj(LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE ca_pdj = 1 "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearPdj(LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE ca_pdj = 1 "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME CA HEBERGEMENT
    public BigDecimal sumCaDayChb(LocalDate dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE Date(date_ca) =:dateReference "
                + "AND ca_chb = 1 ")
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthChb(LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE ca_chb = 1 "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearChb(LocalDate dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE ca_chb = 1 "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ")
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    //SOMME CA BY MODE ENCAISSEMENT
    public BigDecimal sumCaMonthByModeCashing(Integer modeEncaissementId, LocalDate dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_encaissement "
                + "WHERE id_mode_encaissement =:modeEncaissementId "
                + "AND Date(date_encaissement) <=:dateReference AND date_encaissement_mois =:dateMois ORDER BY id_mode_encaissement  ")
                .setParameter("modeEncaissementId", modeEncaissementId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
}