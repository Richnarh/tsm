package com.tsm.entities;

import com.dolphindoors.resource.jpa.BaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richardnarh
 */
@Entity
@Table(name = "packaging")
public class Packaging extends BaseModel{
    public static final String _packagingName="packagingName";
    @Column(name = "packaging_name")
    private String packagingName;

    public String getPackagingName() {
        return packagingName;
    }

    public void setPackagingName(String packagingName) {
        this.packagingName = packagingName;
    }

    @Override
    public String toString() {
        return packagingName;
    }
    
}
