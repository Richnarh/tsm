/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tsm;

import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.enums.Title;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.jpa.QueryBuilder;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.QryOperator;
import com.tsm.entities.Employee;
import com.tsm.entities.system.UserAccount;
import com.tsm.enums.Roles;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;


/**
 *
 * @author richardnarh
 */
@Singleton
@Startup
public class Init {
    @Inject private CrudApi crudApi;
    
    @PostConstruct
    public void init(){
        try{
            String defaultUser = "test@tsm.com";
            Employee emp = QueryBuilder.forClass(crudApi, Employee.class)
                .where(Employee._email, defaultUser)
                .execute();
            if(emp == null){
                start();
                emp = new Employee();
                emp.setEmail("test@tsm.com");
                emp.setFirstName("tsm");
                emp.setSalt(JUtils.generateSalt());
                emp.setStatus(Status.ACTIVE);
                emp.setTitle(Title.MR);
                crudApi.save(emp);
                
                String password = JUtils.hashPassword(defaultUser.toCharArray(), emp.getSalt());

                UserModel userModel = new UserModel();
                userModel.setEmailAddress(defaultUser);
                userModel.setPassword(defaultUser);

                UserAccount user = QueryBuilder.forClass(crudApi, UserAccount.class)
                    .where(UserAccount._employee, emp)
                    .where(UserAccount._password, password, QryOperator.AND)
                    .execute();

                if (user != null){
                    return;
                }

                user = new UserAccount();
                user.setRoles(Roles.SUPER_ADMINISTRATOR);
                user.genCode();
                user.setEmployee(emp);
                user.setPassword(password);

                crudApi.save(user);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void start(){
        System.out.println("******************************************");
        System.out.println("******************************************");

        System.out.println("application started at - " + LocalDateTime.now());
        System.out.println("****  Going to create default uesr *******");

        System.out.println("******************************************");
        System.out.println("******************************************");
    }
}
