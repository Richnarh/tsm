package com.khoders.tsm.admin.services;

import com.khoders.resource.enums.PaymentMethod;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.DefaultService;
import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.dto.CashReceipt;
import com.khoders.tsm.dto.InvoiceDto;
import com.khoders.tsm.dto.ProductDto;
import com.khoders.tsm.dto.SaleItemDto;
import com.khoders.tsm.dto.SalesReceipt;
import com.khoders.tsm.dto.SalesTaxDto;
import com.khoders.tsm.dto.StockDetails;
import com.khoders.tsm.dto.Waybill;
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
import com.khoders.tsm.entities.system.CompanyBranch;
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
    @Inject private DefaultService ds;
    
    public boolean saveUpload(List<StockDetails> stockDetailList, CompanyBranch companyBranch) {
        try {
            for (StockDetails details : stockDetailList) {
                ProductType productType = null;
                if (details.getProductType() != null) {
                     productType = ds.getProductType(details.getProductType());
                    if (productType == null) {
                        productType = new ProductType();
                        productType.genCode();
                        productType.setProductTypeName(details.getProductType());
                        productType.setUserAccount(appSession.getCurrentUser());
                        productType.setCompanyBranch(companyBranch);
                        productType.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(productType);
                    }
                }
                if (details.getProductName() != null) {
                    Product product = ds.getProduct(details.getProductName());
                    if (product == null) {
                        product = new Product();
                        product.setProductName(details.getProductName().trim());
                        product.setProductType(productType != null ? productType : null);
                        product.setUserAccount(appSession.getCurrentUser());
                        product.setCompanyBranch(companyBranch);
                        product.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                        crudApi.save(product);
                    }
                }
                
                if (details.getUnitsMeasurement() != null) {
                    UnitMeasurement unitMeasurement = ds.getUnits(details.getUnitsMeasurement());
                    if (unitMeasurement == null) {
                        unitMeasurement = new UnitMeasurement();
                        unitMeasurement.setCompanyBranch(companyBranch);
                        unitMeasurement.setUnits(details.getUnitsMeasurement());
                        unitMeasurement.genCode();
                        unitMeasurement.setUserAccount(appSession.getCurrentUser());
                        crudApi.save(unitMeasurement);
                    }
                }
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }    
}
