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
@Table(name = "v_pms_edition_etat_prev_real_mois")
@Data
public class VPmsEditionEtatPrevRealMois implements Serializable {
    
    @Id
    @Column(name = "type_client")
    private String typeClient;
    
    @Id
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "nb_chambre")
    private Integer nbChambre;
    
    @Column(name = "nb_chambre_opt")
    private Integer nbChambreOpt;
    
    @Column(name = "nb_chambre_conf")
    private Integer nbChambreConf;
    
}
