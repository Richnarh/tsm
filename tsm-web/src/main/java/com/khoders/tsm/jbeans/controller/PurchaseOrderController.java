/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.listener.AppSession;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richard
 */
@Named(value = "purchaseOrderController")
@SessionScoped
public class PurchaseOrderController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private InventoryService inventoryService;
    @Inject private AppSession appSession;
    
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private List<PurchaseOrder> purchaseOrderList = new LinkedList<>();
    
    private PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
    private List<PurchaseOrderItem> purchaseOrderItemList = new LinkedList<>();
    private List<PurchaseOrderItem> removedOrderItemList = new LinkedList<>();
    
    private FormView pageView = FormView.listForm();
    private String optionText;
    
     private double totalAmount = 0.0;
    
    @PostConstruct
    private void init()
    {
        purchaseOrderList = inventoryService.getPurchaseOrderList();
        
        clearPurchaseOrder();
    }
    
    public void initPurchaseOrder()
    {
        clearPurchaseOrder();
        pageView.restToCreateView();
    }   
    
    public void savePurchaseOrder(){
        try
        {
           purchaseOrder.genOrderCode();
           if(crudApi.save(purchaseOrder)!=null)
           {
               purchaseOrderList = CollectionList.washList(purchaseOrderList, purchaseOrder);
               Msg.info("Purchase order saved");
               clearPurchaseOrder();
           }
           
           closePage();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
  public void deletePurchaseOrder(PurchaseOrder purchaseOrder)
    {
        try 
        {
          if(crudApi.delete(purchaseOrder))
          {
              purchaseOrderList.remove(purchaseOrder);
              Msg.info(Msg.SUCCESS_MESSAGE);
          }
          else
          {
            Msg.info(Msg.FAILED_MESSAGE);
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
  
    public void managePurchaseOrderItem(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
        pageView.restToDetailView();
        totalAmount = 0.0;
        clearPurchaseOrderItem();
        
        purchaseOrderItemList = inventoryService.getPurchaseOrderItem(purchaseOrder);
        totalAmount = purchaseOrderItemList.stream().mapToDouble(PurchaseOrderItem::getCostPrice).sum();
    }

    public void addPurchaseOrderItem()
    {
        try
        {
            Predicate<PurchaseOrderItem> predicate = orderItem -> orderItem.getQtyPurchased() <= 0;
            
            if(predicate.test(purchaseOrderItem))
            {
              Msg.error("Please enter quantity");
              return;
            }
            
            if (purchaseOrderItem.getCostPrice() <= 0.0) {
                Msg.info("Please enter price");
                return;
            }

            if (purchaseOrderItem != null) {
               
                totalAmount += purchaseOrderItem.getCostPrice();
                
                purchaseOrderItemList.add(purchaseOrderItem);
                purchaseOrderItemList = CollectionList.washList(purchaseOrderItemList, purchaseOrderItem);

                Msg.info("Order item added");
            } else {
                Msg.info("Order item removed!");
            }
            clearPurchaseOrderItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveAll()
    {
       try 
        {
            for (PurchaseOrderItem orderItem : purchaseOrderItemList){
                orderItem.genCode();
                orderItem.setPurchaseOrder(purchaseOrder);
                orderItem.setSubTotal(orderItem.getQtyPurchased() * orderItem.getCostPrice());
                crudApi.save(orderItem);
            }

            for (PurchaseOrderItem orderItem : removedOrderItemList)
            {
                crudApi.delete(orderItem);
                removedOrderItemList.remove(orderItem);
            }
            double total = purchaseOrderItemList.stream().mapToDouble(PurchaseOrderItem::getSubTotal).sum();
            purchaseOrder.setTotalAmount(total);
            crudApi.save(purchaseOrder);
            System.out.println("Remove order size after -- " + removedOrderItemList.size());
            
            closePage();
            
            Msg.info("Purchase order item list saved!");
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void editPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        this.purchaseOrderItem = purchaseOrderItem;
        totalAmount -= (purchaseOrderItem.getQtyPurchased() * purchaseOrderItem.getCostPrice());
        purchaseOrderItemList.remove(purchaseOrderItem);
    }
    
    public void removePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        totalAmount -= (purchaseOrderItem.getQtyPurchased() * purchaseOrderItem.getCostPrice());
        removedOrderItemList = CollectionList.washList(removedOrderItemList, purchaseOrderItem);
        purchaseOrderItemList.remove(purchaseOrderItem);
        
        System.out.println("Size on removing --- "+removedOrderItemList.size());
    }
        
    public void editPurchaseOrder(PurchaseOrder purchaseOrder) 
    {
       pageView.restToCreateView();
       this.purchaseOrder=purchaseOrder;
       optionText = "Update";
    }
    
    public void clearPurchaseOrder() 
    {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserAccount(appSession.getCurrentUser());
        purchaseOrder.genCode();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    public void closePage()
    {
       purchaseOrder = new PurchaseOrder();
       optionText = "Save Changes";
       pageView.restToListView();
    }
    public void clearPurchaseOrderItem()
    {
        purchaseOrderItem = new PurchaseOrderItem();
        optionText = "Save Changes";
        purchaseOrderItem.setPurchaseOrder(purchaseOrder);
        SystemUtils.resetJsfUI();
    }
    
    public PurchaseOrder getPurchaseOrder()
    {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderItem getPurchaseOrderItem()
    {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public List<PurchaseOrder> getPurchaseOrderList()
    {
        return purchaseOrderList;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList()
    {
        return purchaseOrderItemList;
    }

    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }    
}
