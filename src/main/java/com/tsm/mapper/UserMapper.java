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
import com.tsm.dto.UserDto;
import com.tsm.entities.Employee;
import com.tsm.entities.system.UserAccount;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pascal
 */
public class UserMapper {
    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);
    @Inject private CrudApi crudApi;
    
    public UserAccount toEntity(UserDto dto){
        UserAccount userAccount = new UserAccount();
        if(dto.getId() != null){
            userAccount.setId(dto.getId());
        }
        userAccount.setAccessLevel(dto.getAccessLevel());
        
        if(dto.getEmployeeId() == null){
            throw new DataNotFoundException("Employee Id is required");
        }
        Employee employee = crudApi.find(Employee.class, dto.getEmployeeId());
//        userAccount.setEmployee(employee);
//        userAccount.setEmail(employee.getEmail());
        userAccount.setPhoneNumber(dto.getPhoneNumber());
        return userAccount;
    } 
    
    public UserDto toDto(UserAccount userAccount){
        UserDto dto = new UserDto();
        if(userAccount.getId() == null) return null;
        dto.setId(userAccount.getId());
        dto.setAccessLevel(userAccount.getAccessLevel());
//        if(userAccount.getEmployee() != null){
//            dto.setEmployeeId(userAccount.getEmployee().getId());
//            if(userAccount.getEmployee().getSurname() == null && userAccount.getEmployee().getOtherName() != null){
//                dto.setEmployeeName(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getOtherName());
//            }else if(userAccount.getEmployee().getOtherName() == null && userAccount.getEmployee().getSurname() != null){
//                dto.setEmployeeName(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getSurname());
//            }else if(userAccount.getEmployee().getSurname() == null && userAccount.getEmployee().getOtherName() == null){
//                dto.setEmployeeName(userAccount.getEmployee().getFirstName());
//            }else{
//                dto.setEmployeeName(userAccount.getEmployee().getFirstName() +" "+userAccount.getEmployee().getSurname() +" "+userAccount.getEmployee().getOtherName());
//            }
//            dto.setPhoneNumber(userAccount.getEmployee().getPhoneNumber());
//            dto.setEmail(userAccount.getEmployee().getEmail());
//        }else{
//            dto.setEmployeeId(userAccount.getId());
//            dto.setEmployeeName(userAccount.getFullname());
//            dto.setPhoneNumber(userAccount.getPhoneNumber());
//            dto.setEmail(userAccount.getEmail());
//        }
        dto.setValueDate(DateUtil.parseLocalDateString(userAccount.getValueDate(), Pattern._ddMMyyyy));
        if(dto.getPhoneNumber() == null)
            dto.setPhoneNumber(userAccount.getPhoneNumber());
        if(dto.getEmail() == null)
//            dto.setEmail(userAccount.getEmail());
        return dto;
        return null;
    }
    
}
