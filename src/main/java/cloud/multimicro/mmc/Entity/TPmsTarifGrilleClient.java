/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_tarif_grille_client")
@Data
public class TPmsTarifGrilleClient implements Serializable {
    @NotNull
    @Column(name = "pms_tarif_grille_id")
    @Id
    private Integer pmsTarifGrilleId;

    @NotNull
    @Column(name = "mmc_client_id")
    @Id
    private Integer mmcClientId;

    public TPmsTarifGrilleClient() {

    }

    public TPmsTarifGrilleClient(Integer pmsTarifGrilleId, Integer mmcClientId) {
        this.pmsTarifGrilleId = pmsTarifGrilleId;
        this.mmcClientId = mmcClientId;
    }
}
