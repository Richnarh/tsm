package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
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
@Table(name = "product_package")
public class ProductPackage extends UserAccountRecord implements Serializable
{
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @JoinColumn(name = "units_measurement", referencedColumnName = "id")
    @ManyToOne
    private UnitMeasurement unitMeasurement;
    
    @Column(name = "units_in_package")
    private double unitsInPackage;
    
    @Column(name = "package_price")
    private double packagePrice;
    
    @Column(name = "description")
    @Lob
    private String description;

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public UnitMeasurement getUnitMeasurement()
    {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement)
    {
        this.unitMeasurement = unitMeasurement;
    }

    public double getUnitsInPackage()
    {
        return unitsInPackage;
    }

    public void setUnitsInPackage(double unitsInPackage)
    {
        this.unitsInPackage = unitsInPackage;
    }

    public double getPackagePrice()
    {
        return packagePrice;
    }

    public void setPackagePrice(double packagePrice)
    {
        this.packagePrice = packagePrice;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return unitMeasurement != null ? unitMeasurement.getUnits() : new UnitMeasurement().toString();
    } 
}
