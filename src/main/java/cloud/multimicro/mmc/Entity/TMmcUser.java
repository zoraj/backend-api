/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "t_mmc_user")
@Data
public class TMmcUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 1, max = 45)
    @NotNull
    @NotBlank
    @Column(name = "login")
    private String login;

    @Size(min = 1, max = 64)
    @NotNull
    private String password;

    @Size(max = 64)
    @NotNull
    private String salt;

    @NotNull
    @Size(min = 1, max = 45)
    private String firstname;

    @NotNull
    @Size(min = 1, max = 45)
    private String lastname;

    @Column(name = "user_type")
    @NotNull
    private String userType;

    @Column(name = "module_authorized")
    @NotNull
    @Size(min = 1, max = 250)
    private String moduleAuthorized;

    @Column(name = "code_waiter")
    private String codeWaiter;

    @Transient
    private String token;

    @Column(name = "DATE_CREATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "DATE_MODIFICATION", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateModification;

    @Column(name = "DATE_DELETION")
    @Temporal(TemporalType.DATE)
    private Date dateDeletion;
}
