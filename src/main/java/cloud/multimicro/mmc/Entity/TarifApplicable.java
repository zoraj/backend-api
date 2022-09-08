package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TarifApplicable {
    Integer typeTarifId;
    String typeTarifLibelle;
    BigDecimal amount;
    Integer pmsTarifGrilleDetailId;
    Integer base;
}
