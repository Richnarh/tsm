package com.khoders.tsm.services;

import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.jbeans.dto.SaleItemDto;
import com.khoders.tsm.jbeans.dto.SalesReceipt;
import com.khoders.tsm.jbeans.dto.SalesTaxDto;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
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

        salesReceipt.setReceiptNumber(SystemUtils.generateRefNo());
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
//            if(posCart.getInventory() != null && posCart.getInventory().getProduct() != null)
//            {
//              itemDto.setProduct(posCart.getInventory().getProduct().getProductName());
//            }
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
            receiptItem.setSellingPrice(0.0);
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
// 
//    public SalesReceipt extractToCashReceipt(Sales sales)
//    {
//        SalesReceipt receipt = new SalesReceipt();
//        
//        List<SalesTax> salesTaxesList  = salesService.getSalesTaxList(sales);
//        
//        double totalTax = salesTaxesList.stream().mapToDouble(SalesTax::getTaxAmount).sum();
//        double invoiceValue = totalTax + sales.getTotalAmount();
//        
//        receipt.setBranchName(appSession.getCompanyBranch() != null ?  appSession.getCompanyBranch().getBranchName() : "");
//        receipt.setCashier(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
//        receipt.setReceiptNumber(sales.getReceiptNumber());
//        receipt.setTotalTax(totalTax);
//        receipt.setTotalAmount(sales.getTotalAmount());
//        receipt.setDate(LocalDateTime.now());
//        receipt.setTotalPayable(invoiceValue);
//        try
//        {
//            receipt.setModeOfPayment(sales.getPaymentMethod().getLabel());
//        } catch (Exception e)
//        {
//        }
//        
//        return receipt;
//    }
//    

    
//    public ProformaInvoiceDto extractInvoice(List<SaleItem> itemList, Sales sales)
//    {
//        ProformaInvoiceDto invoiceDto = new ProformaInvoiceDto();
//        List<SaleItemDto> saleItemList = new LinkedList<>();
//        List<SalesTaxDto> salesTaxes = new LinkedList<>();
//        
//        List<SalesTax> salesTaxesList  = salesService.getSalesTaxList(sales);
//        
//        double totalAmount = itemList.stream().mapToDouble(SaleItem::getSubTotal).sum();
//        double sTaxAmount = salesTaxesList.stream().mapToDouble(SalesTax::getTaxAmount).sum();
//        double invoiceValue = sTaxAmount + sales.getTotalAmount();
//         
//        invoiceDto.setTotalPayable(invoiceValue);
//        invoiceDto.setIssueDate(sales.getValueDate());
//        invoiceDto.setTelNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");
//        invoiceDto.setPaymentMethod(sales.getPaymentMethod() != null ? sales.getPaymentMethod().getLabel() : "");
//        invoiceDto.setTotalAmount(totalAmount);
//        invoiceDto.setReceiptNumber(sales.getReceiptNumber());
//        invoiceDto.setCompanyAddress(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchAddress() : "");
//        if(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null){
//            invoiceDto.setWebsite(appSession.getCompanyBranch().getCompanyProfile().getWebsite());
//        }
//        
//        if(sales.getCustomer() != null){
//            invoiceDto.setCustomerName(sales.getCustomer().getCustomerName());
//            invoiceDto.setAddress(sales.getCustomer().getAddress());
//            invoiceDto.setPhoneNumber(sales.getCustomer().getPhone());
//            invoiceDto.setEmailAddress(sales.getCustomer().getEmailAddress());
//            invoiceDto.setCustomerId(sales.getCustomer().getRefNo());
//        }
//        System.out.println("salesTaxesList -- "+salesTaxesList.size());
//        for (SalesTax salesTax : salesTaxesList)
//        {
//            SalesTaxDto taxItem = new SalesTaxDto();
//            taxItem.setTaxName(salesTax.getTaxName());
//            taxItem.setTaxRate(salesTax.getTaxRate());
//            taxItem.setTaxAmount(salesTax.getTaxAmount());
//
//            salesTaxes.add(taxItem);
//        }
//        
//        for (SaleItem saleItem : itemList)
//        {
//            SaleItemDto dto = new SaleItemDto();
//            
//            dto.setQuantity(saleItem.getQuantity());
//            dto.setUnitPrice(saleItem.getUnitPrice());
//            dto.setSubTotal(saleItem.getSubTotal());
//            if(saleItem.getStockReceiptItem() != null && saleItem.getStockReceiptItem().getProduct() != null)
//            {
//                dto.setProduct(saleItem.getStockReceiptItem().getProduct().getProductName());
//                if(saleItem.getStockReceiptItem().getProductPackage() != null && saleItem.getStockReceiptItem().getProductPackage().getUnitMeasurement() != null)
//                {
//                    dto.setProductPackage(saleItem.getStockReceiptItem().getProductPackage().getUnitMeasurement().getUnits());
//                }
//            }
//            
//            saleItemList.add(dto);
//        }
//       
//        invoiceDto.setSaleItemList(saleItemList);
//        invoiceDto.setTaxList(salesTaxes);
//        
//        return invoiceDto;
//    }
//    
//    public List<ProductDto> extractProduct(){
//        List<ProductDto> dtoList = new LinkedList<>();
//         
//        List<Product> productList = tsmService.getProductList();
//        for (Product product : productList)
//        {
//         ProductDto dto = new ProductDto();
//         dto.setCompanyAddress(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchName() : "");
//         dto.setWebsite(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null ? appSession.getCompanyBranch().getCompanyProfile().getWebsite() : "");
//         dto.setTelNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");
//         dto.setProductCode(product.getRefNo());
//         dto.setProductName(product.getProductName());
//         dto.setReorderLevel(product.getReorderLevel());
//         dto.setPackaging(product.getPackaging() != null ? product.getPackaging().getPackagingName() : "");
//         dto.setProductType(product.getProductType() != null ? product.getProductType().getProductTypeName() : "");
//         dtoList.add(dto);
//        }
//        return dtoList;
//    }
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
}
