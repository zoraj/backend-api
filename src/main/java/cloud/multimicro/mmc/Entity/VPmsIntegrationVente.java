/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
@Table(name = "v_pms_integration_vente")
@Data
public class VPmsIntegrationVente implements Serializable {

    @Id
    @Column(name = "date_note")
    private LocalDate date_note;

    @Id
    @Column(name = "sousfamille")
    private String sousfamille;

    @Id
    @Column(name = "mmc_famille_ca_id")
    private Integer mmc_famille_ca_id;

    @Column(name = "brut")
    private BigDecimal brut;

    @Column(name = "net")
    private BigDecimal net;

    @Column(name = "remise")
    private BigDecimal remise;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "compte_remise")
    private String compte_remise;

    @Column(name = "compte_brut")
    private String compte_brut;

    @Column(name = "compte_commission")
    private String compte_commission;

    @Column(name = "compte_analytique")
    private String compte_analytique;
}
