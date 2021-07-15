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
@Table(name = "v_com_edition_solde_compte")
@Data
public class VComEditionSoldeCompte implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_arrivee")
    LocalDate dateArrivee;

    @Column(name = "date_depart")
    LocalDate dateDepart;

    @Column(name = "compte_client")
    private String compteClient;

    @Column(name = "solde_fact")
    private BigDecimal soldeFact;

    @Column(name = "solde_arrh")
    private BigDecimal soldeArrh;

    @Column(name = "solde")
    private BigDecimal solde;

    @Column(name = "etat")
    private String etat;

    @Column(name = "isConsomme")
    private Integer consomme;

    public VComEditionSoldeCompte() {

    }

    public VComEditionSoldeCompte(Integer idValue, String compteClt, BigDecimal fact, BigDecimal arrh, BigDecimal sold,
            String etatValue, Integer consommeValue) {
        id = idValue;
        compteClient = compteClt;
        soldeFact = fact;
        soldeArrh = arrh;
        solde = sold;
        etat = etatValue;
        consomme = consommeValue;
    }

}
