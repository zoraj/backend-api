/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
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
 * @author Naly
 */
@Entity
@Immutable
@Table(name = "v_pos_edition_journal_operation")
@Data
public class VPosEditionJournalOperation implements Serializable {

    @Id
    @Column(name = "date_journal_operation")
    private Date dateJournalOperation;
    
    @Column(name = "heure")
    private Integer heure;
    
    @Column(name = "poste")
    private String poste;
    
    @Column(name = "action")
    private String action;
    
    @Column(name = "detail")
    private String detail;
    
}
