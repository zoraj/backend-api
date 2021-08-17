/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import javax.json.bind.annotation.JsonbDateFormat;

/**
 *
 * @author herizo
 */
@Entity
@Data
@Table(name = "t_pos_reservation")
public class TPosReservation implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 100)
    @NotNull
    @NotBlank
    @Column(name = "nom")
    private String nom;


    @Column(name = "email")
    private String email;
    

    @NotNull
    @Column(name = "nb_pers")
    private Integer nbPers;
    
    @Column(name = "telephone")
    private String telephone;
    
    @NotNull
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_reservation")
    private LocalDateTime dateReservation;
    
    @Column(name = "note")
    private String note;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
   @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
    
}
