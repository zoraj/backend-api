/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.LocalDate;
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
@Table(name = "t_pos_happy_hours")
@Data
public class TPosHappyHours implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @NotNull @Column(name = "pos_activite_id")
    private Integer posActiviteId;
    
    @Transient
    private String posActiviteLibelle; 
    
    @Column(name = "heure_debut")
    private LocalTime heureDebut;
     
    @Column(name = "heure_fin")
    private LocalTime heureFin;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "active")
    private Boolean active;
    
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
    
    @Transient
    private String frequence;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)

    private LocalDateTime dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)

    private LocalDateTime dateModification;
    
    @Column(name = "DATE_DELETION")
 
    private LocalDateTime dateDeletion;
    
}
