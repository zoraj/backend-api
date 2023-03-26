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

import javax.json.JsonValue;
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
 * @author Tsiory
 */
@Entity
@Table(name = "t_pos_note_detail")
@Data
public class TPosNoteDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pos_note_entete_id")
    private Integer posNoteEnteteId;

    @JoinColumn(name = "pos_note_entete_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosNoteEntete posNoteEntete;

    @NotNull
    @Column(name = "pos_prestation_id")
    private Integer posPrestationId;

    @JoinColumn(name = "pos_prestation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosPrestation posPrestation;

    @NotNull
    @Column(name = "pos_prestation_prix")
    private BigDecimal posPrestationPrix;
    
    @NotNull
    @Column(name = "devise")
    private String devise;

    @NotNull
    @Column(name = "ordre")
    private Integer ordre;

    @NotNull
    @Column(name = "qte")
    private Integer qte;

    @NotNull
    @Column(name = "qte_cde_marche")
    private Integer qteCdeMarche;

    @NotNull
    @Column(name = "vente_type")
    private String venteType;

    @Column(name = "date_emportee")
    private LocalDate dateEmportee;

    @NotNull
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_note")
    private LocalDateTime dateNote;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDateTime dateDeletion;

}
