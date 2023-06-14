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
 * @author Tsiory-PC
 */
@Entity
@Immutable
@Table(name = "v_pms_ca")
@Data
public class VPmsCa implements Serializable {
    
   @Id
   @Column(name = "id")
   private Integer id;
   
   @Column(name = "date_ca")
   private LocalDate dateCa;
   
   @Column(name = "date_ca_mois")
   private String dateCaMois;

   @Column(name = "date_ca_annee")
   private Integer dateCaAnnee;
   
   @Column(name = "montant_ca")
   private BigDecimal montantCa;
   
   @Column(name = "id_sous_famille")
   private Integer idSousFamille;
    
   @Column(name = "libelle_sous_famille")
   private String libelleSousFamille;
   
   @Column(name = "id_famille")
   private Integer idFamille;
    
   @Column(name = "libelle_famille")
   private String libelleFamille;
   
   @Column(name = "numero_chambre")
   private String numeroChambre;
   
   public VPmsCa() {

   }
    
}
