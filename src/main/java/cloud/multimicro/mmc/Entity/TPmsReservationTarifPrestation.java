/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Entity
@Table(name = "t_pms_reservation_tarif_prestation")
@Data
public class TPmsReservationTarifPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @NotNull
    @Column(name = "pms_reservation_tarif_id")
    private Integer pmsReservationTarifId;
    
    @NotNull
    @Column(name = "pms_prestation_id")
    private Integer pmsPrestationId;
    
    @NotNull
    @Column(name = "qte")
    private Integer qte;
    
    @NotNull
    @Column(name = "pu")
    private BigDecimal pu;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "promot")
    private BigDecimal promot;
    
    @Column(name = "commis")
    private BigDecimal commis;
    
    @Column(name = "early_booking")
    private BigDecimal earlyBooking;
    
    @Column(name = "ong")
    private String ong;
    
    @Column(name = "is_extra")
    private Integer isExtra;
    
    @Column(name = "is_recouche")
    private Integer isRecouche;
    
    @Column(name = "total")
    private BigDecimal total;
    
}
