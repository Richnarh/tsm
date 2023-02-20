/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.enums.PaymentStatus;
import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.reports.ReportManager;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.CompoundSale;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.jbeans.ReportFiles;
import com.khoders.tsm.jbeans.dto.CashReceipt;
import com.khoders.tsm.services.SalesService;
import com.khoders.tsm.services.XtractService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "creditPaymentController")
@SessionScoped
public class CreditPaymentController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SalesService salesService;
    @Inject private ReportManager reportManager;
    @Inject private XtractService xtractService;
    
    private String optionText;
    
    private CreditPayment creditPayment = new CreditPayment();
    private List<CreditPayment> creditPaymentList = new LinkedList<>();
    private List<Sales> compoundSalesList = new LinkedList<>();
    private Customer selectedCustomer = null;
   
    private FormView pageView = FormView.listForm();
    private Sales selectedSale = null;
    private double totalAmount = 0.0;
    private PaymentStatus paymentStatus = null;
    
    @PostConstruct
    public void init(){
        clearCreditPayment();
    }
    
    public void initialiseDbbf(){
        if(selectedSale != null)
            compoundSalesList = salesService.getCompoundSales(selectedSale.getCustomer());
        
        if(compoundSalesList.isEmpty())
            Msg.error("There are no records for Dept Balance Brought Forward");
    }
    
    public void saveDbbf(){
        if(compoundSalesList.isEmpty()){
            Msg.error("There are no previous pending/partially paid credit sales for ("+selectedSale.getCustomer()+")");
            return;
        }
        StringBuilder sb = new StringBuilder();
        CompoundSale cs = new CompoundSale();
        double csAmount = selectedSale.getTotalAmount();
        
        for (Sales sales : compoundSalesList) {
            if(sales.getCompoundSale() != null){
                continue;
            }
            String receiptCode = sales.getReceiptNumber().replaceAll(".*/", "");
            sb.append(receiptCode).append("/");
            csAmount += sales.getTotalAmount();
        }
        String receiptCode = selectedSale.getReceiptNumber().replaceAll(".*/", "");
        sb.append(receiptCode);
        System.out.println("receiptCode__....... "+receiptCode);
        CompoundSale cSale = salesService.getCompoundSale(selectedSale.getCustomer());
        if(cSale != null){
            StringBuilder builder = new StringBuilder();
            builder.append(cSale.getRefNo());
            builder.insert(cSale.getRefNo().length(), "/"+receiptCode);
            csAmount += cSale.getCompoundAmount();
            cSale.setCompoundAmount(csAmount);
            System.out.println("Ref No. (cSale) : "+builder.toString());
            cSale.setRefNo(builder.toString());
            if (crudApi.save(cSale) != null) {
                selectedSale.setCompound(true);
                selectedSale.setCompoundSale(cSale);
                crudApi.save(selectedSale);
            }
             Msg.info("DBBF Created Successfully!");
             return;
        }
        
        String[] str = SystemUtils.generateRefNo().split("/");
        sb.insert(0, str[0]+"/");
        System.out.println("Ref No: "+sb.toString());
        cs.setRefNo(sb.toString());
        cs.setCompoundAmount(csAmount);
        cs.setCustomer(selectedSale.getCustomer());
        cs.setPaymentStatus(PaymentStatus.PENDING);
        cs.setUserAccount(appSession.getCurrentUser());
        cs.setCompanyBranch(appSession.getCompanyBranch());
        
        if(crudApi.save(cs) != null){
            for (Sales sales : compoundSalesList) {
                sales.setCompoundSale(cs);
                crudApi.save(sales);
            }
            selectedSale.setCompound(true);
            selectedSale.setCompoundSale(cs);
            crudApi.save(selectedSale);
        }
        Msg.info("DBBF Created Successfully!");
    }
    
    public void initCreditPayment(){
        if(selectedSale == null){
            Msg.error("Please select sale");
            return;
        }
        clearCreditPayment();
        pageView.restToCreateView();
    }
    
   public void selectSale(){
       paymentStatus = null;
       totalAmount = 0.0;
       selectedSale = creditPayment.getSales();
       totalAmount = selectedSale != null ? selectedSale.getTotalAmount() : 0.0;
       creditPaymentList = salesService.getCreditSales(selectedSale);
       for (CreditPayment item : creditPaymentList) {
           if(item.getPaymentStatus().equals(PaymentStatus.FULLY_PAID)){
               paymentStatus = item.getPaymentStatus();
               break;
           }
       }
       if(selectedSale == null)
           paymentStatus = null;
   }

   public void saveCreditPayment()
    {
       try{
           initSales();
          if(crudApi.save(creditPayment) != null){
              creditPaymentList = CollectionList.washList(creditPaymentList, creditPayment);
              Msg.info(Msg.SUCCESS_MESSAGE);
              closePage();
          }else{
              Msg.error("Oops! failed to save creditPayment");
          }
           clearCreditPayment();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
   
    void initSales(){
        creditPayment.setSales(selectedSale);
        creditPayment.genCode();
        creditPayment.setValueDate(selectedSale.getValueDate());
        creditPayment.setTotalCredit(selectedSale.getTotalAmount());
        
        creditPayment.setCustomer(selectedSale.getCustomer());
        creditPayment.setDueDate(selectedSale.getValueDate());
        creditPayment.setDataSource("Credit sales with receipt # "+selectedSale.getReceiptNumber());
        creditPayment.setUserAccount(appSession.getCurrentUser());
        creditPayment.setCompanyBranch(appSession.getCompanyBranch());
        creditPayment.setLastModifiedBy(appSession.getCurrentUser().getFullname());
        
        List<CreditPayment> cpList = salesService.getCreditSales(selectedSale);
        double totalAmountPaid = cpList.stream().mapToDouble(CreditPayment::getAmountPaid).sum();
        System.out.println("cpList: "+cpList.size());
        System.out.println("totalAmountPaid: "+totalAmountPaid);
        System.out.println("selectedSale.getTotalAmount(): "+selectedSale.getTotalAmount());
        double amountRem = selectedSale.getTotalAmount() - totalAmountPaid;
        System.out.println("amountRem: "+amountRem);
        if(amountRem > 0.0){
            creditPayment.setCreditRemaining(amountRem - creditPayment.getAmountPaid());
            if (creditPayment.getCreditRemaining() == 0.0) {
                creditPayment.setPaymentStatus(PaymentStatus.FULLY_PAID);
            } else {
                creditPayment.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
            }
        }else{
            creditPayment.setCreditRemaining(amountRem);
            creditPayment.setPaymentStatus(PaymentStatus.FULLY_PAID);
        }
    }  
    
    public void printReceipt(CreditPayment creditPayment)
    {
        try
        {
            List<CashReceipt> cashReceiptList = new LinkedList<>();

            CashReceipt cashReceipt = xtractService.extractCashReceipt(creditPayment);

            cashReceiptList.add(cashReceipt);
            ReportManager.reportParams.put("logo", ReportFiles.LOGO);
            reportManager.createReport(cashReceiptList, ReportFiles.CASH_RECEIPT_FILE, ReportManager.reportParams);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void editCreditPayment(CreditPayment creditPayment)
    {
       this.creditPayment=creditPayment;
       selectedSale = creditPayment.getSales();
       optionText = "Update";
       pageView.restToCreateView();
    }
    
    public void deleteCreditPayment(CreditPayment creditPayment)
    {
        try
        {
          if(crudApi.delete(creditPayment))
          {
              creditPaymentList.remove(creditPayment);
              Msg.info(Msg.DELETE_MESSAGE);
          }
          else
          {
            Msg.error(Msg.FAILED_MESSAGE);
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void closePage()
    {
       creditPayment = new CreditPayment();
       compoundSalesList = new LinkedList<>();
       selectedSale = null;
       totalAmount = 0.0;
       optionText = "Save Changes";
       pageView.restToListView();
    }
        
    public void clearCreditPayment() {
        creditPayment = new CreditPayment();
        creditPayment.setUserAccount(appSession.getCurrentUser());
        creditPayment.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public CreditPayment getCreditPayment()
    {
        return creditPayment;
    }

    public void setCreditPayment(CreditPayment creditPayment)
    {
        this.creditPayment = creditPayment;
    }

    public List<CreditPayment> getCreditPaymentList()
    {
        return creditPaymentList;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }
    
    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }

    public Sales getSelectedSale() {
        return selectedSale;
    }

    public void setSelectedSale(Sales selectedSale) {
        this.selectedSale = selectedSale;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Sales> getCompoundSalesList() {
        return compoundSalesList;
    }
    
}
