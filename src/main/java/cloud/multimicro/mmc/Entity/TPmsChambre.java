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

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "t_pms_chambre")
@Data
public class TPmsChambre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Size(min = 1, max = 4)
    @NotNull
    @NotBlank
    @Column(name = "numero_chambre")
    private String numeroChambre;

    
    @Column(name = "numero_etage")
    private Integer numeroEtage;

    @NotNull
    @Column(name = "etat_chambre")
    private String etatChambre;

    @NotNull
    @Column(name = "pms_type_chambre_id")
    private Integer pmsTypeChambreId;

    @Transient
    private String pmsTypeChambreLibelle;

    @Transient
    private String pmsCategorieChambreLibelle;

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
