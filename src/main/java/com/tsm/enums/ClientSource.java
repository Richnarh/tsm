/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.tsm.enums;

import com.dolphindoors.resource.utilities.MsgResolver;

/**
 *
 * @author richardnarh
 */
public enum ClientSource implements MsgResolver{
    WEBSITE("WEBSITE", "Website"),
    WALK_IN_CUSTOMER("WALK_IN_CUSTOMER", "Walk In Customer"),
    REFERRAL("REFERRAL", "Referral"),
    INTERNET("INTERNET", "Internet"),
    BILL_BOARD("BILL_BOARD", "Bill Board"),
    TV_RADIO("TV", "Tv"),
    RADIO("RADIO", "Radio"),
    WHATSAPP("WHATSAPP", "WhatsApp"),
    INSTAGRAM("INSTAGRAM", "Instagram"),
    FACEBOOK("FACEBOOK", "Facebook"),
    TELEGRAM("TICK_TOK", "TickTok");
    
    private final String code;
    private final String label;

    private ClientSource(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
