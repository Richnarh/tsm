package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.AppParam;
import com.tsm.dto.ProductDto;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import com.tsm.services.ProductService;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pascal
 */
@Path(ApiEndpoint.PRODUCT_ENDPOINT)
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Inject private CrudApi crudApi;
    @Inject private ProductService productService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ProductDto productDto, @BeanParam AppParam param){
        ProductDto dto = productService.save(productDto, param);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    
    @POST
    @Path("/upload")
    @Consumes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response upload(InputStream inputStream, @BeanParam AppParam param){
        List<ProductDto> dtoList = new LinkedList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.removeRow(sheet.getRow(0));
            for (Row row : sheet) {
                if(row.getCell(0) == null) continue;
                
                Integer reorderLevel = null;
                ProductType prodType = null;
                String productType = null;
                ProductDto dto = new ProductDto();
                
                String productName = row.getCell(0).getStringCellValue();
                if(row.getCell(1) != null){
                    productType = row.getCell(1).getStringCellValue();
                }
                if(row.getCell(2) != null){
                    reorderLevel = (int)row.getCell(2).getNumericCellValue();
                }
                if(row.getCell(3) != null){
                    dto.setInvenQty((int)row.getCell(3).getNumericCellValue());
                }
                
                if(productType != null){
                    prodType = productService.getProductType(productType);
                    if (prodType == null){
                        prodType = new ProductType();
                        prodType.genCode();
                        prodType.setProductTypeName(productType);
                        crudApi.save(prodType);
                    }
                    dto.setProductType(productType);
                }
                
                Product product = productService.getProduct(productName);
                
                if(product == null){
                    dto.setProductName(productName);
                    dto.setProductTypeId(prodType != null ? prodType.getId() : null);
                    dto.setReorderLevel(reorderLevel);
                    productService.save(dto, param);
                }else{
                    dto.setProductName(productName);
                }
                dtoList.add(dto);
            }
            
        } catch (IOException e) {
            e.getMessage();
        }
        productService.createInventory(dtoList);
        log.info(dtoList.size() +" upload successful...");
        return JaxResponse.ok(Msg.SUCCESS_MESSAGE);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(ProductDto productDto, @BeanParam AppParam param){
        ProductDto dto = productService.save(productDto, param);
        return JaxResponse.ok(Msg.UPDATED, dto);
    }
    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("productId") String productId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findProductById(productId));
    }
    
    @GET
    @Path("/product-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findAllProducts());
    }
    @DELETE
    @Path("/{productId}")
    public Response delete(@PathParam("productId") String productId){
        boolean delete = productService.delete(productId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete products",delete);
    }
}
