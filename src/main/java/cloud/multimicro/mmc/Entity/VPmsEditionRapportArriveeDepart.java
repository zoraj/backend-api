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
@Table(name = "v_pms_edition_rapport_arrivee_depart")
@Data
public class VPmsEditionRapportArriveeDepart implements Serializable {

    @Id
    @Column(name = "numero_chambre")
    private String numeroChambre;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "etat_chambre")
    private String etatChambre;
    
    @Column(name = "nom_depart")
    private String nomDepart;
    
    @Column(name = "adult_enfant_depart")
    private Integer adultEnfantDepart;
    
    @Column(name = "enfant_depart")
    private Integer enfantDepart;
    
    @Column(name = "nom_arriv")
    private String nomArriv;
    
    @Column(name = "adult_enfant_arriv")
    private Integer adultEnfantArriv;
    
    @Column(name = "enfant_arriv")
    private Integer enfantArriv;
    
//    @Column(name = "heure")
//    private Date heure;
    
    @Column(name = "nb_nuit")
    private Integer nbNuit; 
}
