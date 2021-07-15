/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_tarif_grille_detail_detail")
@Data
public class TPmsTarifGrilleDetailDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_tarif_grille_detail_id")
    private Integer pmsTarifGrilleDetailId;

    @NotNull
    @Column(name = "pms_prestation_id")
    private Integer pmsPrestationId;

    @Transient
    private String pmsPrestationLibelle;

    @Transient
    private String code;

    @JoinColumn(name = "pms_prestation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsPrestation pmsPrestation;

    @Column(name = "pu")
    private BigDecimal pu;

    @Column(name = "remise")
    private boolean remise;

    @Column(name = "promotion")
    private boolean promotion;

    @Column(name = "commission")
    private boolean commission;

    @Column(name = "recouche")
    private boolean recouche;

    @Column(name = "open")
    private boolean open;

    @Column(name = "is_ong")
    private String isOng;

    @Column(name = "unite")
    private String unite;

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
