package com.tsm.mapper;

import com.dolphindoors.resource.exception.DataNotFoundException;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.AppParam;
import com.tsm.dto.CustomerDto;
import com.tsm.dto.SaleItemDto;
import com.tsm.dto.SalesDto;
import com.tsm.entities.Customer;
import com.tsm.entities.Inventory;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
import com.tsm.services.AppConfigService;
import com.tsm.services.AppService;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
public class SalesMapper {
    private static final Logger log = LoggerFactory.getLogger(SalesMapper.class);
    @Inject private CrudApi crudApi;
    @Inject private AppService as;
    
    public Sales toEntity(SalesDto dto, AppParam param){
        Sales sales = new Sales();
        if (dto.getId() != null){
            sales.setId(dto.getId());
        }else{
            sales.setSalesDate(LocalDateTime.now());
        }
        sales.setPaymentMethod(dto.getPaymentMethod());
        sales.setTotalAmount(dto.getTotalAmount());
        sales.setTotalPayable(dto.getTotalPayable());
        sales.setReceiptNumber(JUtils.generateReceipt());
        sales.setQtyPurchased(dto.getSaleItemList().stream().mapToDouble(SaleItemDto::getQuantity).sum());
        sales.genCode();
        sales.setCompanyBranch(as.getBranch(param.getCompanyBranchId()));
        return sales;
    }
    
    public SalesDto toDto(Sales sales){
        SalesDto dto = new SalesDto();
        if (sales.getId() == null)return null;
        dto.setId(sales.getId());
        dto.setCustomerId(sales.getCustomer().getId());
        dto.setCustomer(sales.getCustomer().getCustomerName() +" "+sales.getCustomer().getPhone());
        dto.setSalesDate(sales.getSalesDate());
        dto.setPaymentMethod(sales.getPaymentMethod());
        dto.setTotalAmount(sales.getTotalAmount());
        dto.setTotalPayable(sales.getTotalPayable());
        dto.setReceiptNumber(sales.getReceiptNumber());
        return dto;
    }
    
    public Customer toEntity(CustomerDto dto){
        Customer customer = new Customer();
        if (dto.getId() != null){
            customer.setId(dto.getId());
        }
        customer.setCustomerName(dto.getCustomerName());
        customer.setPhone(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());
        customer.setEmailAddress(dto.getEmailAddress());
        return customer;
    } 
    
    public CustomerDto toDto(Customer customer){
        CustomerDto dto = new CustomerDto();
        if (dto.getId() == null)return null;
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setPhoneNumber(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setEmailAddress(customer.getEmailAddress());
        return dto;
    } 
    
    public SaleItem toEntity(SaleItemDto dto){
        SaleItem saleItem = new SaleItem();
        if(dto.getId() != null){
            saleItem.setId(dto.getId());
        }
        if(dto.getInventoryId() == null){
            throw new DataNotFoundException("Inventory Id is required");
        }
        Inventory inventory = crudApi.find(Inventory.class, dto.getInventoryId());
        saleItem.setInventory(inventory);
        
        if(dto.getSalesId() == null){
            throw new DataNotFoundException("salesId is required");
        }
        Sales sales = crudApi.find(Sales.class, dto.getSalesId());
        saleItem.setSales(sales);
        saleItem.setUnitPrice(dto.getUnitPrice());
        saleItem.setQuantity(dto.getQuantity());
        saleItem.setSubTotal(dto.getQuantity() * dto.getUnitPrice());
        saleItem.genCode();
        
        int qtyRem = inventory.getQuantity() - dto.getQuantity();
        inventory.setQuantitySold(dto.getQuantity());
        inventory.setQuantity(qtyRem);
        crudApi.save(inventory);
        
        return saleItem;
    }
    
    public SaleItemDto toDto(SaleItem saleItem){
        SaleItemDto dto = new SaleItemDto();
        if(saleItem.getId() == null) return null;
        dto.setId(saleItem.getId());
        dto.setInventory(saleItem.getInventory().getProduct().getProductName());
        dto.setInventoryId(saleItem.getInventory().getId());
        dto.setQuantity(saleItem.getQuantity());
        dto.setSales(saleItem.getSales().getReceiptNumber());
        dto.setSalesId(saleItem.getSales().getId());
        dto.setSubTotal(saleItem.getSubTotal());
        dto.setUnitPrice(saleItem.getUnitPrice());
        return dto;
    }
    
    public List<SaleItem> toEntity(List<SaleItemDto> dtoList, Sales sales){
        List<SaleItem> saleItemList = new LinkedList<>();
        for (SaleItemDto dto : dtoList) {
            dto.setSalesId(sales.getId());
            saleItemList.add(toEntity(dto));
        }
        return saleItemList;
    }
    
    public List<SaleItemDto> toDto(List<SaleItem> saleItemList){
        List<SaleItemDto> dtoList = new LinkedList<>();
        for (SaleItem dto : saleItemList) {
            dtoList.add(toDto(dto));
        }
        return dtoList;
    }
}
