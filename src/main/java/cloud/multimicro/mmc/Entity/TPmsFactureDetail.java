/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author herizo
 */
@Entity
@Table(name = "t_pms_facture_detail")
@Data
public class TPmsFactureDetail implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @Column(name = "pms_facture_numero")
    private String pmsFactureNumero;
    
    @NotNull
    @Column(name = "libelle_prestation")
    private String libellePrestation;
    
    @NotNull
    @Column(name = "qte_cde")
    private Integer qteCde;
    
    @NotNull
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;
    
    @NotNull
    @Column(name = "montant_ht")
    private BigDecimal montantHt;
    
    @NotNull
    @Column(name = "devise")
    private String devise;
    
}
