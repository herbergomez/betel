/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.ejb;

import com.herberconsultor.betel.entities.User;
import com.herberconsultor.betel.entities.enumtypes.PersonTypes;
import com.herberconsultor.betel.constants.ConstantApp;
import com.herberconsultor.betel.entities.enumtypes.UserStatusTypes;
import com.herberconsultor.betel.exeptions.HerberConsultorException;
import com.herberconsultor.betel.serachtypes.UserLoginSearchFields;
import com.herberconsultor.betel.serachtypes.UserSearchFields;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Herber Consultor
 */
@LocalBean
@Stateless(name="UserBean")
public class UserBean {
    private static final Logger LOGGER = Logger.getLogger(UserBean.class.getName());
    
    @PersistenceContext(unitName = "BETEL_EJBPU")
    EntityManager em;
    
    @Inject
    private CreditCardBean creditCardBean;
    
    @Inject
    private KnowledgeBean knowledgeBean;
     
    public User createOrModify(User pr) throws Exception{
        try {
            
            if(pr.getId() == null) {
                if(!emailAlreadyExist(pr.getEmail())) {
                    pr.setStatus(UserStatusTypes.CREATED);
                    em.persist(pr);
                } else {
                    throw new HerberConsultorException("The email already exist.");
                }
            } else {
                Boolean basicInformationCompleted = false;
                if(pr.getPerson() != null ) {
                    Long countCards = creditCardBean.countCardsFromUserId(pr.getId());
                    Long countKnowleges = 0L;
                    if(pr.getPerson().getPersonType().equals(PersonTypes.WORKER)) {
                        countKnowleges = knowledgeBean.countKnowledgesByUserId(pr.getId());
                        if(pr.getPerson().getImage() == null){
                            countKnowleges = 0L;
                        }
                    } else {
                        countKnowleges = 10L;
                    }
                    
                    if(countCards > 0 && countKnowleges > 0) {
                        basicInformationCompleted = true;
                    }
                }
                pr.setBasicInformationCompleted(basicInformationCompleted);
                if(pr.getPerson().getPersonType().equals(PersonTypes.CUSTOMER) || pr.getPerson().getPersonType().equals(PersonTypes.ADMIN)) {
                    pr.setStatus(UserStatusTypes.APPROVED);
                } else if(pr.getPerson().getPersonType().equals(PersonTypes.WORKER)) {
                    //Si es trabajador, obtengo el estado anterior para que no se modifique
                    UserStatusTypes status = getStatusByUserId(pr.getId());
                    if(basicInformationCompleted && status.equals(UserStatusTypes.CREATED)) {
                        pr.setStatus(UserStatusTypes.PENDIND_VERIFY);
                    } else {
                        pr.setStatus(status);
                    }
                    
                } 
                pr = em.merge(pr);
            }
            return pr;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("The username or email can not be duplicated.");
        } catch (HerberConsultorException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw  new Exception(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("Problems to save record");
        }
    }
    
    public User changePassword(User pr) throws Exception{
        try {
            User changePass = getById(pr.getId());
            if(!changePass.getPassword().trim().equals(pr.getPassword().trim())) {
                throw new HerberConsultorException("The current password is wrong.");
            }
            if(changePass.getPassword().trim().equals(pr.getNewPassword().trim())) {
                throw new HerberConsultorException("The current password cannot be the same as the current password.");
            }
            changePass.setPassword(pr.getNewPassword());
            changePass.setLoginFailCount(0);
            changePass.setBlocked(Boolean.FALSE);
            changePass.setBlockedDate(null);
            changePass.setResetPassword(Boolean.TRUE);
            changePass.setResetPasswordDate(new Date());
            changePass = em.merge(changePass);
            return pr;
        } catch (HerberConsultorException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw ex;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* The username or email can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("* Problems to save record");
        }
    }
    
    public List<User> getAll()  throws Exception{
        List<User> result = new ArrayList();
        try {
            result = em.createQuery("select s from User s").getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    }
    
    public void setStatusByUserId(Long userId, UserStatusTypes status)  throws Exception{
        try {
            em.createQuery("update User s set s.status=:status where s.id= :userId").setParameter("status", status).setParameter("userId", userId).executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
    }
    
    public UserStatusTypes getStatusByUserId(Long userId)  throws Exception{
        try {
            return (UserStatusTypes)em.createQuery("select s.status from User s where s.id= :userId").setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get user status.");
        }
    }
    
    public PersonTypes getPersonTypesUserId(Long userId)  throws Exception{
        try {
            return (PersonTypes)em.createQuery("select s.person.personType from User s where s.id= :userId").setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get user status.");
        }
    }
    
    public List<User> getCustomersByCriteria(UserSearchFields criteria)  throws Exception{
        List<User> result = new ArrayList();
        try {
            String query = "select s from User s";
            String queryWhere = "";
            Boolean paramsExist = false;
            if(criteria != null) {
                if(criteria.getEmail() != null) {
                    paramsExist = true;
                    queryWhere += " s.email = :email";
                }
                if(criteria.getUsername() != null && criteria.getUsername().length() > 0) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.username = :username";
                    
                }
                if(criteria.getFullname() != null && criteria.getFullname().length() > 0) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.fullname like :fullname";
                }
                if(criteria.getGender() != null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.gender = :gender";
                }
                if(criteria.getCivilState() != null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.civilState = :civilState";
                }
                if(criteria.getLessThan() != null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.birthDate <= :lessThan";
                }
                if(criteria.getGreatherThan() != null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.birthDate >= :greatherThan";
                }
                
                if(criteria.getDirection() != null && criteria.getDirection().length() > 0) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.direction like :direction";
                }
                
                if(criteria.getDirection() != null && criteria.getDirection().length() > 0) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.person.direction like :direction";
                }
                
                if(criteria.getStatus()!= null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " s.status = :status";
                }
                
                if(criteria.getPersonType() != null) {
                    if(paramsExist) {
                        queryWhere += " and ";
                    } else{
                        paramsExist = true;
                    }
                    queryWhere += " (s.person.personType = :personType or s.person.personType= :typeBoth)";
                }
                if(paramsExist) {
                    queryWhere = " where " + queryWhere;
                }
            }
            Query q = em.createQuery(query + queryWhere + " order by s.person.fullname asc");
            if(paramsExist) {
                if(criteria.getEmail() != null && criteria.getEmail().length() > 0) {
                    q.setParameter("email", criteria.getEmail());
                }
                if(criteria.getUsername() != null && criteria.getUsername().length() > 0) {
                    q.setParameter("username", criteria.getUsername());
                    
                }
                if(criteria.getFullname() != null && criteria.getFullname().length() > 0) {
                    q.setParameter("fullname", criteria.getFullname());
                }
                if(criteria.getGender() != null) {
                    q.setParameter("gender", criteria.getGender());
                }
                if(criteria.getCivilState() != null) {
                    q.setParameter("civilState", criteria.getCivilState());
                }
                if(criteria.getLessThan() != null) {
                    q.setParameter("lessThan", criteria.getLessThan());
                }
                if(criteria.getGreatherThan() != null) {
                    q.setParameter("greatherThan", criteria.getGreatherThan());
                }
                
                if(criteria.getDirection() != null && criteria.getDirection().length() > 0) {
                    q.setParameter("direction", criteria.getDirection());
                }
                
                if(criteria.getPhoneNumber() != null && criteria.getPhoneNumber().length() > 0) {
                    q.setParameter("phoneNumber", criteria.getPhoneNumber());
                }
                
                if(criteria.getStatus()!= null) {
                    q.setParameter("status", criteria.getStatus());
                }
                
                if(criteria.getPersonType() != null) {
                    q.setParameter("personType", criteria.getPersonType());
                    q.setParameter("typeBoth", PersonTypes.BOTH);
                }
            }
            result = q.getResultList();
            if(criteria.getPersonType() != null && 
                    (criteria.getPersonType().equals(PersonTypes.WORKER) || criteria.getPersonType().equals(PersonTypes.BOTH))) {
                for(User usr : result) {
                    if(usr.getStatus() != null && usr.getStatus().equals(UserStatusTypes.APPROVED)) {
                        UserSearchFields f = new UserSearchFields();
                        f.setId(usr.getId());
                        usr.setRating(getWorkerRating(f));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    } 

    public BigDecimal getWorkerRating(UserSearchFields criteria) throws Exception{
        BigDecimal result = BigDecimal.ZERO;
        try {
             Double rating = (Double) em.createQuery( "select avg(pow.rating) from PurchaseOrderWorker pow where pow.worker.id = :workerId and pow.purchaseOrder.workflow = 'COMPLETED' and pow.rated=true and pow.rating is not null")
                            .setParameter("workerId", criteria.getId()).getSingleResult();
            if(rating != null) {
                result = new BigDecimal(rating);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    }
    public User login(UserLoginSearchFields criteria)  throws Exception{
        User user = null;
        User userFinal = null;
        try {
            List<User> result = em.createQuery("select s from User s where s.email = :email")
                    .setParameter("email", criteria.getEmail().trim()).getResultList();
            if(!result.isEmpty()) {
                user = result.get(0);
               // if(user.getPerson() != null && user.getPerson().getPersonType() !=null ) {
                    if(user.getBlocked() != null && user.getBlocked()) {
                        throw new HerberConsultorException("The user has been blocked.");
                    } else {
                        Integer loginFailCount = user.getLoginFailCount() != null ? user.getLoginFailCount() : 0;
                        if(!user.getPassword().equals(criteria.getPassword())) {
                            if(loginFailCount > 3) {
                                user.setBlockedDate(new Date());
                                user.setBlocked(Boolean.TRUE);
                            } else {
                                user.setLoginFailCount(loginFailCount + 1);
                            }
                            createOrModify(user);
                            throw new HerberConsultorException("The email or password do not exist.");
                        } else {
                            user.setBlockedDate(null);
                            user.setLoginFailCount(0);
                            user.setBlocked(Boolean.FALSE);
                            user = createOrModify(user);
                            userFinal = user;
                        }
                    }
                //} else {
                  //  userFinal = user;
                //}
            } else {
                throw new HerberConsultorException("The email or password do not exist.");
            }
        } catch(HerberConsultorException e) { 
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception(e.getMessage());
        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error login user.");
        }
        return userFinal;
    }
    
    public Boolean emailAlreadyExist(String email)  throws Exception{
  
        try {
            Long result = (Long) em.createQuery("select count(s) from User s where s.email = :email")
                    .setParameter("email", email.trim()).getSingleResult();
            if(result < 1) {
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
        }
        return true;
    }
    
    public User getById(Long id) throws Exception{
        User result = null;
        try {
            result = (User) em.createQuery("select s from User s where s.id =:id order by s.id asc")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the service.");
        }
        return result;
    }
    
    public void deleteById(Long id) throws Exception{
        try {
            User pr = getById(id);
            if(pr != null) {
               em.remove(pr);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the user.");
        }
    }
}
