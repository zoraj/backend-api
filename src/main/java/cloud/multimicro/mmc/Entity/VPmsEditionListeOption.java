/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
@Table(name = "v_pms_edition_liste_option")
@Data
public class VPmsEditionListeOption implements Serializable {
    
    @Id
    @Column(name = "num_reservation")
    private String numReservation;
    
    @Column(name = "indiv_groupe")
    private String indivGroupe;
    
    @Column(name = "compte_client")
    private String compteClient;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "date_option")
    private LocalDate dateOption;
    
    @Column(name = "nb_chambre")
    private Integer nbChambre;
    
    @Column(name = "nbr_nuitee")
    private Integer nbrNuitee;
}
