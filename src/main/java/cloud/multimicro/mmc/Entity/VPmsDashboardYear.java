/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
 * @author heriz
 */
@Entity
@Immutable
@Table(name = "v_pms_dashboard_year")
@Data
public class VPmsDashboardYear implements Serializable {
    @Id
    @Column(name = "nb_depart_year")
    private Integer nbDepartYear;

    @Column(name = "nb_arrivees_year")
    private Integer nbArriveesYear;

    @Column(name = "taux_occupation_year")
    private BigDecimal tauxOccupationYear;

    @Column(name = "total_ca_year")
    private BigDecimal totalCaYear;
}