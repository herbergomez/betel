/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="Credit_Cards")
public class CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="cc_card_number",nullable = false)
    private String ccCardNumber;
 
    @Column(name="cc_expiration_date",nullable = false)
    private String ccExpirationDate;
    
    @Column(name="cc_card_cvv")
    private String ccCardCvv;
    
    @Column(name="cc_postal_code")
    private String ccPostalCode;
    
    @Column(name="cc_phone_number")
    private String ccPhoneNumber;  
    
    @Column(name="cc_user_creation")
    private String ccUserCreation; 
    
    @Column(name = "cc_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "cc_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="cc_user_id",referencedColumnName = "usr_id")
    private User user;
    
    @Column(name="cc_version")
    @Version
    private Integer version;
    
    private transient String operation;
    
    public CreditCard() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            version = 1;
        }
        modifyDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCcCardNumber() {
        return ccCardNumber;
    }

    public void setCcCardNumber(String ccCardNumber) {
        this.ccCardNumber = ccCardNumber;
    }

    public String getCcExpirationDate() {
        return ccExpirationDate;
    }

    public void setCcExpirationDate(String ccExpirationDate) {
        this.ccExpirationDate = ccExpirationDate;
    }

    public String getCcCardCvv() {
        return ccCardCvv;
    }

    public void setCcCardCvv(String ccCardCvv) {
        this.ccCardCvv = ccCardCvv;
    }

    public String getCcPostalCode() {
        return ccPostalCode;
    }

    public void setCcPostalCode(String ccPostalCode) {
        this.ccPostalCode = ccPostalCode;
    }

    public String getCcPhoneNumber() {
        return ccPhoneNumber;
    }

    public void setCcPhoneNumber(String ccPhoneNumber) {
        this.ccPhoneNumber = ccPhoneNumber;
    }

    public String getCcUserCreation() {
        return ccUserCreation;
    }

    public void setCcUserCreation(String ccUserCreation) {
        this.ccUserCreation = ccUserCreation;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final CreditCard other = (CreditCard) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
