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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author HERIZO RA
 */
@Entity
@Data
@Table(name = "t_mmc_parametrage_integration_comptable")
public class TMmcParametrageIntegrationComptable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull
    @Column(name = "mmc_sous_famille_ca_id")
    private Integer mmcSousFamilleCaId;
       
    @Column(name = "compte_brut")
    private String compteBrut;
    
    @Column(name = "libelle_brut")
    private String libelleBrut;
    
    @Column(name = "compte_remise")
    private String compteRemise;
    
    @Column(name = "libelle_remise")
    private String libelleRemise;
    
    @Column(name = "compte_commission")
    private String compteCommission;
    
    @Column(name = "libelle_commission")
    private String libelleCommission;
    
    @Column(name = "compte_analytique")
    private String compteAnalytique;
    
    @Column(name = "compte_commission_percevoir")
    private String compteCommissionPercevoir;
    
    @Column(name = "libelle_commission_percevoir")
    private String libelleCommissionPercevoir;
    
    @Column(name = "pourcentage_commission_percevoir")
    private BigDecimal pourcentageCommissionPercevoir;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeletion;
  

    
}
