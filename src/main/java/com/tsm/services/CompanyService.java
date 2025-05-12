package com.tsm.services;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.dto.CompanyBranchDto;
import com.tsm.entities.system.CompanyBranch;
import com.tsm.mapper.CompanyMapper;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richardnarh
 */
public class CompanyService {
    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Inject private CrudApi crudApi;
    @Inject private CompanyMapper mapper;
    @Inject private DefaultService ds;
    
    public CompanyBranchDto save(CompanyBranchDto dto) {
        CompanyBranchDto branchDto = null;
        CompanyBranch companyBranch = mapper.toEntity(dto);
        if(companyBranch != null){
         CompanyBranch branch = crudApi.save(companyBranch);
         if(branch != null){
            branchDto = mapper.toDto(companyBranch);
         }
        }
        return branchDto;
    }

    public CompanyBranchDto findById(String companyBranchId) {
        CompanyBranch companyBranch = crudApi.find(CompanyBranch.class, companyBranchId);
        return mapper.toDto(companyBranch);
    }

    public List<CompanyBranchDto> fetchCompanyBranches() {
        List<CompanyBranch> companyBranchs = crudApi.findAll(CompanyBranch.class);
        List<CompanyBranchDto> dtoList = new LinkedList<>();
        companyBranchs.forEach(companyBranch -> {
            CompanyBranchDto dto = mapper.toDto(companyBranch);
            dtoList.add(dto);
        });
        return dtoList;
    }
    
    public boolean deleteBranch(String companyBranchId) {
        CompanyBranch companyBranch = crudApi.find(CompanyBranch.class, companyBranchId);
        return companyBranch != null && crudApi.delete(companyBranch);
    }
    
}
