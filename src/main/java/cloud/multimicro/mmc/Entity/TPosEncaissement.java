/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Naly
 */
@Entity
@Table(name = "t_pos_encaissement")
@Data
public class TPosEncaissement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;

    @NotNull
    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @Column(name = "montant_ttc")
    private BigDecimal montant;

    @Column(name = "montant_tva")
    private BigDecimal montantTva;
    
    @NotNull
    @Column(name = "date_encaissement")
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEncaissement;

    
    @NotNull
    @Column(name = "pos_note_entete_id")
    private Integer posNoteEnteteId;

    @JoinColumn(name = "pos_note_entete_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosNoteEntete posNoteEntete;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;

}
