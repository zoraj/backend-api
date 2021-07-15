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
@Table(name = "v_pms_edition_liste_verif_arrivee")
@Data
public class VPmsEditionListeVerifArrivee implements Serializable {
   
    @Column(name = "id_client")
    private Integer idClient;
    
    @Column(name = "date_arrivee")
    private Date dateArrivee;
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "quantite")
    private Integer qte;
    
    @Column(name = "nbr_pax")
    private Integer nbrPax;
    
    @Column(name = "client")
    private String client;
    
    @Id
    @Column(name = "num_reservation")
    private String numReservation;
    
    @Column(name = "indiv_groupe")
    private String indivGroupe;
    
    @Column(name = "ref_dossier")
    private String refDossier;
    
    @Column(name = "signature")
    private String signature;
}
