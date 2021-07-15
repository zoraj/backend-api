/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Table(name = "t_pos_calendrier_serveur")
@Data
public class TPosCalendrierServeur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "mmc_user_id")
    private Integer mmcUserId;

    @Transient
    private String mmcUserFirstname;

    @Column(name = "lundi")
    private Boolean lundi;

    @Column(name = "mardi")
    private Boolean mardi;

    @Column(name = "mercredi")
    private Boolean mercredi;

    @Column(name = "jeudi")
    private Boolean jeudi;

    @Column(name = "vendredi")
    private Boolean vendredi;

    @Column(name = "samedi")
    private Boolean samedi;

    @Column(name = "dimanche")
    private Boolean dimanche;

    @Column(name = "jour_absence_debut")
    private LocalDate jourAbsenceDebut;

    @Column(name = "jour_absence_fin")
    private LocalDate jourAbsenceFin;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;
}
