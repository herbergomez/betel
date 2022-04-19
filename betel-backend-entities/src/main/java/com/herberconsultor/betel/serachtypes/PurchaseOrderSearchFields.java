/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.betel.serachtypes;

import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderStateTypes;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderWorkflowTypes;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Usuario
 */
public class PurchaseOrderSearchFields implements Serializable {
    private Long userId;
    private Long purchaseOrderId;
    private BigDecimal rating;
    private String operation;
    private PurchaseOrderStateTypes status;
    private PurchaseOrderWorkflowTypes workflow;
    private Boolean history;
    private Double latitude;
    private Double longitude;
    private String address;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public PurchaseOrderStateTypes getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStateTypes status) {
        this.status = status;
    }

    public PurchaseOrderWorkflowTypes getWorkflow() {
        return workflow;
    }

    public void setWorkflow(PurchaseOrderWorkflowTypes workflow) {
        this.workflow = workflow;
    }

    public Boolean getHistory() {
        return history;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
