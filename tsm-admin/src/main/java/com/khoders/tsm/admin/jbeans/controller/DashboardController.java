package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.tsm.admin.services.StockService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.tsm.entities.Sales;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "dashboardController")
@SessionScoped
public class DashboardController implements Serializable
{
    @Inject
    private CrudApi crudApi;
    @Inject
    private StockService stockService;
    
    private DateRangeUtil dateRange = new DateRangeUtil();
    
    double monthlyTotalSum,weeklyTotalSum,dailyTotalSum=0.0;

    @PostConstruct
    public void init()
    {
       getDailySales();
       getTotalMonthlySales();
       getTotalWeeklySales();
    }
    private void getDailySales()
    {
        dateRange.setFromDate(LocalDate.now());
        dateRange.setToDate(LocalDate.now());
       
        dailyTotalSum  = stockService.getTotalSumPerDateRange(dateRange).stream().mapToDouble(Sales::getTotalAmount).sum();
        Msg.info("Amount updated");
    }
    
    private void getTotalMonthlySales(){
       int todayDateValue = LocalDate.now().getDayOfMonth() - 1;

       LocalDate fromDate = LocalDate.now().minusDays(todayDateValue);
       LocalDate todayDate = fromDate.plusDays(todayDateValue);
        
        dateRange.setFromDate(fromDate);
        dateRange.setToDate(todayDate);
       
        monthlyTotalSum  = stockService.getTotalSumPerDateRange(dateRange).stream().mapToDouble(Sales::getTotalAmount).sum();
        System.out.println("monthlyTotalSum: ");
    }

    private void getTotalWeeklySales()
    {
        int todayDateValue = LocalDate.now().getDayOfWeek().getValue();
        
        LocalDate fromDate = LocalDate.now().minusDays(todayDateValue);
        
        LocalDate todayDate = fromDate.plusDays(6);
        
        dateRange.setFromDate(fromDate);
        dateRange.setToDate(todayDate);
       
        weeklyTotalSum  = stockService.getTotalSumPerDateRange(dateRange).stream().mapToDouble(Sales::getTotalAmount).sum();
    }

    public double getMonthlyTotalSum()
    {
        return monthlyTotalSum;
    }

    public double getWeeklyTotalSum()
    {
        return weeklyTotalSum;
    }

    public double getDailyTotalSum()
    {
        return dailyTotalSum;
    }
}
