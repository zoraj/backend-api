/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author Tsiory-PC
 */
@Entity
@Immutable
@Table(name = "v_pms_reservation_ventilation")
@Data
public class VPmsReservationVentilation implements Serializable {

    @Id
    @Column(name = "id_ventilation")
    private Integer idVentilation;
    
    @Column(name = "id_reservation")
    private Integer idReservation;
    
    @Column(name = "numero_chambre")
    private String numeroChambre;
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "categorie_chambre")
    private String categorieChambre;
    
    @Column(name = "etat_chambre")
    private String etatChambre;
}
