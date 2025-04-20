package com.tsm.jbeans.controller;

import com.dolphindoors.resource.enums.PaymentMethod;
import com.dolphindoors.resource.enums.PaymentStatus;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.reports.ReportManager;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.FormView;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.Customer;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
import com.tsm.entities.SalesTax;
import com.tsm.entities.Tax;
import com.tsm.enums.CustomerType;
import com.tsm.ReportFiles;
import com.tsm.dto.SalesReceipt;
import com.tsm.listener.AppSession;
import com.tsm.services.XtractService;
import com.tsm.entities.Inventory;
import com.tsm.entities.Payment;
import com.tsm.enums.EventModule;
import com.tsm.enums.SaleSource;
import com.tsm.enums.SalesType;
import com.tsm.dto.InvoiceDto;
import com.tsm.dto.SalesTaxDto;
import com.tsm.services.InventoryService;
import com.tsm.services.SalesService;
import com.tsm.services.StockService;
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
@Named(value = "wholeSaleController")
@SessionScoped
public class WholeSaleController implements Serializable
{
    @Inject private CrudApi crudApi; 
    @Inject private AppSession appSession; 
    @Inject private SalesService salesService;
    @Inject private StockService stockService;
    @Inject private InventoryService inventoryService;
    @Inject private XtractService xtractService;
    @Inject private ReportManager reportManager;
        
    private SaleItem saleItem = new SaleItem();
    private List<SaleItem> saleItemList = new LinkedList<>();
    private List<Sales> salesList = new LinkedList<>();
    private SalesType selectedSalesType = SalesType.INSTANT_SALES;
    
    private List<Tax> taxList = new LinkedList<>();
    private List<SalesTax> salesTaxList = new LinkedList<>();
    private List<Customer> customerList = new LinkedList<>();
    
    private Payment payment = new Payment();
    private List<Payment> paymentList = new LinkedList<>();
    private List<Payment> paymentItems = new LinkedList<>();
    List<SalesTaxDto> salesTaxDtoList = new LinkedList<>();
    
    private Sales sales = new Sales();
    private DateUtil dateRange = new DateUtil();
    
    private FormView pageView = FormView.listForm();
    private boolean enableTax,processSale;
     
    double totalAmount,totalSaleAmount,totalPayable = 0.0;
    private int qtyRem = 0;
    private Customer customer = null;
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private LocalDate dueDate;
    private String optionText,invoiceNote;
    
    @PostConstruct
    public void init()
    {
        clearAll();
        salesList = salesService.getSales(SaleSource.WHOLESALE);
        taxList = salesService.getTaxList();
        customerList = inventoryService.getCustomerList();
    }
    
    public void loadClient() {
        customerList = inventoryService.getCustomerList();
        Msg.info("Customer list updated");
    }
    
    public void addNote(Sales sales){
        this.sales = sales;
        Sales s = crudApi.find(Sales.class, sales.getId());
        if(s != null){
            invoiceNote = s.getNotes();
        }
    }
    
    public void saveNote(){
        sales = crudApi.find(Sales.class, sales.getId());
        if(sales != null){
            sales.setNotes(invoiceNote);
            crudApi.save(sales);
            Msg.info("Invoice note added successfully!");
        }
    }
    
    public void initNewSale(){
        enableTax = false;
        enableTax = appSession.getCompanyBranch().isEnableTax();
        System.out.println("enableTax: "+enableTax);
        clearAll();
        paymentItems = salesService.payments(sales);
        appSession.logEvent("Click New Sale", EventModule.SALES, "New Sale");
        pageView.restToCreateView();
    }
        public void addPayment(){
        if(payment.getAmountPaid() == 0.0){
            Msg.error("Please enter amount");
            return;
        }
//        if(paymentList.contains(payment)){
//            Msg.error("Cannot add same method twice");
//            return;
//        }
        paymentList.add(payment);
        paymentList = JUtils.addToList(paymentList, payment);
        clearPayment();
    }
    public void editPayment(Payment payment) {
        optionText = "Update";
        this.payment = payment;
    }

    public void deletePayment(Payment payment) {
        paymentList.remove(payment);
    }
    public void clearPayment() {
        payment = new Payment();
        JUtils.resetJsfUI();
    }
    public void initInvoiceSales()
    {
        clearAll();
        pageView.restToDetailView();
    }
    
    public void filterSales()
    {
      salesList = salesService.getSalesByDates(dateRange, SaleSource.WHOLESALE); 
      appSession.logEvent("Filter Sales", EventModule.SALES, "Filter Sales");
    }
    
    public void wholeSalePrice(){
        
//        saleItem.setUnitPrice(saleItem.getInventory().getWprice());
//        
//        qtyRem = saleItem.getInventory() != null && saleItem.getInventory().getQtyInShop() != null ? (int)saleItem.getInventory().getQtyInShop().doubleValue() : 0;
    }
     
    public void selectSalesType(){
//        this.selectedSalesType = sales.getSalesType();
        if(selectedSalesType != SalesType.INSTANT_SALES){
            customerList.remove(salesService.walkinCustomer());
            customerList.remove(salesService.backLogSupplier());
        }else{
            customerList = inventoryService.getCustomerList();
        }
    }
        
    public void reset(){
      salesList = new LinkedList<>();  
      dateRange = new DateUtil();
    }
    
    public void fetchPackagePrice(Inventory inventory){
        System.out.println("Item selected -- ");
//        Inventory inv = salesService.queryPackagePrice(inventory.getUnitMeasurement(), saleItem.getInventory().getStockReceiptItem());  
//        System.out.println("wholesale price => "+inv.getWprice());
//        saleItem.setUnitPrice(inv.getWprice());
//        saleItem.setInventory(inventory);
    }
    
    public void addSaleItem(){
        if (saleItem.getQuantity() <= 0) {
            Msg.error("Please enter quantity");
            return;
        }

        if (saleItem != null && saleItem.getUnitPrice() == null || saleItem.getUnitPrice() <= 0.0) {
            Msg.error("Please enter price");
            return;
        }
        
        if(qtyRem < 1){
            Msg.error("There is not enough quantity to sell");
            return;
        }

        if (saleItem != null) {

            double salesAmount = saleItem.getQuantity() * saleItem.getUnitPrice();

            saleItem.genCode();
            saleItem.setSubTotal(salesAmount);
            saleItem.setId(crudApi.genId());
            saleItemList.add(saleItem);
            saleItemList = JUtils.addToList(saleItemList, saleItem);

            Msg.info("One item added to cart");
        }
        clear();
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
        paymentItems = salesService.payments(sales);
        System.out.println("enableTax: "+enableTax);
        
        saleItemList = salesService.getSales(sales);
              
        for (SaleItem items : saleItemList) {
            totalAmount += (items.getQuantity() * items.getUnitPrice());
        }
        if (enableTax) {
            processSale = true;
            taxCalculation();
            System.out.println("taxCalculation.......");
        }
        if(totalPayable == 0.0) totalPayable = totalAmount;
    }
    
    public void viewAsInvoiceSale(Sales sales)
    {
        this.sales = sales;
        pageView.restToDetailView();
        clearAll();
        
        saleItemList = salesService.getSales(sales);
              
        for (SaleItem items : saleItemList) {
            totalAmount += (items.getQuantity() * items.getUnitPrice());
        }
        if (enableTax) {
            processSale = true;
            taxCalculation();
            System.out.println("taxCalculation.......");
        }
        if(totalPayable == 0.0) totalPayable = totalAmount;
    }
    public void calculateSale() {
        totalAmount = saleItemList.stream().mapToDouble(SaleItem::getSubTotal).sum();

        if (enableTax) {
            processSale = true;
            taxCalculation();
            System.out.println("taxCalculation.......");
        }else{
            totalPayable = totalAmount;
        }
    }
    public void saveAll(){
        StringBuilder sb = null;
        if (saleItemList.isEmpty()) {
            Msg.error("Cannot process an empty sale");
            return;
        }
//        if (sales.getSalesType() != SalesType.CREDIT_SALES && sales.getSalesType() != SalesType.PROFORMA_INVOICE_SALES) {
//            if (paymentList.isEmpty()) {
//                Msg.error("Please add payment info");
//                return;
//            }
//        }   
        totalAmount = saleItemList.stream().mapToDouble(SaleItem::getSubTotal).sum();
        double qtyBought = saleItemList.stream().mapToDouble(SaleItem::getQuantity).sum();
        double amtPaid = paymentList.stream().mapToDouble(Payment::getAmountPaid).sum();
//        if (sales.getSalesType() != SalesType.CREDIT_SALES && sales.getSalesType() != SalesType.PROFORMA_INVOICE_SALES) {
//            if(amtPaid < totalAmount){
//                Msg.error("Amount paid is less than total amount");
//                return;
//            }
//        }
        Sales catalogue = crudApi.find(Sales.class, sales.getId());
        if(catalogue != null){
            Msg.error("Please this transaction cannot be altered!");
            return;
        }
        try {
            if (sales.getCustomer() == null) {
                sales.setCustomer(salesService.walkinCustomer());
            }

            sales.genCode();
            sales.setPaymentStatus(PaymentStatus.PENDING);
//            sales.setSaleSource(SaleSource.WHOLESALE);
//            sales.setCompound(false);
//            sales.setPurchaseDate(LocalDateTime.now());
            sales.setTotalAmount(totalAmount);
            sales.genReceipt();
            sales.setUserAccount(appSession.getCurrentUser());
            sales.setCompanyBranch(appSession.getCompanyBranch());
            sales.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
            sales.setLastModifiedDate(LocalDateTime.now());
            sales.setQtyPurchased(qtyBought);

            if (crudApi.save(sales) != null) {
                for (SaleItem item : saleItemList) {
                    item.genCode();
                    item.setSales(sales);
                    item.setCompanyBranch(appSession.getCompanyBranch());
                    item.setUserAccount(appSession.getCurrentUser());
                    item.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : "");
                    item.setLastModifiedDate(LocalDateTime.now());
                    crudApi.save(item);

//                    if (sales.getSalesType() == SalesType.INSTANT_SALES) {
//                        if(item.getInventory().getUnitMeasurement() == null){
//                            sb = new StringBuilder();
//                            sb.append("Set units for products: ").append(item.getInventory().getProduct());
//                            break;
//                        }
//                        Inventory inventory = stockService.getProduct(item.getInventory().getProduct(), item.getInventory().getUnitMeasurement());
//                        double qtyInShop = inventory.getQtyInShop();
//                        double newQty = qtyInShop - item.getQuantity();
//                        inventory.setQtyInShop(newQty);

//                        System.out.println("Product: " + inventory.getProduct());
//                        System.out.println("Old qty: " + qtyInShop);
//                        System.out.println("New qty: " + newQty);
//                        System.out.println("............\n");
//                        crudApi.save(inventory);
//                        System.out.println("Qty updated: ");
//                    }
                }
            }
            if(sb != null){
                Msg.error(sb.toString());
                return;
            }
            salesList = JUtils.addToList(salesList, sales);
            
            System.out.println("Executing......");
            if (enableTax) {
                taxCalculation();
                System.out.println("taxCalculation.......");
            }
            savePayment(sales);
            Msg.info("Transaction saved successfully!");
            appSession.logEvent("Save Sales", EventModule.SALES, "Complete Sales");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void savePayment(Sales sales){
        paymentList.forEach(pay ->{
            pay.setSales(sales);
            pay.setPaymentSource(SaleSource.WHOLESALE);
            pay.genCode();
            crudApi.save(pay);
        });
        System.out.println("Payment save complete");
    }
    public void taxCalculation(){
        System.out.println("Executing taxCalculation......");
        System.out.println("taxList......"+taxList.size());
        salesTaxDtoList = new LinkedList<>();
        for (Tax tax : taxList){
            SalesTax salesTax = new SalesTax();

            double calc = processSale ? totalAmount : sales.getTotalAmount() * (tax.getTaxRate()/100);

            salesTax.genCode();
            salesTax.setTaxName(tax.getTaxName());
            salesTax.setTaxRate(tax.getTaxRate());
            salesTax.setTaxAmount(calc);
            salesTax.setReOrder(tax.getReOrder());
            salesTax.setUserAccount(appSession.getCurrentUser());
            salesTax.setCompanyBranch(appSession.getCompanyBranch());
            salesTax.setSales(sales);

            if(!processSale)
                crudApi.save(salesTax);
            else{
                SalesTaxDto dto = new SalesTaxDto();
                dto.setTaxName(tax.getTaxName());
                dto.setTaxRate(tax.getTaxRate());
                dto.setReOrder(tax.getReOrder());
                dto.setTaxAmount(calc);
                salesTaxDtoList.add(dto);
            }
        }
            
        System.out.println("Adding salesTax: "+salesTaxDtoList.size());    
        salesTaxList = salesService.getSalesTaxList(sales);
        
        calculateVat();
    }
    
    private void calculateVat(){
        if(!salesTaxList.isEmpty()){
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

                      if(!processSale)
                crudApi.save(salesVat);
            
        }else{
            System.out.println("Calculating vat...");
            SalesTaxDto nhil = salesTaxDtoList.get(0);
            SalesTaxDto covid19 = salesTaxDtoList.get(1);
            SalesTaxDto salesVat = salesTaxDtoList.get(2);
            double totalLevies = nhil.getTaxAmount()+covid19.getTaxAmount();
            double taxableValue = sales.getTotalAmount() + totalLevies;
            double vat = taxableValue*(salesVat.getTaxRate()/100);
            totalPayable = vat + taxableValue;
            System.out.println("totalPayable: "+totalPayable);
            salesVat.setTaxAmount(vat);
        }
        if(totalPayable == 0.0) totalPayable = totalAmount;
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
            ReportManager.param.put("logo", ReportFiles.LOGO);
            reportManager.createReport(salesReceiptList, ReportFiles.RECEIPT_FILE, ReportManager.param);

        }catch(Exception e)
        {
        }
    }
    
    public void generateInvoice(Sales sales){
        List<InvoiceDto> proformaInvoiceList = new LinkedList<>();
        
        saleItemList = salesService.getSales(sales);
        if(saleItemList.isEmpty()){
            Msg.error("Cannot process an empty invoice!");
            return;
        }
        InvoiceDto proformaInvoiceDto = xtractService.extractInvoice(saleItemList, sales);
        proformaInvoiceList.add(proformaInvoiceDto);
        
        ReportManager.param.put("logo", ReportFiles.LOGO);
        reportManager.createReport(proformaInvoiceList, ReportFiles.INVOICE, ReportManager.param); 
    }
    
    public void generateProInvoice(Sales sales){
        List<InvoiceDto> proformaInvoiceList = new LinkedList<>();
        saleItemList = salesService.getSales(sales);
        if(saleItemList.isEmpty()){
            Msg.error("Cannot process an empty invoice!");
            return;
        }
        InvoiceDto proformaInvoiceDto = xtractService.extractInvoice(saleItemList, sales);
        proformaInvoiceList.add(proformaInvoiceDto);
        
        ReportManager.param.put("logo", ReportFiles.LOGO);
        reportManager.createReport(proformaInvoiceList, ReportFiles.PROFORMA_INVOICE, ReportManager.param);
    }
    
    public void convertProformaToInvoice(Sales sales){
        
    }
    
    public void clear(){
        saleItem = new SaleItem();
        saleItem.genCode();
        JUtils.resetJsfUI();
    }
    
    public void clearAll(){
        optionText = "Save Changes";
        saleItem = new SaleItem();
        payment = new Payment();
        saleItemList = new LinkedList<>();
        paymentList = new LinkedList<>();
        paymentItems = new LinkedList<>();
        saleItem.genCode();
        totalAmount = 0.0;
        totalPayable = 0.0;
        sales.setCustomer(salesService.defaultCustomer(CustomerType.WALK_IN_CUSTOMER));
        saleItem.setUserAccount(appSession.getCurrentUser());
        saleItem.setCompanyBranch(appSession.getCompanyBranch());
        JUtils.resetJsfUI();
    }
    
    public void resetSales(){
        closePage();
        clearAll();
        pageView.restToCreateView();
    }
    
    public void closePage()
    {
       sales = new Sales();
//       sales.setSalesType(null);
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

    public DateUtil getDateRange()
    {
        return dateRange;
    }

    public void setDateRange(DateUtil dateRange)
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

    public boolean isEnableTax() {
        return enableTax;
    }

    public void setEnableTax(boolean enableTax) {
        this.enableTax = enableTax;
    }
     public List<Payment> getPaymentList() {
        return paymentList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getOptionText() {
        return optionText;
    }

    public List<Payment> getPaymentItems() {
        return paymentItems;
    }

    public boolean isProcessSale() {
        return processSale;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public String getInvoiceNote() {
        return invoiceNote;
    }

    public void setInvoiceNote(String invoiceNote) {
        this.invoiceNote = invoiceNote;
    }
    
}
