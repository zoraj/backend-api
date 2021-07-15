package cloud.multimicro.mmc.Entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class RemittanceInBankDetail {

    private Date cashingDate;
    private String clientAccount;
    private String clientName;
    private String information;
    private String cashingMode;
    private BigDecimal amount;

    public RemittanceInBankDetail() {

    }

}
