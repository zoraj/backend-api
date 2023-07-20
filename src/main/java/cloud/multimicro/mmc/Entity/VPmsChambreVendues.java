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
@Table(name = "v_pms_chambre_vendues")
@Data
public class VPmsChambreVendues implements Serializable {

    @Id
    @Column(name = "id_chambre")
    private Integer idChambre;
    
    @Column(name = "id_client")
    private Integer idClient;
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "date_arrivee_mois")
    private String dateArriveeMois;

   @Column(name = "date_arrivee_annee")
   private Integer dateArriveeAnnee;
    
}
