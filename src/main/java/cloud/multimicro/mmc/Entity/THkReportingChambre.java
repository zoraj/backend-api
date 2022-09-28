/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import lombok.Data;

/**
 *
 * @author HERIZO-PC
 */
@Entity
@Table(name = "t_hk_reporting_chambre")
@Data
public class THkReportingChambre implements Serializable {

   private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Column(name = "date_reporting")
    private LocalDateTime dateReporting;
    
    @Column(name = "hk_element_reporting_id")
    private Integer hkElementReportingId;
    
    @JoinColumn(name = "hk_element_reporting_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private THkElementReporting hkElementReporting;
    
    @Column(name = "pms_chambre_id")
    private Integer pmsChambreId;
    
    @JoinColumn(name = "pms_chambre_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsChambre pmsChambre;
    
    @Column(name = "valeur")
    private Boolean valeur;
    
    @Column(name = "note")
    private String note;
    
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
