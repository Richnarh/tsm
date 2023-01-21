/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "product_type")
public class ProductType extends UserAccountRecord implements Serializable
{
    @Column(name = "product_type")
    private String productTypeName;

    public String getProductTypeName()
    {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName)
    {
        this.productTypeName = productTypeName;
    }

    @Override
    public String toString()
    {
        return productTypeName;
    }
    
    
}
