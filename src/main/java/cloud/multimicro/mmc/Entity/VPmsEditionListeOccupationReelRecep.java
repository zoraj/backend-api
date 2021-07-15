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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_liste_occupation_reel_recep")
@Data
public class VPmsEditionListeOccupationReelRecep implements Serializable {

    @Column(name = "segment")
    private String segment;
      
    @Column(name = "compte_client")
    private String compteClient;
    
    @Id
    @Column(name = "num_reservation")
    private String numReservation;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "adult_enfant")
    private Integer adultEnfant;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "date_arrivee")
    private Date dateArrivee;
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "quantite")
    private Integer qte;
    
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Column(name = "nationalite")
    private String nationalite;
    
}
