/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Set;
import java.time.LocalDate;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "t_pos_prestation_groupe")
@Data
public class TPosPrestationGroupe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column(name = "code")
    private String code;

    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "libelle")
    private String libelle;
    
    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "icone_img")
    private String iconeImg;
    
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
