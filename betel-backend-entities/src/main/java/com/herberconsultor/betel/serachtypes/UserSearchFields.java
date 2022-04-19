/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.serachtypes;

import com.herberconsultor.betel.entities.enumtypes.CivilStateTypes;
import com.herberconsultor.betel.entities.enumtypes.GenderTypes;
import com.herberconsultor.betel.entities.enumtypes.PersonTypes;
import com.herberconsultor.betel.entities.enumtypes.UserStatusTypes;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Usuario
 */
public class UserSearchFields implements Serializable {
    private Long id;
    private String email;
    private String username;
    private String fullname;
    private GenderTypes gender;
    private CivilStateTypes civilState;
    private Integer lessThan;
    private Integer greatherThan;
    private String direction;
    private String phoneNumber;
    private UserStatusTypes status;
    private PersonTypes personType;
    private String operation;
    private LocalDateTime creationDate;
        
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public GenderTypes getGender() {
        return gender;
    }

    public void setGender(GenderTypes gender) {
        this.gender = gender;
    }

    public CivilStateTypes getCivilState() {
        return civilState;
    }

    public void setCivilState(CivilStateTypes civilState) {
        this.civilState = civilState;
    }

    public Integer getLessThan() {
        return lessThan;
    }

    public void setLessThan(Integer lessThan) {
        this.lessThan = lessThan;
    }

    public Integer getGreatherThan() {
        return greatherThan;
    }

    public void setGreatherThan(Integer greatherThan) {
        this.greatherThan = greatherThan;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
