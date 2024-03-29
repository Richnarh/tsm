/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.StockService;
import com.khoders.resource.enums.DeliveryMethod;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.BeansUtil;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.enums.ReceiptStatus;
import com.khoders.tsm.dto.StockDetails;
import com.khoders.tsm.services.XtractService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
     private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StockUploadController.class.getName());
     
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private StockService stockService;
    @Inject private XtractService xtractService;
        
    private StockDetails stockDetails = new StockDetails();
    private List<StockDetails> stockDetailList = new LinkedList<>();
    
    private PurchaseOrder selectedPurchaseOrder;
    
    private UploadedFile file = null;
    private boolean prepareOrder, recieveOrder, postToInventory;
    private Location location,fromLocation,toLocation = null;
    
    public String getFileExtension(String filename) {
    if(filename == null)
    {
        return null;
    }
    return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
    
    public void uploadStock()
    {
        if (file.getSize() < 1){
            Msg.error("No excel file is selected!");
            return;
        }

        try
        {
            String extension = getFileExtension(file.getFileName());
            logger.log(Level.INFO, "type ==> {0}", extension);

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
                String wprice = BeansUtil.objToString(currentRow.getCell(6));
                if(wprice != null && !wprice.isEmpty()){
                    stockDetails.setWprice(BeansUtil.objToDouble(wprice));
                }
                
                String retailPrice = BeansUtil.objToString(currentRow.getCell(7));
                if(retailPrice != null && !retailPrice.isEmpty()){
                    stockDetails.setRetailPrice(BeansUtil.objToDouble(retailPrice));
                }
                
                String packaging = BeansUtil.objToString(currentRow.getCell(8));
                if(packaging != null && !packaging.isEmpty()){
                    stockDetails.setPackaging(BeansUtil.objToString(packaging));
                }
                String units = BeansUtil.objToString(currentRow.getCell(9));
                if(units != null && !units.isEmpty()){
                    stockDetails.setUnitsMeasurement(units);
                }
                String unitsInPkg = BeansUtil.objToString(currentRow.getCell(10));
                if(unitsInPkg != null && !unitsInPkg.isEmpty()){
                    stockDetails.setUnitsInPackage(BeansUtil.objToDouble(unitsInPkg));
                }
                
                stockDetailList.add(stockDetails);
//                appSession.logEvent("Stock Upload", null, "Inventory Uploads");
                System.out.println("Iteration "+c+" done!");
            }
        } catch (IOException e)
        {
        }
    }

    public void saveUpload()
    {
        try
        {
            int c=0;
            if(stockDetailList.isEmpty()) return;
            
            PurchaseOrder purchaseOrder = null;
            StockReceipt stockReceipt = null;
            
            if(location == null){
                Msg.error("Please select warehouse");
                return;
            }
            boolean uploads = xtractService.saveUpload(stockDetailList);
            if(uploads){
                if(prepareOrder){
                    purchaseOrder = new PurchaseOrder();
                    purchaseOrder.setCustomer(stockService.walkinCustomer());
                    purchaseOrder.setDeliveryMethod(DeliveryMethod.AT_WAREHOUSE_SHOP);
                    purchaseOrder.setOrderCode(SystemUtils.generatePO());
                    purchaseOrder.setPurchasedDate(LocalDate.now());
                    purchaseOrder.setTotalAmount(stockDetailList.stream().mapToDouble(StockDetails::getCostPrice).sum());
                    purchaseOrder.genCode();
                    purchaseOrder.setUserAccount(appSession.getCurrentUser());
                    purchaseOrder.setCompanyBranch(appSession.getCompanyBranch());
                    purchaseOrder.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                    purchaseOrder.setLocation(location);
                    crudApi.save(purchaseOrder);
                }
                if(recieveOrder && purchaseOrder != null){
                    stockReceipt = new StockReceipt();
                    if(location == null){
                        Msg.error("Receipt location not set.");
                        return;
                    }
                    stockReceipt.setReceiptNo(SystemUtils.generateIN());
                    stockReceipt.setRefNo(purchaseOrder.getOrderCode());
                    stockReceipt.setBatchNo(SystemUtils.generateCode());
                    stockReceipt.setTotalAmount(purchaseOrder.getTotalAmount());
                    stockReceipt.setPurchaseOrder(purchaseOrder);
                    stockReceipt.setTotalAmount(stockDetailList.stream().mapToDouble(StockDetails::getCostPrice).sum());
                    stockReceipt.setLocation(location);
                    stockReceipt.setReceivedBy(appSession.getCurrentUser());
                    stockReceipt.setUserAccount(appSession.getCurrentUser());
                    stockReceipt.setCompanyBranch(appSession.getCompanyBranch());
                    stockReceipt.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                    crudApi.save(stockReceipt);
                }
                for (StockDetails stockData : stockDetailList) {
                    PurchaseOrderItem orderItem = null;
                    StockReceiptItem receiptItem = null;
                    if(prepareOrder && purchaseOrder != null){
                        orderItem = new PurchaseOrderItem();
                        orderItem.setPurchaseOrder(purchaseOrder);
                        orderItem.setCostPrice(stockData.getCostPrice());
                        orderItem.setUnitMeasurement(stockService.getUnits(stockData.getUnitsMeasurement()));
                        orderItem.setProduct(stockService.getProduct(stockData.getProductName()));
                        orderItem.setQtyPurchased(stockData.getQtyInWarehouse());
                        orderItem.setSubTotal(stockData.getCostPrice() * stockData.getQtyInWarehouse());
                        orderItem.setUserAccount(appSession.getCurrentUser());
                        orderItem.setCompanyBranch(appSession.getCompanyBranch());
                        orderItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(orderItem);
                    }
                    if(recieveOrder && stockReceipt != null){
                        receiptItem = new StockReceiptItem();
                        receiptItem.setStockReceipt(stockReceipt);
                        receiptItem.setPurchaseOrderItem(orderItem);
                        receiptItem.setCostPrice(stockData.getCostPrice());
                        receiptItem.setProduct(stockService.getProduct(stockData.getProductName()));
                        receiptItem.setPkgQuantity(stockData.getQtyInWarehouse());
                        receiptItem.setWprice(stockData.getWprice());
                        receiptItem.setUserAccount(appSession.getCurrentUser());
                        receiptItem.setCompanyBranch(appSession.getCompanyBranch());
                        receiptItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        receiptItem.setReceiptStatus(ReceiptStatus.RECEIVED);
                        receiptItem.setUnitMeasurement(stockService.getUnits(stockData.getUnitsMeasurement()));
                        crudApi.save(receiptItem);
                    }
                    if(postToInventory){
                        Inventory inventory = stockService.getProduct(receiptItem.getProduct(), stockService.getUnits(stockData.getUnitsMeasurement()));
                        if (inventory == null) {
                            inventory = new Inventory();
                            inventory.setProduct(receiptItem.getProduct());
                            inventory.setUnitMeasurement(stockService.getUnits(stockData.getUnitsMeasurement()));
                            inventory.setPackagePrice(stockData.getRetailPrice());
                            inventory.setUnitsInPackage(stockData.getUnitsInPackage());
                            inventory.setWprice(stockData.getWprice());
                            inventory.setUserAccount(appSession.getCurrentUser());
                            inventory.setCompanyBranch(appSession.getCompanyBranch());
                            inventory.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                            inventory.setLocation(toLocation);
                            inventory.setQtyInShop(stockData.getQtyInShop());
                            inventory.setDescription("Inventory Upload on: "+LocalDate.now());
                            inventory.setDataSource("Inventory Upload dated: "+LocalDate.now());
                            crudApi.save(inventory);
                        }
                    }
                    
                }
                Msg.info("Upload saved successfully!");
                appSession.logEvent("Create Stocks", null, "Save Stock Uploads");
            }
        } catch (Exception e)
        {
        }
    }
    public void clear()
    {
        stockDetailList = new LinkedList<>();
        file = null;
        stockDetails = new StockDetails();
        location = new Location();
        fromLocation = new Location();
        toLocation = new Location();
        prepareOrder = false;
        recieveOrder = false;
        postToInventory = false;
        file = null;
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

    public List<StockDetails> getStockDetailList() {
        return stockDetailList;
    }

    public PurchaseOrder getSelectedPurchaseOrder()
    {
        return selectedPurchaseOrder;
    }

    public void setSelectedPurchaseOrder(PurchaseOrder selectedPurchaseOrder)
    {
        this.selectedPurchaseOrder = selectedPurchaseOrder;
    }

    public boolean isPrepareOrder() {
        return prepareOrder;
    }

    public void setPrepareOrder(boolean prepareOrder) {
        this.prepareOrder = prepareOrder;
    }

    public boolean isRecieveOrder() {
        return recieveOrder;
    }

    public void setRecieveOrder(boolean recieveOrder) {
        this.recieveOrder = recieveOrder;
    }

    public boolean isPostToInventory() {
        return postToInventory;
    }

    public void setPostToInventory(boolean postToInventory) {
        this.postToInventory = postToInventory;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }
    
}
