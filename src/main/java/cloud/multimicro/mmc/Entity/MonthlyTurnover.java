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
 * @author Tsiory
 */
@Data
public class MonthlyTurnover {
    private Long couvert;
    private String service;
    private int monthCashing;
    private Integer yearCashing;
    private BigDecimal montant;
    private BigDecimal montantTotByMonth;
    private BigDecimal montantTva;
    private BigDecimal montantCaSoir;
    private BigDecimal montantCaMidi;   
}
