/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.ejb;

import com.herberconsultor.betel.constants.ConstantApp;
import com.herberconsultor.betel.entities.CreditCard;
import com.herberconsultor.betel.entities.User;
import com.herberconsultor.betel.entities.enumtypes.PersonTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Herber Consultor
 */
@LocalBean
@Stateless(name="CreditCardBean")
public class CreditCardBean {
    private static final Logger LOGGER = Logger.getLogger(CreditCardBean.class.getName());
    
    @PersistenceContext(unitName = "BETEL_EJBPU")
    EntityManager em;
     
    @Inject
    private UserBean userBean;
    
    public CreditCard createOrModify(CreditCard pr) throws Exception{
        try {
            User usr = userBean.getById(pr.getUser().getId());
            if(pr.getUser() != null && pr.getUser().getId() != null){
                pr.setUser(usr);
            }
            if(pr.getId() == null) {
                em.persist(pr);
            } else {
                pr = em.merge(pr);
            }
            usr = userBean.createOrModify(usr);
            pr.setUser(usr);
            return pr;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("The credit card can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("Problems to save record");
        }
    }

    public List<CreditCard> getCreditCardByUserId(Long userId)  throws Exception{
        List<CreditCard> result = new ArrayList();
        try {
            result = em.createQuery("select s from CreditCard s where s.user.id = :userId").setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    } 

    public CreditCard getById(Long id) throws Exception{
        CreditCard result = null;
        try {
            result = (CreditCard) em.createQuery("select s from CreditCard s where s.id =:id order by s.id asc")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the service.");
        }
        return result;
    }
    
    public User deleteById(Long id) throws Exception{
        try {
            CreditCard pr = getById(id);
            if(pr != null) {
               em.remove(pr);
            }
            return userBean.createOrModify(pr.getUser());  
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the card.");
        }
    }
    
    public Long countCardsFromUserId(Long userId)  throws Exception{
  
        try {
            return (Long) em.createQuery("select count(s) from CreditCard s where s.user.id = :userId")
                    .setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
        }
        return 0L;
    }
}
