/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;


/**
 *
 * @author Tsiory
 */
@Entity
@Table(name = "t_pms_sejour_tarif")
@Data
public class TPmsSejourTarif implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @NotNull
    @Column(name = "pms_sejour_id")
    private Integer pmsSejourId;

    @NotNull
    @Column(name = "date_sejour")
    private Date dateSejour;

    @NotNull
    @Column(name = "pms_model_tarif_id")
    private Integer pmsModelTarifId;

    @NotNull
    @Column(name = "base")
    private Integer base;
    
    public TPmsSejourTarif() {

    }
    
}
