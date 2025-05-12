package com.tsm.entities.system;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author richardnarh
 */
@MappedSuperclass
public class CompanyRecord extends RefNo{
    public static final String _companyBranch = "companyBranch";
    public static final String _companyBranchId = CompanyBranch._id;
    @ManyToOne
    @JoinColumn(name = "company_branch", referencedColumnName = "id")
    private CompanyBranch companyBranch;

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }
    
}
