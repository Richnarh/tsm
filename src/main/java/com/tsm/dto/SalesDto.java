package com.tsm.dto;

import com.dolphindoors.resource.enums.PaymentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author richardnarh
 */
public class SalesDto extends Base{
    private LocalDateTime salesDate;
    private String customer;
    private String customerId;
    private String receiptNumber;
    private Double totalAmount;
    private Double qtyPurchased;
    private LocalDate dueDate;
    private PaymentStatus paymentStatus;
    private String notes;
    private String address;
    
    private CustomerDto customerDto;
    private List<SaleItemDto> saleItemList = new LinkedList<>();

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }
    
    public List<SaleItemDto> getSaleItemList() {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItemDto> saleItemList) {
        this.saleItemList = saleItemList;
    }

    public LocalDateTime getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDateTime salesDate) {
        this.salesDate = salesDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getQtyPurchased() {
        return qtyPurchased;
    }

    public void setQtyPurchased(Double qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
