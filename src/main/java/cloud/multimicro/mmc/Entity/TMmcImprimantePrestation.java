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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Data
@Table(name = "t_mmc_imprimante_prestation")
 public  class TMmcImprimantePrestation  implements Serializable { 
    @NotNull @Column(name = "pos_prestation_id")
    @Id
    private Integer posPrestationId;

    @JoinColumn(name = "pos_prestation_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosPrestation posPrestation;
    
    @NotNull @Column(name = "mmc_imprimante_id")
    @Id
    private Integer mmcImprimanteId;

    @JoinColumn(name = "mmc_imprimante_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TMmcImprimante mmcImprimante;

    public TMmcImprimantePrestation() {
    }

    public TMmcImprimantePrestation(int idProduct, int printer)  {
        this.posPrestationId = idProduct;
        this.mmcImprimanteId = printer;
    }

}
