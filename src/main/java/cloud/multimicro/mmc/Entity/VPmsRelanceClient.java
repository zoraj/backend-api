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
 * @author Tsiory
 */
@Entity
@Immutable
@Table(name = "v_pms_relance_client")
@Data
public class VPmsRelanceClient implements Serializable {
    @Id
    @Column(name = "compte_debiteur")
    private String compteDebiteur;

    @Id
    @Column(name = "date_facture")
    private Date dateFacture;

    @Column(name = "num_facture")
    private String numFacture;

    @Id
    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "solde_facture")
    private BigDecimal soldeFacture;

    @Column(name = "niveau_relance")
    private Integer niveauRelance;

    @Column(name = "date_derniere_relance")
    private Date dateDerniereRelance;

}
