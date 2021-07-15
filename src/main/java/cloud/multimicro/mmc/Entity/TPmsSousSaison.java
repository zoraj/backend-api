/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_sous_saison")
@Data
public class TPmsSousSaison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Size(min = 1, max = 10)
    @NotNull
    @NotBlank
    @Column(name = "reference")
    private String reference;

    @Size(min = 1, max = 100)
    @NotNull
    @NotBlank
    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @Column(name = "pms_saison_id")
    private Integer pmsSaisonId;

    @JoinColumn(name = "pms_saison_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPmsSaison pmsSaison;

    @NotNull
    @Column(name = "date_debut", columnDefinition = "DATE")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateFin;

    @JsonbTransient
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @JsonbTransient
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @JsonbTransient
    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
}
