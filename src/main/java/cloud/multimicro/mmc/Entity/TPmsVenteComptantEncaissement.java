/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * @author andy
 */
@Entity
@Table(name = "t_pms_vente_comptant_encaissement")
@Data
public class TPmsVenteComptantEncaissement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @NotNull
    @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;
    
    @NotNull
    @Column(name = "pms_vente_comptant_id")
    private Integer pmsVenteComptantId;
    
    @Column(name = "pms_arrhes_id")
    private Integer pmsArrhesId;
    
    @NotNull
    @Column(name = "montant")
    private BigDecimal montant;
    
    @NotNull
    @Column(name = "devise")
    private String devise;
    
    @NotNull
    @Column(name = "date_encaissement")
    private LocalDate dateEncaissement;
    
    @Column(name = "signature")
    private String signature;
    
}
