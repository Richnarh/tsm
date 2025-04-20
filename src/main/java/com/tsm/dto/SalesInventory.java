package com.tsm.dto;

/**
 *
 * @author richardnarh
 * Note: Items for the sales page is well organized and structured in this payload.
 */
public class SalesInventory extends InventoryDto{
    private Integer purchasedQuantity;
    private String pricePackage;
    private String pricePackageId;
    private Double sellingPrice; 

    public Integer getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(Integer purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public String getPricePackage() {
        return pricePackage;
    }

    public void setPricePackage(String pricePackage) {
        this.pricePackage = pricePackage;
    }

    public String getPricePackageId() {
        return pricePackageId;
    }

    public void setPricePackageId(String pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
}
