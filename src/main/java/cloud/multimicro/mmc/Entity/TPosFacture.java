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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author herizo
 */
@Entity
@Table(name = "t_pos_facture")
@Data
public class TPosFacture implements Serializable{
        
    @Id
    @NotNull
    @Column(name = "numero")
    private String numero;
    
    @NotNull
    //@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_facture")
    private LocalDate dateFacture;   
    
    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
    
    @Column(name = "entete1")
    private String entete1;
    
    @Column(name = "entete2")
    private String entete2;
    
    @Column(name = "entete3")
    private String entete3;
    
    @Column(name = "bas1")
    private String bas1;
        
    @Column(name = "bas2")
    private String bas2;
            
    @Column(name = "bas3")
    private String bas3;
    
    @NotNull
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;
    
    @NotNull
    @Column(name = "montant_ht")
    private BigDecimal montantHt;
    
    @Column(name = "montant_remise")
    private BigDecimal montantRemise;
    
    @NotNull
    @Column(name = "device_uuid")
    private String deviceUuid;
    
    @NotNull
    @Column(name = "utilisateur")
    private String utilisateur;
}
