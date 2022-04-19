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
import com.herberconsultor.betel.entities.User;
import com.herberconsultor.betel.entities.enumtypes.UserStatusTypes;
import com.herberconsultor.betel.serachtypes.UserLoginSearchFields;
import com.herberconsultor.betel.serachtypes.UserSearchFields;
import com.herberconsultor.constant.ErrorConstants;
import com.herberconsultor.constant.PermitionsConstants;
import com.herberconsultor.ejb.UserBean;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/users") 
public class UsersService extends Application { 
    
    @Inject
    private UserBean userBean;
    
    @Path("/search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search() throws Exception { 
        try {
            return Response.ok(userBean.getAll(),MediaType.APPLICATION_JSON).build();
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/searchByCriteria")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCriteria(UserSearchFields criteria) throws Exception { 
        try {
            if(criteria != null && criteria.getOperation() != null && criteria.getOperation().equals(PermitionsConstants.SEARCH_USERS)) {
                return Response.ok(userBean.getCustomersByCriteria(criteria)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/workerRating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkerRating(UserSearchFields criteria) throws Exception { 
        try {
            if(criteria != null && criteria.getOperation() != null && criteria.getOperation().equals(PermitionsConstants.SEARCH_USERS_RATING)) {
                return Response.ok(userBean.getWorkerRating(criteria)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/login")
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = UserLoginSearchFields.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserLoginSearchFields criteria) throws Exception { 
        try {
            if(criteria != null && criteria.getOperation() != null && criteria.getOperation().equals(PermitionsConstants.LOGIN_USER)) {
                return Response.ok(userBean.login(criteria)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/changePassword")
    @POST
    @Operation(summary = "Crea el objeto indicado con id autogenerada.")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Entidad creada satisfactoriamente.", content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = User.class)))
        ,
        @APIResponse(responseCode = "400", description = "Solicitud con sintaxis incorrecta.")
        ,
        @APIResponse(responseCode = "422", description = "Entidad no se puede procesar. Datos incorrectos.")
        ,
        @APIResponse(responseCode = "500", description = "Error interno del servidor.")}
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(User user) throws Exception { 
        try {
            if(user != null && user.getOperation() != null && user.getOperation().equals(PermitionsConstants.CHANGE_USER_PASSWORD)) {
                return Response.ok(userBean.changePassword(user)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    @Path("/verifyEmail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyEmail(String email,String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.VERIFY_USER_EMAIL)) {
                return Response.ok(userBean.emailAlreadyExist(email)).build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(User pr) throws Exception { 
        try {
            if(pr.getOperation() != null && pr.getOperation().equals(PermitionsConstants.CREATE_USER)) {
                return Response.ok(userBean.createOrModify(pr)).build();
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
    public Response update(@PathParam("id") Long id, User pr) throws Exception { 
        try {
            if(pr.getOperation() != null && pr.getOperation().equals(PermitionsConstants.UPDATE_USER)) {
                pr.setId(id);
                return Response.ok(userBean.createOrModify(pr)).build();
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
            if(operation != null && operation.equals(PermitionsConstants.SEARCH_USERS)) {
                return Response.ok(userBean.getById(id)).build();
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
    @Path("/setStatus/{id}/{status}/{operation}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setStatusByUserId(@PathParam("id") Long id,@PathParam("status") UserStatusTypes status,@PathParam("operation") String operation) throws Exception { 
        try {
            if(operation != null && operation.equals(PermitionsConstants.CHANGE_USER_STATE)) {
                userBean.setStatusByUserId(id, status);
                return Response.ok("Ok").build();
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
            if(operation != null && operation.equals(PermitionsConstants.DELETE_USER)) {
                userBean.deleteById(id);
                return Response.ok("Ok").build();
            } else {
                return Response.serverError().entity(ErrorConstants.NOT_ALLOWED).status(Response.Status.FORBIDDEN).build();
            }
        } catch(Exception e) {
            return Response.serverError().entity(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }  
    } 
}