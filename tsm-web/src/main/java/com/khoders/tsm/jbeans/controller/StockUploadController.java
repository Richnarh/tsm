/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.SalesService;
import com.khoders.tsm.services.StockService;
import com.khoders.resource.enums.DeliveryMethod;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.BeansUtil;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.BatchTransfer;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.TransferItem;
import com.khoders.tsm.enums.CustomerType;
import com.khoders.tsm.enums.TransferStatus;
import com.khoders.tsm.jbeans.dto.StockDetails;
import com.khoders.tsm.services.InventoryService;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author richa
 */
@Named(value = "stockUploadController")
@SessionScoped
public class StockUploadController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private StockService stockService;
    @Inject private InventoryService inventoryService;
    @Inject private SalesService salesService;
        
    private StockDetails stockDetails = new StockDetails();
    private List<StockDetails> stockDetailList = new LinkedList<>();
    
    private PurchaseOrder selectedPurchaseOrder;
    
    private UploadedFile file = null;
    private boolean prepareOrder, recieveOrder, postToInventory;
    private Location location = new Location();
    private Location toLocation = new Location();
    
    public String getFileExtension(String filename) {
    if(filename == null)
    {
        return null;
    }
    return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
    
    public void uploadPrice()
    {
        if (file.getSize() < 1)
        {
            Msg.error("No excel file is selected!");
            return;
        }

        try
        {
            String extension = getFileExtension(file.getFileName());
            System.out.println("type ==> " + extension);

            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            sheet.removeRow(sheet.getRow(0));
            Iterator<Row> iterator = sheet.iterator();
            System.out.println("Starting....");
            int c = 0;
            while (iterator.hasNext())
            {
                c++;
                stockDetails = new StockDetails();
                Row currentRow = iterator.next();
                String productName = BeansUtil.objToString(currentRow.getCell(0));
                if(productName != null && !productName.isEmpty()){
                    stockDetails.setProductName(productName.trim());
                }
                String productType = BeansUtil.objToString(currentRow.getCell(1));
                if(productType != null && !productType.isEmpty()){
                    stockDetails.setProductType(productType.trim());
                }
                String reorderLevel = BeansUtil.objToString(currentRow.getCell(2));
                if(reorderLevel != null && !reorderLevel.isEmpty()){
                    stockDetails.setReorderLevel(BeansUtil.objToInteger(reorderLevel));
                }
                String qtyInWarehouse = BeansUtil.objToString(currentRow.getCell(3));
                if(qtyInWarehouse != null && !qtyInWarehouse.isEmpty()){
                    stockDetails.setQtyInWarehouse(BeansUtil.objToDouble(qtyInWarehouse));
                }
                String qtyInShop = BeansUtil.objToString(currentRow.getCell(4));
                if(qtyInShop != null && !qtyInShop.isEmpty()){
                    stockDetails.setQtyInShop(BeansUtil.objToDouble(qtyInShop));
                }
                String costPrice = BeansUtil.objToString(currentRow.getCell(5));
                if(costPrice != null && !costPrice.isEmpty()){
                    stockDetails.setCostPrice(BeansUtil.objToDouble(costPrice));
                }
                String sellPrice = BeansUtil.objToString(currentRow.getCell(6));
                if(sellPrice != null && !sellPrice.isEmpty()){
                    stockDetails.setSellingPrice(BeansUtil.objToDouble(sellPrice));
                }
                
                stockDetailList.add(stockDetails);
                System.out.println("Iteration "+c+" done!");
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }
        
    @SuppressWarnings("null")
    public void saveUpload()
    {
        try
        {
            int c=0;
            if(stockDetailList.isEmpty()) return;
            
            PurchaseOrder purchaseOrder = null;
            StockReceipt stockReceipt = null;
            BatchTransfer transfer = null;
            
            if(location == null){
                Msg.error("Please select warehouse");
                return;
            }
            boolean uploads = stockService.saveUpload(stockDetailList);
            if(uploads){
                if(prepareOrder){
                    purchaseOrder = new PurchaseOrder();
                    purchaseOrder.setCustomer(stockService.getObj(Customer.class, CustomerType.WALK_IN_CUSTOMER.getLabel()));
                    purchaseOrder.setDeliveryMethod(DeliveryMethod.AT_WAREHOUSE_SHOP);
                    purchaseOrder.setOrderCode(SystemUtils.generatePO());
                    purchaseOrder.setPurchasedDate(LocalDate.now());
                    purchaseOrder.setTotalAmount(stockDetailList.stream().mapToDouble(StockDetails::getCostPrice).sum());
                    purchaseOrder.genCode();
                    purchaseOrder.setUserAccount(appSession.getCurrentUser());
                    purchaseOrder.setCompanyBranch(appSession.getCompanyBranch());
                    purchaseOrder.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                    crudApi.save(purchaseOrder);
                }
                if(recieveOrder && purchaseOrder != null){
                    stockReceipt = new StockReceipt();
                    stockReceipt.setReceiptNo(SystemUtils.generateIN());
                    stockReceipt.setRefNo(purchaseOrder.getOrderCode());
                    stockReceipt.setBatchNo(SystemUtils.generateCode());
                    stockReceipt.setTotalAmount(purchaseOrder.getTotalAmount());
                    stockReceipt.setPurchaseOrder(purchaseOrder);
                    stockReceipt.setLocation(location);
                    stockReceipt.setReceivedBy(appSession.getCurrentUser());
                    stockReceipt.setUserAccount(appSession.getCurrentUser());
                    stockReceipt.setCompanyBranch(appSession.getCompanyBranch());
                    stockReceipt.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                }
                
                if(postToInventory && stockReceipt != null){
                    transfer = new BatchTransfer();
                    transfer.setBatchNumber(stockReceipt.getBatchNo());
                    transfer.setFromLocation(stockReceipt.getLocation());
                    transfer.setToLocation(toLocation);
                    transfer.setTransferDate(LocalDate.now());
                    transfer.setTransferStatus(TransferStatus.ACCEPTED);
                    transfer.setNotes("Transfer at uploads on "+LocalDate.now());
                    transfer.setUserAccount(appSession.getCurrentUser());
                    transfer.genCode();
                    transfer.setCompanyBranch(appSession.getCompanyBranch());
                    transfer.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                }
                
                for (StockDetails stockData : stockDetailList) {
                    PurchaseOrderItem order = null;
                    StockReceiptItem  stockReceiptItem = null;
                    if(prepareOrder && purchaseOrder != null){
                        PurchaseOrderItem orderItem = new PurchaseOrderItem();
                        orderItem.setPurchaseOrder(purchaseOrder);
                        orderItem.setCostPrice(stockData.getCostPrice());
                        orderItem.setProduct(stockService.getObj(Product.class, stockData.getProductName()));
                        orderItem.setQtyPurchased(stockData.getQtyInWarehouse());
                        orderItem.setSubTotal(stockData.getCostPrice() * stockData.getQtyInWarehouse());
                        orderItem.setUserAccount(appSession.getCurrentUser());
                        orderItem.setCompanyBranch(appSession.getCompanyBranch());
                        orderItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(orderItem);
                        order = orderItem;
                    }
                    if(recieveOrder && stockReceipt != null){
                        StockReceiptItem receiptItem = new StockReceiptItem();
                        receiptItem.setStockReceipt(stockReceipt);
                        receiptItem.setPurchaseOrderItem(order);
                        receiptItem.setCostPrice(stockData.getCostPrice());
                        receiptItem.setProduct(stockService.getObj(Product.class, stockData.getProductName()));
                        receiptItem.setPkgQuantity(stockData.getQtyInWarehouse());
                        receiptItem.setUserAccount(appSession.getCurrentUser());
                        receiptItem.setCompanyBranch(appSession.getCompanyBranch());
                        receiptItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        
                        crudApi.save(receiptItem);
                        stockReceiptItem = receiptItem;
                    }
                    if(postToInventory && transfer != null){
                        TransferItem transferItem = new TransferItem();
                        transferItem.setBatchTransfer(transfer);
                        transferItem.setStockReceiptItem(stockReceiptItem);
                        transferItem.setQtyTransferred(stockData.getQtyInShop());
                        transferItem.setUserAccount(appSession.getCurrentUser());
                        transferItem.setCompanyBranch(appSession.getCompanyBranch());
                        transferItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        
                        if(crudApi.save(transferItem) != null){
                            
                            Inventory inventory = inventoryService.postToInventory(transfer);
//                            inventory.setLocation(toLocation);
//                            inventory.setQuantity(transferItem.getQtyTransferred());
//                            inventory.setSellingPrice(stockData.getSellingPrice());
//                            inventory.setStockReceiptItem(stockReceiptItem);
//                            inventory.setCompanyBranch(appSession.getCompanyBranch());
//                            inventory.setUserAccount(appSession.getCurrentUser());
//                            inventory.genCode();
//                            inventory.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
//                            crudApi.save(inventory);
                        }
                    }
                    
                }
                Msg.info("Upload saved successfully!");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void clear()
    {
        stockDetailList = new LinkedList<>();
        file = null;
        stockDetails = new StockDetails();
        SystemUtils.resetJsfUI();
    }
    
    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public PurchaseOrder getSelectedPurchaseOrder()
    {
        return selectedPurchaseOrder;
    }

    public void setSelectedPurchaseOrder(PurchaseOrder selectedPurchaseOrder)
    {
        this.selectedPurchaseOrder = selectedPurchaseOrder;
    }
}
