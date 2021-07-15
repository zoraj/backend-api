/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author HERIZO
 */

@Entity
@Data
@Table(name = "t_mmc_banque")
public class TMmcBanque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 15)
    @NotNull  @NotBlank @Column(name = "code")
    private String code;
    
    @Size(min = 1, max = 100)
    @NotNull @NotBlank @Column (name = "nom")    
    private String nom;
    
    @Column (name = "responsable")
    private String responsable;
    
    @Column (name = "domiciliation")
    private String domiciliation;
    
    @Column (name = "postale")
    private String postale;
    
     @Column (name = "adresse")
    private String adresse;
    
    @Column (name = "ville")
    private String ville;
 
    @Column (name = "telephone")
    private String telephone;
    
    @Column (name = "code_minitel")
    private String codeminitel;
    
    @Column (name = "email")
    private String email;
    
    @Column (name = "web")
    private String web;
    
    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
}
