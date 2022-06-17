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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_tarif_grille_detail")
@Data
public class TPmsTarifGrilleDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_tarif_grille_id")
    private Integer pmsTarifGrilleId;

    @NotNull
    @Column(name = "pms_model_tarif_id")
    private Integer pmsModelTarifId;
    
    @NotNull
    @Column(name = "pms_type_chambre_id")
    private Integer pmsTypeChambreId;

    @NotNull
    @Column(name = "base")
    private Integer base;

    @NotNull
    @Column(name = "date_tarif")
    private LocalDate dateTarif;

    @Transient
    BigDecimal rateGrid;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDateTime dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDateTime dateDeletion;

}
