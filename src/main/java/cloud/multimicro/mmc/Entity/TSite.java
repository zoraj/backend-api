package cloud.multimicro.mmc.Entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
/**
 * Author: Zo
 */
@Entity
@Data
@Table(name = "t_site")
public class TSite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private String code;
    
    @Size(max = 250)
    private String name;
    
    @Size(max = 128)
    @Column(name = "api_key")
    private String apiKey;
}
