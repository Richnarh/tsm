/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm;

import com.dolphindoors.resource.Pager;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Pattern;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Pascal
 */
public class AppParam implements Pager.Pageable{
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
    @QueryParam("filter")
    private String filter;
    
    @QueryParam("pageSize")
    @DefaultValue("20")
    private int pageSize;
    
    @QueryParam("pageNo")
    @DefaultValue("1")
    private int pageNo;
    
    private boolean ignorePagination;

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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Pager getPager()
    {
        Pager pager = new Pager();
        pager.setPageNo(pageNo);
        pager.setPageSize(pageSize);
        
        return pager;
    }
    
    @Override
    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNo()
    {
        if(pageNo == 0)
        {
            pageNo = 1;
        }
        return pageNo;
    }
    
    @Override
    public int getStart() {
        return getPageNo() * pageSize;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public boolean isIgnorePagination()
    {
        return ignorePagination;
    }

    public void setIgnorePagination(boolean ignorePagination)
    {
        this.ignorePagination = ignorePagination;
    }

    @Override
    public String toString()
    {
        return "DefaultSearchParam { fromDate=" + fromDate + ", toDate=" + toDate + ", pageSize=" + pageSize + ", pageNo=" + pageNo + '}';
    }
}
