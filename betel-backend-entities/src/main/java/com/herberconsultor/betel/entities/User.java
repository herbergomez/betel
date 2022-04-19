/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;


import com.herberconsultor.betel.entities.enumtypes.UserStatusTypes;
import com.herberconsultor.betel.entities.enumtypes.WorkerStatusTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Herber Consultor
 */
//@EntityListeners(AuditListener.class)
@Entity
@Table(name="Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
    @Column(name="usr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="usr_email",nullable = false)
    private String email;
 
    @Column(name="usr_username",nullable = false)
    private String username;
    
    @Column(name="usr_password")
    private String password;
    
    @Column(name="usr_accept_police")
    private Boolean acceptPolice;
    
    @Column(name="usr_blocked")
    private Boolean blocked;
    
    @Column(name="usr_status")
    @Enumerated(EnumType.STRING)
    private UserStatusTypes status;
    
    @Column(name="usr_worker_status")
    @Enumerated(EnumType.STRING)
    private WorkerStatusTypes workerStatus;
    
    @Column(name="usr_basic_information_completed")
    private Boolean basicInformationCompleted;
    
    @Column(name = "usr_blocked_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date blockedDate;
    
    @Column(name = "usr_reset_password_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date resetPasswordDate;
    
    @Column(name="usr_reset_password")
    private Boolean resetPassword;
 
    @Column(name="usr_login_fail_count")
    private Integer loginFailCount;

    
    @Column(name = "usr_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date creationDate;
    
    @Column(name = "usr_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date modifyDate;
    
    
    @ManyToOne(optional=false,cascade = CascadeType.ALL) 
    @JoinColumn(name="usr_per_id",referencedColumnName = "per_id")
    private Person person;
    
    @Column(name="usr_version")
    @Version
    private Integer version;
    
    private transient String newPassword;
    private transient String operation;
    private transient BigDecimal rating;
    public User() {
    }

    public User(Long id) {
        this.id = id;
    }
    
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            basicInformationCompleted = false;
            version = 1;
            status = UserStatusTypes.CREATED;
        }
        modifyDate = new Date();
        if(username != null) {
            username = username.trim();
        }
        if(email != null) {
            email = email.trim();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public Date getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Date blockedDate) {
        this.blockedDate = blockedDate;
    }

    public Date getResetPasswordDate() {
        return resetPasswordDate;
    }

    public void setResetPasswordDate(Date resetPasswordDate) {
        this.resetPasswordDate = resetPasswordDate;
    }

    public Boolean getBasicInformationCompleted() {
        return basicInformationCompleted;
    }

    public void setBasicInformationCompleted(Boolean basicInformationCompleted) {
        this.basicInformationCompleted = basicInformationCompleted;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public UserStatusTypes getStatus() {
        return status;
    }

    public void setStatus(UserStatusTypes status) {
        this.status = status;
    }

    public Boolean getAcceptPolice() {
        return acceptPolice;
    }

    public void setAcceptPolice(Boolean acceptPolice) {
        this.acceptPolice = acceptPolice;
    }

    public WorkerStatusTypes getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(WorkerStatusTypes workerStatus) {
        this.workerStatus = workerStatus;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
