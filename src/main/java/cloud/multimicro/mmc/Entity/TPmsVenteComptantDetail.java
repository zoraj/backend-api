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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Naly-PC
 */
@Entity
@Table(name = "t_pms_vente_comptant_detail")
@Data
public class TPmsVenteComptantDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @NotNull @Column(name = "pms_vente_comptant_id")
    private Integer pmsVenteComptantId;

    @NotNull @Column(name = "pms_prestation_id")
    private Integer pmsPrestationId;

    @Column(name = "qte")
    private Integer qte;
    
    @Column(name = "pu")
    private BigDecimal pu;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "commission")
    private BigDecimal commission;
    
    @Size(min = 1, max = 45)
    @Column(name = "origine")
    private String origine;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
}
