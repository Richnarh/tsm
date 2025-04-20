package com.tsm.entities;

import com.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richard
 */
@Entity
@Table(name = "unit_measurement")
public class UnitMeasurement extends UserAccountRecord implements Serializable
{
    public static final String _units = "units";
    @Column(name = "units")
    private String units;

    public String getUnits()
    {
        return units;
    }

    public void setUnits(String units)
    {
        this.units = units;
    }

    @Override
    public String toString()
    {
        return units;
    }
}
