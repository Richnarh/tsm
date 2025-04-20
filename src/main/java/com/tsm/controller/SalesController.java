package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Msg;
import com.dolphindoors.resource.utilities.Pattern;
import com.tsm.ApiEndpoint;
import com.tsm.AppParam;
import com.tsm.dto.ReceiptHeader;
import com.tsm.dto.SaleItemDto;
import com.tsm.dto.SalesDto;
import com.tsm.services.AppConfigService;
import com.tsm.services.SalesService;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author richardnarh
 */
@Path(ApiEndpoint.SALES_ENDPOINT)
public class SalesController {
    @Inject private SalesService salesService;
    @Inject private AppConfigService acs;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(SalesDto salesDto){
        SalesDto dto = salesService.saveAll(salesDto);
        if(dto != null)
            return JaxResponse.created(Msg.CREATED, dto);
        return JaxResponse.error(Msg.FAILED, "Could not save sales");
    }
    
    @GET
    @Path("/{salesId}/receipt")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateReceipt(@PathParam("salesId") String salesId){
        byte[] reportByte = salesService.generateReceipt(salesId);
        return JaxResponse.ok(Msg.RECORD_FOUND, Base64.getEncoder().encodeToString(reportByte));
    }
    @GET
    @Path("/receipt-header")
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiptParams(){
        ReceiptHeader receipt = new ReceiptHeader();
        receipt.setSalesDate(DateUtil.localDateTimeToString(LocalDateTime.now(), Pattern.ddMMyyyyhma));
//        receipt.setReceiptNumber(JUtils.generateReceipt(4));
//        receipt.setBusinessAddress(acs.getAppConfig("business.address").getConfigValue());
//        receipt.setPhoneNumber(acs.getAppConfig("business.phone").getConfigValue());
        return JaxResponse.ok(Msg.RECORD_FOUND, receipt);
    }
    
    @GET
    @Path("/search-sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByDate(@BeanParam AppParam param){
        List<SalesDto> invoices = salesService.searchByDate(param);
        return JaxResponse.ok(invoices);
    }
    
        
    @GET
    @Path("/{salesId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchDetails(@PathParam("salesId") String salesId){
        List<SaleItemDto> itemDtos = salesService.salesDetails(salesId);
        return JaxResponse.ok(itemDtos);
    }
}
