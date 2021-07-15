/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Tsiory
 */
@Data
public class PassageHistory {
    private int nbPassage;
    private String name;
    private BigDecimal admission;
    private BigDecimal subvention;
    private BigDecimal liquidDebit;
    private BigDecimal solidDebit;
    private BigDecimal credit;
    private BigDecimal zoneSup;
    private BigDecimal debitBalance;
    private BigDecimal creditBalance;
    private BigDecimal balance;
    private Date datePassage;
    private String caseName;
    private String ticketNumber;
    private int idNoteHeader;

    public PassageHistory() {
        
    }

   
    
}
