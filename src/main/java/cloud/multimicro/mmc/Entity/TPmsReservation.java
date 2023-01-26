/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_reservation")
@Data
public class TPmsReservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "date_arrivee")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateArrivee;

    @NotNull
    @Column(name = "date_depart")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateDepart;

    @Column(name = "date_option")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateOption;
    
    @NotNull
    @Column(name = "date_saisie")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDateTime dateSaisie;

    @NotNull @Size(min = 1, max = 45)
    @Column(name = "nom_reservation")
    private String nomReservation;

    @NotNull @Size(min = 1, max = 45)
    @Column(name = "numero_reservation")
    private String numeroReservation;

    @NotNull
    @Column(name = "nb_chambre")
    private Integer nbChambre;

    @NotNull
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "nb_enf")
    private Integer nbEnfant;    

    @Size(min = 1, max = 15)    
    @Column(name = "signature")
    private String signature;

    @Column(name = "observation")
    private String observation;

    @Column(name = "brigade")
    private Integer brigade;

    @Column(name = "poste_uuid")
    private String posteUuid;

    @NotNull
    @Column(name = "reservation_type")
    private String reservationType;

    @Column(name = "mmc_client_id")
    private Integer mmcClientId;

    @Column(name = "pms_tarif_grille_id")
    private Integer pmsTarifGrilleId;

    @Column(name = "mmc_type_client_id")
    private Integer mmcTypeClientId;

    @Column(name = "mmc_segment_client_id")
    private Integer mmcSegmentClientId;

    @Column(name = "pms_prescripteur_id")
    private Integer pmsPrescripteurId;

    private String nationalite;

    @Column(name = "cardex")
    private String cardex;

    @Column(name = "civilite")
    private String civilite;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adresse1")
    private String adresse1;

    @Column(name = "adresse2")
    private String adresse2;

    @Column(name = "tel")
    private String tel;

    @Column(name = "email")
    private String email;

    @Column(name = "cp")
    private String cp;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @Column(name = "origine")
    private String origine;
    
    @Column(name = "cb_type")
    private String cbType;

    @Column(name = "cb_numero")
    private String cbNumero;

    @Column(name = "cb_titulaire")
    private String cbTitulaire;

    @Column(name = "cb_exp")
    private LocalDate cbExp;

    @Column(name = "cb_cvv")
    private String cbCvv;

    @Column(name = "motif_annulation")
    private String motifAnnulation;
    
    @JoinColumn(name = "pms_reservation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TPmsReservationVentilation> ventilations = new ArrayList<>();
    
    @JoinColumn(name = "pms_reservation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TPmsSejour> sejours = new ArrayList<>();
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
}
