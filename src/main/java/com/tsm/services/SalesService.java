package com.tsm.services;

import com.dolphindoors.resource.Pager;
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
import com.tsm.entities.Customer;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
import com.tsm.mapper.SalesMapper;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
    
    public SalesDto saveAll(SalesDto salesDto, AppParam param) {
        log.info("Saving sales");
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
        Sales sales = mapper.toEntity(salesDto, param);
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
    
    public Pager getCreditSales(AppParam param){
        List<Sales> salesList = crudApi.findAll(Sales.class).stream().filter(in -> in.isCreditSale() == true).collect(Collectors.toList());
        List<SalesDto> dtoList = new LinkedList<>();
        
        salesList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        int totalRecords = totalRecords = dtoList.size();
        
        return new Pager(dtoList, param.getPageNo(), totalRecords);
    }
}
