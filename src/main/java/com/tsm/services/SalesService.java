package com.tsm.services;

import com.dolphindoors.resource.enums.PaymentStatus;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.reports.ReportManager;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Pattern;
import com.tsm.AppParam;
import com.tsm.ReportFiles;
import com.tsm.dto.CustomerDto;
import com.tsm.dto.Receipt;
import com.tsm.dto.SaleItemDto;
import com.tsm.dto.SalesDto;
import com.tsm.entities.CompoundSale;
import com.tsm.entities.CreditPayment;
import com.tsm.entities.Customer;
import com.tsm.entities.DeliveryInfo;
import com.tsm.entities.Inventory;
import com.tsm.entities.Payment;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
import com.tsm.entities.SalesTax;
import com.tsm.entities.ShippingInfo;
import com.tsm.entities.StockReceiptItem;
import com.tsm.entities.Tax;
import com.tsm.entities.UnitMeasurement;
import com.tsm.enums.CustomerType;
import com.tsm.enums.SaleSource;
import com.tsm.listener.AppSession;
import com.tsm.mapper.SalesMapper;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
@Stateless
public class SalesService {
    private static final Logger log = LoggerFactory.getLogger(SalesService.class);
    @Inject private CrudApi crudApi;
    @Inject private SalesMapper mapper;
    @Inject private ReportManager reportManager;
    @Inject private XtractService xs;
    @Inject private AppSession appSession;
    
    public SalesDto saveAll(SalesDto salesDto) {
        log.info("Saving proforma sales");
        Customer customer = null;
        if(salesDto.getCustomerId() == null){
            customer = mapper.toEntity(salesDto.getCustomerDto());
            crudApi.save(customer);
        }else{
            customer = crudApi.find(Customer.class, salesDto.getCustomerId());
            if(salesDto.getAddress() != null){
                customer.setAddress(salesDto.getAddress());
                crudApi.save(customer);
            }
        }
        Sales sales = mapper.toEntity(salesDto);
        sales.setCustomer(customer);
        SalesDto dto = null;
        CustomerDto customerDto = null;
        if(crudApi.save(sales) != null){
            List<SaleItem> saleItemList = mapper.toEntity(salesDto.getSaleItemList(), sales);
            saleItemList.forEach(saleItem -> {
                crudApi.save(saleItem);
            });
            List<SaleItemDto> dtoList = mapper.toDto(saleItemList);
            customerDto = mapper.toDto(customer);
            dto = mapper.toDto(sales);
            dto.setSaleItemList(dtoList);
            dto.setCustomerDto(customerDto);
        }
        return dto;
    }
    
    public byte[] generateReceipt(String salesId){
        List<Receipt> receiptList = new LinkedList<>();
        Sales sales = crudApi.find(Sales.class, salesId);
        Receipt receipt = null;
//        Receipt receipt = xs.extractToReceipt(sales);

        receiptList.add(receipt);
        ReportManager.param.put("logo", ReportFiles.LOGO);
        return reportManager.createByteReport(receiptList, ReportFiles.RECEIPT_FILE, ReportManager.param);
    }
    
    public List<SaleItemDto> salesDetails(String salesId) {
        Sales sales = crudApi.findById(Sales.class, salesId);
        List<SaleItemDto> dtoList = new LinkedList<>();
        List<SaleItem> salesItemList = crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales =:sales", SaleItem.class)
                    .setParameter(SaleItem._sales, sales).getResultList();
        salesItemList.forEach(item ->{
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public List<SalesDto> searchByDate(AppParam param) { 
        LocalDate fromDate = DateUtil.parseLocalDate(param.getFromDate(), Pattern._yyyyMMdd);
        LocalDate toDate = DateUtil.parseLocalDate(param.getToDate(), Pattern._yyyyMMdd);
        DateUtil dateRange = new DateUtil(fromDate, toDate);
        
        List<Sales> salesList = new LinkedList<>();
        List<SalesDto> dtoList = new LinkedList<>();
        
        if(dateRange.getFromDate() == null && dateRange.getToDate() == null){
            salesList = crudApi.getEm().createQuery("SELECT e FROM Sales e ORDER BY e.issuedDate DESC", Sales.class).getResultList();
        }else if(dateRange.getFromDate() != null && dateRange.getToDate() == null){
            salesList = crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2 ORDER BY e.issuedDate DESC", Sales.class)
                .setParameter(1, dateRange.getFromDate())
                .setParameter(2, LocalDate.now())
                .getResultList();
        }else{
            salesList = crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2 ORDER BY e.issuedDate DESC", Sales.class)
                .setParameter(1, dateRange.getFromDate())
                .setParameter(2, dateRange.getToDate())
                .getResultList();
        }
        
        salesList.forEach(sale -> {
            dtoList.add(mapper.toDto(sale));
        });
        
        return dtoList;
    }
    
    
    public List<SalesTax> getSalesTaxList(Sales sales){
      return crudApi.getEm().createQuery("SELECT e FROM SalesTax e WHERE e.sales = :sales AND e.userAccount = :userAccount ORDER BY e.reOrder ASC", SalesTax.class)
              .setParameter(SalesTax._sales, sales)
              .setParameter(SalesTax._userAccount, appSession.getCurrentUser())
              .getResultList();
    }
    
    public List<Tax> getTaxList(){
       return crudApi.getEm().createQuery("SELECT e FROM Tax e ORDER BY e.reOrder ASC", Tax.class).getResultList();
    }
        
    public Customer walkinCustomer() {
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName = :customerName", Customer.class)
                .setParameter(Customer._customerName, CustomerType.WALK_IN_CUSTOMER.getLabel())
                .getResultStream().findFirst().orElse(null);
    }
    
    public Customer backLogSupplier() {
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName = :customerName", Customer.class)
                .setParameter(Customer._customerName, CustomerType.BACK_LOG_SUPPLIER.getLabel())
                .getResultStream().findFirst().orElse(null);
    }
    
    public List<SaleItem> getSales(Sales sales){
        return crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales AND e.companyBranch = :companyBranch", SaleItem.class)
                        .setParameter(SaleItem._sales, sales)
                        .setParameter(SaleItem._companyBranch, appSession.getCompanyBranch())
                        .getResultList();
    }
    public Sales getSale(String receiptNumber){
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.receiptNumber = :receiptNumber AND e.companyBranch = :companyBranch", Sales.class)
                        .setParameter(Sales._receiptNumber, receiptNumber)
                        .setParameter(SaleItem._companyBranch, appSession.getCompanyBranch())
                        .getResultStream().findFirst().orElse(null);
    }
    
    public List<Sales> getSales(SaleSource saleSource){
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN :valueDate AND :valueDate AND e.saleSource = :saleSource AND e.companyBranch = :companyBranch", Sales.class)
                   .setParameter(Sales._valueDate, LocalDate.now())
                   .setParameter(Sales._valueDate, LocalDate.now())
//                   .setParameter(Sales._saleSource, saleSource)
                   .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                   .getResultList();
    }
    
    public List<Sales> getSalesByDates(DateUtil dateRange, SaleSource saleSource){
        if(dateRange.getFromDate() == null || dateRange.getToDate() == null) {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.saleSource = :saleSource AND e.companyBranch = :companyBranch ORDER BY e.valueDate DESC", Sales.class)
//                    .setParameter(Sales._saleSource, saleSource)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
        }
            
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.saleSource = :saleSource AND e.valueDate BETWEEN :valueDate AND :valueDate AND e.companyBranch = :companyBranch ORDER BY e.valueDate DESC", Sales.class)
//                .setParameter(Sales._saleSource, saleSource)
                .setParameter(Sales._valueDate, dateRange.getFromDate())
                .setParameter(Sales._valueDate, dateRange.getToDate())
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public List<Inventory> queryPackagePrice(StockReceiptItem receiptItem){
         return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem =:stockReceiptItem AND e.companyBranch = :companyBranch", Inventory.class)
//                 .setParameter(Inventory._stockReceiptItem, receiptItem)
                 .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
                 .getResultList();
    }
    
    public Inventory queryPackagePrice(UnitMeasurement unitMeasurement, StockReceiptItem StockReceiptItem){
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.unitMeasurement = :unitMeasurement AND e.stockReceiptItem = :StockReceiptItem AND e.companyBranch = :companyBranch", Inventory.class)
//                .setParameter(Inventory._unitMeasurement, unitMeasurement)
//                .setParameter(Inventory._stockReceiptItem, StockReceiptItem)
                .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }

    public List<CreditPayment> getCreditSales(CompoundSale compoundSale) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.compoundSale = :compoundSale AND e.companyBranch = :companyBranch ORDER BY e.paymentDate DESC", CreditPayment.class)
                    .setParameter(CreditPayment._compoundSale, compoundSale)
                    .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }
    public List<CreditPayment> getCreditSales(Sales sales) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.sales = :sales AND e.companyBranch = :companyBranch ORDER BY e.paymentDate DESC", CreditPayment.class)
                .setParameter(CreditPayment._sales, sales)
                .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }

    public List<Sales> getCompoundSales(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.salesType = :salesType AND e.compound = :compound AND e.companyBranch = :companyBranch", Sales.class)
                .setParameter(Sales._customer, customer)
//                .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
//                .setParameter(Sales._compound, false)
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public CompoundSale getCompoundSale(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e WHERE e.customer = :customer AND e.paymentStatus <> :paymentStatus AND e.companyBranch = :companyBranch", CompoundSale.class)
                .setParameter(CompoundSale._customer, customer)
                .setParameter(CompoundSale._paymentStatus, PaymentStatus.FULLY_PAID)
                .setParameter(CompoundSale._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }

    public List<CompoundSale> getCompoundSales() {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e WHERE e.companyBranch = :companyBranch", CompoundSale.class)
                .setParameter(CompoundSale._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public Sales getCreditSales(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.salesType = :salesType AND e.compound = :compound AND e.companyBranch = :companyBranch", Sales.class)
                .setParameter(Sales._customer, customer)
//                .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
//                .setParameter(Sales._compound, false)
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }
    
    public List<Sales> getCustomerSales(Customer selectedCustomer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND  e.companyBranch = :companyBranch", Sales.class)
                    .setParameter(Sales._customer, selectedCustomer)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<CreditPayment> getCreditPayments(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.customer = :customer AND e.companyBranch = :companyBranch", CreditPayment.class)
                .setParameter(CreditPayment._customer, customer)
                .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public Customer defaultCustomer(CustomerType customerType){
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName= :customerName", Customer.class)
                .setParameter(Customer._customerName, customerType.getLabel())
                .getResultStream().findFirst().orElse(null);
    }

    public List<SaleItem> getDeliveries(String receiptNumber) {
        Sales sales = getSale(receiptNumber);
        return getSales(sales);
    }
    public List<DeliveryInfo> getWaybills(String receiptNumber) {
        return crudApi.getEm().createQuery("SELECT e FROM DeliveryInfo e WHERE e.receiptNumber = :receiptNumber", DeliveryInfo.class)
                .setParameter(DeliveryInfo._receiptNumber, receiptNumber)
                .getResultList();
    }

    public ShippingInfo getShippingInfo(String receiptNumber) {
        return crudApi.getEm().createQuery("SELECT e FROM ShippingInfo e WHERE e.receiptNumber= :receiptNumber", ShippingInfo.class)
                .setParameter(ShippingInfo._receiptNumber, receiptNumber)
                .getResultStream().findFirst().orElse(null);
    }

    public Integer getWp(String id) {
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.id =:id", Inventory.class)
                .setParameter(Inventory._id, id)
                .getResultStream().findFirst().orElse(null).getQuantity();
    }

    public List<Payment> payments(Sales sales) {
        if(sales == null) return new LinkedList<>();
        return crudApi.getEm().createQuery("SELECT e FROM Payment e WHERE e.sales = :sales", Payment.class)
                    .setParameter(Payment._sales, sales)
                    .getResultList();
    }
}
