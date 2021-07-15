/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author HERIZO RA
 */
@Entity
@Table(name = "v_pms_edition_client_present")
@Data
public class VPmsEditionClientPresent implements Serializable {
    
    @Id
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "etat_chambre")
    private String etatChambre;        

    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "adult_enfant")
    private Integer adultEnfant; 
    
    @Column(name = "enfant")
    private Integer enfant; 
    
    @Column(name = "arrhes")
    private BigDecimal arrhes;
    
    @Column(name = "model_tarif")
    private String modelTarif;
    
    @Column(name = "tarif_jour")
    private BigDecimal tarifJour;
    
    @Column(name = "compte")
    private String compte;
    
    @Column(name = "balance")
    private BigDecimal Balance;
    
}
