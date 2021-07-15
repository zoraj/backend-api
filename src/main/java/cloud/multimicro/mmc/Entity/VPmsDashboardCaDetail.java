
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
@Table(name = "v_pms_dashboard_ca_detail")
@Data
public class VPmsDashboardCaDetail implements Serializable {
    @Id
    @Column(name = "ca_hors_commis")
    private BigDecimal caHorsCommis;

    @Column(name = "ca_pdj")
    private BigDecimal caPdj;

    @Column(name = "ca_heb")
    private BigDecimal caHeb;

    @Column(name = "ca_net")
    private BigDecimal caNet;
}
