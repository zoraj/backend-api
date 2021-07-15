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
@Table(name = "v_com_edition_detail_compte")
@Data
public class VComEditionDetailCompte implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "mmcClientId")
    private Integer mmcClientId;

    @Column(name = "compte")
    private String compte;

    @Column(name = "type")
    private String typeLigne;

    @Column(name = "date_saisie")
    private LocalDate saisie;

    @Column(name = "nom")
    private String nom;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "reglement")
    private String reglement;

    @Column(name = "infos")
    private String infos;

    @Column(name = "etat")
    private String etat;

    public VComEditionDetailCompte() {

    }

    public VComEditionDetailCompte(Integer idValue, String typeLigneValue, LocalDate saisieValue, String nomValue,
            BigDecimal montantValue, String reglementValue, String infoValue, String etatValue) {
        id = idValue;
        typeLigne = typeLigneValue;
        saisie = saisieValue;
        nom = nomValue;
        montant = montantValue;
        reglement = reglementValue;
        infos = infoValue;
        etat = etatValue;
    }

}
