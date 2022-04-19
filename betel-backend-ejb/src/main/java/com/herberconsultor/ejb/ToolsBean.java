/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.ejb;

import com.herberconsultor.betel.constants.ConstantApp;
import com.herberconsultor.betel.entities.Tool;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Herber Consultor
 */
@LocalBean
@Stateless(name="ToolsBean")
public class ToolsBean {
    
    private static final Logger LOGGER = Logger.getLogger(ToolsBean.class.getName());
    
    @PersistenceContext(unitName = "BETEL_EJBPU")
    EntityManager em;
    
    public Tool createOrModify(Tool pr) throws Exception{
        try {
            if(pr.getId() == null) {
                em.persist(pr);
            } else {
                pr = em.merge(pr);
            }
            return pr;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* The name can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* Problems to save record");
        }
    }
    
    public List<Tool> getAll()  throws Exception{
        List<Tool> result = new ArrayList();
        try {
            result = em.createQuery("select s from Tool s").getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get tools.");
        }
        return result;
    }
    
    public List<Tool> getAllEnabled()  throws Exception{
        List<Tool> result = new ArrayList();
        try {
            result = em.createQuery("select s from Tool s where s.enabled=true").getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get tools.");
        }
        return result;
    }
    
    
    public Tool getById(Long id) throws Exception{
        Tool result = null;
        try {
            result = (Tool) em.createQuery("select s from Tool s where s.id =:id order by s.id asc")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the tool.");
        }
        return result;
    }
    
    public void deleteById(Long id) throws Exception{
        try {
            Tool pr = getById(id);
            if(pr != null) {
               em.remove(pr);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the tool.");
        }
    }
}
