/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Tsiory-PC
 */
@Data
public class TotalRoomOutOfOrderByType {
    private int typeChambreId;   
    private int roomCountOutOfOrder;
    private LocalDate dateOutOfOrder;  
}
