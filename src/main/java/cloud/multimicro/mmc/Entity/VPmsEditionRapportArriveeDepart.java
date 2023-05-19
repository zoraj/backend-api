/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "etat_chambre")
    private String etatChambre;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "pax")
    private Integer pax;
    
    @Column(name = "enfant")
    private Integer enfant;
    
    @Column(name = "heure")
    private String heure;
    
    @Column(name = "nb_nuit")
    private Integer nbNuit;
    
    @Column(name = "date_arrivee")
    private String dateArrivee;
    
    @Column(name = "date_depart")
    private String dateDepart;
    
}