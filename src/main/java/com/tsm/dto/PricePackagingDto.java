package com.tsm.dto;

/**
 *
 * @author richardnarh
 */
public class PricePackagingDto extends Base{
    private String packaging;
    private String packagingId;
    private Double sellingPrice;
    private String inventory;
    private String inventoryId;

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getPackagingId() {
        return packagingId;
    }

    public void setPackagingId(String packagingId) {
        this.packagingId = packagingId;
    }
    
    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }
    
}
