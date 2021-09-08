/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_consigne_vente")
@Data
public class TPmsConsigneVente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "date_debut")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateFin;

    @Size(min = 1, max = 255)
    @NotNull
    @NotBlank
    @Column(name = "message")
    private String message;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
}
