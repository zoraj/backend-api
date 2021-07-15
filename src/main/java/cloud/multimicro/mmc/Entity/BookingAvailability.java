/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;


/**
 *
 * @author Naly
 */
@Data
public class BookingAvailability {
    private TPmsTypeChambre pmsTypeChambre;
    private TPmsModelTarif pmsModelTarif;
    private Integer nbRoomRequested;
    private Integer totalRoom;
    private Integer availableRoom;
    private Integer nbAdulte;
    private List<TPmsTarifOption> pmsTarifOption;

}

