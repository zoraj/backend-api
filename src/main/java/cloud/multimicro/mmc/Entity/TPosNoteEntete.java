/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pos_note_entete")
@Data
public class TPosNoteEntete implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "num_table")
    private Integer numTable;

    @Size(min = 1, max = 12)
    @Column(name = "num_facture")
    private String numFacture;

    @Column(name = "nb_couvert")
    private Integer nbCouvert;

    @NotNull
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_note")
    private LocalDateTime dateNote;
    
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_emportee")
    private LocalDateTime dateEmportee;

    @Column(name = "montant_remise")
    private BigDecimal montantRemise;

    @Column(name = "pourcentage_remise")
    private Double pourcentageRemise;

    @Column(name = "mmc_client_id")
    private Integer mmcClientId;

    @JoinColumn(name = "mmc_client_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcClient mmcClientObject;

    @Size(min = 1, max = 128)
    @NotNull
    @NotBlank
    @Column(name = "poste_uuid")
    private String posteUuid;

    @Column(name = "service")
    private String service;

    @Column(name = "etat")
    private String etat;

    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @Transient
    private String mmcClient;

    @Transient
    private int nbPassage;

    @Transient
    private BigDecimal montant;

    @Column(name = "adresse_livraison")
    private String adresseLivraison;

    @Column(name = "telephone_livraison")
    private String telephoneLivraison;

    @Column(name = "pos_client_vae_id")
    private Integer posClientVaeId;

    @Column(name = "pos_activite_id")
    private Integer posActiviteId;

    @Column(name = "code_promo")
    private String codePromo;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "note_vae")
    private String noteVae;

    @Column(name = "cb_numero")
    private String cbNumero;

    @Column(name = "cb_titulaire")
    private String cbTitulaire;

    @Column(name = "cb_exp")
    private LocalDate cbExp;

    @Column(name = "cb_cvv")
    private String cbCvv;

    @Column(name = "date_reservation")
    private LocalDate dateReservation;

    @Column(name = "facturation_type")
    private String facturationType;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDateTime dateDeletion;
}