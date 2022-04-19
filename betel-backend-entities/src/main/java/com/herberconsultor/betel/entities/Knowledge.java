/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;


import com.herberconsultor.betel.entities.enumtypes.YearExperienceTypes;
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
@Table(name="knowledges")
public class Knowledge implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="kn_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="kn_description")
    private String description;
    
    @Column(name="kn_year_experience",nullable = false)
    @Enumerated(EnumType.STRING)
    private YearExperienceTypes yearExperience;
 
    @Column(name="kn_year_experience_min")
    private Integer yearExperienceMin;
    
    @Column(name="kn_year_experience_max")
    private Integer yearExperienceMax;
    
    @Column(name="kn_reference_names")
    private String referenceNames;
    
    @Column(name="kn_reference_phone")
    private String referencePhone;
    
    @Column(name = "kn_creation_date",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "kn_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="kn_usr_id",referencedColumnName = "usr_id")
    private User user;
    
    @Column(name="kn_version")
    @Version
    private Integer version;
      
    private transient String operation;
    
    public Knowledge() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            version = 1;
            
        }
        if(yearExperience==null){
            yearExperience = YearExperienceTypes.Y_0_1;
        }
        modifyDate = new Date();
        if(yearExperience == YearExperienceTypes.Y_0_1) {
            yearExperienceMin=0;
            yearExperienceMax=1;
        } else if(yearExperience == YearExperienceTypes.Y_1_3) {
            yearExperienceMin=1;
            yearExperienceMax=3;
        } else if(yearExperience == YearExperienceTypes.Y_3_5) {
            yearExperienceMin=3;
            yearExperienceMax=5;
        } else if(yearExperience == YearExperienceTypes.Y5) {
            yearExperienceMin=5;
            yearExperienceMax=50;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YearExperienceTypes getYearExperience() {
        return yearExperience;
    }

    public void setYearExperience(YearExperienceTypes yearExperience) {
        this.yearExperience = yearExperience;
    }

    public Integer getYearExperienceMin() {
        return yearExperienceMin;
    }

    public void setYearExperienceMin(Integer yearExperienceMin) {
        this.yearExperienceMin = yearExperienceMin;
    }

    public Integer getYearExperienceMax() {
        return yearExperienceMax;
    }

    public void setYearExperienceMax(Integer yearExperienceMax) {
        this.yearExperienceMax = yearExperienceMax;
    }

    public String getReferenceNames() {
        return referenceNames;
    }

    public void setReferenceNames(String referenceNames) {
        this.referenceNames = referenceNames;
    }

    public String getReferencePhone() {
        return referencePhone;
    }

    public void setReferencePhone(String referencePhone) {
        this.referencePhone = referencePhone;
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
        final Knowledge other = (Knowledge) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
