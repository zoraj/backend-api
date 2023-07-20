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
@Table(name = "v_pms_chambre_louable")
@Data
public class VPmsChambreLouable implements Serializable {

    @Id
    @Column(name = "id_chambre")
    private Integer idChambre;
    
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "date_debut_mois")
    private String dateDebutMois;

   @Column(name = "date_debut_annee")
   private Integer dateDebutAnnee;
   
   @Column(name = "date_fin_mois")
    private String dateFinMois;

   @Column(name = "date_fin_annee")
   private Integer dateFinAnnee;
    
}
