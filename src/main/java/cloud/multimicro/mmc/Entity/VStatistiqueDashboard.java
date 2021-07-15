
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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Tsiory
 */
@Entity
@Immutable
@Table(name = "v_statistique_dashboard")
@Data
public class VStatistiqueDashboard implements Serializable {
    @Id
    @Column(name = "taux_occupation")
    private BigDecimal tauxOccupation;

    @Column(name = "prix_moyenne_chambre")
    private BigDecimal prixMoyenneChambre;

    @Column(name = "chambre_louables_vendues")
    private Integer chambreLouablesVendues;

    @Column(name = "indice_frenquentation")
    private Integer indiceFrenquentation;
}
