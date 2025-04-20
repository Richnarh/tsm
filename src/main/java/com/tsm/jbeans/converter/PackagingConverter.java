package com.tsm.jbeans.converter;

import com.tsm.entities.PricePackage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.omnifaces.converter.SelectItemsConverter;

/**
 *
 * @author richa
 */
@FacesConverter(forClass = PricePackage.class)
public class PackagingConverter extends SelectItemsConverter
{
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return null;
        }
        return ((PricePackage) value).getId();
    } 
}
