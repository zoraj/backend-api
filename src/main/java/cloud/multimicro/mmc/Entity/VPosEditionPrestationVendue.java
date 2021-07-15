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
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_edition_prestation_vendue")
@Data
public class VPosEditionPrestationVendue implements Serializable {

    @Id
    @Column(name = "date_presta")
    private Date datePresta;
    
    @Column(name = "famille")
    private String famille;
    
    @Column(name = "sous_famille")
    private String sousFamille;
    
    @Column(name = "libelle")
    private String libelle;
    
    @Column(name = "quantite")
    private Integer qte;
    
    @Column(name = "ca_ttc_brut")
    private BigDecimal caTtcBrut;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "qte_offert")
    private Integer qteOffert;
}
