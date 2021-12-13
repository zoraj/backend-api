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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Tsiory-PC
 */
@Entity
@Immutable
@Table(name = "v_pms_encaissement")
@Data
public class VPmsEncaissement implements Serializable {

   @Id
   @Column(name = "id")
   private Integer id;
   
   @Column(name = "date_encaissement")
   private LocalDate dateEncaissement;
   
   @Column(name = "date_encaissement_mois")
   private String dateEncaissementMois;

   @Column(name = "date_encaissement_annee")
   private Integer dateEncaissementAnnee;
   
   @Column(name = "montant")
   private BigDecimal montant;
   
   @Column(name = "id_mode_encaissement")
   private Integer idModeEncaissement;
   
   @Column(name = "libelle_encaissement")
   private String libelleEncaissement;
   
   public VPmsEncaissement(){
       
   }
    
}
