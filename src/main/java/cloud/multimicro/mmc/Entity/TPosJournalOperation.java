/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Naly
 */
@Data
public class TPosJournalOperation {
    
    private LocalDateTime dateOperation;
    private String activity;
    private String poste;
    private String serveur;
    private String action;
    private String detail;
    
}