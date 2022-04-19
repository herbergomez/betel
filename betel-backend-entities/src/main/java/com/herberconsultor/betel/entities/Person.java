/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.herberconsultor.betel.entities.enumtypes.CivilStateTypes;
import com.herberconsultor.betel.entities.enumtypes.PersonTypes;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="Persons")
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id
    @Column(name="per_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="per_names",nullable = false )
    private String names;
 
    @Column(name="per_last_names",nullable = false)
    private String lastNames;

    @Column(name="per_full_surname")
    private String fullname;
    
    @Column(name="per_civil_state")
    @Enumerated(EnumType.STRING)
    private CivilStateTypes civilState;
    
    @Column(name="per_birthdate")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;
    
    @Column(name="per_direction")
    private String direction;
    
    @Column(name="per_phone_number")
    private String phoneNumber;
    
    @Column(name="per_person_type")
    @Enumerated(EnumType.STRING)
    private PersonTypes personType;
    
    @Column(name = "per_createion_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date creationDate;
    
    @Column(name = "per_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.zzz")
    private Date modifyDate;
    
    @Column(name="per_image")
    private byte[] image;
    
    @Column(name="per_version")
    @Version
    private Integer version;
    
    public Person() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            version = 1;
        }
        if(personType == null) {
            personType = PersonTypes.CUSTOMER;
        }
        modifyDate = new Date();
        
        fullname=getFullName();
        if(fullname != null) {
            fullname=fullname.toLowerCase();
        }
    }

    private String getFullName() {
        String fullName = "";
        if(names != null && names.length() > 0) {
            fullName = names;
        }

        if(lastNames != null && lastNames.length() > 0) {
            fullName += " " + lastNames;
        }
        
        return fullName;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public CivilStateTypes getCivilState() {
        return civilState;
    }

    public void setCivilState(CivilStateTypes civilState) {
        this.civilState = civilState;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PersonTypes getPersonType() {
        return personType;
    }

    public void setPersonType(PersonTypes personType) {
        this.personType = personType;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
        final Person other = (Person) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
