/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author Pascal
 */
public enum EventType implements MsgResolver{
   
    CREATE("CREATE", "Create"),
    UPDATE("UPDATE", "Update"),
    DELETE("DELETE", "Delete"),
    REFRESH("REFRESH", "Refresh"),
    CLICK_SEARCH("CLICK_SEARCH", "Click Search"),
    CLICK_UPLOAD("CLICK_UPLOAD", "Click Upload"),
    CLICK_EDIT("CLICK_EDIT", "Click Edit"),
    CLICK_OPEN("CLICK_OPEN", "Click Open"),
    PRINT("PRINT", "Print"),
    EXECUTE("EXECUTE", "Execute"),
    EXPORT_EXCEL("EXPORT_EXCEL", "Export Excel"),
    EXPORT_PDF("EXPORT_PDF", "Export PDF"),
    IMPORT_FILE("IMPORT_FILE", "Import File"),
    CLICK_CLEAR("CLICK_CLEAR", "Click Clear"),
    CLICK_CLOSE("CLICK_CLOSE", "Click Close"),
    RESET("RESET", "Reset"),
    DOWNLOAD("DOWNLOAD","Download");
    
   private final String code;
   private final String label;
   
   private EventType(String code, String label){
       this.code=code;
       this.label=label;
   }
   
    @Override
    public String getCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLabel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString()
    {
        return label;
    }
}
