/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "t_pms_type_chambre")
@Data
public class TPmsTypeChambre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column(name = "libelle")
    private String libelle;

    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "reference")
    private String reference;
    
    @Column(name = "salon")
    private Boolean salon;
     
    @Column(name = "is_par_defaut")
    private Boolean isParDefaut;

    @NotNull @Column(name = "pms_categorie_chambre_id")
    private Integer pmsCategorieChambreId;

    @JoinColumn(name = "pms_type_chambre_id",referencedColumnName = "id", insertable = false, updatable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TPmsChambre> pmsChambre;

    @Transient
    private String pmsCategorieChambreLibelle;

    @Column(name = "pers_min")
    private Integer persMin;

    @Column(name = "pers_max")
    private Integer persMax;

    @Column(name = "nb_enfant")
    private Integer nbEnfant;

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
