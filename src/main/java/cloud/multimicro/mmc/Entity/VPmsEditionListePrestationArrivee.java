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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_liste_prestation_arrivee")
@Data
public class VPmsEditionListePrestationArrivee implements Serializable {
    
    @Id
    @Column(name = "id_prestation")
    private Integer idPrestation;
    
    @Column(name = "libelle_prestation")
    private String libellePrestation;

    @Column(name = "date_arrivee")
    private Date dateArrivee;
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "nombre")
    private Integer nombre;
    
    @Column(name = "client")
    private String client;
     
    @Id
    @Column(name = "num_reservation")
    private String numReservation;

    @Column(name = "reference")
    private String reference;
    
}
