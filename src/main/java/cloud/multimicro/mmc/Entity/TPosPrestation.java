/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */

package cloud.multimicro.mmc.Entity;

import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Table(name = "t_pos_prestation")
@Data
public class TPosPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(min = 1, max = 45)
    @NotNull
    @NotBlank
    @Column(name = "libelle")
    private String libelle;

    @Size(min = 1, max = 10)
    @NotNull
    @NotBlank
    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private String type;

    @Column(name = "saisie_type")
    private Boolean saisieType;

    @Column(name = "couleur")
    private String couleur;

    @Column(name = "autorise_vae")
    private Boolean autoriseVAE;

    private BigDecimal prix;

    @Column(name = "prix_minimum")
    private BigDecimal prixMinimum;

    @Column(name = "prix_a_emporter")
    private BigDecimal prixAEmporter;

    @Column(name = "cde_cuisine")
    private Boolean cdeCuisine;

    @Column(name = "soumise_remise")
    private Boolean soumiseRemise;

    @Column(name = "gerer_chambre_pdj")
    private Boolean gererChambrePdj;

    @Column(name = "soumis_happy_hours")
    private Boolean soumisHappyHours;

    @Column(name = "prix_happy_hours")
    private BigDecimal prixHappyHours;

    @Column(name = "gestion_point")
    private Integer gestionPoint;

    @Column(name = "val_point")
    private BigDecimal valPoint;

    @Size(max = 45)
    @Column(name = "icone_img")
    private String iconeImg;

    @Column(name = "multi_ca")
    private Boolean multiCa;

    @Column(name = "gere_en_stock")
    private Boolean gererStock;

    @Column(name = "coef_exploit")
    private BigDecimal coefExploit;

    @Size(max = 45)
    @Column(name = "code_fiche_technique")
    private String codeFicheTechnique;

    @Column(name = "utilise_fiche_technique")
    private Boolean utiliseFicheTechnique;

    @Column(name = "mmc_sous_famille_ca_id")
    private Integer mmcSousFamilleCaId;

    @JoinColumn(name = "mmc_sous_famille_ca_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcSousFamilleCa mmcSousFamilleCa;

    @Column(name = "gestion_type")
    private String gestionType;

    @Column(name = "pos_sirop_parfum_categorie_id")
    private Integer posSiropParfumCategorieId;

    @Column(name = "pos_prestation_groupe_id")
    private Integer posPrestationGroupeId;

    @JoinColumn(name = "pos_prestation_groupe_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosPrestationGroupe posPrestationGroupe;

    @Column(name = "mmc_societe_id")
    private Integer mmcSocieteId;

    @Column(name = "position_commande")
    private Integer positionCommande;

    @Column(name = "nature")
    private String nature;
    
    @Size(max = 200)
    @Column(name = "description")
    private String description;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDate dateDeletion;

}
