/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_sejour")
@Data
public class TPmsSejour implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "mmc_client_id")
    private Integer mmcClientId;

    @NotNull
    @Column(name = "pms_tarif_grille_id")
    private Integer pmsTarifGrilleId;

    @NotNull
    @Column(name = "mmc_type_client_id")
    private Integer mmcTypeClientId;

    @NotNull
    @Column(name = "mmc_segment_client_id")
    private Integer mmcSegmentClientId;

    @NotNull
    @Column(name = "pms_prescripteur_id")
    private Integer pmsPrescripteurId;
    
    @Column(name = "pms_reservation_id")
    private Integer pmsReservationId;

    @NotNull
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @NotNull
    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "pms_type_chambre_id")
    private Integer pmsTypeChambreId;

    @Column(name = "pms_chambre_id")
    private Integer pmsChambreId;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "nb_pax")
    private Integer nbPax;

    @Column(name = "nb_enfant")
    private Integer nbEnfant;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "poste_uuid")
    private String posteUuid;

    @NotNull
    @Column(name = "brigade")
    private Integer brigade;

    @NotNull
    @Column(name = "date_note")
    private LocalDateTime dateNote;
    
    @NotNull
    @Column(name = "statut")
    private String statut;

    @Column(name = "date_annulation")
    private LocalDate dateAnnulation;

    @Column(name = "civilite")
    private String civilite;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "adresse_comp")
    private String adresseComp;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "ville")
    private String ville;

    @NotNull
    @Column(name = "pays")
    private String pays;

    @Column(name = "tel_mobile")
    private String telMobile;

    @Column(name = "email")
    private String email;

    @Column(name = "observation")
    private String observation;

    @Column(name = "carte_paiement_type")
    private String cartePaiementType;

    @Column(name = "carte_paiement_numero")
    private String cartePaiementNumero;

    @Column(name = "carte_paiement_expiration")
    private LocalDate cartePaiementExpiration;

    @Column(name = "carte_paiement_titulaire")
    private String cartePaiementTitulaire;

    @Column(name = "carte_paiement_cvv")
    private String cartePaiementCVV;
    
    @Transient
    String contentReminder;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;

    public TPmsSejour() {
    }
}
