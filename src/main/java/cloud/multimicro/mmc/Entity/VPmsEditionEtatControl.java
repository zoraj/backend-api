/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "v_pms_edition_etat_control")
@Data
public class VPmsEditionEtatControl implements Serializable {

    @Id
    @Column(name = "numero_chambre")
    private String numeroChambre;
    
    @Column(name = "compte_client")
    private String compteClient;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "qte_fact")
    private Integer qteFact;
    
    @Column(name = "prix")
    private BigDecimal prix;
    
    @Column(name = "segment")
    private String segment;
    
    @Column(name = "type_client")
    private String typeClient;
    
    @Column(name = "etat")
    private String etat;
}
