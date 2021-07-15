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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Table(name = "t_pms_calendrier_nettoyage")
@Data
public class TPmsCalendrierNettoyage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "date_nettoyage")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNettoyage;

    @NotNull
    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @NotNull
    @Column(name = "pms_chambre_id")
    private Integer pmsChambreId;
    
    @JoinColumn(name = "pms_chambre_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsChambre pmsChambre;
    
    @JoinColumn(name = "mmc_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcUser mmcUser;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;

}
