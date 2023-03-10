package com.khoders.tsm.services;

import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.jbeans.dto.SaleItemDto;
import com.khoders.tsm.jbeans.dto.SalesReceipt;
import com.khoders.tsm.jbeans.dto.SalesTaxDto;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.jbeans.dto.CashReceipt;
import com.khoders.tsm.jbeans.dto.ProductDto;
import com.khoders.tsm.jbeans.dto.StockDetails;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class XtractService
{
    @Inject private AppSession appSession;
    @Inject private CrudApi crudApi;
    @Inject private StockService stockService;
    @Inject private InventoryService inventoryService;
    @Inject private SalesService salesService;
    
    public SalesReceipt extractToPosReceipt(List<SaleItem> cartList, Sales sales){
        double receiptTotal = 0.0;

        SalesReceipt salesReceipt = new SalesReceipt();
        List<SaleItemDto> saleItemList = new LinkedList<>();
        List<SalesTaxDto> salesTaxes = new LinkedList<>();

        salesReceipt.setReceiptNumber(sales.getReceiptNumber());
        salesReceipt.setBranchName(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchName() : "");
        salesReceipt.setDate(LocalDateTime.now());
        salesReceipt.setCashier(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
        salesReceipt.setPhoneNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");

        List<SalesTax> salesTaxesList  = salesService.getSalesTaxList(sales);

        double totalAmount = cartList.stream().mapToDouble(SaleItem::getSubTotal).sum();
        double sTaxAmount = salesTaxesList.stream().mapToDouble(SalesTax::getTaxAmount).sum();
        double invoiceValue = sTaxAmount + sales.getTotalAmount();

        salesReceipt.setTotalAmount(totalAmount);
        salesReceipt.setTotalPayable(invoiceValue);
        for (SalesTax salesTax : salesTaxesList)
        {
            SalesTaxDto taxItem = new SalesTaxDto();
            taxItem.setTaxName(salesTax.getTaxName());
            taxItem.setTaxRate(salesTax.getTaxRate());
            taxItem.setTaxAmount(salesTax.getTaxAmount());

            salesTaxes.add(taxItem);
        }
        
        for (SaleItem posCart : cartList)
        {
            SaleItemDto itemDto = new SaleItemDto();
            if(posCart.getInventory() != null && posCart.getInventory().getStockReceiptItem() != null)
            {
              itemDto.setProduct(posCart.getInventory().getStockReceiptItem().getProduct().getProductName());
            }
            itemDto.setQuantity(posCart.getQuantity());
            itemDto.setUnitPrice(posCart.getUnitPrice());

            receiptTotal+=(posCart.getQuantity()*posCart.getUnitPrice());

            saleItemList.add(itemDto);
        }
        salesReceipt.setTotalAmount(receiptTotal);

        salesReceipt.setTaxList(salesTaxes);
        salesReceipt.setSaleItemList(saleItemList);

        return salesReceipt;
       
    }
    
    public List<StockReceiptItem> extractStockReceiptItems(PurchaseOrder purchaseOrder, StockReceipt stockReceipt){
       List<StockReceiptItem> stockReceiptItemList = new LinkedList<>();
       
       List<PurchaseOrderItem> orderItemList = inventoryService.getPurchaseOrderItem(purchaseOrder);
       for (PurchaseOrderItem item : orderItemList) {
            StockReceiptItem receiptItem = new StockReceiptItem();
            receiptItem.setCostPrice(item.getCostPrice());
            receiptItem.setWprice(0.0);
            receiptItem.setProduct(item.getProduct());
            receiptItem.setPurchaseOrderItem(item);
            receiptItem.setUnitMeasurement(item.getUnitMeasurement());
            receiptItem.setStockReceipt(stockReceipt);
            receiptItem.setPkgQuantity(item.getQtyPurchased());
            receiptItem.setDescription(item.getDescription());
            receiptItem.setUserAccount(appSession.getCurrentUser());
            receiptItem.setCompanyBranch(appSession.getCompanyBranch());
            receiptItem.genCode();
            stockReceiptItemList.add(receiptItem);
        }
       return stockReceiptItemList;
    }
    
    public List<ProductDto> extractProduct(){
        List<ProductDto> dtoList = new LinkedList<>();
         
        List<Product> productList = inventoryService.getProductList();
        for (Product product : productList){
         ProductDto dto = new ProductDto();
         dto.setCompanyAddress(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchName() : "");
         dto.setWebsite(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null ? appSession.getCompanyBranch().getCompanyProfile().getWebsite() : "");
         dto.setTelNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");
         dto.setProductCode(product.getRefNo());
         dto.setProductName(product.getProductName());
         dto.setReorderLevel(product.getReorderLevel());
         dto.setPackaging(product.getPackaging() != null ? product.getPackaging().getPackagingName() : "");
         dto.setProductType(product.getProductType() != null ? product.getProductType().getProductTypeName() : "");
         dtoList.add(dto);
        }
        return dtoList;
    }
//    
//    public List<StockSummary> extractStockSummary(){
//        List<StockSummary> viewStockList = new LinkedList<>();
//        List<Object[]> objects = stockService.getStockReceiptItems();
//        for (Object[] object : objects)
//        {
//          StockSummary dto = new StockSummary();
//          dto.setCompanyAddress(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchAddress() : "");
//          dto.setWebsite(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null ? appSession.getCompanyBranch().getCompanyProfile().getWebsite() : "");
//          dto.setTelNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");
//          
//          dto.setId(Stringz.objectToString(object[0]));
//          dto.setRefNo(Stringz.objectToString(object[1]));
//          dto.setProductName(Stringz.objectToString(object[2]));
//          dto.setPkgQuantity(ParseValue.parseDoubleValue(object[3]));
//          dto.setProductPackage(Stringz.objectToString(object[4]));
//          dto.setPackageFactor(ParseValue.parseDoubleValue(object[5]));
//          dto.setCostPrice(ParseValue.parseDoubleValue(object[6]));
//          dto.setPackagePrice(ParseValue.parseDoubleValue(object[7]));
//          dto.setReorderLevel(ParseValue.parseIntegerValue(object[8]));
//          dto.setQtySold(ParseValue.parseDoubleValue(object[9-1]));
//          viewStockList.add(dto);
//        }
//        return viewStockList;
//    }

    public CashReceipt extractCashReceipt(CreditPayment creditPayment) {
        CashReceipt cashReceipt = new CashReceipt();
        if (appSession.getCurrentUser().getCompanyBranch() != null)
        {
            cashReceipt.setBranchName(appSession.getCurrentUser().getCompanyBranch() + "");
        }
        if (appSession.getCurrentUser().getCompanyBranch() != null)
        {
            cashReceipt.setWebsite(appSession.getCurrentUser().getCompanyBranch().getCompanyProfile().getWebsite());
        }
        cashReceipt.setReceiptNumber(creditPayment.getSales().getReceiptNumber());
        cashReceipt.setAmountPaid(creditPayment.getAmountPaid());
        cashReceipt.setAmountRem(creditPayment.getCreditRemaining());
        cashReceipt.setInvoiceAmnt(creditPayment.getSales().getTotalAmount());
        cashReceipt.setDate(LocalDateTime.now());
        try
        {
            cashReceipt.setModeOfPayment(creditPayment.getPaymentMethod().getLabel());
        } catch (Exception e)
        {
        }
        
        return cashReceipt;
    }
    
    public boolean saveUpload(List<StockDetails> stockDetailList) {
        try {
            for (StockDetails details : stockDetailList) {
                ProductType productType = null;
                if (details.getProductType() != null) {
                     productType = stockService.getProductType(details.getProductType());
                    if (productType == null) {
                        productType = new ProductType();
                        productType.genCode();
                        productType.setProductTypeName(details.getProductType());
                        productType.setUserAccount(appSession.getCurrentUser());
                        productType.setCompanyBranch(appSession.getCompanyBranch());
                        productType.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(productType);
                    }
                }
                if (details.getProductName() != null) {
                    Product product = stockService.getProduct(details.getProductName());
                    if (product == null) {
                        product = new Product();
                        product.setProductName(details.getProductName().trim());
                        product.setProductType(productType != null ? productType : null);
                        product.setUserAccount(appSession.getCurrentUser());
                        product.setCompanyBranch(appSession.getCompanyBranch());
                        product.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(product);
                    }
                }
                
                if (details.getUnitsMeasurement() != null) {
                    UnitMeasurement unitMeasurement = stockService.getUnits(details.getUnitsMeasurement());
                    if (unitMeasurement == null) {
                        unitMeasurement = new UnitMeasurement();
                        unitMeasurement.setCompanyBranch(appSession.getCompanyBranch());
                        unitMeasurement.setUnits(details.getUnitsMeasurement());
                        unitMeasurement.genCode();
                        unitMeasurement.setUserAccount(appSession.getCurrentUser());
                        crudApi.save(unitMeasurement);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
