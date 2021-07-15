/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
@Table(name = "v_com_edition_agee_arrhe")
@Data
public class VComEditionAgeeArrhe implements Serializable {

    @Id
    @Column(name = "compte_client")
    private String compteClient;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_saisie")
    private Date dateSaisie;
    
    @Column(name = "debit")
    private Integer debit;
    
    @Column(name = "credit")
    private Integer credit;
    
    @Column(name = "reglement")
    private String reglement;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "etat")
    private String etat;
    
}
