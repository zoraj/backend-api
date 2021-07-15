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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author HERIZO RA
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_liste_occupation")
@Data
public class VPmsEditionListeOccupation implements Serializable {    
    
    @Id
    @Column(name = "segment")
    private String segment;
     
    @Column(name = "compte_client")
    private String compteClient;
     
    @Column(name = "numero_reservation")
    private String numeroReservation; 
       
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "adulte_enfant")
    private Integer adulteEnfant;
    
    @Column(name = "type_chambre")
    private String typeChambre;    
        
    @Column(name = "qte")
    private Integer qte;
       
    @Column(name = "num_chambre")
    private String numChambre;

    @Column(name = "nationalite")
    private String nationalite;
  
}