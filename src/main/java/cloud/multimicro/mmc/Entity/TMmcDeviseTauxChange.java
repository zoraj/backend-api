/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

/**
 *
 * @author Tsiory
 */
@Entity
@Data
@Table(name = "t_mmc_devise_taux_change")
public class TMmcDeviseTauxChange implements Serializable {
    private static final long serialVersionUID = 1L;
      
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    
    @NotNull @Column(name = "mmc_devise_reference_id")  
    private Integer mmcDeviseReferenceId;
    
    @Transient
    private String mmcDeviseReferenceLibelle;
    
    
    @NotNull @Column(name = "mmc_devise_id")
    private Integer mmcDeviseId;
      
    @Transient
    private String mmcDeviseLibelle;
    
    @Column(name = "date_taux_change")
    private String dateTauxChange;
    
    
    @Column(name = "taux")
    private BigDecimal taux;

   
}
