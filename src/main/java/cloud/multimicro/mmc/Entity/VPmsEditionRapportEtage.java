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
 * @author HERIZO RA
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_rapport_etage")
@Data
public class VPmsEditionRapportEtage implements Serializable {
   
    @Id    
    @Column(name = "num_etage")
    private String numEtage;
    
    @Column(name = "numero_chambre")
    private String numeroChambre;
    
    @Column(name = "etat")
    private String etat;
    
    @Column(name = "type")
    private String type;    
        
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "adulte")
    private String adulte;
    
    @Column(name = "date_arrivee")
    private Date dateArrivee;
    
    @Column(name = "date_depart")
    private Date dateDepart;
}
