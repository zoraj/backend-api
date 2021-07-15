
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
@Table(name = "v_pms_dashboard")
@Data
public class VPmsDashboard implements Serializable {
    @Id
    @Column(name = "nb_depart")
    private Integer nbDepart;

    @Column(name = "nb_arrivees")
    private Integer nbArrivees;

    @Column(name = "taux_occupation")
    private BigDecimal tauxOccupation;

    @Column(name = "total_ca")
    private BigDecimal totalCa;
}
