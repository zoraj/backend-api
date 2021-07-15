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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_arrhes")
@Data
public class TPmsArrhe implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pms_reservation_id")
    private Integer pmsReservationId;
    
    @NotNull @Column(name = "montant")
    private BigDecimal montant;

    @NotNull
    @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;

    @NotNull
    @Column(name = "mmc_client_id")
    private Integer mmcClientId;
    
    @Column(name = "observation")
    private String observation;

    @NotNull
    @Column(name = "date_reglement")
    private LocalDate dateReglement;
    
    @Column(name = "date_remboursement")
    private LocalDate dateRemboursement;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
}
