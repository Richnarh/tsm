/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.ProductType;
import com.tsm.listener.AppSession;
import com.tsm.services.InventoryService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "productTypeController")
@SessionScoped
public class ProductTypeController implements Serializable {
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;

    private String optionText;

    private ProductType productType = new ProductType();
    private List<ProductType> productTypeList = new LinkedList<>();

    @PostConstruct
    private void init() {
        clearProductType();
        productTypeList = inventoryService.getProductTypeList();
    }

    public void saveProductType() {
        try {
            if (crudApi.save(productType) != null) {
                productTypeList = JUtils.addToList(productTypeList, productType);
                Msg.info(Msg.SUCCESS_MESSAGE);
            } else {
                Msg.error(Msg.FAILED_MESSAGE);
            }
            clearProductType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editProductType(ProductType productType) {
        optionText = "Update";
        this.productType = productType;
    }

    public void deleteProductType(ProductType productType) {
        try {
            if (crudApi.delete(productType)) {
                productTypeList.remove(productType);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.DELETE_MESSAGE, null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearProductType() {
        productType = new ProductType();
        productType.setUserAccount(appSession.getCurrentUser());
        productType.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

}
