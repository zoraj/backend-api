/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author HERIZO
 */
@Entity
@Table(name = "t_pos_tarif_prestation")
@Data
public class TPosTarifPrestation implements Serializable {

    @NotNull @Column(name = "pos_prestation_id")
    @Id
    private Integer posPrestationId;
    
    @JoinColumn(name = "pos_prestation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosPrestation posPrestation;
   
    @NotNull @Column(name = "pos_tarif_id")
    @Id
    private Integer posTarifId;
    
    @JoinColumn(name = "pos_tarif_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosTarif posTarif;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "prix_minimum")
    private BigDecimal prixminimum;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;
    
   
    public TPosTarifPrestation(Integer posPrestationId, Integer posTarifId) {
        this.posPrestationId = posPrestationId;
        this.posTarifId = posTarifId;
    }
    
}
