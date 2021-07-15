/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Table(name = "t_pms_type_chambre_tarif_applicable")
@Data
public class TPmsTypeChambreTarifApplicable implements Serializable {

    @NotNull @Column(name = "pms_type_chambre_id")
    @Id
    private Integer pmsTypeChambreId;

    @JoinColumn(name = "pms_type_chambre_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsTypeChambre pmsTypeChambre;

    @NotNull @Column(name = "pms_model_tarif_id")
    @Id
    private Integer pmsModelTarifId;

    @JoinColumn(name = "pms_model_tarif_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsModelTarif pmsModelTarif;
    
    @Transient
    private TPmsTarifOption pmsTarifOption;

    public TPmsTypeChambreTarifApplicable() {
    }

    public TPmsTypeChambreTarifApplicable(Integer pmsModelTarifId,Integer pmsTypeChambreId) {
        this.pmsTypeChambreId = pmsTypeChambreId;
        this.pmsModelTarifId = pmsModelTarifId;
    }
    
    public TPmsTypeChambreTarifApplicable(TPmsTypeChambre pmsTypeChambre, TPmsModelTarif pmsModelTarif, TPmsTarifOption pmsTarifOption) {
        this.pmsTypeChambre = pmsTypeChambre;
        this.pmsModelTarif = pmsModelTarif;
        this.pmsTarifOption = pmsTarifOption;
    }
}
