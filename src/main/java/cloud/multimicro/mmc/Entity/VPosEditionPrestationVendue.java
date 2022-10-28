/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    private LocalDate datePresta;
    
    @Id
    @Column(name = "famille")
    private String famille;
    
    @Id
    @Column(name = "sous_famille")
    private String sousFamille;
    
    @Id
    @Column(name = "libelle")
    private String libelle;
    
    @Id
    @Column(name = "quantite")
    private Integer qte;
    
    @Id
    @Column(name = "ca_ttc_brut")
    private BigDecimal caTtcBrut;
    
    @Id
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Id
    @Column(name = "qte_offert")
    private Integer qteOffert;
    
    @Id
    @Column(name = "id_activite")
    private Integer idActivite;
    
    @Id
    @Column(name = "id_prestation")
    private Integer idPrestation;
}