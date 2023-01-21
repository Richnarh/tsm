/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.services;

import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.ProductType;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pascal
 */
@Stateless
public class TsmService {
    @Inject private CrudApi crudApi;
    
    public List<ProductType> getProductTypeList(){
        try{
            return crudApi.getEm().createQuery("SELECT e FROM ProductType e ORDER BY e.createdDate DESC", ProductType.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
        
    public Customer customertExist(String phone){
        try{
            String qryString = "SELECT e FROM Customer e WHERE e.phone=?1";
            TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class)
                    .setParameter(1, phone);
                    return typedQuery.getResultStream().findFirst().orElse(null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
