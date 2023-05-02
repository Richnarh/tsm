/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "product")
public class Product extends UserAccountRecord implements Serializable
{
    public static final String _productName = "productName";
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode = SystemUtils.generateShortCode();
        
    @Column(name = "reorder_level")
    private int reorderLevel;
        
    @JoinColumn(name = "product_type")
    @ManyToOne
    private ProductType productType;
    
    @JoinColumn(name = "packaging")
    @ManyToOne
    private Packaging Packaging;
    
    @Lob
    @Column(name = "description")
    private String description;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public ProductType getProductType()
    {
        return productType;
    }

    public void setProductType(ProductType productType)
    {
        this.productType = productType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getReorderLevel()
    {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel)
    {
        this.reorderLevel = reorderLevel;
    }

    public Packaging getPackaging() {
        return Packaging;
    }

    public void setPackaging(Packaging Packaging) {
        this.Packaging = Packaging;
    }

    @Override
    public String toString()
    {
        return productName;
    }
}
