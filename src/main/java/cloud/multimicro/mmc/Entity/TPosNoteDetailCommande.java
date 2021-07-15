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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author herizo
 */
@Entity
@Table(name = "t_pos_note_detail_commande")
@Data
public class TPosNoteDetailCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @NotNull @Column(name = "pos_note_detail_id")
    private Integer posNoteDetailId;

    @JoinColumn(name = "pos_note_detail_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosNoteEntete posNoteDetail;
    
    @NotNull @Column(name = "qte_cde")
    private Integer qteCde;
    
    
    @Column(name = "pos_cuisson_id")
    private Integer posCuissonId;
    
    @JoinColumn(name = "pos_cuisson_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosCuisson posCuisson;
    
    @Column(name = "pos_accompagnement_id")
    private Integer posAccompagnementId;
    
    @JoinColumn(name = "pos_accompagnement_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosAccompagnement posAccompagnement;
    
    @Column(name = "pos_sirop_parfum_id")
    private Integer posSiropParfumId;
    
    
    @JoinColumn(name = "pos_sirop_parfum_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private TPosSiropParfum posSiropParfum;
    
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
    
}
