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
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_sejour_tarif_detail")
@Data
public class TPmsSejourTarifDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_sejour_tarif_id")
    private Integer pmsSejourTarifId;

    @NotNull
    @Column(name = "t_pms_prestation_id")
    private Integer pmsPrestationId;

    @NotNull
    @Column(name = "qte")
    private Integer qte;

    @NotNull
    @Column(name = "pu")
    private BigDecimal pu;

    @Column(name = "taux_remise")
    private BigDecimal tauxRemise;

    @Column(name = "taux_commission")
    private BigDecimal tauxCommission;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    
    @Column(name = "is_ONG")
    private String isONG;

    @Column(name = "is_extra")
    private Integer isExtra;

    @Column(name = "is_recouche")
    private Integer isRecouche;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;

    public TPmsSejourTarifDetail() {
    } 
    
}
