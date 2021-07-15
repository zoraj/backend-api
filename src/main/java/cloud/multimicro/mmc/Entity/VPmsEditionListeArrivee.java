/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
@Table(name = "v_pms_edition_liste_arrivee")
@Data
public class VPmsEditionListeArrivee implements Serializable {

    @Id
    @Column(name = "num_reservation")
    private String numReservation;

    @Column(name = "indiv_groupe")
    private String indivGroupe;

    @Column(name = "nom")
    private String nom;

    @Column(name = "compte_client")
    private String compteClient;

    @Column(name = "type_chambre")
    private String typeChambre;

    @Column(name = "numero_chambre")
    private String numeroChambre;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "nb_nuit")
    private Integer nbNuit;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "heure_arrivee")
    private LocalTime heureArrivee;
    
    @Column(name = "tarif")
    private String tarif;
    
    @Column(name = "prix_jour")
    private BigDecimal prixJour;
}
