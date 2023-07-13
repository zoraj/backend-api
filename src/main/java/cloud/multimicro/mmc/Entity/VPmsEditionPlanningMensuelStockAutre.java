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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly-PC
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_planning_mensuel_stock_autre")
@Data
public class VPmsEditionPlanningMensuelStockAutre implements Serializable {

    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    @Column(name = "nb_sejour")
    private Integer nbSejour;
    
    @Id
    @Column(name = "libelle_stock_autre")
    private String libelleStockAutre;
    
    @Column(name = "reference")
    private String reference;
    
    @Column(name = "nom_reservation")
    private String nomReservation;
    
}
