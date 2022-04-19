/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;


import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderStateTypes;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderWorkflowTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
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
@Table(name="purchase_orders")
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="po_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="po_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStateTypes status;
    
    @Column(name="po_workflow")
    @Enumerated(EnumType.STRING)
    private PurchaseOrderWorkflowTypes workflow;
    
    @Column(name="po_total_cost")
    private BigDecimal totalCost;
    
    @Column(name="po_total_tax")
    private BigDecimal totalTax;
    
    @Column(name="po_total_worker")
    private BigDecimal totalWorker;
    
    @Column(name="po_total_cost_plus_tax")
    private BigDecimal totalCostPlusTax;
    
    @Column(name="po_latitude")
    private Double latitude;
    
    @Column(name="po_longitude")
    private Double longitude;
    
    @Column(name="po_address")
    private String address;
    
    @Column(name = "po_creation_date",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "po_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="po_usr_id",referencedColumnName = "usr_id")
    private User user;
    
    @Column(name="po_version")
    @Version
    private Integer version;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItemList;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PurchaseOrderWorker> purchaseOrderWorkerList;
    
    
    private transient String operation;
    
    public PurchaseOrder() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            version = 1;
            status = PurchaseOrderStateTypes.PENDING;
        }
        modifyDate = new Date();
        if(totalCost == null){
            totalCost = BigDecimal.ZERO;
        }
        if(totalTax == null) {
            totalTax = BigDecimal.ZERO;
        }
        if(totalCostPlusTax == null) {
            totalCostPlusTax = BigDecimal.ZERO;
        }
        if(totalWorker == null) {
            totalWorker = BigDecimal.ZERO;
        }
        if(purchaseOrderItemList == null){
            purchaseOrderItemList = new ArrayList<>();
        }
        if(purchaseOrderWorkerList == null){
            purchaseOrderWorkerList = new ArrayList<>();
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

    public PurchaseOrderStateTypes getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStateTypes status) {
        this.status = status;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList() {
        return purchaseOrderItemList;
    }

    public void setPurchaseOrderItemList(List<PurchaseOrderItem> purchaseOrderItemList) {
        this.purchaseOrderItemList = purchaseOrderItemList;
    }

    public List<PurchaseOrderWorker> getPurchaseOrderWorkerList() {
        return purchaseOrderWorkerList;
    }

    public void setPurchaseOrderWorkerList(List<PurchaseOrderWorker> purchaseOrderWorkerList) {
        this.purchaseOrderWorkerList = purchaseOrderWorkerList;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalCostPlusTax() {
        return totalCostPlusTax;
    }

    public void setTotalCostPlusTax(BigDecimal totalCostPlusTax) {
        this.totalCostPlusTax = totalCostPlusTax;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public PurchaseOrderWorkflowTypes getWorkflow() {
        return workflow;
    }

    public void setWorkflow(PurchaseOrderWorkflowTypes workflow) {
        this.workflow = workflow;
    }

    public BigDecimal getTotalWorker() {
        return totalWorker;
    }

    public void setTotalWorker(BigDecimal totalWorker) {
        this.totalWorker = totalWorker;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        final PurchaseOrder other = (PurchaseOrder) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
