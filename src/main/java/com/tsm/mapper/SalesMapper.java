package com.tsm.mapper;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.CustomerDto;
import com.tsm.dto.SaleItemDto;
import com.tsm.dto.SalesDto;
import com.tsm.entities.Customer;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
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
    
    public Sales toEntity(SalesDto dto){
        Sales sales = new Sales();
//        if (dto.getId() != null){
//            sales.setId(dto.getId());
//        }
//        sales.setDiscountRate(dto.getDiscountRate());
//        sales.setModeOfPayment(dto.getModeOfPayment());
//        sales.setTotalAmount(dto.getTotalAmount());
//        sales.setTotalPayable(dto.getTotalPayable());
//        sales.setReceiptNo(dto.getReceiptNo());
        return sales;
    }
    
    public SalesDto toDto(Sales sales){
        SalesDto dto = new SalesDto();
        if (sales.getId() == null)return null;
//        dto.setId(sales.getId());
//        dto.setCustomerId(sales.getCustomer().getId());
//        dto.setCustomer(sales.getCustomer().getCustomerName() +" "+sales.getCustomer().getPhoneNumber());
//        dto.setDiscountRate(sales.getDiscountRate());
//        dto.setIssuedDate(DateUtil.localDateTimeToString(sales.getIssuedDate(), Pattern.ddMMyyyyhma));
//        dto.setModeOfPayment(sales.getModeOfPayment());
//        dto.setTotalAmount(sales.getTotalAmount());
//        dto.setTotalPayable(sales.getTotalPayable());
//        dto.setReceiptNo(sales.getReceiptNo());
        return dto;
    }
    
    public Customer toEntity(CustomerDto dto){
        Customer customer = new Customer();
        if (dto.getId() != null){
            customer.setId(dto.getId());
        }
        customer.setCustomerName(dto.getCustomerName());
//        customer.setClientSource(dto.getClientSource());
//        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAddress(dto.getAddress());
        customer.setEmailAddress(dto.getEmailAddress());
        return customer;
    } 
    
    public CustomerDto toDto(Customer customer){
        CustomerDto dto = new CustomerDto();
        if (dto.getId() == null)return null;
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
//        dto.setClientSource(customer.getClientSource());
//        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setEmailAddress(customer.getEmailAddress());
        return dto;
    } 
    
    public SaleItem toEntity(SaleItemDto dto){
        SaleItem saleItem = new SaleItem();
//        if(dto.getId() != null){
//            saleItem.setId(dto.getId());
//        }
//        saleItem.setDescription(dto.getDescription());
//        if(dto.getInventoryId() == null){
//            throw new DataNotFoundException("Inventory Id is required");
//        }
//        Inventory inventory = crudApi.find(Inventory.class, dto.getInventoryId());
//        saleItem.setInventory(inventory);
//        
//        if(dto.getSalesId() == null){
//            throw new DataNotFoundException("salesId is required");
//        }
//        Sales sales = crudApi.find(Sales.class, dto.getSalesId());
//        saleItem.setSales(sales);
//        saleItem.setQuantitySold(dto.getQuantitySold());
//        saleItem.setUnitPrice(dto.getUnitPrice());
//        saleItem.setSubTotal(dto.getQuantitySold()* dto.getUnitPrice());
//        
//        int qtyRem = inventory.getQuantity() - dto.getQuantitySold();
//        inventory.setQuantitySold(dto.getQuantitySold());
//        inventory.setQuantity(qtyRem);
//        crudApi.save(inventory);
        
        return saleItem;
    }
    
    public SaleItemDto toDto(SaleItem saleItem){
        SaleItemDto dto = new SaleItemDto();
        if(saleItem.getId() == null) return null;
//        dto.setId(saleItem.getId());
//        dto.setDescription(saleItem.getDescription());
//        dto.setInventory(saleItem.getInventory().getProduct().getProductName());
//        dto.setInventoryId(saleItem.getInventory().getId());
//        dto.setItemCode(saleItem.getItemCode());
//        dto.setQuantitySold(saleItem.getQuantitySold());
//        dto.setSales(saleItem.getSales().getReceiptNo());
//        dto.setSalesId(saleItem.getSales().getId());
        dto.setSubTotal(saleItem.getSubTotal());
        dto.setUnitPrice(saleItem.getUnitPrice());
        return dto;
    }
    
    public List<SaleItem> toEntity(List<SaleItemDto> dtoList, Sales sales){
        List<SaleItem> saleItemList = new LinkedList<>();
        for (SaleItemDto dto : dtoList) {
//            dto.setSalesId(sales.getId());
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
