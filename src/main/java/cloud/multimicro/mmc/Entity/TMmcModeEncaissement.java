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
 * @author Naly
 */
@Entity
@Data
@Table(name = "t_mmc_mode_encaissement")
public class TMmcModeEncaissement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 45)
    @NotNull @NotBlank @Column(name = "code")
    private String code;
    
    @NotNull @NotBlank
    @Size(min = 1, max = 45)
    private String libelle;
    
    @Column(name = "compte_encaissement")
    private String compteEncaissement;
    
    @Column(name = "compte_comptable")
    private String compteComptable;
    
    @Column(name = "compte_analytique")
    private String compteAnalytique;
    
    @Column(name = "transfert_debiteur")
    private boolean transfertDebiteur;
    
    @Column(name = "transfert_arrangement_debours")
    private boolean transfertArrangementDebours;
    
    @Column(name = "report_note_sur_note")
    private boolean reportNoteSurNote;
    
    @Column(name = "report_chambre")
    private boolean reportChambre;
    
    @Column(name = "rendu_monnaie")
    private boolean rendueMonnaie;
    
    @Column(name = "encaissement_type")
    private String encaissementType;
    
    @Column(name = "compte_type")
    private String compteType;
    
    @Column(name = "icone_img")
    private String iconeImg;
    
    @Column(name = "nature")
    private String nature;
    
    @Column(name = "autorise_arrhes")
    private boolean autoriseArrhes;
    
    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;



}
