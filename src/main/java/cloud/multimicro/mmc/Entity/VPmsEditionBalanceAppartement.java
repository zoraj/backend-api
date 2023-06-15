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
@Table(name = "v_pms_edition_balance_appartement")
@Data
public class VPmsEditionBalanceAppartement implements Serializable {

    @Id
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Column(name = "id_sejour")
    private Integer idSejour;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "dont_enfant")
    private Integer dontEnfant;
    
    @Column(name = "tarif")
    private String tarif;
    
    @Column(name = "date_prestation")
    private LocalDate datePrestation;
    
    @Id
    @Column(name = "code_prestation")
    private String codePrestation;
    
    @Id
    @Column(name = "libelle_prestation")
    private String libellePrestation;
    
    @Column(name = "qte_prestation")
    private Integer qtePrestation;
    
    @Column(name = "pu_prestation")
    private BigDecimal puPrestation;
    
    @Column(name = "prix_prestation")
    private BigDecimal prixPrestation;
    
}
