/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "v_pms_tarif_grille_detail")
@Data
public class VPmsTarifGrilleDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "pms_tarif_grille_id")
    private Integer pmsTarifGrilleId;

    @Size(min = 1, max = 100)
    @Column(name = "model_tarif")
    private String modelTarif;

    @Column(name = "pms_model_tarif_id")
    private Integer pmsModelTarifId;

    @Column(name = "base")
    private Integer base;

    @Column(name = "pms_tarif_grille_detail_id")
    private Integer pmsTarifGrilleDetailId;

    @Column(name = "pu")
    private BigDecimal pu;

    @Column(name = "date_tarif")
    private LocalDate dateTarif;

}
