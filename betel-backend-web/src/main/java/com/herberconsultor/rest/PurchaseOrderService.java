/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.rest;

/**
 *
 * @author Herber Consultor
 */
import com.herberconsultor.betel.constants.ConstantApp;
import com.herberconsultor.betel.entities.PurchaseOrder;
import com.herberconsultor.betel.entities.PurchaseOrderItem;
import com.herberconsultor.betel.entities.PurchaseOrderWorker;
import com.herberconsultor.betel.entities.User;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderStateTypes;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderWorkflowTypes;
import com.herberconsultor.betel.serachtypes.PurchaseOrderSearchFields;
import com.herberconsultor.constant.ErrorConstants;
import com.herberconsultor.constant.PermitionsConstants;
import com.herberconsultor.ejb.PurchaseOrderBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/purchaseorder") 
public class PurchaseOrderService extends Application { 
    
    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderService.class.getName());
    
    @Inject
    private PurchaseOrderBean purchaseOrderBean;
     
    
    @Path("/searchByCriteria")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByUser(PurchaseOrderSearchFields criteria) throws Exception { 
        try {
            if(criteria.getOperation() != null && criteria.getOperation().equals(PermitionsConstants.SEARCH_PURCHASE_ORDERS) && criteria.getUserId()!= null) {
                List<PurchaseOrder> result = new ArrayList();
                if(criteria.getHistory() != null && criteria.getHistory()) {
                    result = purchaseOrderBean.getPurchaseOrderByUserId(criteria.getUserId());
                } else {
                    result = purchaseOrderBean.getPurchaseOrderNotCompletedByUserId(criteria.getUserId());
                }
                
                for(PurchaseOrder po :result ) {
                    if(po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(result).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(PurchaseOrder pr) throws Exception { 
        try {
            if(pr.getOperation() != null && pr.getOperation().equals(PermitionsConstants.CREATE_PURCHASE_ORDERS)) {
                PurchaseOrder po =  purchaseOrderBean.createOrModify(pr);  
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, PurchaseOrder pr) throws Exception { 
        try {
            if(pr.getOperation() != null && pr.getOperation().equals(PermitionsConstants.UPDATE_PURCHASE_ORDERS)) {
                pr.setId(id);
                PurchaseOrder po =  purchaseOrderBean.createOrModify(pr);  
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
    @Path("/rateworker")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rateWorker(PurchaseOrderSearchFields pow) throws Exception { 
        try {
            if(pow.getOperation() != null && pow.getOperation().equals(PermitionsConstants.RATE_PURCHASE_ORDER_WORKER)) {
                purchaseOrderBean.rateWorkerById(pow.getPurchaseOrderId(),pow.getUserId(), pow.getRating());
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
    @Path("/updateaddress")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateaddress(PurchaseOrderSearchFields fields) throws Exception { 
        try {
            if(fields.getOperation() != null && fields.getOperation().equals(PermitionsConstants.UPDATE_PURCHASE_ORDERS) 
                    && fields.getPurchaseOrderId() != null && fields.getLatitude() != null && fields.getLongitude() !=null) {
                purchaseOrderBean.updateAddress(fields); 
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Path("/{id}/{operation}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBiId(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_PURCHASE_ORDERS)) {
                PurchaseOrder po =  purchaseOrderBean.getById(id);  
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
            
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/{id}/pending/{operation}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastPurchaseOrderPendingByUserId(@PathParam("id") Long userId,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_PURCHASE_ORDERS)) {
                PurchaseOrder po =  purchaseOrderBean.getLastPurchaseOorderPending(userId);  
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                } else {
                    po = new PurchaseOrder();
                    po.setStatus(PurchaseOrderStateTypes.PENDING);
                    po.setVersion(1);
                    po.setPurchaseOrderItemList(new ArrayList());
                    po.setPurchaseOrderWorkerList(new ArrayList());
                    po.setTotalCost(BigDecimal.ZERO);
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
            
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
            
    @Path("/{id}/{operation}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_PURCHASE_ORDERS)) {
                purchaseOrderBean.deleteById(id);
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
            
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/service/{id}/{operation}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteServiceById(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.DELETE_PURCHASE_ORDER_SERVICE)) {
                PurchaseOrderItem powDelete = purchaseOrderBean.getServiceById(id);
                Long powId = powDelete.getPurchaseOrder().getId();
                purchaseOrderBean.deleteServiceById(powDelete);  

                PurchaseOrder po =  purchaseOrderBean.getById(powId); 
                po = purchaseOrderBean.createOrModify(po);
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/worker/{id}/{operation}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWorkerById(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.DELETE_PURCHASE_ORDER_WORKER)) {
                PurchaseOrderWorker powDelete = purchaseOrderBean.getWorkerById(id);
                Long powId = powDelete.getPurchaseOrder().getId();
                purchaseOrderBean.deleteWorkerById(powDelete);  

                PurchaseOrder po =  purchaseOrderBean.getById(powId); 
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/changeStatus")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(PurchaseOrderSearchFields poFiels) throws Exception { 
        try {
            if(poFiels.getOperation() != null && poFiels.getOperation().equals(PermitionsConstants.UPDATE_PURCHASE_ORDERS_CHANGE_STATE)) {
                PurchaseOrder po =  purchaseOrderBean.getById(poFiels.getPurchaseOrderId());
                
                if(poFiels.getStatus() != null) {
                    po.setStatus(poFiels.getStatus());
                }
                if(poFiels.getWorkflow() != null) {
                    po.setWorkflow(poFiels.getWorkflow());
                }
                
                po =  purchaseOrderBean.createOrModify(po); 
                
                //Esto debe modificarse despues
                if(poFiels.getWorkflow() != null && poFiels.getWorkflow().equals(PurchaseOrderWorkflowTypes.NOTIFIED_WORKER)) {
                    PurchaseOrderWorker poW = new PurchaseOrderWorker();
                    poW.setWorker(new User(11L));
                    purchaseOrderBean.addWorker(po, poW);
                }
                
                
                po =  purchaseOrderBean.getById(poFiels.getPurchaseOrderId());
                if(po != null ) {
                    if( po.getPurchaseOrderItemList() != null) {
                        for(PurchaseOrderItem poi : po.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(null);
                        }
                    }
                    if(po.getPurchaseOrderWorkerList() != null) {
                        for(PurchaseOrderWorker pow : po.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(null);
                        }
                    }
                }
                return Response.ok(po).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
}