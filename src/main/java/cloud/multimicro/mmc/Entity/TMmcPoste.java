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
@Table(name = "t_mmc_poste")
public class TMmcPoste implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(max = 3)
    @Column(name = "numero")
    private String numero;
    
    @Size(max = 45)
    private String libelle;

    @Column(name = "mmc_site_id")
    private Integer mmcSiteId;

    @Column(name = "etat")
    private String etat;
    
    @Size(max = 128)
    @Column(name = "adresse_ip")
    private String adresseIp;
    
    @Size(max = 3)
    @Column(name = "devise")
    private String devise;

    @Size(max = 45)
    private String email;

    @Size(max = 9)
    @Column(name = "langue")
    private String langue;

    @Size(max = 255)
    @Column(name = "os")
    private String os;

    @Size(max = 10)
    @Column(name = "cpu")
    private String cpu;

    @Column(name = "type")
    private String type;

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


