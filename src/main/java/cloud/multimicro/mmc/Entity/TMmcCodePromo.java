/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.json.bind.annotation.JsonbTransient;
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
 * @author andy
 */
@Entity
@Data
@Table(name = "t_mmc_code_promo")
public class TMmcCodePromo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "code")
    @NotNull
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "expiration")
    @NotNull
    private LocalDate dateExpiration;
    
    @Column(name = "valeur")
    @NotNull
    private BigDecimal valeur;
    
    @Column(name = "type_remise")
    private String typeRemise;
    
    @Column(name = "limite_utilisation")
    private Integer limiteUtilisation;
    
    @Column(name = "min_applicable")
    private BigDecimal minApplicable;
    
    @Column(name = "max_applicable")
    private BigDecimal maxApplicable;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDate dateDeletion;
    
}