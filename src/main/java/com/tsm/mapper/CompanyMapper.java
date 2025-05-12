package com.tsm.mapper;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.CompanyBranchDto;
import com.tsm.entities.system.CompanyBranch;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
public class CompanyMapper {
    private static final Logger log = LoggerFactory.getLogger(CompanyMapper.class);
    @Inject private CrudApi crudApi;
    
    public CompanyBranch toEntity(CompanyBranchDto dto){
        CompanyBranch company = new CompanyBranch();
        if (dto.getId() != null){
            company.setId(dto.getId());
        }
        company.setBranchName(dto.getBranchName());
        company.setBranchAddress(dto.getBranchAddress());
        company.setGpsAddress(dto.getGpsAddress());
        company.setTelephoneNo(dto.getTelephoneNo());
        return company;
    }
    
    public CompanyBranchDto toDto(CompanyBranch cb){
        CompanyBranchDto dto = new CompanyBranchDto();
        if (cb.getId() == null) return null;
        dto.setId(cb.getId());
        dto.setBranchName(cb.getBranchName());
        dto.setBranchAddress(cb.getBranchAddress());
        dto.setGpsAddress(cb.getGpsAddress());
        dto.setTelephoneNo(cb.getTelephoneNo());
        return dto;
    }
}
