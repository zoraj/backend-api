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
import javax.json.bind.annotation.JsonbDateFormat;
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
@Table(name = "t_pms_vente_comptant")
@Data
public class TPmsVenteComptant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column(name = "nom_note")
    private String nomNote;
    
    @Column(name = "date_note")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateNote;
    
    @NotNull @Column(name = "mmc_segment_client_id")
    private Integer mmcSegmentClientId;
    
    @NotNull @Column(name = "mmc_client_id")
    private Integer mmcClientId;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "commission")
    private BigDecimal commission;
    
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
