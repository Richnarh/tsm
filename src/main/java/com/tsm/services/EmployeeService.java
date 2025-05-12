/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.services;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.EmployeeDto;
import com.tsm.dto.JobTitleDto;
import com.tsm.dto.UserDto;
import com.tsm.entities.Employee;
import com.tsm.entities.JobTitle;
import com.tsm.entities.system.UserAccount;
import com.tsm.mapper.EmployeeMapper;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Narh
 */
@Stateless
public class EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Inject private CrudApi crudApi;
    @Inject private EmployeeMapper mapper;
    @Inject private DefaultService ds;

    public EmployeeDto save(EmployeeDto dto) {
        EmployeeDto empDto = null;
        Employee employee = mapper.toEntity(dto);
        if(employee != null){
         Employee emp = crudApi.save(employee);
         if(emp != null){
            empDto = mapper.toDto(employee);
         }
        }
        return empDto;
    }

    public UserDto updateUser(UserDto dto) {
        UserDto userDto = null;
        UserAccount user = mapper.toEntity(dto);
        if(user != null){
         UserAccount ua = crudApi.save(user);
         if(ua != null){
            userDto = mapper.toDto(ua);
         }
        }
        return userDto;
    }

    public EmployeeDto findById(String employeeId) {
        Employee employee = crudApi.find(Employee.class, employeeId);
        return mapper.toDto(employee);
    }
    public UserDto findUser(String employeeId) {
        Employee employee = crudApi.find(Employee.class, employeeId);
        UserAccount user = getUser(employee);
        if(user != null) return mapper.toDto(user);
        return null;
    }
    
    public List<EmployeeDto> fetchEmployees() {
        List<Employee> employees = crudApi.findAll(Employee.class);
        List<EmployeeDto> dtoList = new LinkedList<>();
        employees.forEach(employee -> {
            EmployeeDto dto = mapper.toDto(employee);
            UserAccount user = getUser(employee);
            dto.setIsAccountCreated(user != null);
            dtoList.add(dto);
        });
        return dtoList;
    }
    
    public boolean deleteEmployee(String employeeId) {
        Employee employee = crudApi.find(Employee.class, employeeId);
        return employee != null && crudApi.delete(employee);
    }
    
    public Employee getEmployee(String email){
        return crudApi.getEm().createQuery("SELECT e FROM Employee e WHERE e.email =:email", Employee.class)
                .setParameter(Employee._email, email)
                .getResultStream()
                .findFirst().orElse(null);
    }
    
    public UserAccount getUser(Employee employee){
        return crudApi.getEm().createQuery("SELECT e FROM UserAccount e WHERE e.employee =:employee", UserAccount.class)
                .setParameter(UserAccount._employee, employee)
                .getResultStream()
                .findFirst().orElse(null);
    }

    public JobTitleDto save(JobTitleDto dto) {
        JobTitle jobTitle = crudApi.save(mapper.toEntity(dto));
         return mapper.toDto(jobTitle);
    }

    public JobTitleDto findJobTitleById(String jobTitleId) {
       JobTitle jobTitle = crudApi.find(JobTitle.class, jobTitleId);
        return mapper.toDto(jobTitle);
    }

    public List<JobTitleDto> fetchJobTitles() {
        List<JobTitle> userAccounts = crudApi.findAll(JobTitle.class);
        List<JobTitleDto> dtoList = new LinkedList<>();
        userAccounts.forEach(user -> {
            dtoList.add(mapper.toDto(user));
        });
        return dtoList;
    }
    
    public boolean deleteTitle(String jobTitleId){
        JobTitle jobTitle = crudApi.find(JobTitle.class, jobTitleId);
        return jobTitle != null ? crudApi.delete(jobTitle) : false;
    }
}
