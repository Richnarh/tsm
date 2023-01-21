package com.khoders.tsm.jbeans.converter;

import com.khoders.tsm.entities.StockReceiptItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.omnifaces.converter.SelectItemsConverter;

/**
 *
 * @author richa
 */
@FacesConverter(forClass = StockReceiptItem.class)
public class StockReceiptItemConverter extends SelectItemsConverter
{
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return null;
        }
        return ((StockReceiptItem) value).getId();
    } 
}
