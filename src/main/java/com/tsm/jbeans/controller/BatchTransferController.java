/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.FormView;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.BatchTransfer;
import com.tsm.entities.Inventory;
import com.tsm.entities.StockReceiptItem;
import com.tsm.entities.TransferItem;
import com.tsm.enums.TransferStatus;
import com.tsm.listener.AppSession;
import com.tsm.services.InventoryService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pascal
 */
@Named(value = "batchTransferController")
@SessionScoped
public class BatchTransferController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private List<BatchTransfer> batchTransferList = new LinkedList();
    private BatchTransfer batchTransfer = new BatchTransfer();
    private List<TransferItem> transferItemList = new LinkedList<>();
    private TransferItem transferItem = new TransferItem();
    
    private FormView pageView = FormView.listForm();
    private String optionText;
    
    @PostConstruct
    public void init(){
        batchTransferList = inventoryService.getBatchTransferList();
        clearTransfer();
    }
    
    public void initTransfer(){
        clearBatchTransfer();
        pageView.restToCreateView();
    }
    
    public void saveBatchTransfer(){
        try {
            if(batchTransfer != null && batchTransfer.getFromLocation().equals(batchTransfer.getToLocation())){
                Msg.error("Cannot make transfer within same location");
                return;
            }
            if(crudApi.save(batchTransfer) != null){
                batchTransferList = JUtils.addToList(batchTransferList, batchTransfer);
                Msg.info(Msg.SUCCESS_MESSAGE);
                
                closeItemPage();
            }
            clearBatchTransfer();
        } catch (Exception e) {
        }
    }
     
    public void editBatchTransfer(BatchTransfer batchTransfer){
        pageView.restToCreateView();
       this.batchTransfer=batchTransfer;
       optionText = "Update";
    }
    public void deleteBatchTransfer(BatchTransfer batchTransfer){
        try {
            if(crudApi.delete(batchTransfer)){
                batchTransferList.remove(batchTransfer);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
        }
    }
    
    public void saveTransfer(){
        try {
            transferItem.setBatchTransfer(batchTransfer);
            if(crudApi.save(transferItem) != null){
                transferItemList = JUtils.addToList(transferItemList, transferItem);
                
                StockReceiptItem receiptItem = crudApi.find(StockReceiptItem.class, transferItem.getStockReceiptItem().getId());
                System.out.println("Product: "+receiptItem.getProduct().getProductName());
                System.out.println("Quantity: "+receiptItem.getPkgQuantity());
                System.out.println("transferItem Quantity: "+transferItem.getQtyTransferred());
                double qtyLeft = receiptItem.getPkgQuantity() - transferItem.getQtyTransferred();
                receiptItem.setPkgQuantity(qtyLeft);
                crudApi.save(receiptItem);
                
                Msg.info(Msg.SUCCESS_MESSAGE);
                
                clearTransfer();
            }
        } catch (Exception e) {
        }
    }
     
    public void editTransfer(TransferItem transferItem){
       this.transferItem=transferItem;
       optionText = "Update";
    }
    public void deleteTransfer(TransferItem transferItem){
        try {
            TransferItem item = crudApi.find(TransferItem.class, transferItem.getId());
            if(crudApi.delete(transferItem)){
                
                StockReceiptItem receiptItem = crudApi.find(StockReceiptItem.class, transferItem.getStockReceiptItem().getId());
                
                double qty = receiptItem.getPkgQuantity()+ item.getQtyTransferred();
                receiptItem.setPkgQuantity(qty);
                crudApi.save(receiptItem);
                
                transferItemList.remove(transferItem);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void manageTransfer(BatchTransfer batchTransfer){
        pageView.restToDetailView();
        clearTransfer();
        this.batchTransfer = batchTransfer;
        transferItemList = inventoryService.getTransferItemList(batchTransfer);
    }
    
    public void updateBatchStatus(BatchTransfer batchTransfer){
        this.batchTransfer = batchTransfer;
    }
    public void saveBatchTransferStatus(){
        if(crudApi.save(batchTransfer) != null){
            if(batchTransfer.getTransferStatus() == TransferStatus.ACCEPTED){
                Inventory inventory = inventoryService.postToInventory(batchTransfer);
                if(inventory != null){
                    Msg.info("batch status and "+batchTransfer.getToLocation().getLocationName() +" updated with records");
                }else{
                    Msg.error("Oops! something went wrong, inventory updates failed!");
                }
            }
        }else {
            Msg.error("Oops! something went wrong, update failed!");
        }
    }
    
    public void closePage(){
        clearBatchTransfer();
        pageView.restToListView();
    }
    
    public void closeItemPage(){
        transferItem = new TransferItem();
        optionText = "Save Changes";
        pageView.restToListView();
    }
    
    public void clearTransfer() {
        transferItem = new TransferItem();
        transferItem.setUserAccount(appSession.getCurrentUser());
        transferItem.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }
    
    public void clearBatchTransfer() {
        batchTransfer = new BatchTransfer();
        batchTransfer.setUserAccount(appSession.getCurrentUser());
        batchTransfer.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }
    
    public List<TransferItem> getTransferItemList() {
        return transferItemList;
    }

    public TransferItem getTransferItem() {
        return transferItem;
    }

    public void setTransferItem(TransferItem transferItem) {
        this.transferItem = transferItem;
    }

    public BatchTransfer getBatchTransfer() {
        return batchTransfer;
    }

    public void setBatchTransfer(BatchTransfer batchTransfer) {
        this.batchTransfer = batchTransfer;
    }

    public FormView getPageView() {
        return pageView;
    }

    public void setPageView(FormView pageView) {
        this.pageView = pageView;
    }

    public List<BatchTransfer> getBatchTransferList() {
        return batchTransferList;
    }

    public String getOptionText() {
        return optionText;
    }
}
