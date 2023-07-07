/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * @author Naly-PC
 */
@Entity
@Immutable
@Table(name = "v_pms_tarif_grille_detail_detail")
@Data
public class VPmsTarifGrilleDetailDetail implements Serializable {
    
    @Id
    @Column(name = "date_tarif")
    private LocalDate dateTarif;
    
    @Column(name = "base")
    private Integer base;
    
    @Id
    @Column(name = "pms_model_tarif_id")
    private Integer pmsModelTarifId;
    
    @Id
    @Column(name = "pms_type_chambre_id")
    private Integer pmsTypeChambreId;
    
    @Id
    @Column(name = "pms_tarif_grille_detail_id")
    private Integer pmsTarifGrilleDetailId;
    
    @Id
    @Column(name = "pms_prestation_id")
    private Integer pmsPrestationId;
    
    @Column(name = "libelle_prestation")
    private String libellePrestation;
    
    @Column(name = "pu")
    private BigDecimal pu;
    
    @Column(name = "pms_tarif_grille_id")
    private Integer pmsTarifGrilleId;
    
    @Id
    @Column(name = "mmc_client_id")
    private Integer mmcClientId;
    
}
