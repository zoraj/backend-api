/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "v_pos_edition_visualisation_mode_encaissement")
@Data
public class VPosEditionVisualisationModeEncaissement implements Serializable {

    @Id
    @Column(name = "libelle_mode_encaissement")
    private String libelleModeEncaissement;
    
    @Id
    @Column(name = "date_encaissement")
    private LocalDateTime dateEncaissement;
    
    @Id
    @Column(name = "activite_id")
    private Integer activiteId;
    
    @Id
    @Column(name = "mode_encaissement_id")
    private Integer modeEncaissementId;
    
    @Id
    @Column(name = "debit_jour")
    private BigDecimal debitJour;
    
    @Id
    @Column(name = "credit_jour")
    private BigDecimal creditJour;
    
    @Id
    @Column(name = "solde_jour")
    private BigDecimal soldeJour;
    
    @Column(name = "constate_jour")
    private BigDecimal constateJour;
    
}
