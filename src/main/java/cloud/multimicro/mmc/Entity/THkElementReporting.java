/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author HERIZO-PC
 */
@Entity
@Table(name = "t_hk_element_reporting")
@Data
public class THkElementReporting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
   
    @Size(min = 1, max = 100)
    @NotNull
    @NotBlank
    @Column(name = "libelle")
    private String libelle;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
    
}
