/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Data
public class EditionEffectifPrevFamTarif {

    private List<VPmsEditionEffectifPrevFamTarif> pmsEditionEffectifPrevFamTarif;
    private List<Integer> nbOccupation;
    private List<Integer> nbArriv;
    private List<Integer> nbDepart;
    private List<Integer> nbRec;
    private List<Integer> nbPdj;
    private List<Integer> nbPax;
    private List<Integer> nbAdultEnfant;
    private List<Integer> nbCvtMidi;
    private List<Integer> nbCvtSoir;
    
}
