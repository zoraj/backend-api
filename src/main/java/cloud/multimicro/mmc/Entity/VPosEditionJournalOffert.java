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
@Table(name = "v_pos_edition_journal_offert")
@Data
public class VPosEditionJournalOffert implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "heure")
    private String heure;

    @Column(name = "note")
    private String note;

    @Column(name = "code")
    private String code;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "service")
    private String service;

    @Column(name = "quantite")
    private Integer qte;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "tva")
    private BigDecimal tva;

    @Column(name = "taux_remise")
    private BigDecimal tauxRemise;

    @Column(name = "taux_service")
    private BigDecimal tauxService;

    @Column(name = "serveur")
    private String serveur;

    @Column(name = "reglement")
    private BigDecimal reglement;

    @Column(name = "compte")
    private String compte;

    @Column(name = "chambre")
    private String chambre;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "dateNote")
    private LocalDate dateNote;

}
