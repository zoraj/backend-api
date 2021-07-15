/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_etat_debit_chambre")
@Data
public class VPmsEditionEtatDebitChambre implements Serializable {

    @Id
    @Column(name = "nbr_chbr_louable_jour")
    private Integer nbrChbrLouableJour;
    
    @Column(name = "nbr_chbr_louable_mois")
    private Integer nbrChbrLouableMois;
    
    @Column(name = "nbr_chbr_louable_annee")
    private Integer nbrChbrLouableAnnee;
    
    @Column(name = "ca_hebergement_jour")
    private Integer caHebergementJour;
    
    @Column(name = "ca_hebergement_mois")
    private Integer caHebergementMois;
    
    @Column(name = "ca_hebergement_annee")
    private Integer caHebergementAnnee;
    
    @Column(name = "remise_accorde_jour")
    private Integer remiseAccordeJour;
    
    @Column(name = "remise_accorde_mois")
    private Integer remiseAccordeMois;
    
    @Column(name = "remise_accorde_annee")
    private Integer remiseAccordeAnnee;
        
    @Column(name = "sous_total_hebergement_jour")
    private Integer sousTotalHebergementJour;
    
    @Column(name = "sous_total_hebergement_mois")
    private Integer sousTotalHebergementMois;
    
    @Column(name = "sous_total_hebergement_annee")
    private Integer sousTotalHebergementAnnee;   
    
    @Column(name = "total_ca_hebergement_jour")
    private Integer totalCaHebergementJour;
    
    @Column(name = "total_ca_hebergement_mois")
    private Integer totalCaHebergementMois;
    
    @Column(name = "total_ca_hebergement_annee")
    private Integer totalCaHebergementAnnee;
    
    @Column(name = "commission_jour")
    private Integer commissionJour;
    
    @Column(name = "commission_mois")
    private Integer commissionMois;
    
    @Column(name = "commission_annee")
    private Integer commissionAnnee;
    
    @Column(name = "nbr_chbr_vendue_jour")
    private Integer nbrChbrVendueJour;
    
    @Column(name = "nbr_chbr_vendue_mois")
    private Integer nbrChbrVendueMois;
    
    @Column(name = "nbr_chbr_vendue_annee")
    private Integer nbrChbrVendueAnnee;
    
    @Column(name = "taux_occupation_jour")
    private Integer tauxOccupationJour;
    
    @Column(name = "taux_occupation_mois")
    private Integer tauxOccupationMois;
    
    @Column(name = "taux_occupation_annee")
    private Integer tauxOccupationAnnee;
    
    @Column(name = "nbr_client_jour")
    private Integer nbrClientJour;
    
    @Column(name = "nbr_client_mois")
    private Integer nbrClientMois;
    
    @Column(name = "nbr_client_annee")
    private Integer nbrClientAnnee;
    
    @Column(name = "indice_frequentation_jour")
    private Integer indiceFrequentationJour;
    
    @Column(name = "indice_frequentation_mois")
    private Integer indiceFrequentationMois;
    
    @Column(name = "indice_frequentation_annee")
    private Integer indiceFrequentationAnnee;
    
    @Column(name = "sejour_moyen_jour")
    private Integer sejourMoyenJour;
    
    @Column(name = "sejour_moyen_mois")
    private Integer sejourMoyenMois;
    
    @Column(name = "sejour_moyen_annee")
    private Integer sejourMoyenAnnee;
    
    @Column(name = "prix_moyen_jour")
    private Integer prixMoyenJour;
    
    @Column(name = "prix_moyen_mois")
    private Integer prixMoyenMois;
    
    @Column(name = "prix_moyen_annee")
    private Integer prixMoyenAnnee;
    
    @Column(name = "ca_petit_dej_jour")
    private Integer caPetitDejJour;
    
    @Column(name = "ca_petit_dej_mois")
    private Integer caPetitDejMois;
    
    @Column(name = "ca_petit_dej_annee")
    private Integer caPetitDejAnnee;
    
    @Column(name = "remise_petit_dej_jour")
    private Integer remisePetitDejJour;
    
    @Column(name = "remise_petit_dej_mois")
    private Integer remisePetitDejMois;
    
    @Column(name = "remise_petit_dej_annee")
    private Integer remisePetitDejAnnee;
    
    @Column(name = "ca_petit_dej_net_jour")
    private Integer caPetitDejNetJour;
    
    @Column(name = "ca_petit_dej_net_mois")
    private Integer caPetitDejNetMois;
    
    @Column(name = "ca_petit_dej_net_annee")
    private Integer caPetitDejNetAnnee;
    
    @Column(name = "nbr_petit_dej_jour")
    private Integer nbrPetitDejJour;
    
    @Column(name = "nbr_petit_dej_mois")
    private Integer nbrPetitDejMois;
    
    @Column(name = "nbr_petit_dej_annee")
    private Integer nbrPetitDejAnnee;
    
    @Column(name = "prix_moyen_chbr_ttc_jour")
    private Integer prixMoyenChbrTtcJour;
    
    @Column(name = "prix_moyen_chbr_ttc_mois")
    private Integer prixMoyenChbrTtcMois;
    
    @Column(name = "prix_moyen_chbr_ttc_annee")
    private Integer prixMoyenChbrTtcAnnee;
    
    @Column(name = "taux_captage_jour")
    private Integer tauxCaptageJour;
    
    @Column(name = "taux_captage_mois")
    private Integer tauxCaptageMois;
    
    @Column(name = "taux_captage_annee")
    private Integer tauxCaptageAnnee;
    
    @Column(name = "service_ht_petit_dej_jour")
    private Integer serviceHtPetitDejJour;
    
    @Column(name = "service_ht_petit_dej_mois")
    private Integer serviceHtPetitDejMois;
    
    @Column(name = "service_ht_petit_dej_annee")
    private Integer serviceHtPetitDejAnnee;  
    
    @Column(name = "sous_total_petit_dej_jour")
    private Integer sousTotalPetitDejJour;
    
    @Column(name = "sous_total_petit_dej_mois")
    private Integer sousTotalPetitDejMois;
    
    @Column(name = "sous_total_petit_dej_annee")
    private Integer sousTotalPetitDejAnnee;    
    
    @Column(name = "commission_petit_dej_jour")
    private Integer commissionPetitDejJour;
    
    @Column(name = "commission_petit_dej_mois")
    private Integer commissionPetitDejMois;
    
    @Column(name = "commission_petit_dej_annee")
    private Integer commissionPetitDejAnnee;
    
    @Column(name = "ca_petit_dej_total_jour")
    private Integer caPetitDejTotalJour;
    
    @Column(name = "ca_petit_dej_total_mois")
    private Integer caPetitDejTotalMois;
    
    @Column(name = "ca_petit_dej_total_annee")
    private Integer caPetitDejTotalAnnee;
    
    @Column(name = "ca_resto_jour")
    private Integer caRestoJour;
    
    @Column(name = "ca_resto_mois")
    private Integer caRestoMois;
    
    @Column(name = "ca_resto_annee")
    private Integer caRestoAnnee;
    
    @Column(name = "remise_resto_jour")
    private Integer remiseRestoJour;
    
    @Column(name = "remise_resto_mois")
    private Integer remiseRestoMois;
    
    @Column(name = "remise_resto_annee")
    private Integer remiseRestoAnnee;
    
    @Column(name = "ca_resto_net_jour")
    private Integer caRestoNetJour;
    
    @Column(name = "ca_resto_net_mois")
    private Integer caRestoNetMois;
    
    @Column(name = "ca_resto_net_annee")
    private Integer caRestoNetAnnee;
    
    @Column(name = "nbr_couvert_jour")
    private Integer nbrCouvertJour;
    
    @Column(name = "nbr_couvert_mois")
    private Integer nbrCouvertMois;
    
    @Column(name = "nbr_couvert_annee")
    private Integer nbrCouvertAnnee;
    
    @Column(name = "taux_captage_client_jour")
    private Integer tauxCaptageClientJour;
    
    @Column(name = "taux_captage_client_mois")
    private Integer tauxCaptageClientMois;
    
    @Column(name = "taux_captage_client_annee")
    private Integer tauxCaptageClientAnnee;
    
    @Column(name = "prix_moyen_ttc_jour")
    private Integer prixMoyenTtcJour;
    
    @Column(name = "prix_moyen_ttc_mois")
    private Integer prixMoyenTtcMois;
    
    @Column(name = "prix_moyen_ttc_annee")
    private Integer prixMoyenTtcAnnee;
    
    @Column(name = "service_ht_resto_jour")
    private Integer serviceHtRestoJour;
    
    @Column(name = "service_ht_resto_mois")
    private Integer serviceHtRestoMois;
    
    @Column(name = "service_ht_resto_annee")
    private Integer serviceHtRestoAnnee;   
    
    @Column(name = "sous_total_ht_resto_jour")
    private Integer sousTotalHtRestoJour;
    
    @Column(name = "sous_total_ht_resto_mois")
    private Integer sousTotalHtRestoMois;
    
    @Column(name = "sous_total_ht_resto_annee")
    private Integer sousTotalHtRestoAnnee;
     
    @Column(name = "commission_resto_jour")
    private Integer commissionRestoJour;
    
    @Column(name = "commission_resto_mois")
    private Integer commissionRestoMois;
    
    @Column(name = "commission_resto_annee")
    private Integer commissionRestoAnnee;
    
    @Column(name = "ca_resto_total_jour")
    private Integer caRestoTotalJour;
    
    @Column(name = "ca_resto_total_mois")
    private Integer caRestoTotalMois;
    
    @Column(name = "ca_resto_total_annee")
    private Integer caRestoTotalAnnee;
    
    @Column(name = "ca_global_resto_jour")
    private Integer caGlobalRestoJour;
    
    @Column(name = "ca_global_resto_mois")
    private Integer caGlobalRestoMois;
    
    @Column(name = "ca_global_resto_annee")
    private Integer caGlobalRestoAnnee;
    
    @Column(name = "total_remise_jour")
    private Integer totalRemiseJour;
    
    @Column(name = "total_remise_mois")
    private Integer totalRemiseMois;
    
    @Column(name = "total_remise_annee")
    private Integer totalRemiseAnnee;
    
    @Column(name = "ca_global_resto_net_jour")
    private Integer caGlobalRestoNetJour;
    
    @Column(name = "ca_global_resto_net_mois")
    private Integer caGlobalRestoNetMois;
    
    @Column(name = "ca_global_resto_net_annee")
    private Integer caGlobalRestoNetAnnee;
    
    @Column(name = "service_ht_global_resto_jour")
    private Integer serviceHtGlobalRestoJour;
    
    @Column(name = "service_ht_global_resto_mois")
    private Integer serviceHtGlobalRestoMois;
    
    @Column(name = "service_ht_global_resto_annee")
    private Integer serviceHtGlobalRestoAnnee;  
    
    @Column(name = "sous_total_ht_global_resto_jour")
    private Integer sousTotalHtGlobalRestoJour;
    
    @Column(name = "sous_total_ht_global_resto_mois")
    private Integer sousTotalHtGlobalRestoMois;
    
    @Column(name = "sous_total_ht_global_resto_annee")
    private Integer sousTotalHtGlobalRestoAnnee;    
    
    @Column(name = "total_commission_jour")
    private Integer totalCommissionJour;
    
    @Column(name = "total_commission_mois")
    private Integer totalCommissionMois;
    
    @Column(name = "total_commission_annee")
    private Integer totalCommissionAnnee;
    
    @Column(name = "ca_global_resto_ttc_jour")
    private Integer caGlobalRestoTtcJour;
    
    @Column(name = "ca_global_resto_ttc_mois")
    private Integer caGlobalRestoTtcMois;
    
    @Column(name = "ca_global_resto_ttc_annee")
    private Integer caGlobalRestoTtcAnnee;
    
    @Column(name = "ca_telephone_jour")
    private Integer caTelephoneJour;
    
    @Column(name = "ca_telephone_mois")
    private Integer caTelephoneMois;
    
    @Column(name = "ca_telephone_annee")
    private Integer caTelephoneAnnee;
    
    @Column(name = "prix_moyen_chbr_vendu_jour")
    private Integer prixMoyenChbrVenduJour;
    
    @Column(name = "prix_moyen_chbr_vendu_mois")
    private Integer prixMoyenChbrVenduMois;
    
    @Column(name = "prix_moyen_chbr_vendu_annee")
    private Integer prixMoyenChbrVenduAnnee;
    
    @Column(name = "ca_total_vente_jour")
    private Integer caTotalVenteJour;
    
    @Column(name = "ca_total_vente_mois")
    private Integer caTotalVenteMois;
    
    @Column(name = "ca_total_vente_annee")
    private Integer caTotalVenteAnnee;
    
    @Column(name = "ca_total_ttc_jour")
    private Integer caTotalTtcJour;
    
    @Column(name = "ca_total_ttc_mois")
    private Integer caTotalTtcMois;
    
    @Column(name = "ca_total_ttc_annee")
    private Integer caTotalTtcAnnee;
    
    @Column(name = "taxe_sejour_jour")
    private Integer taxeSejourJour;
    
    @Column(name = "taxe_sejour_mois")
    private Integer taxeSejourMois;
    
    @Column(name = "taxe_sejour_annee")
    private Integer taxeSejourAnnee;
    
    @Column(name = "autre_debour_jour")
    private Integer autreDebourJour;
    
    @Column(name = "autre_debour_mois")
    private Integer autreDebourMois;
    
    @Column(name = "autre_debour_annee")
    private Integer autreDebourAnnee;
    
    @Column(name = "ca_total_hors_commission_jour")
    private Integer caTotalHorsCommissionJour;
    
    @Column(name = "ca_total_hors_commission_mois")
    private Integer caTotalHorsCommissionMois;
    
    @Column(name = "ca_total_hors_commission_annee")
    private Integer caTotalHorsCommissionAnnee;
    
    @Column(name = "total_commission_final_jour")
    private Integer totalCommissionFinalJour;
    
    @Column(name = "total_commission_final_mois")
    private Integer totalCommissionFinalMois;
    
    @Column(name = "total_commission_final_annee")
    private Integer totalCommissionFinalAnnee;
    
    @Column(name = "total_ca_general_jour")
    private Integer totalCaGeneralJour;
    
    @Column(name = "total_ca_general_mois")
    private Integer totalCaGeneralMois;
    
    @Column(name = "total_ca_general_annee")
    private Integer totalCaGeneralAnnee;
    
    @Column(name = "rev_ttc_jour")
    private Integer revTtcJour;
    
    @Column(name = "rev_ttc_mois")
    private Integer revTtcMois;
    
    @Column(name = "rev_ttc_annee")
    private Integer revTtcAnnee;
}
