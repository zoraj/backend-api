/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Data
public class EditionListePrestationArrivee {
    
    private String libellePrestation;
    private List<VPmsEditionListePrestationArrivee> sejour;
    private Integer total; 
    
}
