/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "v_pms_chambre_disponibilite_etat")
@Data
public class VPmsChambreDisponibiliteEtat implements Serializable {

    @Id
    @Column(name = "id_chambre")
    private Integer idChambre;
    
    @Id
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Id
    @Column(name = "id_type_chambre")
    private Integer idTypeChambre;
    
    @Id
    @Column(name = "type_chambre")
    private String typeChambre; 
    
    @Id
    @Column(name = "date_arrivee")
    @JsonbProperty(nillable = true)
    private LocalDate dateArrivee;
    
    @Id
    @Column(name = "date_depart")
    @JsonbProperty(nillable = true)
    private LocalDate dateDepart;
    
}
