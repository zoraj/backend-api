
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
@Table(name = "v_collectivite_dashboard")
@Data
public class VCollectiviteDashboard implements Serializable {
    @Id
    @Column(name = "nb_passage_jour")
    private Integer nbPassageJour;

    @Column(name = "facturation_jour")
    private BigDecimal facturationJour;

    @Column(name = "nb_client")
    private Integer nbClient;

    @Column(name = "ca_annuel")
    private BigDecimal caAnnuel;
}
