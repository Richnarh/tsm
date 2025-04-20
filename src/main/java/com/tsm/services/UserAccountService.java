/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.services;

import com.dolphindoors.resource.jpa.CrudApi;
import static com.dolphindoors.resource.utilities.JUtils.hashText;
import com.tsm.entities.system.AppPage;
import com.tsm.UserModel;
import com.tsm.entities.system.UserAccount;
import com.tsm.entities.system.UserPage;
import com.tsm.entities.system.UserPageAction;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author Khoders
 */
@Stateless
public class UserAccountService {

    @Inject
    private CrudApi crudApi;

    public UserAccount login(UserModel userModel) {

        try {
            String qryString = "SELECT e FROM UserAccount e WHERE e.emailAddress=?1 AND e.password=?2";
            TypedQuery<UserAccount> typedQuery = crudApi.getEm().createQuery(qryString, UserAccount.class)
                    .setParameter(1, userModel.getEmailAddress())
                    .setParameter(2, hashText(userModel.getPassword()));

            return typedQuery.getResultStream().findFirst().orElse(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isTaken(String email) {
        String qryString = "SELECT e FROM UserAccount e WHERE e.email_address=?1";
        try {
            UserAccount account = crudApi.getEm().createQuery(qryString, UserAccount.class)
                    .setParameter(1, email)
                    .getSingleResult();

            return account != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserPageAction> userPermissions(UserAccount userAccount) {
        return crudApi.getEm().createQuery("SELECT e FROM UserPageAction e WHERE e.userAccount=:userAccount", UserPageAction.class)
                .setParameter("userAccount", userAccount)
                .getResultList();
    }

    public List<UserPage> userPages(UserAccount userAccount) {
        return crudApi.getEm().createQuery("SELECT e FROM UserPage e WHERE e.userAccount=:userAccount ORDER BY e.appPage.reorder ASC", UserPage.class)
                .setParameter("userAccount", userAccount)
                .getResultList();
    }

    public List<AppPage> getAppPageList() {
        return crudApi.getEm().createQuery("SELECT e FROM AppPage e ORDER BY e.reorder ASC", AppPage.class).getResultList();
    }
}
