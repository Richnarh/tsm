package com.khoders.tsm.admin;

import org.omnifaces.util.Faces;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
public class Pages{
    public static String index = Faces.getRequestBaseURL() + "admin/index.xhtml?faces-redirect=true";
    public static String login = Faces.getRequestBaseURL();
}
