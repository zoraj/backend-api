/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
@Table(name = "t_pos_note_approvision")
@Data
public class TPosNoteApprovision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @NotNull @Column(name = "pos_note_entete_id")
    private Integer posNoteEnteteId;
    
    @JoinColumn(name = "pos_note_entete_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosNoteEntete posNoteEntete;
    
    @NotNull @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;
    
    @JoinColumn(name = "mmc_mode_encaissement_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcModeEncaissement mmcModeEncaissement;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
  
}
