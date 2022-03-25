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
import java.util.*;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "t_pms_chambre_hors_service")
@Data
public class TPmsChambreHorsService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_chambre_id")
    private Integer pmsChambreId;

    @NotNull
    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "is_soustraire_dispo")
    private Boolean isSoustraireDispo;

    private String signature;

    private String motif;

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
