/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Data;
/**
 *
 * @author Tsiory
 */
@Entity
@Data
@Table(name = "t_mmc_site")
public class TMmcSite implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(max = 30)
    @Column(name = "cle")
    private String cle;
    
    @Size(max = 45)
    private String libelle;
    
    @Size(max = 45)
    private String telephone;
    
    @Size(max = 45)
    private String mobile;
    
    @Size(max = 45)
    private String adresse;
    
    @Size(max = 45)
    private String cp;
    
    @Size(max = 45)
    private String ville;
    
    @Size(max = 45)
    private String pays;
    
    @Size(max = 45)
    private String email;
    
    @Size(max = 45)
    @Column(name = "site_web")
    private String siteWeb;
    
    @Size(max = 45)
    private String siret;
    
    @Size(max = 45)
    @Column(name = "num_tva")
    private String numTva;
    
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
