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
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/**
 *
 * @author HERIZO RA
 */
@Entity
@Immutable
@Table(name = "v_pms_edition_occupation")
@Data
public class VPmsEditionOccupation implements Serializable {
   
    @Id
    @Column(name = "reference")    
    private String reference;
     
    @Column(name = "stock")
    private Integer stock; 
       
    @Column(name = "preaf")
    private Integer preaf;
    
    @Column(name = "note")
    private Integer note;  
    
    @Column(name = "date_depart")
    private Date dateDepart;
    
    @Column(name = "soldee")
    private Integer soldee;    
        
    @Column(name = "hors_service")
    private Integer horsService;
       
    @Column(name = "en_attente")
    private Integer enAttente;
    
    @Column(name = "libre")
    private Integer libre;  
}