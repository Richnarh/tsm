package com.tsm.services;

import com.dolphindoors.resource.jpa.QueryBuilder;
import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.AppParam;
import com.tsm.dto.CreditPaymentDto;
import com.tsm.entities.CreditPayment;
import com.tsm.entities.Sales;
import com.tsm.mapper.SalesMapper;
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
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Inject private CrudApi crudApi;
    @Inject private SalesMapper mapper;
    @Inject private SmsService ss;
    
    public CreditPaymentDto save(CreditPaymentDto paymentDto, AppParam param){
        log.info("Saving credit payment");
        CreditPayment payment = mapper.toEntity(paymentDto, param);
        CreditPaymentDto dto = null;
        if(crudApi.save(payment) != null){
            dto = mapper.toDto(payment);
            Sales invoice = crudApi.find(Sales.class, paymentDto.getSalesId());
            List<String> numbers = new LinkedList<>();
            numbers.add(invoice.getCustomer().getPhone());
            String message = "Round No.: "+dto.getRound()+ " \n";
             message += "Ref: "+dto.getRefNo()+ " \n";
             message += "Amount Paid: GHS "+dto.getAmountPaid()+ " \n";
             if(dto.getAmountRemaining() == -0.0){
                 dto.setAmountRemaining(0.0);
             }
             message += "Amount Remaining: GHS "+String.format("%.2f", dto.getAmountRemaining())+ " \n";
             message += "Date: "+dto.getValueDate()+ " \n";
             
             System.out.println("message: "+ message);
             ss.sms(message, numbers);
        }
        return dto;
    }
    
    public List<CreditPaymentDto> getCreditPaymentsByInvoice(String salesId){
        Sales sales = QueryBuilder.forClass(crudApi,Sales.class)
                .where("id", salesId)
                .execute();
        
        List<CreditPayment> paymentList = QueryBuilder.forClass(crudApi,CreditPayment.class)
                .where(CreditPayment._sales, sales)
                .orderByDesc("round")
                .executeList();
        
        List<CreditPaymentDto> dtoList = new LinkedList<>();
        for (CreditPayment creditPayment : paymentList) {
            dtoList.add(mapper.toDto(creditPayment));
        }
        return dtoList;
    }

    public CreditPaymentDto findById(String creditPaymentId) {
        CreditPayment creditPayment = crudApi.find(CreditPayment.class, creditPaymentId);
        return mapper.toDto(creditPayment);
    }

    public boolean deleteCreditPayment(String creditPaymentId) {
        CreditPayment creditPayment = crudApi.find(CreditPayment.class, creditPaymentId);
        return creditPayment != null ? crudApi.delete(creditPayment) : false;
    }
    
    public List<CreditPaymentDto> findAllCreditPayments(){
        List<CreditPayment> paymentList = crudApi.findAll(CreditPayment.class);
        List<CreditPaymentDto> dtoList = new LinkedList<>();
        for (CreditPayment creditPayment : paymentList) {
            dtoList.add(mapper.toDto(creditPayment));
        }
        return dtoList;
    }
}
