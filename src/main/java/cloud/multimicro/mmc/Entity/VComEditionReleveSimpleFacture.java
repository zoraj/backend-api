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
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_com_edition_releve_simple_facture")
@Data
public class VComEditionReleveSimpleFacture implements Serializable {

    @Id
    @Column(name = "date_facture")
    private Date dateFacture;
    
    @Column(name = "num_facture")
    private Integer numFacture;
    
    @Column(name = "client")
    private String client;
    
    @Column(name = "num_dossier")
    private Integer numDossier;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "regler")
    private Integer regler;
    
    @Column(name = "solde_fact")
    private BigDecimal soldeFact;
    
    @Column(name = "solde")
    private BigDecimal solde;
    
}
