/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;

import lombok.Data;

/**
 *
 * @author Naly
 */
@Data
public class RemittanceInBank {
    private BigDecimal amount;
    private String cashingMode;

    public RemittanceInBank() {

    }

}
