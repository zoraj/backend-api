package cloud.multimicro.mmc.Entity;
import java.util.List;

import lombok.Data;

@Data
public class RoomByType {
    private int qteDispo;
    private int qteTotal;
    private int pmsTypeChambreId;
    private int nbChild;
    private int persMin;
    private int persMax;
    private String typeChambre;
    private List<TarifApplicable> tarif;
    private List<TPmsTypeChambrePhoto> roomPhoto;
    private int mmcModeEncaissementId;
    private int mmcClientId;
    private int pmsTarifGrilleId;
}
