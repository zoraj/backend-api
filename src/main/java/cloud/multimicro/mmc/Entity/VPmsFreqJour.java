/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

@Entity
@Immutable
@Table(name = "v_pms_freq_jour")
@Data
public class VPmsFreqJour implements Serializable {
    
   @Id
   @Column(name = "id")
   private Integer id;
   
   @Column(name = "numero_chambre")
   private String numeroChambre;
   
   @Column(name = "client")
   private String client;
   
   @Column(name = "arrivee")
   private LocalDate dateArrivee;
   
   @Column(name = "depart")
   private LocalDate dateDepart;
   
   @Column(name = "nb_adultes")
   private int nbAdultes;
   
   @Column(name = "nb_enfants")
   private int nbEnfants;
   
   @Column(name = "nb_pax")
   private int nbPax;
   
}
