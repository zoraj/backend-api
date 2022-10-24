/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Entity;

/**
 *
 * @author HERIZO-PC
 */
import java.math.BigDecimal;
import lombok.Data;

@Data
public class DebitLiquide {
    private int id;
    private BigDecimal debitLiquide;
    private BigDecimal debitSolide;
    private BigDecimal admission;
    private BigDecimal cotisation;
    private BigDecimal subvention;
    private String nom;
    private String prenom;
    private String code;
    private int nbClt;
}
