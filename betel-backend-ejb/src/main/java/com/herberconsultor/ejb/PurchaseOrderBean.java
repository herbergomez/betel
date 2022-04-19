/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.herberconsultor.ejb;

import com.herberconsultor.betel.constants.ConstantApp;
import com.herberconsultor.betel.entities.Product;
import com.herberconsultor.betel.entities.PurchaseOrder;
import com.herberconsultor.betel.entities.PurchaseOrderItem;
import com.herberconsultor.betel.entities.PurchaseOrderWorker;
import com.herberconsultor.betel.entities.Setting;
import com.herberconsultor.betel.entities.User;
import com.herberconsultor.betel.entities.enumtypes.PersonTypes;
import com.herberconsultor.betel.entities.enumtypes.PurchaseOrderStateTypes;
import com.herberconsultor.betel.exeptions.HerberConsultorException;
import com.herberconsultor.betel.serachtypes.PurchaseOrderSearchFields;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
/**
 *
 * @author Herber Consultor
 */
@LocalBean
@Stateless(name="PurchaseOrderBean")
public class PurchaseOrderBean {
     private static final Logger LOGGER = Logger.getLogger(PurchaseOrderBean.class.getName());
    
    @PersistenceContext(unitName = "BETEL_EJBPU")
    EntityManager em;
    
    @Inject
    private UserBean userBean;
    
    @Inject
    private ArticulosBean articulosBean;
    
    @Inject
    private SettingsBean settingsBean;
    
    public PurchaseOrder createOrModify(PurchaseOrder pr) throws Exception{
        try {
           // LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, "INICIA METODO createOrModify");
            if(pr.getUser() != null && pr.getUser().getId() != null){
                pr.setUser(userBean.getById(pr.getUser().getId()));
               // LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, "SETEA USUARIO");
            }
            //LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, "pr.getStatus(): " + pr.getStatus());
            if(pr.getStatus() != null) {
                if(pr.getStatus().equals(PurchaseOrderStateTypes.PENDING) || pr.getStatus().equals(PurchaseOrderStateTypes.PAYED)) {
                    BigDecimal totalCost = BigDecimal.ZERO;
                    if(pr.getPurchaseOrderItemList() != null) {
                        //LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, "pr.getPurchaseOrderItemList(): " + pr.getPurchaseOrderItemList().size());
                        for(PurchaseOrderItem poi :pr.getPurchaseOrderItemList()) {
                            poi.setPurchaseOrder(pr);
                            if(poi.getProduct() != null && poi.getProduct().getId() != null) {
                                Product product = articulosBean.getById(poi.getProduct().getId());
                                poi.setProduct(product);
                                if(product != null) {
                                    poi.setUnitCost(product.getPrice());
                                    BigDecimal itemCost = product.getPrice().multiply(new BigDecimal(poi.getCountUom()));
                                    totalCost=totalCost.add(itemCost);
                                } else {
                                    throw  new HerberConsultorException("The service does not exist");
                                } 
                            }
                        }
                    }
                    Setting tax = settingsBean.findByname("TAX_PERCENTAGE");
                    Setting ownerPercent = settingsBean.findByname("OWNER_PERCENT");
                    BigDecimal taxtPercent = BigDecimal.ZERO;
                    BigDecimal workerPercentFinal = BigDecimal.ZERO;
                    BigDecimal totalTax = BigDecimal.ZERO;
                    BigDecimal totalWorker = BigDecimal.ZERO;
                    if(tax != null && StringUtils.isNotBlank(tax.getValue())) {
                        taxtPercent = new BigDecimal(tax.getValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    }
                    if(ownerPercent != null && StringUtils.isNotBlank(ownerPercent.getValue())) {
                        BigDecimal workerPercent  = new BigDecimal("100").subtract(new BigDecimal(ownerPercent.getValue()));
                        workerPercentFinal = workerPercent.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    }
                    totalTax = totalCost.multiply(taxtPercent);
                    totalWorker = totalCost.multiply(workerPercentFinal);
                    
                    pr.setTotalWorker(totalWorker);
                    pr.setTotalTax(totalTax);
                    pr.setTotalCost(totalCost);
                    pr.setTotalCostPlusTax(totalCost.add(totalTax));
                }
                if(pr.getStatus().equals(PurchaseOrderStateTypes.PAYED)) {
                    if(pr.getPurchaseOrderWorkerList() != null && !pr.getPurchaseOrderWorkerList().isEmpty()) {
                        for(PurchaseOrderWorker pow :pr.getPurchaseOrderWorkerList()) {
                            pow.setPurchaseOrder(pr);
                            if(pow.getWorker() != null && pow.getWorker().getId() != null) {
                                User usr = userBean.getById(pow.getWorker().getId());
                                pow.setWorker(usr);
                                if(usr == null) {
                                    throw  new HerberConsultorException("The worker does not exist");
                                }
                            }
                        }
                    }
                }
            }
            
            if(pr.getId() == null) {
                em.persist(pr);
            } else {
                pr = em.merge(pr);
            }
            return pr;
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("The purchase order can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("Problems to save record");
        }
    }
    
    public void addWorker(PurchaseOrder po, PurchaseOrderWorker pow) throws Exception{
        try {
            Boolean add = Boolean.TRUE;
            if(po != null) {
                if(po.getPurchaseOrderWorkerList() != null && !po.getPurchaseOrderWorkerList().isEmpty()) {
                    for(PurchaseOrderWorker powItem :po.getPurchaseOrderWorkerList()) {   
                        if(powItem.getWorker() != null && pow.getWorker().getId() != null && powItem.getWorker().getId().equals(pow.getWorker().getId())) {
                            add = Boolean.FALSE;
                            break;
                        }
                    }
                }
            }
            if(add) {
                User usr = userBean.getById(pow.getWorker().getId());
                pow.setWorker(usr);
                pow.setPurchaseOrder(po);
                pow.setPayment(po.getTotalWorker());
                if(usr == null) {
                    throw  new HerberConsultorException("The worker does not exist");
                }
                em.persist(pow);
            }
        } catch (ConstraintViolationException ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("The purchase order can not be duplicated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, ex);
            throw new Exception("Problems to save record");
        }
    }
    
    public List<PurchaseOrder> getPurchaseOrderByUserId(Long userId)  throws Exception{
        List<PurchaseOrder> result = new ArrayList();
        try {
            PersonTypes type = userBean.getPersonTypesUserId(userId);
            //Show de first 10 orders
            if(type.equals(PersonTypes.ADMIN)) {
                result = em.createQuery("select s from PurchaseOrder s order by s.id desc").setMaxResults(25).getResultList();
            } else if(type.equals(PersonTypes.WORKER)) {
                result = em.createQuery("select s.purchaseOrder from PurchaseOrderWorker s where s.worker.id=:workerId order by s.id desc")
                        .setParameter("workerId", userId)
                        .setMaxResults(10).getResultList();
            } else if(type.equals(PersonTypes.CUSTOMER)) {
                result = em.createQuery("select s from PurchaseOrder s where s.user.id=:userId order by s.id desc")
                        .setParameter("userId", userId)
                        .setMaxResults(10).getResultList();
            }
            
            for(PurchaseOrder po :result ) {
                if(po.getPurchaseOrderItemList() != null) {
                    po.getPurchaseOrderItemList().size();
                }
                if(po.getPurchaseOrderWorkerList() != null) {
                    po.getPurchaseOrderWorkerList().size();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    } 
    
    public List<PurchaseOrder> getPurchaseOrderNotCompletedByUserId(Long userId)  throws Exception{
        List<PurchaseOrder> result = new ArrayList();
        try {
            PersonTypes type = userBean.getPersonTypesUserId(userId);
            //Show de first 10 orders
            if(type.equals(PersonTypes.ADMIN)) {
                result = em.createQuery("select s from PurchaseOrder s where s.workflow !='COMPLETED' order by s.id desc").setMaxResults(25).getResultList();
            } else if(type.equals(PersonTypes.WORKER)) {
                result = em.createQuery("select s.purchaseOrder from PurchaseOrderWorker s where s.worker.id=:workerId and s.purchaseOrder.workflow !='COMPLETED'order by s.id desc")
                        .setParameter("workerId", userId)
                        .setMaxResults(5).getResultList();
            } else if(type.equals(PersonTypes.CUSTOMER)) {
                result = em.createQuery("select s from PurchaseOrder s where s.user.id=:userId and s.workflow !='COMPLETED' order by s.id desc")
                        .setParameter("userId", userId)
                        .setMaxResults(5).getResultList();
            }
            
            for(PurchaseOrder po :result ) {
                if(po.getPurchaseOrderItemList() != null) {
                    po.getPurchaseOrderItemList().size();
                }
                if(po.getPurchaseOrderWorkerList() != null) {
                    po.getPurchaseOrderWorkerList().size();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get users.");
        }
        return result;
    } 
    
    public PurchaseOrder getById(Long id) throws Exception{
        PurchaseOrder result = null;
        try {
            result = (PurchaseOrder) em.createQuery("select s from PurchaseOrder s where s.id =:id")
                    .setParameter("id", id).getSingleResult();
            if(result != null ) {
                if(result.getPurchaseOrderItemList() != null) {
                    result.getPurchaseOrderItemList().size();
                }
                if(result.getPurchaseOrderWorkerList() != null) {
                    result.getPurchaseOrderWorkerList().size();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the purchase order.");
        }
        return result;
    }
    
    public void updateAddress(PurchaseOrderSearchFields fields) throws Exception{
        try {
            if(fields != null && fields.getAddress() != null && fields.getLatitude() != null && fields.getLongitude() != null) {
               em.createQuery("update PurchaseOrder pow set pow.latitude =:latitude, pow.longitude =:longitude,pow.address =:address where pow.id =:poId")
                       .setParameter("latitude", fields.getLatitude())
                       .setParameter("longitude", fields.getLongitude())
                       .setParameter("address", fields.getAddress())
                       .setParameter("poId", fields.getPurchaseOrderId())
                       .executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the purchase order.");
        }
    }
    
    public PurchaseOrderWorker getWorkerById(Long id) throws Exception{
        PurchaseOrderWorker result = null;
        try {
            result = (PurchaseOrderWorker) em.createQuery("select s from PurchaseOrderWorker s where s.id =:id")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the purchase order.");
        }
        return result;
    }
    
    public PurchaseOrderItem getServiceById(Long id) throws Exception{
        PurchaseOrderItem result = null;
        try {
            result = (PurchaseOrderItem) em.createQuery("select s from PurchaseOrderItem s where s.id =:id")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the purchase order.");
        }
        return result;
    }
    
    public PurchaseOrder getLastPurchaseOorderPending(Long userId) throws Exception{
        PurchaseOrder result = null;
        try {
            List<PurchaseOrder> list = em.createQuery("select s from PurchaseOrder s where s.status =:status and s.user.id = :userId order by s.id desc")
                    .setParameter("status", PurchaseOrderStateTypes.PENDING).setParameter("userId", userId).getResultList();
            if(list != null && !list.isEmpty()) {
                result = list.get(0);
                if(result.getPurchaseOrderItemList() != null) {
                    result.getPurchaseOrderItemList().size();
                }
                if(result.getPurchaseOrderWorkerList() != null) {
                    result.getPurchaseOrderWorkerList().size();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to get the purchase order.");
        }
        return result;
    }
    public void deleteById(Long id) throws Exception{
        try {
            PurchaseOrder pr = getById(id);
            if(pr != null) {
               em.remove(pr);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the purchase order.");
        }
    }
    
    public void deleteWorkerById(PurchaseOrderWorker pow) throws Exception{
        try {
            if(pow != null) {
               em.createQuery("delete from PurchaseOrderWorker pow where pow.id=:id").setParameter("id", pow.getId()).executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the purchase order worker.");
        }
    }
    
    
    public void rateWorkerById(Long poId, Long workerId, BigDecimal rate) throws Exception{
        try {
            if(poId != null && workerId != null && rate != null) {
               em.createQuery("update PurchaseOrderWorker pow set pow.rating= :rating, pow.rated=true where pow.purchaseOrder.id =:poId and pow.worker.id =:workerId")
                       .setParameter("rating", rate).setParameter("poId", poId).
                       setParameter("workerId", workerId).executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the purchase order.");
        }
    }
    
    public void deleteServiceById(PurchaseOrderItem poi) throws Exception{
        try {
            if(poi != null) {
               em.createQuery("delete from PurchaseOrderItem pow where pow.id=:id").setParameter("id", poi.getId()).executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
            throw new Exception("* Error to delete the purchase order worker.");
        }
    }
    
    public Long totalPurchaseOrderNotCompletedByUserId(String email)  throws Exception{
        try {
            return (Long) em.createQuery("select count(s) from PurchaseOrder s where s.status = 'PENDING'").getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, ConstantApp.APLICATION_LOG_NAME, e);
        }
        return 0L;
    }
}
