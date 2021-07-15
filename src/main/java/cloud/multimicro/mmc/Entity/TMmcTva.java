/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 *
 * @author Tsiory
 */

@Entity
@Data
@Table(name = "t_mmc_tva")
public class TMmcTva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_effective")
    private LocalDate dateEffective;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull @Column(name = "valeur")
    private BigDecimal valeur;
    
    @Column(name = "compte_comptable")
    private String compteComptable; 
    
    @Column(name = "compte_analytique")
    private String compteAnalytique; 
}
