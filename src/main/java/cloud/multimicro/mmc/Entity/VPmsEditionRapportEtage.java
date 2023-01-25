/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
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
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Id    
    @Column(name = "num_etage")
    private String numEtage;
    
    @Id 
    @Column(name = "type")
    private String type;
    
    @Id 
    @Column(name = "numero_chambre")
    private String numeroChambre;
    
    @Id 
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "situation")
    private String situation;
    
    @Id 
    @Column(name = "etat")
    private String etat;    

    @Column(name = "pax")
    private Integer pax;

    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "pax_arrivee")
    private Integer paxArrivee;
    
    @Column(name = "remarque")
    private String remarque;
}
