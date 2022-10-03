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
@Table(name = "t_pms_cloture_definitive")
@Data
public class TPmsClotureDefinitive implements Serializable {

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

    @Column(name = "montant_jour")
    private BigDecimal montantJour;

    @Column(name = "montant_periode")
    private BigDecimal montantPeriode;

    @Column(name = "montant_mois")
    private BigDecimal montantMois;

    @Column(name = "montant_annee")
    private BigDecimal montantAnnee;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    public TPmsClotureDefinitive() {
        
    }
    
}
