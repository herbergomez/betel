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
import com.herberconsultor.betel.entities.CreditCard;
import com.herberconsultor.constant.ErrorConstants;
import com.herberconsultor.constant.PermitionsConstants;
import com.herberconsultor.ejb.CreditCardBean;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cards") 
@Produces(MediaType.APPLICATION_JSON) 
@Consumes(MediaType.APPLICATION_JSON) 
public class CreditCardService extends Application { 
    
    @Inject
    private CreditCardBean creditCardBean;
    
    @Path("/searchByUser/{id}/{operation}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByUser(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_CREDIT_CARDS)) {
                return Response.ok(creditCardBean.getCreditCardByUserId(id)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @POST
    public Response save(CreditCard s) throws Exception { 
        try {
            if(s.getOperation() != null && s.getOperation().equals(PermitionsConstants.CREATE_CREDIT_CARDS)) {
                return Response.ok(creditCardBean.createOrModify(s)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
            
        } catch(Exception e) {
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
    public Response getBiId(@PathParam("id") Long id, @PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_CREDIT_CARDS)) {
                return Response.ok(creditCardBean.getById(id)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/{id}/{operation}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") Long id, @PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.DELETE_CREDIT_CARDS)) {
                creditCardBean.deleteById(id);
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
}