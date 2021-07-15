/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_edition_statistique_couvet")
@Data
public class VPmsEditionStatistiqueCouvert implements Serializable {

    @Id
    @Column(name = "activite")
    private String activite;
    
    @Column(name = "date_couvert")
    private Date dateCouvert;
    
    @Column(name = "ca_global")
    private Integer caGlobal;
    
    @Column(name = "ca_midi")
    private Integer caMidi;
    
    @Column(name = "ca_soir")
    private Integer caSoir;
    
    @Column(name = "total_couvert")
    private Integer totalCouvert;
    
    @Column(name = "couvert_midi")
    private Integer couvertMidi;
    
    @Column(name = "couvert_soir")
    private Integer couvertSoir;
    
    @Column(name = "pm_global")
    private Integer pmGlobal;
    
    @Column(name = "pm_midi")
    private Integer pmMidi;
    
    @Column(name = "pm_soir")
    private Integer pmSoir;
    
}
