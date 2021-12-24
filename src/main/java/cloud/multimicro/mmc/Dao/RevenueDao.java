/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.VPmsCa;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
public class RevenueDao {
    @PersistenceContext
    private EntityManager entityManager;

    // v_pos_ca
    public List<VPmsCa> getVPmsCa() {
            List<VPmsCa> caLists = entityManager.createQuery("FROM VPmsCa ").getResultList();
            return caLists;
    }
    
    public BigDecimal sumCaDayBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) =:dateReference  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal sumCaMonthBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ORDER BY id_famille, id_sous_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal sumCaYearBySubFamily(Integer familleId, Integer sousFamilleId, String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId AND id_sous_famille =:sousFamilleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ORDER BY id_famille, id_sous_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("sousFamilleId", sousFamilleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public BigDecimal totalSumCaDayByFamily(Integer familleId, String dateReference) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) =:dateReference ORDER BY id_famille  ")
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference).getSingleResult();       
    }
    
    public BigDecimal totalSumCaMonthByFamily(Integer familleId, String dateReference, String dateMois) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_mois =:dateMois ORDER BY id_famille  ")      
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateMois", dateMois).getSingleResult();       
    }
    
    public BigDecimal totalSumCaYearByFamily(Integer familleId, String dateReference, String dateAnnee) {
        return (BigDecimal) entityManager.createNativeQuery("SELECT IFNULL(SUM(montant_ca), 0) FROM v_pms_ca "
                + "WHERE id_famille =:familleId "
                + "AND Date(date_ca) <=:dateReference AND date_ca_annee =:dateAnnee ORDER BY id_famille  ")      
                .setParameter("familleId", familleId)
                .setParameter("dateReference", dateReference)
                .setParameter("dateAnnee", dateAnnee).getSingleResult();       
    }
    
    public JsonArray getAllListCaByFamily(String dateReference) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsCa  ");
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        String dateLog = settingData.getValeur();
        var dateEntry = "";
        var dateMonthEntry = "";
        var dateYearEntry = "";
        
        if (!Objects.isNull(dateReference)) {
            stringBuilder.append(" WHERE Date(dateCa) <= '" + dateReference + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateReference.substring(0, 4);
            dateMonthEntry = dateReference.substring(0, 7);
            dateEntry = dateReference;
        } else {
            stringBuilder.append(" WHERE Date(dateCa) <= '" + dateLog + "' ORDER BY idFamille, idSousFamille ");
            dateYearEntry = dateLog.substring(0, 4);
            dateMonthEntry = dateLog.substring(0, 7);
            dateEntry = dateLog;
        }       
        List<VPmsCa> listCa = entityManager.createQuery(stringBuilder.toString()).getResultList();
        
        var caResults = Json.createArrayBuilder();
        var familyResults = Json.createArrayBuilder();
        
        if (listCa.size() > 0) {
            VPmsCa valueListCa = listCa.get(0);
            Integer idFamilyInitial = valueListCa.getIdFamille();
            Integer idSousfamilleInitial = valueListCa.getIdSousFamille();

            var libelleSousFamille = "";
            var libelleFamille = "";
            
            libelleFamille = valueListCa.getLibelleFamille();
            libelleSousFamille = valueListCa.getLibelleSousFamille();
            
            var montantCaJour   = BigDecimal.ZERO;
            //var montantCaPeriode = new BigDecimal("0");
            var montantCaMois   = BigDecimal.ZERO;
            var montantCaAnnee  = BigDecimal.ZERO;
            
            var totalMontantCaJour      = BigDecimal.ZERO;
            //var totalMontantCaPeriode = BigDecimal.ZERO;
            var totalMontantCaMois      = BigDecimal.ZERO;
            var totalMontantCaAnnee     = BigDecimal.ZERO;
            
            for (VPmsCa caList : listCa) {
                if (idFamilyInitial.equals(caList.getIdFamille())) {
                    totalMontantCaJour = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    totalMontantCaMois = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);                              
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                    
                    if(idSousfamilleInitial.equals(caList.getIdSousFamille())){                        
                        //cumul CA par sous famille
                        montantCaJour   = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois   = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee  = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                        
                    } else {
                        var caJson = Json.createObjectBuilder()
                            .add("libelleSousFamille", libelleSousFamille)
                            .add("montantCaJour", montantCaJour)
                            .add("montantCaPeriode", montantCaJour)
                            .add("montantCaMois", montantCaMois)
                            .add("montantCaAnnee", montantCaAnnee).build();
                        familyResults.add(caJson);
                           
                        //remise a zero cumul CA par sous famille
                        montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                        montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                        montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                        idSousfamilleInitial    = caList.getIdSousFamille();
                        libelleSousFamille      = caList.getLibelleSousFamille();
   
                    }
                               
                } else {                    
                    var caJson = Json.createObjectBuilder()
                            .add("libelleSousFamille", libelleSousFamille)
                            .add("montantCaJour", montantCaJour)
                            .add("montantCaPeriode", montantCaJour)
                            .add("montantCaMois", montantCaMois)
                            .add("montantCaAnnee", montantCaAnnee).build();
                        familyResults.add(caJson);
                           
                    var object = Json.createObjectBuilder()
                            .add("libelleFamille", libelleFamille)
                            .add("listByFamily", familyResults)
                            .add("totalMontantCaJour", totalMontantCaJour)
                            .add("totalMontantCaPeriode", totalMontantCaJour)
                            .add("totalMontantCaMois", totalMontantCaMois)
                            .add("totalMontantCaAnnee", totalMontantCaAnnee).build();

                    caResults.add(object);
                   
                    familyResults = Json.createArrayBuilder();
                    
                    idFamilyInitial         = caList.getIdFamille();
                    libelleFamille          = caList.getLibelleFamille();                   
                    idSousfamilleInitial    = caList.getIdSousFamille();
                    libelleSousFamille      = caList.getLibelleSousFamille();
                    
                    montantCaJour           = BigDecimal.ZERO;
                    montantCaMois           = BigDecimal.ZERO;
                    montantCaAnnee          = BigDecimal.ZERO;
                    
                    montantCaJour           = sumCaDayBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry);
                    montantCaMois           = sumCaMonthBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateMonthEntry);
                    montantCaAnnee          = sumCaYearBySubFamily(caList.getIdFamille(), caList.getIdSousFamille(), dateEntry, dateYearEntry);
                    
                    totalMontantCaJour      = BigDecimal.ZERO;
                    //totalMontantCaPeriode   = BigDecimal.ZERO;
                    totalMontantCaMois      = BigDecimal.ZERO;
                    totalMontantCaAnnee     = BigDecimal.ZERO;
                     
                    totalMontantCaJour  = totalSumCaDayByFamily(caList.getIdFamille(), dateEntry);
                    //totalMontantCaPeriode   = totalMontantCaPeriode.add(montantCaJour);
                    totalMontantCaMois  = totalSumCaMonthByFamily(caList.getIdFamille(), dateEntry, dateMonthEntry);
                    totalMontantCaAnnee = totalSumCaYearByFamily(caList.getIdFamille(), dateEntry, dateYearEntry);
                }
            }
            
            var caJson = Json.createObjectBuilder()
                        .add("libelleSousFamille", libelleSousFamille)
                        .add("montantCaJour", montantCaJour)
                        .add("montantCaPeriode", montantCaJour)
                        .add("montantCaMois", montantCaMois)
                        .add("montantCaAnnee", montantCaAnnee).build();
                       familyResults.add(caJson);

            var object = Json.createObjectBuilder()
                        .add("libelleFamille", libelleFamille)
                        .add("listByFamily", familyResults)
                        .add("totalMontantCaJour", totalMontantCaJour)
                        .add("totalMontantCaPeriode", totalMontantCaJour)
                        .add("totalMontantCaMois", totalMontantCaMois)
                        .add("totalMontantCaAnnee", totalMontantCaAnnee).build();
            
            caResults.add(object);
        }
        return caResults.build();
    }

}
