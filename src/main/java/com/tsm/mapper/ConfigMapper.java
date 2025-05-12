package com.tsm.mapper;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.AppConfigDto;
import com.tsm.entities.AppConfig;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
public class ConfigMapper {
    private static final Logger log = LoggerFactory.getLogger(ConfigMapper.class);
    @Inject private CrudApi crudApi;
    
        
    public AppConfig toEntity(AppConfigDto dto) {
        AppConfig appConfig = new AppConfig();
        if (dto.getId() != null) {
            appConfig.setId(dto.getId());
        }
        appConfig.setConfigName(dto.getConfigName());
        appConfig.setConfigValue(dto.getConfigValue());
        appConfig.setConfigStatus(dto.getConfigStatus());
        return appConfig;
    }
    
    public AppConfigDto toDto(AppConfig appConfig) {
        AppConfigDto dto = new AppConfigDto();
        if (appConfig.getId() != null) {
            dto.setId(appConfig.getId());
        }
        dto.setId(appConfig.getId());
        dto.setConfigName(appConfig.getConfigName());
        dto.setConfigValue(appConfig.getConfigValue());
        dto.setConfigStatus(appConfig.getConfigStatus());
        return dto;
    }
    
}
