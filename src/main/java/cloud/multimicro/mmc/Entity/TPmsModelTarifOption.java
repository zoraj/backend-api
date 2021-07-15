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
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_model_tarif_option")
@Data
public class TPmsModelTarifOption implements Serializable {
    @NotNull
    @Column(name = "pms_model_tarif_id")
    @Id
    private Integer pmsModelTarifId;
    
    @JoinColumn(name = "pms_model_tarif_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsModelTarif pmsModelTarif;

    @NotNull
    @Column(name = "pms_tarif_option_id")
    @Id
    private Integer pmsTarifOptionId;
    
    @JoinColumn(name = "pms_tarif_option_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsTarifOption pmsTarifOption;

    public TPmsModelTarifOption() {
    }

    public TPmsModelTarifOption(Integer pmsModelTarifId, Integer pmsTarifOptionId) {
        this.pmsModelTarifId = pmsModelTarifId;
        this.pmsTarifOptionId = pmsTarifOptionId;
    }
    
    public TPmsModelTarifOption(TPmsModelTarif pmsModelTarif, TPmsTarifOption pmsTarifOption) {
        this.pmsModelTarif = pmsModelTarif;
        this.pmsTarifOption = pmsTarifOption;
    }
}
