/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Data
public class EditionRemplissageAnnuel {

    private List<VPmsEditionRemplissageAnnuel> pmsEditionRemplissageAnnuel;
    private List<BigDecimal> tauxOccupation;
    private List<BigDecimal> total;
        
}
