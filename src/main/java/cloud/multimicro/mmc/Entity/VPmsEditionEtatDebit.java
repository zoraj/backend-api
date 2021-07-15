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
@Table(name = "v_pms_edition_etat_debit")
@Data
public class VPmsEditionEtatDebit implements Serializable {
    
    @Id
    @Column(name = "ca_midi_jour")
    private Integer caMidiJour;
    
    @Column(name = "ca_midi_mois")
    private Integer caMidiMois;
    
    @Column(name = "ca_midi_annee")
    private Integer caMidiAnnee;
    
    @Column(name = "ca_soir_jour")
    private Integer caSoirJour;
    
    @Column(name = "ca_soir_mois")
    private Integer caSoirMois;
    
    @Column(name = "ca_soir_annee")
    private Integer caSoirAnnee;
    
    @Column(name = "nb_couvert_midi_jour")
    private Integer nbCouvertMidiJour;
    
    @Column(name = "nb_couvert_midi_mois")
    private Integer nbCouvertMidiMois;
    
    @Column(name = "nb_couvert_midi_annee")
    private Integer nbCouvertMidiAnnee;
    
    @Column(name = "nb_couvert_soir_jour")
    private Integer nbCouvertSoirJour;
    
    @Column(name = "nb_couvert_soir_mois")
    private Integer nbCouvertSoirMois;
    
    @Column(name = "nb_couvert_soir_annee")
    private Integer nbCouvertSoirAnnee;
    
    @Column(name = "prix_moyen_midi_jour")
    private Integer prixMoyenMidiJour;
    
    @Column(name = "prix_moyen_midi_mois")
    private Integer prixMoyenMidiMois;
    
    @Column(name = "prix_moyen_midi_annee")
    private Integer prixMoyenMidiAnnee;
    
    @Column(name = "prix_moyen_soir_jour")
    private Integer prixMoyenSoirJour;
    
    @Column(name = "prix_moyen_soir_mois")
    private Integer prixMoyenSoirMois;
    
    @Column(name = "prix_moyen_soir_annee")
    private Integer prixMoyenSoirAnnee;
    
    @Column(name = "ca_jour")
    private Integer caJour;
    
    @Column(name = "ca_mois")
    private Integer caMois;
    
    @Column(name = "ca_annee")
    private Integer caAnnee;
    
    @Column(name = "nb_couvert_jour")
    private Integer nbCouvertJour;
    
    @Column(name = "nb_couvert_mois")
    private Integer nbCouvertMois;
    
    @Column(name = "nb_couvert_annee")
    private Integer nbCouvertAnnee;
    
    @Column(name = "prix_moyen_jour")
    private Integer prixMoyenJour;
    
    @Column(name = "prix_moyen_mois")
    private Integer prixMoyenMois;
    
    @Column(name = "prix_moyen_annee")
    private Integer prixMoyenAnnee;
    
    @Column(name = "dont_remise_jour")
    private Integer dontRemiseJour;
    
    @Column(name = "dont_remise_mois")
    private Integer dontRemiseMois;
    
    @Column(name = "dont_remise_annee")
    private Integer dontRemiseAnnee;
    
    @Column(name = "ca_petit_dej_jour")
    private Integer caPetitDejJour;
    
    @Column(name = "ca_petit_dej_mois")
    private Integer caPetitDejMois;
    
    @Column(name = "ca_petit_dej_annee")
    private Integer caPetitDejAnnee;
    
    @Column(name = "ca_resto_jour")
    private Integer caRestoJour;
    
    @Column(name = "ca_resto_mois")
    private Integer caRestoMois;
    
    @Column(name = "ca_resto_annee")
    private Integer caRestoAnnee;
    
    @Column(name = "ca_resto_petit_dej_jour")
    private Integer caRestoPetitDejJour;
    
    @Column(name = "ca_resto_petit_dej_mois")
    private Integer caRestoPetitDejMois;
    
    @Column(name = "ca_resto_petit_dej_annee")
    private Integer caRestoPetitDejAnnee;
    
    @Column(name = "dont_remise_recep_jour")
    private Integer dontRemiseRecepJour;
    
    @Column(name = "dont_remise_recep_mois")
    private Integer dontRemiseRecepMois;
    
    @Column(name = "dont_remise_recep_annee")
    private Integer dontRemiseRecepAnnee;
    
    @Column(name = "ca_total_jour")
    private Integer caTotalJour;
    
    @Column(name = "ca_total_mois")
    private Integer caTotalMois;
    
    @Column(name = "ca_total_annee")
    private Integer caTotalAnnee;
    
}
