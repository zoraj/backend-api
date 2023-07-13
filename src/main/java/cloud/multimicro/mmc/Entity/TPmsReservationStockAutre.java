/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Naly-PC
 */
@Entity
@Table(name = "t_pms_reservation_stock_autre")
@Data
public class TPmsReservationStockAutre implements Serializable {

    @NotNull
    @Id
    @Column(name = "pms_reservation_id")
    private Integer pmsReservationId;
    
    @NotNull
    @Id
    @Column(name = "pms_stock_autre_id")
    private Integer pmsStockAutreId;
    
    @NotNull
    @Column(name = "date_debut")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateFin;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    private LocalDate dateModification;

    @Column(name = "DATE_DELETION")
    private LocalDate dateDeletion;
    
}