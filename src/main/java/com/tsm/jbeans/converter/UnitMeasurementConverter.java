package com.tsm.jbeans.converter;

import com.tsm.entities.UnitMeasurement;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.omnifaces.converter.SelectItemsConverter;

/**
 *
 * @author richard
 */
@FacesConverter(forClass = UnitMeasurement.class)
public class UnitMeasurementConverter extends SelectItemsConverter
{
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return null;
        }
        return ((UnitMeasurement) value).getId();
    } 
}
