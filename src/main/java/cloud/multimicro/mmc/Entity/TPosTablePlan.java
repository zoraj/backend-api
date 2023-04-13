/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Tsiory-PC
 */
@Entity
@Table(name = "t_pos_table_plan")
@Data
public class TPosTablePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id; 
    
    @Size(min = 1, max = 4)
    @NotNull @NotBlank @Column(name = "numero")
    private String numero;
    
    @NotNull
    @Column(name = "pos_activite_id")
    private Integer posActiviteId;
    
    @Column(name = "x")
    private Integer x;
    
    @Column(name = "y")
    private Integer y;
    
    @NotNull
    @Column(name = "forme")
    private String forme;
    
    @Column(name = "dimension")
    private Integer dimension;
    
    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateCreation;
    
    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @JsonbTransient
    private LocalDateTime dateModification;
    
    @Column(name = "DATE_DELETION")
    @JsonbTransient
    private LocalDateTime dateDeletion;
    
}
