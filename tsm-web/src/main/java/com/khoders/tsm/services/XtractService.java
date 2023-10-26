package com.khoders.tsm.services;

import com.khoders.resource.enums.PaymentMethod;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.dto.SaleItemDto;
import com.khoders.tsm.dto.SalesReceipt;
import com.khoders.tsm.dto.SalesTaxDto;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.CompoundSale;
import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.entities.DeliveryInfo;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.entities.ShippingInfo;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.dto.CashReceipt;
import com.khoders.tsm.dto.InvoiceDto;
import com.khoders.tsm.dto.ProductDto;
import com.khoders.tsm.dto.StockDetails;
import com.khoders.tsm.dto.Waybill;
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
        String paymentMethod = null;
        if(!salesService.payments(sales).isEmpty()){
            paymentMethod = salesService.payments(sales).get(0).getPaymentMethod().getLabel();
        }
        salesReceipt.setModeOfPayment(paymentMethod == null ? PaymentMethod.CASH.getLabel() : paymentMethod);
        salesReceipt.setCustomer(sales.getCustomer().getCustomerName());

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
            if(posCart.getInventory() != null && posCart.getInventory() != null)
            {
              itemDto.setProduct(posCart.getInventory().getProduct().getProductName());
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

    public CashReceipt extractCashReceipt(CreditPayment creditPayment, CompoundSale compoundSale) {
        CashReceipt cashReceipt = new CashReceipt();
        if (appSession.getCurrentUser().getCompanyBranch() != null)
        {
            cashReceipt.setBranchName(appSession.getCurrentUser().getCompanyBranch() + "");
        }
        if (appSession.getCurrentUser().getCompanyBranch() != null)
        {
            cashReceipt.setWebsite(appSession.getCurrentUser().getCompanyBranch().getCompanyProfile().getWebsite());
        }
        cashReceipt.setRefNo(creditPayment.getRefNo());
        cashReceipt.setAmountPaid(creditPayment.getAmountPaid());
        cashReceipt.setAmountRem(creditPayment.getCreditRemaining());
        cashReceipt.setInvoiceAmnt(creditPayment.getTotalCredit());
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
                        unitMeasurement.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
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

    public SalesReceipt extractWaybill(List<DeliveryInfo> deliveryInfos,String receiptNumber) {
       Waybill waybill = new Waybill();
       List<SaleItemDto> saleItems = new LinkedList<>();
       
       Sales sales = salesService.getSale(receiptNumber);
       ShippingInfo shippingInfo = salesService.getShippingInfo(receiptNumber);
       
       waybill.setDriverName(shippingInfo.getDriverName());
       waybill.setCarNo(shippingInfo.getCarNumber());
       waybill.setCustomer(sales.getCustomer().getCustomerName());
       waybill.setAddress(sales.getCustomer().getAddress());
       waybill.setPhoneNumber(sales.getCustomer().getPhone());
       waybill.setRef(sales.getRefNo());
       waybill.setBranchName(appSession.getCompanyBranch().getBranchName());
       waybill.setTelNumber(appSession.getCompanyBranch().getTelephoneNo());
       waybill.setCompanyAddress(appSession.getCompanyBranch().getBranchAddress());
       waybill.setWebsite(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null ? appSession.getCompanyBranch().getCompanyProfile().getWebsite() : "");
       
        double sum = 0.0;
        for (DeliveryInfo info : deliveryInfos) {
            SaleItemDto item = new SaleItemDto();
            if(info.getSaleItem() != null && info.getSaleItem().getInventory() != null){
                item.setProduct(info.getSaleItem().getInventory().getStockReceiptItem().getProduct().getProductName());
                item.setProductPackage(info.getSaleItem().getInventory().getUnitMeasurement().getUnits());
            }
            item.setQuantity(info.getSaleItem().getQuantity());
            item.setUnitPrice(info.getSaleItem().getUnitPrice());
            
            saleItems.add(item);
            
            sum += (info.getSaleItem().getQuantity() * info.getSaleItem().getUnitPrice());
        }
        
        waybill.setSaleItemList(saleItems);
        waybill.setTotalAmount(sum);
        waybill.setDeliveryDate(deliveryInfos.get(0).getDeliveryDate());
        return waybill;
    }

    public InvoiceDto extractInvoice(List<SaleItem> itemList, Sales sales){
        InvoiceDto invoiceDto = new InvoiceDto();
        List<SaleItemDto> saleItemList = new LinkedList<>();
        List<SalesTaxDto> salesTaxes = new LinkedList<>();
        
        List<SalesTax> salesTaxesList  = salesService.getSalesTaxList(sales);
        
        double totalAmount = itemList.stream().mapToDouble(SaleItem::getSubTotal).sum();
        double sTaxAmount = salesTaxesList.stream().mapToDouble(SalesTax::getTaxAmount).sum();
        double invoiceValue = sTaxAmount + sales.getTotalAmount();
         
        invoiceDto.setTotalPayable(invoiceValue);
        invoiceDto.setIssueDate(sales.getValueDate());
        invoiceDto.setTelNumber(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getTelephoneNo() : "");
        String paymentMethod = null;
        if(!salesService.payments(sales).isEmpty()){
            paymentMethod = salesService.payments(sales).get(0).getPaymentMethod().getLabel();
        }
        invoiceDto.setPaymentMethod(paymentMethod == null ? PaymentMethod.CASH.getLabel() : paymentMethod);
        invoiceDto.setTotalAmount(totalAmount);
        invoiceDto.setInvoiceNotes(sales.getNotes());
        invoiceDto.setReceiptNumber(sales.getReceiptNumber());
        invoiceDto.setCompanyAddress(appSession.getCompanyBranch() != null ? appSession.getCompanyBranch().getBranchAddress() : "");
        if(appSession.getCompanyBranch() != null && appSession.getCompanyBranch().getCompanyProfile() != null){
            invoiceDto.setWebsite(appSession.getCompanyBranch().getCompanyProfile().getWebsite());
        }
        
        if(sales.getCustomer() != null){
            invoiceDto.setCustomerName(sales.getCustomer().getCustomerName());
            invoiceDto.setAddress(sales.getCustomer().getAddress());
            invoiceDto.setPhoneNumber(sales.getCustomer().getPhone());
            invoiceDto.setEmailAddress(sales.getCustomer().getEmailAddress());
            invoiceDto.setCustomerId(sales.getCustomer().getRefNo());
        }
        System.out.println("salesTaxesList -- "+salesTaxesList.size());
        for (SalesTax salesTax : salesTaxesList) {
            SalesTaxDto taxItem = new SalesTaxDto();
            taxItem.setTaxName(salesTax.getTaxName());
            taxItem.setTaxRate(salesTax.getTaxRate());
            taxItem.setTaxAmount(salesTax.getTaxAmount());

            salesTaxes.add(taxItem);
        }
        
        for (SaleItem saleItem : itemList) {
            SaleItemDto dto = new SaleItemDto();

            dto.setQuantity(saleItem.getQuantity());
            dto.setUnitPrice(saleItem.getUnitPrice());
            dto.setSubTotal(saleItem.getSubTotal());
            if (saleItem.getInventory() != null && saleItem.getInventory().getProduct() != null) {
                dto.setProduct(saleItem.getInventory().getProduct().getProductName());
            }
            if (saleItem.getInventory().getUnitMeasurement() != null) {
                dto.setProductPackage(saleItem.getInventory().getUnitMeasurement().getUnits());
            }

            saleItemList.add(dto);
        }
       
        invoiceDto.setSaleItemList(saleItemList);
        invoiceDto.setTaxList(salesTaxes);
        
        return invoiceDto;
    }
    
}
