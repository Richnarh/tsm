/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tsm.jbeans.commons;

import com.dolphindoors.resource.enums.ClientType;
import com.dolphindoors.resource.enums.DeliveryMethod;
import com.dolphindoors.resource.enums.DeliveryStatus;
import com.dolphindoors.resource.enums.PaymentMethod;
import com.dolphindoors.resource.enums.PaymentStatus;
import com.tsm.enums.InvoiceType;
import com.tsm.enums.TransferStatus;
import com.tsm.enums.LocType;
import com.tsm.enums.SalesType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "commonClass")
@SessionScoped
public class CommonClass implements Serializable
{
    public List<ClientType> getClientTypeList()
    {
        return Arrays.asList(ClientType.values());
    }
    public List<PaymentMethod> getPaymentMethodList()
    {
        return Arrays.asList(PaymentMethod.values());
    }
    public List<DeliveryMethod> getDeliveryMethodList()
    {
        return Arrays.asList(DeliveryMethod.values());
    }
    public List<InvoiceType> getInvoiceTypeList()
    {
        return Arrays.asList(InvoiceType.values());
    }
    public List<TransferStatus> getTransferStatusList()
    {
        return Arrays.asList(TransferStatus.values());
    }
    public List<SalesType> getSalesTypeList()
    {
        return Arrays.asList(SalesType.values());
    }
    public List<DeliveryStatus> getDeliveryStatusList()
    {
        return Arrays.asList(DeliveryStatus.values());
    }
    public List<PaymentStatus> getPaymentStatusList()
    {
        return Arrays.asList(PaymentStatus.values());
    }
    public List<LocType> getLocTypeList()
    {
        return Arrays.asList(LocType.values());
    }
}
