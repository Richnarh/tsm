/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.mapper;

import com.dolphindoors.resource.exception.DataNotFoundException;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.DateUtil;
import com.dolphindoors.resource.utilities.Pattern;
import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.dto.EmployeeDto;
import com.tsm.dto.JobTitleDto;
import com.tsm.dto.UserDto;
import com.tsm.entities.Employee;
import com.tsm.entities.JobTitle;
import com.tsm.entities.system.CompanyBranch;
import com.tsm.entities.system.UserAccount;
import java.time.LocalDate;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Narh
 */
public class EmployeeMapper {
    private static final Logger log = LoggerFactory.getLogger(EmployeeMapper.class);
    @Inject private CrudApi crudApi;
    
    public Employee toEntity(EmployeeDto dto){
        Employee employee = new Employee();
        if (dto.getId() != null){
            employee.setId(dto.getId());
            Employee em = crudApi.find(Employee.class, dto.getId());
            employee.setSalt(em.getSalt());
        }
        employee.setTitle(dto.getTitle());
        if(dto.getJobTitleId()== null){
            throw new DataNotFoundException("JobTitle Id is required");
        }
        if(dto.getCompanyBranchId() == null){
            throw new DataNotFoundException("CompanyBranchId is required");
        }
        JobTitle jobTitle = crudApi.find(JobTitle.class, dto.getJobTitleId());
        employee.setJobTitle(jobTitle);
        
        CompanyBranch branch = crudApi.find(CompanyBranch.class, dto.getCompanyBranchId());
        employee.setCompanyBranch(branch);
        employee.setEmail(dto.getEmail());
        employee.setFirstName(dto.getFirstName());
        employee.setSurname(dto.getSurname());
        employee.setOtherName(dto.getOtherName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setStatus(dto.getStatus());
        employee.setValueDate(LocalDate.now());
        employee.genCode();
        if(dto.getId() == null){
            employee.setSalt(JUtils.generateSalt());
        }
        return employee;
    }
    
    public EmployeeDto toDto(Employee employee){
        EmployeeDto dto = new EmployeeDto();
        if(employee.getId() == null) return null;
        dto.setId(employee.getId());
        dto.setTitle(employee.getTitle());
        if(employee.getJobTitle() != null){
            dto.setJobTitleId(employee.getJobTitle().getId());
            dto.setJobTitle(employee.getJobTitle().getTitleName());
        }
        if(employee.getCompanyBranch() != null){
            dto.setCompanyBranch(employee.getCompanyBranch().getBranchName());
            dto.setCompanyBranchId(employee.getCompanyBranch().getId());
        }
        dto.setEmail(employee.getEmail());
        dto.setFirstName(employee.getFirstName());
        dto.setSurname(employee.getSurname());
        dto.setOtherName(employee.getOtherName());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setStatus(employee.getStatus());
        dto.setValueDate(DateUtil.parseLocalDateString(employee.getValueDate(), Pattern.ddMMyyyy));
        if(employee.getSurname() == null && employee.getOtherName() != null){
            dto.setFullName(employee.getFirstName() +" "+employee.getOtherName());
        }else if(employee.getOtherName() == null && employee.getSurname() != null){
            dto.setFullName(employee.getFirstName() +" "+employee.getSurname());
        }else if(employee.getSurname() == null && employee.getOtherName() == null){
            dto.setFullName(employee.getFirstName());
        }else{
            dto.setFullName(employee.getFirstName() +" "+employee.getSurname() +" "+employee.getOtherName());
        }
        dto.setSelected(false);
        return dto;
    }

    public JobTitle toEntity(JobTitleDto dto){
        JobTitle jt = new JobTitle();
        if(dto.getId() != null){
            jt.setId(dto.getId());
        }
        jt.setTitleName(dto.getTitleName());
        jt.setDescription(dto.getDescription());
        return jt;
    }
    
    public JobTitleDto toDto(JobTitle jt){
        JobTitleDto dto = new JobTitleDto();
        if(jt.getId() == null) return null;
        dto.setId(jt.getId());
        dto.setTitleName(jt.getTitleName());
        dto.setDescription(jt.getDescription());
        return dto;
    }
    
    public UserAccount toEntity(UserDto dto){
        UserAccount userAccount = new UserAccount();
        if(dto.getId() != null){
            userAccount.setId(dto.getId());
        }
        
        if(dto.getEmployeeId() == null){
            throw new DataNotFoundException("Employee Id is required");
        }
        Employee employee = crudApi.find(Employee.class, dto.getEmployeeId());
        userAccount.setEmployee(employee);
        userAccount.setRoles(dto.getRoles());
        userAccount.genCode();
        userAccount.setPassword(JUtils.hashPassword(dto.getPassword().toCharArray(), employee.getSalt()));
        return userAccount;
    } 
    
    public UserDto toDto(UserAccount userAccount){
        UserDto dto = new UserDto();
        dto.setId(userAccount.getId());
        if(userAccount.getEmployee() != null){
            dto.setEmployeeId(userAccount.getEmployee().getId());
            dto.setCompanyId(userAccount.getEmployee().getCompanyBranch().getId());
            if(userAccount.getEmployee().getSurname() == null && userAccount.getEmployee().getOtherName() != null){
                dto.setEmployee(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getOtherName());
            }else if(userAccount.getEmployee().getOtherName() == null && userAccount.getEmployee().getSurname() != null){
                dto.setEmployee(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getSurname());
            }else if(userAccount.getEmployee().getSurname() == null && userAccount.getEmployee().getOtherName() == null){
                dto.setEmployee(userAccount.getEmployee().getFirstName());
            }else{
                dto.setEmployee(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getSurname() +" "+userAccount.getEmployee().getOtherName());
            }
        }
        dto.setRoles(userAccount.getRoles());
        dto.setValueDate(DateUtil.parseLocalDateString(userAccount.getValueDate(), Pattern._ddMMyyyy));
        return dto;
    }
}
