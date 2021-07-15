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
@Table(name = "v_pos_edition_note_solde_jour")
@Data
public class VPosEditionNoteSoldeJour implements Serializable {
    @Id
    @Column(name = "num_note")
    private String numNote;

    @Column(name = "num_table")
    private Integer numTable;

    @Column(name = "nom_client")
    private String nomClient;

    @Column(name = "nb_couvert")
    private Integer nbCouvert;

    @Column(name = "serveur")
    private String serveur;

    @Column(name = "service")
    private String service;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date_note")
    private LocalDate dateNote;

}
