/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspectime.service.defPlain;

import com.inspectime.application.client.gui.registration.RegistrationDialog;
import org.ujorm.extensions.Property;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading parameters
 * @author Ponec
 */
public interface ParamSystemService extends UjoMiddle<ParamSystemService> {

    /** Is the Kasvig in a DEBUG mode? */
    public static final Property<ParamSystemService, Boolean> debug = Property.newInstance("debug", false);
    /** Generate Demo Data for a new company? */
    public static final Property<ParamSystemService, Boolean> createCompanyDemoData = Property.newInstance("companyDemoData", true);
    /** Ujorm database configuration file (http) */
    public static final Property<ParamSystemService, String> dbConfig = Property.newInstance("dbConfig", "");
    /** Salt for a password encoding */
    public static final Property<ParamSystemService, String> salt = Property.newInstance("salt", "jws.salt.5697458");
    /** Http link to a page of the Inspectime Terms of Use */
    public static final Property<ParamSystemService, String> termsOfUseLink = Property.newInstance("termsOfUseLink", RegistrationDialog.DEFAULT_TERM_OF_USE_LINK);

    /** Database connection user */
    public static final Property<ParamSystemService, String> dbUser = Property.newInstance("dbUser", "inspectime");
    /** Database connection user */
    public static final Property<ParamSystemService, String> dbPassword = Property.newInstance("dbPassword", "inspectime");
    /** JDBC URL */
    public static final Property<ParamSystemService, String> jdbcUrl = Property.newInstance("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/");

}
