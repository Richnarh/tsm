/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lestieshop.lookups;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richa
 */
public class LookupSetup
{
    private static final Logger log = LoggerFactory.getLogger(LookupSetup.class);
    
    public static <E extends Enum<E>> List<LookupItem> PrepareEnum(E[] eEnum)
    {
        List<LookupItem> dtoList = new LinkedList<>();
        for (int i = 0; i <= Arrays.asList(eEnum).size() - 1; i++)
        {
            LookupItem item = new LookupItem();
            item.setId(eEnum[i].name());
            item.setItemName(eEnum[i].toString());
            dtoList.add(item);
        }
        return dtoList;
    }
}
