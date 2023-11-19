/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Packaging;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.enums.CustomerType;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Richard Narh
 */
@Stateless
public class DefaultService {
    @Inject private CrudApi crudApi;

    public List<Object[]> getStockReceiptItems()
    {
        String queryString = "SELECT e.id, e.`ref_no`, p.`product_name`, SUM(e.pkg_quantity) AS Quantity, u.`units`, pkg.`units_in_package`, e.`cost_price`, pkg.`package_price`,p.`reorder_level` FROM `stock_recept_item` e \n" +
"		INNER JOIN product p ON p.id=e.product \n" +
"		INNER JOIN product_package pkg ON pkg.id=e.product_package \n" +
"		INNER JOIN `unit_measurement` u ON u.id=pkg.`units_measurement` \n" +
"		GROUP BY p.product_name, pkg.`units_measurement` ORDER BY p.product_name DESC";
        
        Query query = crudApi.getEm().createNativeQuery(queryString);
        return query.getResultList();
    }
    
    public StockReceipt getStockReceipt(PurchaseOrder purchaseOrder) {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM StockReceipt e WHERE e.purchaseOrder=:purchaseOrder", StockReceipt.class)
                    .setParameter(StockReceipt._purchaseOrder, purchaseOrder)
                    .getResultStream().findAny().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StockReceipt getStockReceipt(String orderCode) {
        return crudApi.getEm().createQuery("SELECT e FROM StockReceipt e WHERE e.receiptNo=:receiptNo", StockReceipt.class)
                    .setParameter(StockReceipt._receiptNo, orderCode)
                    .getResultStream().findAny().orElse(null);
    }
    public Inventory getProduct(Product product, UnitMeasurement unitMeasurement, CompanyBranch companyBranch) {
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.product=:product AND e.unitMeasurement=:unitMeasurement AND e.companyBranch=:companyBranch", Inventory.class)
                .setParameter(Inventory._product, product)
                .setParameter(Inventory._unitMeasurement, unitMeasurement)
                .setParameter(Inventory._companyBranch, companyBranch)
                .getResultStream().findFirst().orElse(null);
    }
    public ProductType getProductType(String prdtType){
        return crudApi.getEm().createQuery("SELECT e FROM ProductType e WHERE e.productTypeName = :productTypeName", ProductType.class)
                .setParameter(ProductType._productTypeName, prdtType)
                .getResultStream().findFirst().orElse(null);
    }
    public UnitMeasurement getUnits(String units) {
        return crudApi.getEm().createQuery("SELECT e FROM UnitMeasurement e WHERE e.units = :units", UnitMeasurement.class)
                .setParameter(UnitMeasurement._units, units)
                .getResultStream().findFirst().orElse(null);
    }

    public Packaging getPackage(String packageString) {
        return crudApi.getEm().createQuery("SELECT e FROM Packaging e WHERE e.packagingName=:packagingName", Packaging.class)
                .setParameter(Packaging._packagingName, packageString)
                .getResultStream().findFirst().orElse(null);
    }

    public Product getProduct(String productName) {
        return crudApi.getEm().createQuery("SELECT e FROM Product e WHERE e.productName=:productName", Product.class)
                .setParameter(Product._productName, productName)
                .getResultStream().findFirst().orElse(null);
    }

    public List<Inventory> inventoryProduct(StockReceiptItem receiptItem) {
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem=:stockReceiptItem", Inventory.class)
                .setParameter(Inventory._stockReceiptItem, receiptItem)
                .getResultList();
    }
    
    public Customer walkinCustomer() {
        String qryString = "SELECT e FROM Customer e WHERE e.customerName=:customerName";
        TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class)
                .setParameter(Customer._customerName, CustomerType.WALK_IN_CUSTOMER.getLabel());
        return typedQuery.getResultStream().findFirst().orElse(null);
    }
    
    public <T> T getObj(Class<T> clazz, String fName, Object fValue) {
        return getObj(clazz, fName, null, fValue, null);
    }
    
    public <T> T getObj(Class<T> clazz, String fName, String fName2, Object fValue, Object fValue2) {
        Query query = null;
        if(fName2 == null && fValue2 == null){
            query = crudApi.getEm().createQuery("SELECT e FROM " + clazz.getSimpleName() + " e WHERE e." + fName + "=:"+fName, clazz);
            query.setParameter(fName, fValue);
        }else{
            query = crudApi.getEm().createQuery("SELECT e FROM " + clazz.getSimpleName() + " e WHERE e." + fName + "=:"+fName +" AND e."+fName2 + "=:"+fName2, clazz);
            query.setParameter(fName, fValue);
            query.setParameter(fName2, fValue2);
        }
        
        return getSingleQry(query);
    }
    
    public <T> List<T> getObjList(Class<T> clazz, String fName, Object fValue) {
        Query query = crudApi.getEm().createQuery("SELECT e FROM " + clazz.getSimpleName() + " e WHERE e." + fName + "=:"+fName, clazz);
        query.setParameter(fName, fValue);
        
        return getResultList(query);
    }
    
    private <T> T getSingleQry(Query qry){
       return (T) qry.getResultStream().findFirst().orElse(null);
    }
    
    private <T> List<T> getResultList(Query qry){
       return qry.getResultList();
    }
    
    public Sales getSales(String receiptNumber) {
        return getObj(Sales.class, receiptNumber, Sales._receiptNumber);
//        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.receiptNumber=:receiptNumber", Sales.class)
//                .setParameter("receiptNumber", receiptNumber)
//                .getResultStream().findFirst().orElse(null);
    }
    public List<SaleItem> getSales(Sales sales) {
            return crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales", SaleItem.class)
                   .setParameter(SaleItem._sales, sales)
                   .getResultList();
    }

    public Inventory getInventoryByStockReceiptItem(StockReceiptItem stockReceiptItem) {
       return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem =:stockReceiptItem", Inventory.class)
                .setParameter(Inventory._stockReceiptItem, stockReceiptItem)
                .getResultStream().findFirst().orElse(null); 
    }
}
