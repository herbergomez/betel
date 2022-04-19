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
import com.herberconsultor.betel.entities.Knowledge;
import com.herberconsultor.constant.ErrorConstants;
import com.herberconsultor.constant.PermitionsConstants;
import com.herberconsultor.ejb.KnowledgeBean;
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

@Path("/knowledges") 
@Produces(MediaType.APPLICATION_JSON) 
@Consumes(MediaType.APPLICATION_JSON) 
public class KnowledgeService extends Application { 
    
    @Inject
    private KnowledgeBean knowledgeBean;
    
    @Path("/searchByUser/{id}/{operation}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByUser(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_KNOWLEGES)) {
                return Response.ok(knowledgeBean.getKnowledgesByUserId(id)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @POST
    public Response save(Knowledge s) throws Exception { 
        try {
            if(s.getOperation() != null && s.getOperation().equals(PermitionsConstants.CREATE_KNOWLEGES)) {
                return Response.ok(knowledgeBean.createOrModify(s)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    }
    
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Knowledge pr) throws Exception { 
        try {
            if(pr.getOperation() != null && pr.getOperation().equals(PermitionsConstants.UPDATE_KNOWLEGES)) {
                pr.setId(id);
                return Response.ok(knowledgeBean.createOrModify(pr)).build();
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
    public Response getBiId(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_KNOWLEGES)) {
                return Response.ok(knowledgeBean.getById(id)).build();
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
    public Response deleteById(@PathParam("id") Long id,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.DELETE_KNOWLEGES)) {
                knowledgeBean.deleteById(id);
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
}