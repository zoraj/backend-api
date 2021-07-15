/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
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
@Table(name = "v_collectivite_valorisation_bac")
@Data
public class VCollectiviteValorisationBac implements Serializable {   

    @Id
    @Column(name = "periodeJour")
    private Integer periodeJour;

    @Column(name = "periodeMois")
    private Integer periodeMois;

    @Column(name = "periodeAnnee")
    private Integer periodeAnnee;

    @Column(name = "debitSolideJour")
    private Integer debitSolideJour;

    @Column(name = "debitSolideMois")
    private Integer debitSolideMois;

    @Column(name = "debitSolideAnnee")
    private Integer debitSolideAnnee;

    @Column(name = "debitLiquideJour")
    private Integer debitLiquideJour;

    @Column(name = "debitLiquideMois")
    private Integer debitLiquideMois;

    @Column(name = "debitLiquideAnnee")
    private Integer debitLiquideAnnee;

    @Column(name = "zoneSuppJour")
    private Integer zoneSuppJour;

    @Column(name = "zoneSuppMois")
    private Integer zoneSuppMois;

    @Column(name = "zoneSuppAnnee")
    private Integer zoneSuppAnnee;

    @Column(name = "versementCreditJour")
    private Integer versementCreditJour;

    @Column(name = "versementCreditMois")
    private Integer versementCreditMois;

    @Column(name = "versementCreditAnnee")
    private Integer versementCreditAnnee;

    @Column(name = "nbAdmissionJour")
    private Integer nbAdmissionJour;

    @Column(name = "nbAdmissionMois")
    private Integer nbAdmissionMois;

    @Column(name = "nbAdmissionAnnee")
    private Integer nbAdmissionAnnee;

    @Column(name = "montantAdmissionJour")
    private Integer montantAdmissionJour;

    @Column(name = "montantAdmissionMois")
    private Integer montantAdmissionMois;

    @Column(name = "montantAdmissionAnnee")
    private Integer montantAdmissionAnnee;

    @Column(name = "nbPassagePdjJour")
    private Integer nbPassagePdjJour;

    @Column(name = "nbPassagePdjMois")
    private Integer nbPassagePdjMois;

    @Column(name = "nbPassagePdjAnnee")
    private Integer nbPassagePdjAnnee;

    @Column(name = "nbPassageMidiJour")
    private Integer nbPassageMidiJour;

    @Column(name = "nbPassageMidiMois")
    private Integer nbPassageMidiMois;

    @Column(name = "nbPassageMidiAnnee")
    private Integer nbPassageMidiAnnee;

    @Column(name = "nbPassageSoirJour")
    private Integer nbPassageSoirJour;

    @Column(name = "nbPassageSoirMois")
    private Integer nbPassageSoirMois;

    @Column(name = "nbPassageSoirAnnee")
    private Integer nbPassageSoirAnnee;

    @Column(name = "nbPassageForceJour")
    private Integer nbPassageForceJour;

    @Column(name = "nbPassageForceMois")
    private Integer nbPassageForceMois;

    @Column(name = "nbPassageForceAnnee")
    private Integer nbPassageForceAnnee;

    @Column(name = "totauxPassageJour")
    private Integer totauxPassageJour;

    @Column(name = "totauxPassageMois")
    private Integer totauxPassageMois;

    @Column(name = "totauxPassageAnnee")
    private Integer totauxPassageAnnee;

    @Column(name = "montantSubvJour")
    private Integer montantSubvJour;

    @Column(name = "montantSubvMois")
    private Integer montantSubvMois;

    @Column(name = "montantSubvAnnee")
    private Integer montantSubvAnnee;

    @Column(name = "variationPeriodeJour")
    private Integer variationPeriodeJour;

    @Column(name = "variationPeriodeMois")
    private Integer variationPeriodeMois;

    @Column(name = "variationPeriodeAnnee")
    private Integer variationPeriodeAnnee;

    @Column(name = "totauxSoldConviveJour")
    private Integer totauxSoldConviveJour;

    @Column(name = "totauxSoldConviveMois")
    private Integer totauxSoldConviveMois;

    @Column(name = "totauxSoldConviveAnnee")
    private Integer totauxSoldConviveAnnee;

}