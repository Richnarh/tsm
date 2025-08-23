package com.tsm.services;

import com.dolphindoors.resource.jpa.QueryBuilder;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.dto.SmsParam;
import com.tsm.entities.AppConfig;
import java.util.List;
import javax.inject.Inject;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author richardnarh
 */
public class SmsService {
    @Inject private CrudApi crudApi;
    
     public String sms(String text, List<String> phoneNumbers){
        String msg = "Failed sending SMS";
          String sender = QueryBuilder.forClass(crudApi, AppConfig.class)
                  .where(AppConfig._configName, "sms.api.key")
                  .execute()
                  .getConfigValue();
          
          String apiKey = QueryBuilder.forClass(crudApi, AppConfig.class)
                  .where(AppConfig._configName, "sms.sender.id")
                  .execute()
                  .getConfigValue();
          
        String apiUrl = "https://api.smsonlinegh.com/v5/message/sms/send";
        
        SmsParam param = null;
        
        param = new SmsParam(text, 0, sender, phoneNumbers);
        
        String payload = JUtils.Json().toJson(param);
        
        RequestBody requestBody = RequestBody.create(payload, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        
        Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .addHeader("Host", "api.smsonlinegh.com")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .addHeader("Authorization", "key "+apiKey)
                    .build();
        
        try {
            Response response = JUtils.http().newCall(request).execute();
            if(response.isSuccessful()){
                String body = response.body().string();
                msg = "SMS sent successfully.";
            }else {
                msg = "Failed: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         System.out.println("msg: "+msg);
        return msg;
    }
}
