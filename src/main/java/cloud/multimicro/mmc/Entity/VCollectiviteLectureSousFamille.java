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
@Table(name = "v_collectivite_lecture_sous_famille")
@Data
public class VCollectiviteLectureSousFamille implements Serializable {    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "date_note")
    private LocalDate dateNote;

    @Column(name = "code")
    private String code;
    
    @Column(name = "libelle")
    private String libelle;
    
    @Column(name = "nature")
    private String nature;
    
    @Column(name = "quantite")
    private Integer qte;
    
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    @Column(name = "montant_ht")
    private BigDecimal montantHt;
    
    @Column(name = "tx_tva")
    private BigDecimal txTva;
        
    @Column(name = "tva")
    private BigDecimal tva;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "code_client")
    private String codeClient;

}
