/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "v_pms_edition_liste_global_arrivee")
@Data
public class VPmsEditionListeGlobalArrivee implements Serializable {
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "type_client")
    private String typeClient;
    
    @Column(name = "segment_client")
    private String segmentClient;
    
    @Column(name = "compte_client")
    private String compteClient;
    
    @Id
    @Column(name = "num_reservation")
    private String numReservation;
    
    @Column(name = "client")
    private String client;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "adult_enfant")
    private Integer adultEnfant;
    
    @Column(name = "nombre")
    private Integer nombre;
    
    @Id
    @Column(name = "prestation")
    private String prestation;
    
    @Column(name = "quantite")
    private Integer quantite;
    
    @Column(name = "observation")
    private String observation;
    
    @Column(name = "numero")
    private Integer numero;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "date_creation")
    private LocalDate dateCreation;
    
}
