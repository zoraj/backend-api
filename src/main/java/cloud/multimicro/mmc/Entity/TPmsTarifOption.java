/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "t_pms_tarif_option")
@Data
public class TPmsTarifOption implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "libelle")
    private String libelle;
    
    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "icone_img")
    private String iconeImg;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
    
    @Transient
    private boolean isChecked;
}
