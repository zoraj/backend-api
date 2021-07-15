/* 
 * Copyright (C) 2019 MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Entity;
import java.io.Serializable;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
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
import javax.persistence.Transient;
import lombok.Data;


@Entity
@Data
@Table(name = "t_mmc_sous_famille_ca")
public class TMmcSousFamilleCa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    private String code;
    private String libelle;
    @Column(name = "mmc_famille_ca_id")
    private Integer mmcFamilleCaId;
    
    @JoinColumn(name = "mmc_famille_ca_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcFamilleCa mmcFamilleCa;
    
    @Transient
    private String mmcFamilleCaLibelle;
    
    @Column(name = "compte_comptable_brut")
    private String compteComptableBrut;
    
    @Column(name = "mmc_tva_id")
    private Integer mmcTvaId;
    
    @JoinColumn(name = "mmc_tva_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcTva mmcTva;
    
    @Column(name = "mmc_service_id")
    private Integer mmcServiceId;
    
    @JoinColumn(name = "mmc_service_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcService mmcService;
    
    @Column(name = "nature")
    private String nature;
    
    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;

}