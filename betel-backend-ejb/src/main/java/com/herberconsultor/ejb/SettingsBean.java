/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.ejb;

import com.herberconsultor.betel.entities.Setting;
import com.herberconsultor.betel.constants.ConstantApp;
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
@Stateless(name="SettingsBean")
public class SettingsBean {
    
    private static final Logger LOGGER = Logger.getLogger(SettingsBean.class.getName());
    
    @PersistenceContext(unitName = "BETEL_EJBPU")
    EntityManager em;
    
    public Setting createOrModify(Setting pr) throws Exception{
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
    
    public List<Setting> createOrModifyList(List<Setting> list) throws Exception{
        try {
            for(Setting s : list) {
                List<Setting> result = em.createQuery("select s from Setting s where s.name= :name").setParameter("name", s.getName()).getResultList();
                if(result.isEmpty()) {
                    em.persist(s);
                } else{
                    Setting sTemp = result.get(0);
                    sTemp.setValue(s.getValue());
                    s = em.merge(sTemp);
                }
            }
            
            return list;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* The name can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* Problems to save record");
        }
    }
    
    public List<Setting> getAll()  throws Exception{
        List<Setting> result = new ArrayList();
        try {
            result = em.createQuery("select s from Setting s").getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get Setting.");
        }
        return result;
    }

    public Setting findByname(String name)  throws Exception{
       Setting result = null;
        try {
            List<Setting> list = em.createQuery("select s from Setting s where s.name = :name").setParameter("name", name).getResultList();
            if(!list.isEmpty()) {
                result = list.get(0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get Setting.");
        }
        return result;
    }
    
    public Setting getById(Long id) throws Exception{
        Setting result = null;
        try {
            result = (Setting) em.createQuery("select s from Setting s where s.id =:id order by s.id asc")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the Setting.");
        }
        return result;
    }
    
    public void deleteById(Long id) throws Exception{
        try {
            Setting pr = getById(id);
            if(pr != null) {
               em.remove(pr);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the Setting.");
        }
    }
}
