package com.tsm.entities.system;

import com.dolphindoors.resource.jpa.BaseModel;
import com.dolphindoors.resource.utilities.JUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author richard
 */
@MappedSuperclass
public class RefNo extends BaseModel implements Serializable
{
    public static final String _refNo = "refNo";
    @Column(name = "ref_no")
    private String refNo;

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
   
    public void genCode()
    {
        if (getRefNo() != null)
        {
            setRefNo(getRefNo());
        } else
        {
            setRefNo(JUtils.generateCode());
        }
    } 
}
