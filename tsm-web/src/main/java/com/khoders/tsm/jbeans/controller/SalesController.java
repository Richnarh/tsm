package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.enums.PaymentMethod;
import com.khoders.resource.enums.PaymentStatus;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
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
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.enums.EventModule;
import com.khoders.tsm.enums.SalesType;
import java.io.Serializable;
import java.time.LocalDate;
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
        
    private SaleItem saleItem = new SaleItem();
    private List<SaleItem> saleItemList,itemList = new LinkedList<>();
    private List<Sales> salesList = new LinkedList<>();
    private SalesType selectedSalesType = SalesType.NORMAL_SALES;
    
    private List<Tax> taxList = new LinkedList<>();
    private List<SalesTax> salesTaxList = new LinkedList<>();
    
    private Sales sales = new Sales();
    private DateRangeUtil dateRange = new DateRangeUtil();
    
    private FormView pageView = FormView.listForm();
    private boolean enableTax;
     
    double totalAmount,totalSaleAmount,totalPayable = 0.0;
    private int qtyRem = 0;
    private Customer customer = null;
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private LocalDate dueDate;
    
    @PostConstruct
    private void init()
    {
        clearAll();
        salesList = salesService.getSales();
        taxList = salesService.getTaxList();
    }
    
    public void initNewSale()
    {
        enableTax = false;
        enableTax = appSession.getCompanyBranch().isEnableTax();
        System.out.println("enableTax: "+enableTax);
        clearAll();
        appSession.logEvent("Click New Sale", EventModule.SALES, "New Sale");
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
      appSession.logEvent("Filter Sales", EventModule.SALES, "Filter Sales");
    }
    
    public void inventoryProperties(){
        saleItem.setUnitPrice(saleItem.getInventory().getPackagePrice());
        qtyRem = (int) saleItem.getInventory().getQtyInShop();
    }
    
    public void selectSalesType(){
        this.selectedSalesType = sales.getSalesType();
    }
        
    public void reset(){
      salesList = new LinkedList<>();  
      dateRange = new DateRangeUtil();
    }
    
    public void fetchPackagePrice(Inventory inventory)
    {
        System.out.println("Item selected -- ");
        double packagePrice = salesService.queryPackagePrice(inventory.getUnitMeasurement(), saleItem.getInventory().getStockReceiptItem());  
        System.out.println("packagePrice => "+packagePrice);
        saleItem.setUnitPrice(packagePrice);
        saleItem.setInventory(inventory);
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
                saleItem.setId(crudApi.genId());
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
        saleItemList.remove(saleItem);
    }
    
    public void viewSales(Sales sales)
    {
        this.sales = sales;
        enableTax = false;
        
        pageView.restToCreateView();
        clearAll();
        
        enableTax = appSession.getCompanyBranch().isEnableTax();
        System.out.println("enableTax: "+enableTax);
        
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
                System.out.println("customer: "+sales.getCustomer());
                if(sales.getCustomer() == null){
                    sales.setCustomer(salesService.walkinCustomer());
                }
                if(sales.getSalesType() == SalesType.CREDIT_SALES){
                    System.out.println("Credit selling....");
                    Sales creditSale = salesService.checkCustomerCredit(sales.getCustomer());
                    if(creditSale != null){
                        creditSale.setCompound(true);
                        crudApi.save(creditSale);
                        System.out.println("Credit selling....True");
                    }
                }
                sales.genCode();
                sales.setPaymentStatus(PaymentStatus.PENDING);
                sales.setCompound(false);
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
                
                System.out.println("Executing......");
                if(enableTax){
                    taxCalculation();
                    System.out.println("taxCalculation.......");
                }
                
                Msg.info("Transaction saved successfully!");
                appSession.logEvent("Save Sales", EventModule.SALES, "Complete Sales");
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void taxCalculation()
    {
        System.out.println("Executing taxCalculation......");
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
        sales.setCustomer(salesService.defaultCustomer(CustomerType.WALK_IN_CUSTOMER));
        saleItem.setUserAccount(appSession.getCurrentUser());
        saleItem.setCompanyBranch(appSession.getCompanyBranch());
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
       sales.setSalesType(null);
       pageView.restToListView();
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

    public SalesType getSelectedSalesType() {
        return selectedSalesType;
    }

    public void setSelectedSalesType(SalesType selectedSalesType) {
        this.selectedSalesType = selectedSalesType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getQtyRem() {
        return qtyRem;
    }
}
