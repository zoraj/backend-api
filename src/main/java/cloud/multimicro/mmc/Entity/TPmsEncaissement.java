/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * @author Naly
 */
@Entity
@Table(name = "t_pms_encaissement")
@Data
public class TPmsEncaissement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;

    @Column(name = "pms_note_entete_id")
    private Integer pmsNoteEnteteId;

    @NotNull
    @Column(name = "date_encaissement")
    private LocalDate dateEncaissement;

    @NotNull
    @Column(name = "poste_uuid")
    private String posteUuid;

    @NotNull
    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @Column(name = "montant")
    private BigDecimal montant;
    
    @NotNull
    @Column(name = "devise")
    private String devise;

    @Column(name = "observation")
    private String observation;
    
    @Column(name = "is_reglmt_debiteur")
    private Boolean isReglmtDebiteur;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;
}