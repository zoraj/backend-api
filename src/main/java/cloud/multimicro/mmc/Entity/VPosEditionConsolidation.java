/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_edition_consolidation")
@Data
public class VPosEditionConsolidation implements Serializable {

    @Id
    @Column(name = "ca_famille")
    private String caFamille;
    
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    
    @Column(name = "ca_ht")
    private BigDecimal caHt;
    
    @Column(name = "tva")
    private BigDecimal tva;
    
    @Column(name = "ca_ttc")
    private BigDecimal caTtc;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "offert")
    private BigDecimal offert;
    
    @Column(name = "service")
    private BigDecimal service;
    
}
