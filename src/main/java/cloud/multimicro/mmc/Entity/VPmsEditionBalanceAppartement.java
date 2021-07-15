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
@Table(name = "v_pms_edition_balance_appartement")
@Data
public class VPmsEditionBalanceAppartement implements Serializable {

    @Id
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_arrivee")
    private Date dateArrivee;
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "dont_enfant")
    private Integer dontEnfant;
    
    @Column(name = "tarif")
    private BigDecimal tarif;
    
}
