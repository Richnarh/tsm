/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm;

import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Pascal
 */
public class AppParam{
    @HeaderParam("userAccountId")
    private String userAccountId;
    @HeaderParam("companyId")
    private String companyBranchId;
    @QueryParam("paymentStatus")
    private String paymentStatus;
    @QueryParam("fromDate")
    private String fromDate;
    @QueryParam("toDate")
    private String toDate;
    @QueryParam("filterType")
    private String filterType;
    
    @QueryParam("pageSize")
    @DefaultValue("10")
    private int pageSize;
    
    @QueryParam("pageNo")
    @DefaultValue("1")
    private int pageNo;
    
    private boolean ignorePagination;

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getCompanyBranchId() {
        return companyBranchId;
    }

    public void setCompanyBranchId(String companyBranchId) {
        this.companyBranchId = companyBranchId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    public DateUtil getDateRange()
    {
        return new DateUtil(DateUtil.parseLocalDate(fromDate, Pattern._yyyyMMdd), DateUtil.parseLocalDate(toDate, Pattern._yyyyMMdd));
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }


    @Override
    public String toString()
    {
        return "DefaultSearchParam { fromDate=" + fromDate + ", toDate=" + toDate + ", pageSize=" + pageSize + ", pageNo=" + pageNo + '}';
    }
}
