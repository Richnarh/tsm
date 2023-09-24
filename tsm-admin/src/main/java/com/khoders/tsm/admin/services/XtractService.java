package com.khoders.tsm.admin.services;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.DefaultService;
import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.dto.StockDetails;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.entities.system.CompanyBranch;
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
