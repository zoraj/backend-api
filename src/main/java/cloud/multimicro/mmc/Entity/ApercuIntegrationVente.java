/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Tsiory
 */

@Data
public class ApercuIntegrationVente {
    private LocalDate date_note;
    private String compte;
    private String sousfamille;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal solde;

}
