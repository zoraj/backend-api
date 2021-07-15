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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_collectivite_lecture_famille")
@Data
public class VCollectiviteLectureFamille implements Serializable {    
    
    @Id
    @Column(name = "date_note")
    private LocalDate dateNote;

    @Id
    @Column(name = "code")
    private String code;
    
    @Id
    @Column(name = "libelle")
    private String libelle;
    
    @Id
    @Column(name = "quantite")
    private Integer qte;
    
    @Id
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    @Id
    @Column(name = "montant_ht")
    private BigDecimal montantHt;

}
