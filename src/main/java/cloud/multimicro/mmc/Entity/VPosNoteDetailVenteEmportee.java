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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_note_detail_vente_emportee")
@Data
public class VPosNoteDetailVenteEmportee implements Serializable {
    
    @Id
    @Column(name = "note_detail_id")
    private Integer noteDetailId;
    
    @Column(name = "note_entete_id")
    private Integer noteEnteteId;
    
    @Column(name = "prestation_id")
    private Integer prestationId;
    
    @Column(name = "qte")
    private Integer qte;
    
    @Column(name = "libelle_prestation")
    private String libellePrestation;
    
    @Column(name = "libelle_cuisson")
    private String libelleCuisson;
    
    @Id
    @Column(name = "libelle_accompagnement")
    private String libelleAccompagnement;
}
