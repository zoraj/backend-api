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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_edition_ca_activite")
@Data
public class VPosEditionCaActivite implements Serializable {

    @Id
    @Column(name = "libelle")
    private String libelle;
    
    @Column(name = "ca_ttc_brut")
    private BigDecimal caTtcBrut;
    
}
