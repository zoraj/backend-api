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
 * @author Naly
 */
@Entity
@Table(name = "t_pms_prestation")
@Data
public class TPmsPrestation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(min = 1, max = 45)
    @NotNull
    @NotBlank
    @Column(name = "code")
    private String code;

    @Size(min = 1, max = 45)
    @NotNull
    @NotBlank
    @Column(name = "libelle")
    private String libelle;

    @Column(name = "prix")
    private BigDecimal prix;

    @Column(name = "remise")
    private Boolean remise;

    @Column(name = "commission")
    private Boolean commission;

    @Column(name = "promotion")
    private Boolean promotion;

    @Column(name = "early_booking")
    private Boolean earlyBooking;
    
    @Column(name = "is_qte_chb")
    private Boolean isQteChb;

    @Column(name = "is_qte_pdj")
    private Boolean isQtePdj;
    
    @NotNull
    @Column(name = "mmc_sous_famille_ca_id")
    private Integer mmcSousFamilleCaId;

    @NotNull
    @Column(name = "mmc_famille_ca_id")
    private Integer mmcFamilleCaId;

    @NotNull
    @Column(name = "statut")
    private String statut = "AVAIL";

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeletion;

    public TPmsPrestation() {
    }
}
