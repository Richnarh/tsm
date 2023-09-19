/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Product;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author khoders
 */
@Named(value = "productController")
@SessionScoped
public class ProductController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private String optionText;
    
    private Product product = new Product();
    private List<Product> productList = new LinkedList<>();
    private StreamedContent productImge = null;
   
    private FormView pageView = FormView.listForm();
    
    @PostConstruct
    public void init()
    {
        clearProduct();
    } 
    
    public void loadProducts(){
        productList = inventoryService.getProducts();
    }
    
    public void initProduct()
    {
        clearProduct();
        pageView.restToCreateView();
    }

   public void saveProduct()
    {
       try{
           product.genCode();
          if(crudApi.save(product) != null){
              productList = CollectionList.washList(productList, product);
              Msg.info(Msg.SUCCESS_MESSAGE);
              closePage();
          }else{
              Msg.error("Oops! failed to save product");
          }
           clearProduct();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
   
   
    public void editProduct(Product product)
    {
       pageView.restToCreateView();
       this.product=product;
       optionText = "Update";
    }
    
    public void deleteProduct(Product product)
    {
        try
        {
          if(crudApi.delete(product))
          {
              productList.remove(product);
              
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.DELETE_MESSAGE, null)); 
          }
          else
          {
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void closePage()
    {
       product = new Product();
       optionText = "Save Changes";
       pageView.restToListView();
    }
        
    public void clearProduct() {
        product = new Product();
        product.setUserAccount(appSession.getCurrentUser());
        product.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public List<Product> getProductList()
    {
        return productList;
    }

    public StreamedContent getProductImge()
    {
        return productImge;
    }

    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }
    
}
