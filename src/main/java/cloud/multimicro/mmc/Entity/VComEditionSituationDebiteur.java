/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
@Table(name = "v_com_edition_situation_debiteur")
@Data
public class VComEditionSituationDebiteur implements Serializable {

    @Id
    @Column(name = "debiteur")
    private String debiteur;
    
    @Column(name = "periode_prec")
    private Date periodePrec;
    
    @Column(name = "fevrier")
    private Integer fevrier;
    
    @Column(name = "mars")
    private Integer mars;
    
    @Column(name = "avril")
    private Integer avril;
    
    @Column(name = "mai")
    private Integer mai;
    
    @Column(name = "juin")
    private Integer juin;
    
    @Column(name = "juillet")
    private Integer juillet;
    
    @Column(name = "periode_suiv")
    private Date periodeSuiv;
    
    @Column(name = "total_compte")
    private BigDecimal totalCompte;
    
}
