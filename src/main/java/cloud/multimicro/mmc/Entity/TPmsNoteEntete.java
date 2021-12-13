/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbTransient;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_note_entete")
@Data
public class TPmsNoteEntete  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_sejour_id")
    private Integer pmsSejourId;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "etat")
    private String etat;
    
    @NotNull
    @Column(name = "date_note")
    private LocalDateTime dateNote;
    
    @Column(name = "date_etat_solde")
    private LocalDateTime dateEtatSolde;

    @Column(name = "num_facture")
    private String numFacture;

    @Column(name = "date_facture")
    private LocalDate dateFacture;

    @Column(name = "date_annulation_facture")
    private LocalDate dateAnnulationFacture;

    @Column(name = "statut_pointage")
    private Integer statutPointage;

    @Column(name = "paiement_mmc_client_Id")
    private Integer paiementMmcClientId;

    @Column(name = "paiement_pms_sejour_Id")
    private Integer paiementPmsSejourId;

    @Column(name = "is_report_tpv")
    private Integer isReportTpv;

    @NotNull
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

    @Column(name = "pays")
    private String pays;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDateTime dateDeletion;

    public TPmsNoteEntete(){
    }
}
