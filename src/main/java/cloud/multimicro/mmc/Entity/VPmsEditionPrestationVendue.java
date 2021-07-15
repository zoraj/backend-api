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
@Table(name = "v_pms_edition_prestation_vendue")
@Data
public class VPmsEditionPrestationVendue implements Serializable {    
    
    @Column(name = "date_note")
    private LocalDate dateNote;

    @Id
    @Column(name = "code")
    private String code;
    
    @Column(name = "libelle")
    private String libelle;
    
    @Column(name = "quantite")
    private Integer qte;
    
    @Column(name = "ca")
    private BigDecimal ca;

}
