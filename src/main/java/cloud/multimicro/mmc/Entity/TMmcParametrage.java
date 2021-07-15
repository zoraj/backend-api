package cloud.multimicro.mmc.Entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
@Entity
@Table(name = "t_mmc_parametrage")
@Data
public class TMmcParametrage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Size(min = 1, max = 45)
    @NotNull @NotBlank
    private String cle;
    
    
    @Basic(optional = false)
    private String valeur;
    public TMmcParametrage() {
    }
    public TMmcParametrage(String key, String value) {
        this.cle = key;
        this.valeur = value;
    }    
}