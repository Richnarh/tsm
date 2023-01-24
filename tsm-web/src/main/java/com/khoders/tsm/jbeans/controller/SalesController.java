package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.Tax;
import com.khoders.tsm.enums.CustomerType;
import com.khoders.tsm.jbeans.ReportFiles;
import com.khoders.tsm.jbeans.dto.SalesReceipt;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.SalesService;
import com.khoders.tsm.services.XtractService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.reports.ReportManager;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richard
 */
@Named(value = "salesController")
@SessionScoped
public class SalesController implements Serializable
{
    @Inject private CrudApi crudApi; 
    @Inject private AppSession appSession; 
    @Inject private SalesService salesService;
    @Inject private XtractService xtractService;
    @Inject private ReportManager reportManager;
        
//    @Inject private InventoryService salesService;
    private SaleItem saleItem = new SaleItem();
    private List<SaleItem> saleItemList = new LinkedList<>();
    private List<Sales> salesList = new LinkedList<>();
    
    private List<Tax> taxList = new LinkedList<>();
    private List<SalesTax> salesTaxList = new LinkedList<>();
    
    private Sales sales = new Sales();
    private StockReceiptItem stockReceiptItem = new StockReceiptItem();
    
    private DateRangeUtil dateRange = new DateRangeUtil();
    
    private FormView pageView = FormView.listForm();
    private boolean enableType = false;
    private String customerName,phoneNumber,address;
    
    double totalAmount,totalSaleAmount,totalPayable = 0.0;
    private Customer customer = null;
    
    @PostConstruct
    private void init()
    {
        clearAll();
        salesList = salesService.getSales();
        taxList = salesService.getTaxList();
    }
    
    public void initPosSales()
    {
        clearAll();
        pageView.restToCreateView();
    }
    
    public void initInvoiceSales()
    {
        clearAll();
        pageView.restToDetailView();
    }
    
    public void filterSales()
    {
      salesList = salesService.getSalesByDates(dateRange); 
    }
    
    public void selectedCustomerType(){
        customer = crudApi.find(Customer.class, sales.getCustomer().getId());
        if(customer != null)
        {
            if(customer.getCustomerName().equals(CustomerType.WALK_IN_CUSTOMER.getLabel()) || customer.getCustomerName().equals(CustomerType.BACK_LOG_SUPPLIER.getLabel())){
                enableType = true;
            }else{
                enableType = false;
            }
        }
    }
    public void inventoryProperties(){
        System.out.println("Over here----");
        if(saleItem.getInventory().getPackagePrice() != 0.0){
          saleItem.setUnitPrice(saleItem.getInventory().getPackagePrice());
        }
    }
    public void reset()
    {
      salesList = new LinkedList<>();  
      dateRange = new DateRangeUtil();
    }

    public void addSaleItem()
    {
        try
        {
            if (saleItem.getQuantity() <= 0)
            {
                Msg.error("Please enter quantity");
                return;
            }
            
            if (saleItem.getUnitPrice() <= 0.0) {
                Msg.error("Please enter price");
                return;
            }

            if (saleItem != null) {
               
                double salesAmount = saleItem.getQuantity() * saleItem.getUnitPrice();
                
                saleItem.genCode();
                saleItem.setSubTotal(salesAmount);
                saleItemList.add(saleItem);
                saleItemList = CollectionList.washList(saleItemList, saleItem);
                
                Msg.info("One item added to cart");
            }
            clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void removeCartItem(SaleItem saleItem)
    {
//        System.out.println("Sales item -- "+saleItem.getStockReceiptItem().getProduct().getProductName());
        saleItemList.remove(saleItem);
        
        System.out.println("Size on removing --- "+saleItemList.size());
    }
    
    public void viewAsPosSale(Sales sales)
    {
        this.sales = sales;
        pageView.restToCreateView();
        clearAll();
        
        saleItemList = salesService.getSales(sales);
              
        for (SaleItem items : saleItemList) 
        {
            totalAmount += (items.getQuantity() * items.getUnitPrice());
        }
    }
    
    public void viewAsInvoiceSale(Sales sales)
    {
        this.sales = sales;
        pageView.restToDetailView();
        clearAll();
        
        if(sales.getCustomer() != null)
        {
          System.out.println("cust Id --- "+sales.getCustomer().getId());
          enableType=true;
          customerName = sales.getCustomer().getCustomerName();
          phoneNumber = sales.getCustomer().getPhone();
          address = sales.getCustomer().getAddress();
          
          System.out.println("customerName -- "+customerName +"\t phoneNumber --- "+phoneNumber +"\t address"+address);
        }
        
        saleItemList = salesService.getSales(sales);
              
        for (SaleItem items : saleItemList) 
        {
            totalAmount += (items.getQuantity() * items.getUnitPrice());
        }
    }
    
    public void saveAll()
    {
        if (saleItemList.isEmpty())
        {
            Msg.error("Cannot process an empty sale");
            return;
        }
        totalAmount = saleItemList.stream().mapToDouble(SaleItem::getSubTotal).sum();
        double qtyBought = saleItemList.stream().mapToDouble(SaleItem::getQuantity).sum();
        try 
        {
                customer = salesService.walkinCustomer();
                sales.setCustomer(customer);
            
                sales.genCode();
                sales.setPurchaseDate(LocalDateTime.now());
                sales.setTotalAmount(totalAmount);
                sales.genReceipt();
                sales.setUserAccount(appSession.getCurrentUser());
                sales.setCompanyBranch(appSession.getCompanyBranch());
                sales.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
                sales.setLastModifiedDate(LocalDateTime.now());
                sales.setQtyPurchased(qtyBought);
                
                Sales catalogue = crudApi.find(Sales.class, sales.getId());
                
                if(catalogue != null){
                    Msg.error("Please this transaction cannot be altered!");
                    return;
                }
                
                if (crudApi.save(sales) != null)
                {
                    for (SaleItem item : saleItemList)
                    {
                         item.genCode();
                         item.setSales(sales);
                         item.setCompanyBranch(appSession.getCompanyBranch());
                         item.setUserAccount(appSession.getCurrentUser());
                         item.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
                         item.setLastModifiedDate(LocalDateTime.now());
                         crudApi.save(item);
                    }
                   
                }
                
                salesList = CollectionList.washList(salesList, sales);
                
                System.out.println("Execting......");
                taxCalculation();
                
                Msg.info("Transaction saved successfully!");
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void taxCalculation()
    {
        System.out.println("Execting taxCalculation......");
        System.out.println("taxList......"+taxList.size());
        for (Tax tax : taxList)
        {
            SalesTax salesTax = new SalesTax();

            double calc = sales.getTotalAmount() * (tax.getTaxRate()/100);

            salesTax.genCode();
            salesTax.setTaxName(tax.getTaxName());
            salesTax.setTaxRate(tax.getTaxRate());
            salesTax.setTaxAmount(calc);
            salesTax.setReOrder(tax.getReOrder());
            salesTax.setUserAccount(appSession.getCurrentUser());
            salesTax.setCompanyBranch(appSession.getCompanyBranch());
            salesTax.setSales(sales);

            crudApi.save(salesTax);
        }
            
        salesTaxList = salesService.getSalesTaxList(sales);
        
        calculateVat();
    }
    
    private void calculateVat()
    {
        if(!salesTaxList.isEmpty())
        {
            SalesTax nhil = salesTaxList.get(0);
//            SalesTax getFund = salesTaxList.get(1);
            SalesTax covid19 = salesTaxList.get(1);
            SalesTax salesVat = salesTaxList.get(2);

            double totalLevies = nhil.getTaxAmount()+covid19.getTaxAmount();

            double taxableValue = sales.getTotalAmount() + totalLevies;
            
//            System.out.println("saleAmount => "+sales.getTotalAmount());
//            System.out.println("taxableValue => "+taxableValue);
//            System.out.println("totalLevies => "+totalLevies);
//            
            double vat = taxableValue*(salesVat.getTaxRate()/100);
            
//            System.out.println("vat => "+vat);

            totalPayable = vat + taxableValue;
            
            salesVat.setTaxAmount(vat);

            crudApi.save(salesVat);
            
        }
    }
    
    public void generatePOSReceipt(Sales sales)
    {
        try
        {
            List<SalesReceipt> salesReceiptList = new LinkedList<>();
            
            saleItemList = salesService.getSales(sales);
            if(saleItemList.isEmpty())
            {
                Msg.error("Cannot process an empty receipt!");
                return;
            }
            SalesReceipt extractedItem = xtractService.extractToPosReceipt(saleItemList, sales);

            salesReceiptList.add(extractedItem);
            ReportManager.reportParams.put("logo", ReportFiles.LOGO);
            reportManager.createReport(salesReceiptList, ReportFiles.RECEIPT_FILE, ReportManager.reportParams);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clear()
    {
        saleItem = new SaleItem();
        saleItem.genCode();
        SystemUtils.resetJsfUI();
    }
    
    public void clearAll()
    {
        saleItem = new SaleItem();
        saleItemList = new LinkedList<>();
        saleItem.genCode();
        totalAmount = 0.0;
        enableType=false;
        saleItem.setUserAccount(appSession.getCurrentUser());
        saleItem.setCompanyBranch(appSession.getCompanyBranch());
        resetEnable();
        SystemUtils.resetJsfUI();
    }
    
    public void resetSales(){
        closePage();
        clearAll();
        pageView.restToCreateView();
    }
    
    public void closePage()
    {
       sales = new Sales();
       enableType = false;
       resetEnable();
       pageView.restToListView();
    }
    
    public void resetEnable(){
        customerName = null;
        phoneNumber = null;
        address = null;
    }

    public SaleItem getSaleItem()
    {
        return saleItem;
    }

    public void setSaleItem(SaleItem saleItem)
    {
        this.saleItem = saleItem;
    }

    public List<SaleItem> getSaleItemList()
    {
        return saleItemList;
    }

    public List<Sales> getSalesList()
    {
        return salesList;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public List<SaleItem> getCartList()
    {
        return saleItemList;
    }

    public List<Sales> getSalesCatalogueList()
    {
        return salesList;
    }

    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }

    public DateRangeUtil getDateRange()
    {
        return dateRange;
    }

    public void setDateRange(DateRangeUtil dateRange)
    {
        this.dateRange = dateRange;
    }

    public Sales getSales()
    {
        return sales;
    }

    public void setSales(Sales sales)
    {
        this.sales = sales;
    }

    public boolean isEnableType()
    {
        return enableType;
    }

    public void setEnableType(boolean enableType)
    {
        this.enableType = enableType;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public List<Tax> getTaxList()
    {
        return taxList;
    }

    public List<SalesTax> getSalesTaxList()
    {
        return salesTaxList;
    }

    public double getTotalSaleAmount()
    {
        return totalSaleAmount;
    }

    public double getTotalPayable()
    {
        return totalPayable;
    }
}
