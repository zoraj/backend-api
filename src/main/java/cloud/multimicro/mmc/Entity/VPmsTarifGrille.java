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
 * @author Tsiory
 */
@Entity
@Immutable
@Table(name = "v_pms_tarif_grille")
@Data
public class VPmsTarifGrille implements Serializable {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "reference")
    private String reference;
    
    @Column(name = "nom")
    private String nom;

    @Column(name = "nb_client")
    private Integer nbClient;

    @Column(name = "saison_id")
    private Integer saisonId;

    @Column(name = "sous_saison_id")
    private Integer sousSaisonId;
}
