/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
@Table(name = "v_pms_edition_planning_jour")
@Data
public class VPmsEditionPlanningJour implements Serializable {
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Id
    @Column(name = "nom_client")
    private String nomClient;
    
    @Column(name = "nb_sejour")
    private Integer nbSejour;
    
    @Id
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Id
    @Column(name = "num_chambre")
    private String numChambre;
    
}
