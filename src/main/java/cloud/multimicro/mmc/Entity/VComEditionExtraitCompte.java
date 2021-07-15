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
@Table(name = "v_com_edition_extrait_compte")
@Data
public class VComEditionExtraitCompte implements Serializable {

    @Id
    @Column(name = "type_ligne")
    private String typeLigne;
    
    @Column(name = "saisie")
    private String saisie;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "debit")
    private Integer debit;
    
    @Column(name = "credit")
    private Integer credit;
    
    @Column(name = "reglement")
    private String reglement;
    
    @Column(name = "infos")
    private String infos;
    
    @Column(name = "etat")
    private String etat;
    
}
