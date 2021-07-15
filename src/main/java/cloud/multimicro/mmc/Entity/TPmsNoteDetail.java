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
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_note_detail")
@Data
public class TPmsNoteDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_note_entete_id")
    private Integer pmsNoteEnteteId;

    @NotNull
    @Column(name = "pms_prestation_id")
    private Integer pmsPrestationId;

    @Column(name = "qte")
    private Integer qte;

    @Column(name = "pu")
    private BigDecimal pu;

    @Column(name = "taux_remise")
    private BigDecimal tauxRemise;

    @Column(name = "taux_commission")
    private BigDecimal tauxCommission;
    
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    
    @NotNull
    @Column(name = "date_note")
    private LocalDate dateNote;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDateTime dateDeletion;

    public TPmsNoteDetail() {
    }

    public TPmsNoteDetail(Integer id) {
        this.id = id;
    }
}
