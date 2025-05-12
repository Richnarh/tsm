package com.tsm.services;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.AppParam;
import com.tsm.dto.CustomerDto;
import com.tsm.dto.InventoryDto;
import com.tsm.dto.PackagingDto;
import com.tsm.dto.PricePackagingDto;
import com.tsm.dto.ProductDto;
import com.tsm.dto.ProductTypeDto;
import com.tsm.dto.SalesInventory;
import com.tsm.entities.Customer;
import com.tsm.entities.Inventory;
import com.tsm.entities.Packaging;
import com.tsm.entities.PricePackage;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import com.tsm.mapper.ProductMapper;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
@Stateless
public class ProductService {
private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Inject private CrudApi crudApi;
    @Inject private ProductMapper mapper;
    
    public ProductDto save(ProductDto productDto, AppParam param) {
        log.info("saving products");
        Product product = mapper.toEntity(productDto, param);
        ProductDto dto = null;
        if(crudApi.save(product) != null){
            dto = mapper.toDto(product);
        }
        return dto;
    }

    public ProductDto findProductById(String productId) {
        Product product = crudApi.find(Product.class, productId);
        return mapper.toDto(product);
    }

    public List<ProductDto> findAllProducts() {
        List<Product> productList = crudApi.findAll(Product.class);
        List<ProductDto> dtoList = new LinkedList<>();
        for (Product product : productList) {
            ProductDto dto = new ProductDto();
            dto = mapper.toDto(product);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean delete(String productId) {
        Product product = crudApi.find(Product.class, productId);
        return product != null ? crudApi.delete(product) : false;
    }
    
    public InventoryDto save(InventoryDto inventoryDto){
        log.debug("Saving inventory");
        InventoryDto dto = null;
        Inventory inventory = mapper.toEntity(inventoryDto);
        inventory.setLastModifiedDate(LocalDateTime.now());
        if(crudApi.save(inventory) != null){
            dto = mapper.toDto(inventory);
        }
        return dto;
    }
    
    public List<InventoryDto> findAll(){
        List<Inventory> inventoryList = crudApi.findAll(Inventory.class);
        List<InventoryDto> dtoList = new LinkedList<>();
        
        inventoryList.forEach(item -> {
            InventoryDto dto = mapper.toDto(item);
            dtoList.add(dto);
        });
        return dtoList;
    }
    
    public InventoryDto findById(String inventoryId){
        Inventory inventory = crudApi.findById(Inventory.class, inventoryId);
        return mapper.toDto(inventory);
    }
    
    public List<PricePackagingDto> loadPackages(String inventoryId){
        List<PricePackagingDto> dtoList = new LinkedList<>();
        Inventory inventory = crudApi.findById(Inventory.class, inventoryId);
        List<PricePackage> packages = crudApi.getEm().createQuery("SELECT e FROM PricePackage e WHERE e.inventory =:inventory", PricePackage.class)
                .setParameter(PricePackage._inventory, inventory)
                .getResultList();
        for (PricePackage pack : packages) {
            dtoList.add(mapper.toDto(pack));
        }
        return dtoList;
    }
    
    public boolean deleteInventory(String inventoryId){
        Inventory inventory = crudApi.findById(Inventory.class, inventoryId);
        return inventory != null ? crudApi.delete(inventory) : false;
    }
    
    
    // product type
    public ProductTypeDto save(ProductTypeDto typeDto) {
        log.debug("saving product type");
        ProductType productType = mapper.toEntity(typeDto);
        ProductTypeDto dto = null;
        if(crudApi.save(productType) != null){
            dto = mapper.toDto(productType);
        }
        return dto;
    }
    
    public List<ProductTypeDto> findAllProductTypes(){
        log.debug("Fetching all product types");
        List<ProductType> productTypeList = crudApi.findAll(ProductType.class);
        List<ProductTypeDto> dtoList = new LinkedList<>();
        productTypeList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public ProductTypeDto findByTypeId(String productTypeId) {
        ProductType productType = crudApi.find(ProductType.class, productTypeId);
        return mapper.toDto(productType);
    }
    public boolean deleteType(String productTypeId) {
        ProductType productType = crudApi.find(ProductType.class, productTypeId);
        return productType != null ? crudApi.delete(productType) : false;
    }
    
    
    // packaging
    public PackagingDto save(PackagingDto packaging) {
        log.debug("saving packaging");
        Packaging pd = mapper.toEntity(packaging);
        PackagingDto dto = null;
        if(crudApi.save(pd) != null){
            dto = mapper.toDto(pd);
        }
        return dto;
    }
    
    public List<PackagingDto> findAllPackages(){
        log.debug("Fetching all packages");
        List<Packaging> packagingList = crudApi.findAll(Packaging.class);
        List<PackagingDto> dtoList = new LinkedList<>();
        packagingList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public PricePackagingDto findPackageById(String packageById) {
        PricePackage packaging = crudApi.find(PricePackage.class, packageById);
        return mapper.toDto(packaging);
    }
    
    public boolean deletePackaging(String packageById) {
      Packaging  packaging = crudApi.find(Packaging.class, packageById);
      return packaging != null ? crudApi.delete(packaging) : false;
    }
    
    // price packaging
    public PricePackagingDto save(PricePackagingDto packaging) {
        log.debug("saving product type");
        PricePackage pd = mapper.toEntity(packaging);
        PricePackagingDto dto = null;
        if(crudApi.save(pd) != null){
            dto = mapper.toDto(pd);
        }
        return dto;
    }
    
    public List<PricePackagingDto> findAllPrices(){
        log.debug("Fetching all price packages");
        List<PricePackage> packagingList = crudApi.findAll(PricePackage.class);
        List<PricePackagingDto> dtoList = new LinkedList<>();
        packagingList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public PricePackagingDto findPricePackageById(String packageById) {
        PricePackage packaging = crudApi.find(PricePackage.class, packageById);
        return mapper.toDto(packaging);
    }
    public List<PricePackagingDto> loadPricePackageByInventory(String inventoryId) {
        Inventory inventory = crudApi.find(Inventory.class, inventoryId);
        
        List<PricePackage> packagingList = crudApi.getEm().createQuery("SELECT e FROM PricePackage e WHERE e.inventory=:inventory", PricePackage.class)
                .setParameter(PricePackage._inventory, inventory)
                .getResultList();
        
        List<PricePackagingDto> dtoList = new LinkedList<>();
        packagingList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public boolean deletePricePackaging(String packageById) {
        PricePackage  packaging = crudApi.find(PricePackage.class, packageById);
        return packaging != null ? crudApi.delete(packaging) : false;
    }
    
    public CustomerDto save(CustomerDto typeDto) {
        log.debug("saving customer");
        Customer customer = mapper.toEntity(typeDto);
        CustomerDto dto = null;
        if(crudApi.save(customer) != null){
            dto = mapper.toDto(customer);
        }
        return dto;
    }
    
    public List<CustomerDto> fetchAllCustomers(){
        log.debug("Fetching all customers");
        List<Customer> productTypeList = crudApi.findAll(Customer.class);
        List<CustomerDto> dtoList = new LinkedList<>();
        productTypeList.forEach(item -> {
            dtoList.add(mapper.toDto(item));
        });
        return dtoList;
    }
    
    public CustomerDto findCustomerById(String productTypeId) {
        Customer customer = crudApi.find(Customer.class, productTypeId);
        return mapper.toDto(customer);
    }
    
    public boolean deleteCustomer(String customerId) {
        Customer customer = crudApi.find(Customer.class, customerId);
        return customer != null ? crudApi.delete(customer) : false;
    }
    
    public List<SalesInventory> getSaleInventories(){
//        String queryString = "SELECT p.product_name, inv.quantity, pp.selling_price, inv.default_package, pack.packaging_name\n" +
//                            "FROM inventory inv \n" +
//                            "JOIN price_package pp ON inv.id = pp.inventory \n" +
//                            "JOIN product p ON inv.product = p.id \n" +
//                            "JOIN packaging pack ON pp.packaging = pack.id ";

    String queryString = "SELECT inv.product, p.product_name, inv.quantity, pp.selling_price, inv.default_package, pp.packaging \n" +
                            "FROM inventory inv \n" +
                            "JOIN price_package pp ON inv.id = pp.inventory \n" +
                            "JOIN product p ON inv.product = p.id";
        
        Query query = crudApi.getEm().createNativeQuery(queryString);
        
        List<SalesInventory> salesList = new LinkedList<>();
        List<Object[]> list = query.getResultList();
        System.out.println("list: "+list.size());

        for (Object[] row : list) {
            SalesInventory inventory = new SalesInventory();
            inventory.setProductId((String)row[0]);
            inventory.setProduct((String)row[1]);
            inventory.setQuantity((Integer)row[2]);
            inventory.setSellingPrice((Double)row[3]);
            salesList.add(inventory);
        }
        return salesList;
    }
    
    public ProductType getProductType(String prdtType){
       return crudApi.getEm().createQuery("SELECT e FROM ProductType e WHERE e.productTypeName = :productTypeName", ProductType.class)
                .setParameter(ProductType._productTypeName, prdtType)
                .getResultStream().findFirst().orElse(null);
    }
    
    public Product getProduct(String productName) {
        return crudApi.getEm().createQuery("SELECT e FROM Product e WHERE e.productName=:productName", Product.class)
                .setParameter(Product._productName, productName)
                .getResultStream().findFirst().orElse(null);
    }
    
    public void createInventory(List<ProductDto> dtoList){
        for (ProductDto dto : dtoList) {
            Inventory inventory = new Inventory();
            Product product = getProduct(dto.getProductName());
            inventory.setProduct(product);
            inventory.setQuantity(dto.getInvenQty());
            inventory.genCode();
            crudApi.save(inventory);
        }
    }
}
