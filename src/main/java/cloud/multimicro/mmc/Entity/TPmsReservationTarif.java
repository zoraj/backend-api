/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_reservation_tarif")
@Data
public class TPmsReservationTarif implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    @Column(name = "pms_reservation_id")
    private Integer pmsReservationId;

    @NotNull
    @Column(name = "pms_type_chambre_id")
    private Integer pmsTypeChambreId;

    @Column(name = "pms_chambre_id")
    private Integer pmsChambreId;

    @Column(name = "nom")
    private String nom;

    @NotNull
    @Column(name = "nb_adult")
    private Integer nbAdult;

    @Column(name = "nb_enf")
    private Integer nbEnf;

    @NotNull
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @NotNull
    @Column(name = "date_fin")
    private LocalDate dateFin;    

    
    @Column(name = "base")
    private Integer base;

    @NotNull
    @Column(name = "pms_tarif_grille_detail_id")
    private Integer pmsTarifGrilleDetailId;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;


    public TPmsReservationTarif() {

    }
}
