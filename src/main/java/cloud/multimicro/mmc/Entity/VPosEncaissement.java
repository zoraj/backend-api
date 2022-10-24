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
@Table(name = "v_pos_encaissement")
@Data
public class VPosEncaissement implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Id
    @Column(name = "date_encaissement")
    private LocalDate dateEncaissement;

    @Id
    @Column(name = "poste_uuid")
    private String posteUuid;

    @Id
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    @Id
    @Column(name = "pos_activite_id")
    private Integer posActiviteId;
    
    @Column(name = "libelle_activite")
    private String libelleActivite;

    @Id
    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @Id
    @Column(name = "mmc_mode_encaissement_id")
    private Integer mmcModeEncaissementId;
    
    @Column(name = "libelle_mode_encaissement")
    private String libelleModeEncaissement;
    
    @Column(name = "service")
    private String service;

    public VPosEncaissement() {

    }

    public VPosEncaissement(BigDecimal montantTtcValue, String posteUuidValue) {
        this.montantTtc = montantTtcValue;
        this.posteUuid = posteUuidValue;
    }
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "qte")
    private BigDecimal qte;
}
