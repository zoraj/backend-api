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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Tsiory-PC
 */
@Entity
@Immutable
@Table(name = "t_pos_cloture_definitive")
@Data
public class TPosClotureDefinitive implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "date_cloture")
    private LocalDate dateCloture;
    
    @Column(name = "libelle")
    private String libelle;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    
    @Column(name = "montant_ht")
    private BigDecimal montantHt;
    
    @Column(name = "montant_tva")
    private BigDecimal montantTva;
    
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;
    
    @Column(name = "montant_remise")
    private BigDecimal montantRemise;
    
    @Column(name = "montant_offert")
    private BigDecimal montantOffert;
    
    @Column(name = "service")
    private String service;
    
    @Column(name = "nb_couvert")
    private Integer nbCouvert;
    
    @Column(name = "montant_encaissement")
    private BigDecimal montantEncaissement;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    public TPosClotureDefinitive() {
        
    }
    
}
