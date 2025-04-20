/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.services;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.AppConfigDto;
import com.tsm.entities.AppConfig;
import com.tsm.mapper.ConfigMapper;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pascal
 */
@Stateless
public class AppConfigService {
    private static final Logger log = LoggerFactory.getLogger(AppConfigService.class);
    @Inject private CrudApi crudApi;
    @Inject private ConfigMapper mapper;
    @Inject private DefaultService ds;
    
    public AppConfigDto save(AppConfigDto dto){
        log.debug("creating config");
        AppConfigDto configDto = null;
        AppConfig appConfig = mapper.toEntity(dto);
        if(appConfig != null && crudApi.save(appConfig) != null){
            configDto = mapper.toDto(appConfig);
        }
        return configDto;
    }
    
    public boolean delete(String configId){
        AppConfig appConfig = crudApi.find(AppConfig.class, configId);
        return appConfig != null ? crudApi.delete(appConfig) : false;
    }

    public List<AppConfigDto> fetchAllConfigs() {
        log.debug("fetching all configs");
        List<AppConfig> appConfigs = crudApi.findAll(AppConfig.class);
        List<AppConfigDto> dtoList = new LinkedList<>();
        appConfigs.forEach(config -> {
            dtoList.add(mapper.toDto(config));
        });
        return dtoList;
    }

    public AppConfigDto findById(String configId) {
        AppConfig appConfig = crudApi.find(AppConfig.class, configId);
        return mapper.toDto(appConfig);
    }
    
    public AppConfigDto findByConfigName(String configName) {
        AppConfig appConfig = ds.getAppConfig(configName);
        return mapper.toDto(appConfig);
    }

    public AppConfigDto update(String configName, String configValue) {
        AppConfigDto configDto = null;
        AppConfig appConfig = ds.getAppConfig(configName);
        appConfig.setConfigValue(configValue);
        if(crudApi.save(appConfig) != null){
            configDto = mapper.toDto(appConfig);
        }
        return configDto;
    }
    
    public AppConfig getAppConfig(String configName) {
        return crudApi.getEm().createQuery("SELECT e FROM AppConfig e WHERE e.configName = :configName", AppConfig.class)
                .setParameter(AppConfig._configName, configName)
                .getResultStream().findFirst().orElse(null);
    }
}
