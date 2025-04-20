package com.tsm.entities;

import com.dolphindoors.resource.jpa.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richardnarh
 */
@Entity
@Table(name = "job_title")
public class JobTitle extends BaseModel implements Serializable{
    @Column(name = "title_name")
    private String titleName;
    
    @Column(name = "description")
    private String description;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
