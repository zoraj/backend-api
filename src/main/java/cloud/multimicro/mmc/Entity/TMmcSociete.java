/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author HERIZO
 */
@Entity
@Data
@Table(name = "t_mmc_societe")
public class TMmcSociete implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column(name = "reference")
    private String reference;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank  @Column(name = "nom")
    private String nom;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "max_passage")
    private Integer maxpassage;
    
    @Column(name = "montant_blocage")
    private BigDecimal montantBlocage;
    
    @Column(name = "montant_min_appro")
    private BigDecimal montantMinappro;
    
    @Column(name = "mmc_banque_id")
    private Integer mmcBanqueId;
    
    @Column(name = "code_subvention")
    private String codeSubvention;
    
    @Column(name = "code_admission")
    private String codeAdmission;
    
    @JoinColumn(name = "mmc_banque_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcBanque mmcBanque;
   
    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;

}
