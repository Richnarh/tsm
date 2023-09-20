/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Packaging;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.dto.ProductDetails;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.tsm.services.StockService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.BeansUtil;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author richa
 */
@Named(value = "productUploadController")
@SessionScoped
public class ProductUploadController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private StockService stockService;
    @Inject private InventoryService inventoryService;
    private ProductDetails product = new ProductDetails();
    private List<ProductDetails> productList = new LinkedList<>();
    private List<Product> failedProductList = new LinkedList<>();
    
    private UploadedFile file = null;
    
    public String getFileExtension(String filename) {
    if(filename == null)
    {
        return null;
    }
    return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
    
    public void uploadProduct()
    {
        if (file.getSize() < 1)
        {
            Msg.error("No excel file is selected!");
            return;
        }

        try
        {
            String extension = getFileExtension(file.getFileName());
            System.out.println("type ==> " + extension);

            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            sheet.removeRow(sheet.getRow(0));
            Iterator<Row> iterator = sheet.iterator();
            System.out.println("Starting....");
            int c = 0;
            while (iterator.hasNext())
            {
                c++;
                product = new ProductDetails();
                Row currentRow = iterator.next();
                String prodName = BeansUtil.objToString(currentRow.getCell(0));
                if(prodName == null || prodName.isEmpty()){
                    Msg.error("Please the excel has  product name field(s) being empty!");
                    return;
                }
                product.setProductName(prodName);
                String object1 = BeansUtil.objToString(currentRow.getCell(1));
                if(object1 != null && !object1.isEmpty()){
                    product.setProductType(object1);
                }
                
                String object2 = BeansUtil.objToString(currentRow.getCell(2));
                if(object2 != null && !object2.isEmpty()){
                    product.setPackaging(object2);
                }
                
                productList.add(product);
                
                System.out.println("Iteration "+c+" done!");
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }
        
    public void saveUpload()
    {
        try
        {
            int c=0;
            if(productList.isEmpty()) return;
            for (ProductDetails details : productList)
            {
                ProductType productType = stockService.getProductType(details.getProductType() != null ? details.getProductType().trim() : null);
                if (productType == null)
                {
                    ProductType type = new ProductType();
                    type.genCode();
                    type.setProductTypeName(details.getProductType());
                    type.setUserAccount(appSession.getCurrentUser());
                    type.setCompanyBranch(appSession.getCompanyBranch());
                    type.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                
                    crudApi.save(type);
                }
                
                Packaging packages = stockService.getPackage(details.getPackaging());
                if (packages == null)
                {
                    Packaging pack = new Packaging();
                    pack.genCode();
                    pack.setPackagingName(details.getPackaging());
                    pack.setUserAccount(appSession.getCurrentUser());
                    pack.setCompanyBranch(appSession.getCompanyBranch());
                    pack.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
                
                    crudApi.save(pack);
                }
                
                Product prod = new Product();
                prod.setProductType(productType);
                prod.setPackaging(packages);
                prod.setProductName(details.getProductName().trim());
                Product oldProduct = stockService.getProduct(prod.getProductName());
                if(oldProduct == null){
                    Product newProduct = new Product();
                    newProduct.genCode();
                    newProduct.setProductName(details.getProductName().trim());
                    newProduct.setProductType(productType);
                    newProduct.setPackaging(packages);
                    newProduct.setUserAccount(appSession.getCurrentUser());
                    newProduct.setCompanyBranch(appSession.getCompanyBranch());
                    newProduct.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);  
                    System.out.println("New Saving........"+c++);
                    crudApi.save(newProduct);
                }   
                else{
                   failedProductList.add(oldProduct);
                }
                
            }
            System.out.println("productList -- "+productList.size());
            System.out.println("failedProductList -- "+failedProductList.size());
            failedProductList.forEach(i->System.out.println("Item: "+i.getProductName()));
            Msg.info("Product upload saved!");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void clear()
    {
        productList = new LinkedList<>();
        file = null;
        product = new ProductDetails();
        SystemUtils.resetJsfUI();
    }
    
    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public List<ProductDetails> getProductList()
    {
        return productList;
    }

}
