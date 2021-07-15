/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
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
@Table(name = "v_pms_edition_effectif_prev_debours")
@Data
public class VPmsEditionEffectifPrevDebours implements Serializable {
    
    @Id
    @Column(name = "date_effectif")
    private Integer dateEffectif;
    
    @Column(name = "nb_occupation")
    private Integer nbOccupation;
    
    @Column(name = "nb_arriv")
    private Integer nbArriv;
    
    @Column(name = "nb_depart")
    private Integer nbDepart;
    
    @Column(name = "nb_rec")
    private Integer nbRec;
    
    @Column(name = "nb_pdj")
    private Integer nbPdj;
    
    @Column(name = "nb_pax")
    private Integer nbPax;
    
    @Column(name = "nb_adult_enfant")
    private Integer nbAdultEnfant;
    
    @Column(name = "nb_cvt_midi")
    private Integer nbCvtMidi;
    
    @Column(name = "nb_cvt_soir")
    private Integer nbCvtSoir;
    
}
