/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Data
@Table(name = "t_mmc_client")
public class TMmcClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(min = 1, max = 15)
    @NotNull
    @NotBlank
    @Column(name = "code")
    private String code;

    @Size(min = 1, max = 100)
    @NotNull
    @NotBlank
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @Column(name = "pms_prescripteur_id")
    private Integer pmsPrescripteurId;
    @JoinColumn(name = "pms_prescripteur_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsPrescripteur pmsPrescripteur;

    @Column(name = "mmc_type_client_id")
    private Integer mmcTypeClientId;
    @JoinColumn(name = "mmc_type_client_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcTypeClient mmcTypeClient;

    @Column(name = "mmc_segment_client_id")
    private Integer mmcSegmentClientId;
    @JoinColumn(name = "mmc_segment_client_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcSegmentClient mmcSegmentClient;

    @Column(name = "compte_comptable")
    private String compteComptable;

    @Column(name = "compte_auxiliaire")
    private String compteAuxiliaire;

    @Column(name = "num_badge")
    private String numBadge;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "qualite")
    private String qualite;

    @Column(name = "pms_type_compte_client_id")
    private Integer pmsTypeCompteClientId;

    @Column(name = "max_passage")
    private Integer maxPassage;

    @Column(name = "password")
    private String password;

    @Column(name = "mmc_societe_id")
    private Integer mmcSocieteId;

    @Transient
    private String mmcSocieteLibelle;

    @Column(name = "code_subvention")
    private String codeSubvention;

    @Column(name = "code_admission")
    private String codeAdmission;

    @Column(name = "pos_tarif_id")
    private Integer posTarifId;

    @Column(name = "nom_facturation")
    private String nomFacturation;

    @Column(name = "prenom_facturation")
    private String prenomFacturation;

    @Column(name = "adresse_facturation")
    private String adresseFacturation;

    @Column(name = "ville_facturation")
    private String villeFacturation;

    @Column(name = "code_postal_facturation")
    private String codePostalFacturation;

    @Column(name = "telephone_facturation")
    private String telephoneFacturation;

    @Column(name = "mobile_facturation")
    private String mobileFacturation;

    @Column(name = "email_facturation")
    private String emailFacturation;

    @Column(name = "pays_facturation")
    private String paysFacturation;

    @Column(name = "qualite_facturation")
    private String qualiteFacturation;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;

}
