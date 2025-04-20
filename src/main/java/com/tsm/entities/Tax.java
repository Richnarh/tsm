/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.jpa.BaseModel;
import com.dolphindoors.resource.utilities.JUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.SystemUtils;

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
  private Double taxRate;
  
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

    public Double getTaxRate()
    {
        return taxRate;
    }

    public void setTaxRate(Double taxRate)
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
            setTaxId(JUtils.generateCode());
        }
    }
        
    @Override
    public String toString()
    {
        return taxName +" - "+taxRate;
    }
}
