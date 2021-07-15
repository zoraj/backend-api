/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
@Table(name = "v_pms_edition_cloture_definitive")
@Data
public class VPmsEditionClotureDefinitive implements Serializable {

    @Id
    @Column(name = "famille")
    private Integer famille;
    
    @Column(name = "sous_famille")
    private Integer SousFamille;
    
    @Column(name = "montant")
    private Integer montant;
    
    @Column(name = "date_ca")
    private Integer dateCa;
    
}
