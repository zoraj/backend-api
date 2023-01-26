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
@Table(name = "v_pms_edition_depart_prevu")
@Data
public class VPmsEditionDepartPrevu implements Serializable {
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;
    
    @Id
    @Column(name = "numero_reservation")
    private String numeroReservation;
    
    @Column(name = "master")
    private String master;
    
    @Column(name = "num_chambre")
    private String numChambre;
    
    @Column(name = "type_chambre")
    private String typeChambre;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "adult_enfant")
    private String adultEnfant;
    
    @Column(name = "adult")
    private Integer adult;
    
    @Column(name = "enfant")
    private Integer enfant;
    
    @Column(name = "sejour")
    private String sejour;
    
    @Column(name = "qte")
    private Integer quantite;
    
    @Column(name = "nationalite")
    private String nationalite;
    
    @Column(name = "segment")
    private String segment;
    
    @Column(name = "total")
    private BigDecimal total;
    
    @Column(name = "id_sejour")
    private Integer idSejour;
    
    @Column(name = "id_chambre_sejour")
    private Integer idChambreSejour;
}
