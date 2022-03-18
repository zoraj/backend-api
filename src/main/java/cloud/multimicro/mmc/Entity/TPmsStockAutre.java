/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
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

/**
 *
 * @author Tsiory-PC
 */
@Entity
@Table(name = "t_pms_stock_autre")
@Data
public class TPmsStockAutre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(min = 1, max = 5)
    @NotNull @NotBlank @Column(name = "reference")
    private String reference;

    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "libelle")
    private String libelle;
    
    @NotNull
    @Column(name = "nb_par_jour")
    private Integer nbParJour;
    
    @Column(name = "is_rapport_etage")
    private Boolean isRapportEtage;
    
    @Column(name = "is_salon")
    private Boolean isSalon;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
    
}
