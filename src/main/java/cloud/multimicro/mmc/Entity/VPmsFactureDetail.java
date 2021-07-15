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
 * @author naly
 */
@Entity
@Immutable
@Table(name = "v_pms_facture_detail")
@Data
public class VPmsFactureDetail implements Serializable {
    
    @Column(name = "num_facture")
    private String numFacture;
    
    @Id
    @Column(name = "descriptif")
    private String descriptif;
    
    @Id
    @Column(name = "qte")
    private Integer qte;
    
    @Id
    @Column(name = "pu")
    private BigDecimal pu;
    
    @Id
    @Column(name = "prix_ht")
    private BigDecimal prixHt;
    
    @Id
    @Column(name = "total_ht")
    private BigDecimal totalHt;
    
    @Id
    @Column(name = "tva")
    private BigDecimal tva;
    
    @Id
    @Column(name = "prix_ttc")
    private BigDecimal prixTtc;
    
    @Id
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Id
    @Column(name = "total_ttc")
    private BigDecimal totalTtc;
    
}
