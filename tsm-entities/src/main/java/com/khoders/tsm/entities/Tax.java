/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.resource.jpa.BaseModel;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "tax")
public class Tax extends BaseModel implements Serializable
{
  @Column(name = "tax_id")
  private String taxId;
  
  @Column(name = "tax_name")
  private String taxName;
  
  @Column(name = "tax_rate")
  private double taxRate;
  
  @Column(name = "reorder")
  private int reOrder;

    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    public String getTaxName()
    {
        return taxName;
    }

    public void setTaxName(String taxName)
    {
        this.taxName = taxName;
    }

    public double getTaxRate()
    {
        return taxRate;
    }

    public void setTaxRate(double taxRate)
    {
        this.taxRate = taxRate;
    }

    public int getReOrder()
    {
        return reOrder;
    }

    public void setReOrder(int reOrder)
    {
        this.reOrder = reOrder;
    }

    public void genCode()
    {
        if (getTaxId() != null)
        {
            setTaxId(getTaxId());
        } else
        {
            setTaxId(SystemUtils.generateCode());
        }
    }
        
    @Override
    public String toString()
    {
        return taxName +" - "+taxRate;
    }
}
