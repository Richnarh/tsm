/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.mapper;

import com.dolphindoors.resource.exception.DataNotFoundException;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Pattern;
import com.tsm.AppParam;
import com.tsm.dto.CustomerDto;
import com.tsm.dto.InventoryDto;
import com.tsm.dto.PackagingDto;
import com.tsm.dto.PricePackagingDto;
import com.tsm.dto.ProductDto;
import com.tsm.dto.ProductTypeDto;
import com.tsm.entities.Customer;
import com.tsm.entities.Inventory;
import com.tsm.entities.Packaging;
import com.tsm.entities.PricePackage;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pascal
 */
public class ProductMapper {
    private static final Logger log = LoggerFactory.getLogger(ProductMapper.class);
    @Inject private CrudApi crudApi;
    
    public Inventory toEntity(InventoryDto dto){
        log.info("mapping inventory dto to entity");
        Inventory inventory = new Inventory();
        if(dto.getId() != null){
            inventory.setId(dto.getId());
        }
        
        if(dto.getProductId() == null){
            throw new DataNotFoundException("ProductId is Required");
        }
        inventory.setDescription(dto.getDescription());

        inventory.setQuantity(dto.getQuantity());
        inventory.setProduct(crudApi.find(Product.class, dto.getProductId()));
        inventory.setQuantity(dto.getQuantity());
        return inventory;
    }
    
    public InventoryDto toDto(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        if(inventory.getId() == null)return null;
        dto.setId(inventory.getId());
        dto.setDescription(inventory.getDescription());
        dto.setInventoryCode(inventory.getInventoryCode().toUpperCase());
        dto.setQuantity(inventory.getQuantity());
        if(inventory.getProduct() != null){
            dto.setProduct(inventory.getProduct().getProductName());
            dto.setProductId(inventory.getProduct().getId());
            
            if(inventory.getProduct().getProductType() != null){
                dto.setProductType(inventory.getProduct().getProductType().getProductTypeName());
                dto.setProductTypeId(inventory.getProduct().getProductType().getId());
            }
        }
        dto.setValueDate(DateUtil.parseLocalDateString(inventory.getValueDate(), Pattern.ddMMyyyy));
        return dto;
    }
    
    public Product toEntity(ProductDto dto, AppParam param){
        log.info("mapping product dto to product entity");
        System.out.println("Creating entities");
        Product product = new Product();
        if(dto.getId() != null){
            product.setId(dto.getId());
        }
        if(dto.getProductCode() != null){
            product.setProductCode(dto.getProductCode());
        }
        
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        if(dto.getProductTypeId() != null){
            ProductType productType = crudApi.find(ProductType.class, dto.getProductTypeId());
            product.setProductType(productType);
        }
        
        if(dto.getReorderLevel() != null){
            product.setReorderLevel(dto.getReorderLevel());
        }
        
        return product;
    }
    
    public ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();
        if(product.getId() == null)return null;
        dto.setId(product.getId());
        dto.setProductCode(product.getProductCode().toUpperCase());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setProductType(product.getProductType() != null ? product.getProductType().getProductTypeName() : null);
        dto.setProductTypeId(product.getProductType() != null ? product.getProductType().getId() : null);
        dto.setReorderLevel(product.getReorderLevel());
        dto.setValueDate(DateUtil.parseLocalDateString(product.getValueDate(), Pattern.ddMMyyyy));
        return dto;
    }
    
    public ProductType toEntity(ProductTypeDto typeDto) {
        ProductType productType = new ProductType();
        if(typeDto.getId() != null){
            productType.setId(typeDto.getId());
        }
        productType.setProductTypeName(typeDto.getProductTypeName());
        return productType;
    }

    public ProductTypeDto toDto(ProductType productType) {
        ProductTypeDto dto = new ProductTypeDto();
        if(productType.getId() == null)return null;
        dto.setId(productType.getId());
        dto.setProductTypeName(productType.getProductTypeName());
        return dto;
    }
     
    public Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        if(dto.getId() != null){
            customer.setId(dto.getId());
        }
        customer.setAddress(dto.getAddress());
        customer.setClientSource(dto.getClientSource());
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmailAddress(dto.getEmailAddress());
        customer.setPhone(dto.getPhoneNumber());
        return customer;
    }

    public CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        if(customer.getId() == null)return null;
        dto.setId(customer.getId());
        if(dto.getId() != null){
            customer.setId(customer.getId());
        }
        dto.setAddress(customer.getAddress());
        dto.setClientSource(customer.getClientSource());
        dto.setCustomerName(customer.getCustomerName());
        dto.setEmailAddress(customer.getEmailAddress());
        dto.setPhoneNumber(customer.getPhone());
        dto.setCustomerCode(customer.getCustomerCode());
        return dto;
    }
    
    public PricePackage toEntity(PricePackagingDto dto){
        PricePackage packaging = new PricePackage();
        if(dto.getId() != null){
            packaging.setId(dto.getId());
        }
        if(dto.getInventoryId() == null){
            throw new RuntimeException("InventoryId is required");
        }
        if(dto.getPackagingId() == null){
            throw new RuntimeException("PackagingId is required");
        }
        Inventory inventory = crudApi.find(Inventory.class, dto.getInventoryId());
        Packaging packaging1 = crudApi.find(Packaging.class, dto.getPackagingId());
        packaging.setInventory(inventory);
        packaging.setPackaging(packaging1);
        packaging.setSellingPrice(dto.getSellingPrice());
        return packaging;
    }
    
    public PricePackagingDto toDto(PricePackage packaging){
        PricePackagingDto dto = new PricePackagingDto();
        if(packaging.getId() == null)return null;
        dto.setId(packaging.getId());
        dto.setInventory(packaging.getInventory().getProduct()+"");
        dto.setInventoryId(packaging.getInventory().getId());
        dto.setPackaging(packaging.getPackaging().getPackagingName());
        dto.setPackagingId(packaging.getPackaging().getId());
        dto.setSellingPrice(packaging.getSellingPrice());
        return dto;
    }
     
    public Packaging toEntity(PackagingDto dto){
        Packaging packaging = new Packaging();
        if(dto.getId() != null){
            packaging.setId(dto.getId());
        }
        packaging.setPackagingName(dto.getPackagingName());
        return packaging;
    }
    
    public PackagingDto toDto(Packaging packaging){
        PackagingDto dto = new PackagingDto();
        if(packaging.getId() == null)return null;
        dto.setId(packaging.getId());
        dto.setPackagingName(packaging.getPackagingName());
        return dto;
    }
}