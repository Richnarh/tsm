package com.tsm.dto;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author richardnarh
 */
public class SmsParam {
    String text;
    int type;
    String sender;
    List<String> destinations = new LinkedList<>();

    public SmsParam() {
    }

    public SmsParam(String text, int type, String sender, List<String> destinations) {
        this.text = text;
        this.type = type;
        this.sender = sender;
        this.destinations = destinations;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
