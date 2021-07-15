
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import javax.json.bind.annotation.JsonbDateFormat;
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
@Table(name = "v_pos_ca")
@Data
public class VPosCa implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_ca")
    private LocalDate dateCa;

    @Column(name = "year_month_note")
    private String yearMonthNote;

    @Column(name = "year_note")
    private Integer yearNote;

    @Column(name = "poste_uuid")
    private String posteUuid;

    @Column(name = "montant_ca")
    private BigDecimal montantCa;

    @Column(name = "pos_activite_id")
    private Integer posActiviteId;
    
    @Column(name = "libelle_activite")
    private String libelleActivite;

    @Column(name = "mmc_user_id")
    private Integer mmcUserId;
    
    @Column(name = "id_sous_famille")
    private Integer idSousFamille;
    
    @Column(name = "libelle_sous_famille")
    private String libelleSousFamille;
    
    @Column(name = "tx_tva")
    private BigDecimal txTva;
    
    @Column(name = "ca_ht")
    private BigDecimal caHt;
    
    @Column(name = "tva")
    private BigDecimal tva;
    
    @Column(name = "remise")
    private BigDecimal remise;
    
    @Column(name = "offert")
    private BigDecimal offert;
    
    @Column(name = "service")
    private String service;

    public VPosCa() {

    }

    public VPosCa(LocalDate dateCaValue, BigDecimal montantCaValue) {
        this.dateCa = dateCaValue;
        this.montantCa = montantCaValue;
    }

    public VPosCa(String value, BigDecimal montantCaValue) {
        this.yearMonthNote = value;
        this.montantCa = montantCaValue;
    }

    public VPosCa(BigDecimal montantCaValue, String posteUuidValue) {
        this.montantCa = montantCaValue;
        this.posteUuid = posteUuidValue;
    }

    public VPosCa(Integer value, BigDecimal montantCaValue) {
        this.yearNote = value;
        this.montantCa = montantCaValue;
    }

    public VPosCa(LocalDate dateCaValue, Integer posActiviteIdValue, BigDecimal montantCaValue) {
        this.dateCa = dateCaValue;
        this.montantCa = montantCaValue;
        this.posActiviteId = posActiviteIdValue;
    }

}
