/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_saison")
@Data
public class TPmsSaison implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Size(min = 1, max = 10)
    @NotNull @NotBlank @Column(name = "reference")
    private String reference;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column(name = "libelle")
    private String libelle;
    
    @JsonbTransient
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;
    
    @JsonbTransient
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @JsonbTransient
    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
}