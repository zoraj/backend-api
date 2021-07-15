/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Data
@Table(name = "t_mmc_client_facturation")
public class TMmcClientFacturation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "mmc_client_id")
    private Integer mmcClientId;

    @Column(name = "compte_prise_en_charge")
    private Integer comptePriseEnCharge;

    @Column(name = "compte_tarification")
    private Integer compteTarification;

    @Column(name = "compte_allotement")
    private Integer compteAllotement;

    @Column(name = "compte_regroupement")
    private Integer compteRegroupement;
    
    @Column(name = "num_carte_fidelite")
    private Integer numCarteFidelite;
    
    @Column(name = "validite_carte_fidelite")
    private Date validiteCarteFidelite;

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
