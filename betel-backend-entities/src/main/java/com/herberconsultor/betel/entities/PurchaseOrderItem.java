/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.entities;


import com.herberconsultor.betel.entities.enumtypes.ProductTypes;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderStateTypes;
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
@Table(name="purchase_orders_items")
public class PurchaseOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="poi_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name="poi_unit_of_measure",updatable = false)
    @Enumerated(EnumType.STRING)
    private ProductTypes unitOfMeasure;
 
    @Column(name="poi_unit_cost")
    private BigDecimal unitCost;
    
    @Column(name="poi_count_uom")
    private Integer countUom;
    
    @Column(name="poi_total_cost")
    private BigDecimal totalCost;
    
    @Column(name = "poi_creation_date",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "poi_modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="poi_po_id",referencedColumnName = "po_id")
    private PurchaseOrder purchaseOrder;
    
    @ManyToOne(optional=false) 
    @JoinColumn(name="poi_product_id",referencedColumnName = "pr_id")
    private Product product;
    
    @Column(name="poi_version")
    @Version
    private Integer version;
    
    public PurchaseOrderItem() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(id == null){
            creationDate = new Date();
            version = 1;
        }
        modifyDate = new Date();
        if(product != null) {
            unitOfMeasure = product.getUnitOfMeasure();
            unitCost = product.getPrice();
        }
        
        if(countUom == null){
            countUom = 0;
        }
        if(unitCost == null){
            unitCost = BigDecimal.ZERO;
        }
        totalCost = unitCost.multiply(new BigDecimal(countUom));
        if(totalCost == null) {
            totalCost = BigDecimal.ZERO;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public ProductTypes getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(ProductTypes unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Integer getCountUom() {
        return countUom;
    }

    public void setCountUom(Integer countUom) {
        this.countUom = countUom;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        final PurchaseOrderItem other = (PurchaseOrderItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
