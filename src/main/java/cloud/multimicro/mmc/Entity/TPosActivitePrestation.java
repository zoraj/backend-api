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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Data
@Table(name = "t_pos_activite_prestation")
public class TPosActivitePrestation implements Serializable {
    @NotNull
    @Column(name = "pos_prestation_id")
    @Id
    private Integer posPrestationId;

    @NotNull
    @Column(name = "pos_activite_id")
    @Id
    private Integer posActiviteId;

    @Transient
    private String libelleActivite;

    @Transient
    private boolean isChecked;

    public TPosActivitePrestation() {
    }

    public TPosActivitePrestation(int idProduct, int activite) {
        this.posPrestationId = idProduct;
        this.posActiviteId = activite;
    }

}
