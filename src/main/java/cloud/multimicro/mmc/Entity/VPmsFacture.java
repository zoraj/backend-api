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
import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pms_facture")
@Data
public class VPmsFacture implements Serializable {
    
    @Id
    @Column(name = "num_facture")
    private String numFacture;

    @Column(name = "date_facture")
    private Date dateFacture;
        
    @Column(name = "nom_client")
    private String nomClient;
    
    @Column(name = "statutPointage")
    private Integer statutPointage;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "ville")
    private String ville;
    
    @Column(name = "pays")
    private String pays;
    
    @Column(name = "nom")
    private String nom;
    
}