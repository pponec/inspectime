/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspectime.service.defPlain;

import com.inspectime.application.client.gui.registration.RegistrationDialog;
import org.ujorm.Key;
import org.ujorm.core.KeyFactory;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading parameters
 * @author Ponec
 */
public interface ParamSystemService extends UjoMiddle<ParamSystemService> {
    
    /** Key factory */
    public static final KeyFactory $f = KeyFactory.Builder.get(ParamSystemService.class);

    /** Is the Kasvig in a DEBUG mode? */
    public static final Key<ParamSystemService, Boolean> debug = $f.newKey("debug", false);
    /** Generate Demo Data for a new company? */
    public static final Key<ParamSystemService, Boolean> createCompanyDemoData = $f.newKey("companyDemoData", true);
    /** Ujorm database configuration file (http) */
    public static final Key<ParamSystemService, String> dbConfig = $f.newKey("dbConfig", "");
    /** Salt for a password encoding */
    public static final Key<ParamSystemService, String> salt = $f.newKey("salt", "jws.salt.5697458");
    /** Http link to a page of the Inspectime Terms of Use */
    public static final Key<ParamSystemService, String> termsOfUseLink = $f.newKey("termsOfUseLink", RegistrationDialog.DEFAULT_TERM_OF_USE_LINK);

    /** Database connection user */
    public static final Key<ParamSystemService, String> dbUser = $f.newKey("dbUser", "inspectime");
    /** Database connection user */
    public static final Key<ParamSystemService, String> dbPassword = $f.newKey("dbPassword", "inspectime");
    /** JDBC URL */
    public static final Key<ParamSystemService, String> jdbcUrl = $f.newKey("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/");
    
    /** Lock the Factory */
    public static final boolean $locked = $f.lockAndSize()>=0;

}
