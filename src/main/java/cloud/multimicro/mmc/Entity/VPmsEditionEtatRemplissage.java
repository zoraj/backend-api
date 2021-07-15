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
@Table(name = "v_pms_edition_etat_remplissage")
@Data
public class VPmsEditionEtatRemplissage implements Serializable {
    
    @Id
    @Column(name = "date_remplissage")
    private Date dateRemplissage;
    
    @Column(name = "total_stock")
    private Integer totalStock;
    
    @Column(name = "indiv_ferme")
    private Integer indivFerme;
    
    @Column(name = "indiv_opt")
    private Integer indiv_opt;
    
    @Column(name = "group_ferme")
    private Integer groupFerme;
    
    @Column(name = "group_opt")
    private Integer groupOpt;
    
    @Column(name = "total_ferme")
    private Integer totalFerme;
    
    @Column(name = "total_opt")
    private Integer totalOpt;
    
    @Column(name = "total_occup")
    private Integer totalOccup;
    
    @Column(name = "taux_occ_ferme")
    private Integer tauxOccFerme;
    
    @Column(name = "taux_occ_opt")
    private Integer tauxOccOpt;
    
    @Column(name = "occup")
    private Integer occup;
    
    @Column(name = "reste_allot")
    private Integer resteAllot;
    
}
