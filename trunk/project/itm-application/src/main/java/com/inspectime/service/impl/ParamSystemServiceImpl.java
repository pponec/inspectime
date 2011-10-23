/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.server.core.database.InspectimeDatabase;
import com.inspectime.service.defPlain.ParamSystemService;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ujorm.Ujo;
import org.ujorm.UjoProperty;
import org.ujorm.UjoPropertyList;
import org.ujorm.core.UjoManager;
import org.ujorm.core.UjoManagerRBundle;
import org.ujorm.implementation.map.MapUjo;

/**
 * Services for reading system parameters
 * @author Ponec
 */
@org.springframework.stereotype.Service("sysParamService")
public class ParamSystemServiceImpl extends MapUjo implements ParamSystemService {

    static final private Logger LOGGER = Logger.getLogger(ParamSystemServiceImpl.class.getName());
    static final private UjoPropertyList propertyList = UjoManager.getInstance().readProperties(ParamSystemService.class);
    static final private String DB_CONFIG_FILE = "dbconfig.xml";
    //
    private boolean lock = false;
    private String paramFileName;
    private String ujormFileName;


    public ParamSystemServiceImpl(String parameterFileName, String ujormFileName) {
        this.paramFileName = parameterFileName;
        this.ujormFileName = ujormFileName;
    }

    public ParamSystemServiceImpl() {
        String mainDir = System.getProperty("user.home")
                + "/.inspectime/"
                ;
        this.paramFileName = mainDir + "config.properties";
        this.ujormFileName = mainDir + DB_CONFIG_FILE;
    }

    /** Initialize parameters */
    @SuppressWarnings("unchecked")
    public void init() {

        File paramFile = new File(this.paramFileName);
        File ujormFile = new File(this.ujormFileName);

        try {
            if (paramFile.exists()) {
                LOGGER.log(Level.INFO, "The JWS configuration is loaded from the file: " + paramFile.getCanonicalPath());

                ParamSystemService ps = UjoManagerRBundle.getInstance(ParamSystemServiceImpl.class).loadResourceBundle(paramFile, false, this);
                for (UjoProperty p : propertyList) {
                    writeValue(p, p.of(ps));
                }
            } else {

                StringBuilder a = new StringBuilder();
                a.append("################################################################\n");
                a.append("# Uncoment line to set properties - default values are commented\n");
                a.append("# to restore default values - delete or rename this file and restart inspectime node\n\n");
                a.append("#dbConfig=~/.inspectime/"+DB_CONFIG_FILE+"\n");

                for (UjoProperty p : propertyList) {
                    a.append("#");
                    a.append(p);
                    a.append("=");
                    a.append(this.getText(p));
                    //a.append(p.getDefault());
                    a.append("\n");
                }


                File dir = paramFile.getParentFile();
                if (dir != null) {
                    dir.mkdirs();
                }
                paramFile.createNewFile();

                FileWriter fw = new FileWriter(paramFile);
                fw.write(a.toString());
                fw.close();
            }

            if (!ujormFile.exists()) {

                StringBuilder a = new StringBuilder();
                a.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                a.append("\n<body>");
                a.append("\n    <database id=\"").append(InspectimeDatabase.class.getSimpleName()).append("\">");
                a.append("\n        <schema>inspectime</schema>");
                a.append("\n        <dialect>org.ujorm.orm.dialect.MySqlDialect</dialect>");
                a.append("\n        <jndi>java:comp/env/jdbc/inspectime_db</jndi>");
                a.append("\n        <!-- -->");
                a.append("\n        <user>inspectime</user>");
                a.append("\n        <password>inspectime</password>");
                a.append("\n        <jdbcUrl>jdbc:mysql://127.0.0.1:3306/</jdbcUrl>");
                a.append("\n        <jdbcDriver>com.mysql.jdbc.Driver</jdbcDriver>");
                a.append("\n    </database>");
                a.append("\n</body>");

                File dir = ujormFile.getParentFile();
                if (dir != null) {
                    dir.mkdirs();
                }
                ujormFile.createNewFile();

                FileWriter fw = new FileWriter(ujormFile);
                fw.write(a.toString());
                fw.close();
            }

        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Can't load system parameters from form " + paramFileName);
        } finally {
            lock = true;
        }

    }

    /** Getter based on one UjoProperty */
    @Override
    @SuppressWarnings("unchecked")
    public <UJO extends ParamSystemService, VALUE> VALUE get(UjoProperty<UJO, VALUE> property) {
        return property.of((UJO) this);
    }

    /** Setter  based on UjoProperty. Type of value is checked in the runtime. */
    @Override
    @SuppressWarnings("unchecked")
    public <UJO extends ParamSystemService, VALUE> Ujo set(UjoProperty<UJO, VALUE> property, VALUE value) {
        property.setValue((UJO) this, value);
        return this;
    }

    // ----------- TEXT --------------
    /**
     * Returns a String value by a NULL context.
     * otherwise method returns an instance of String.
     *
     * @param property A Property
     * @return If property type is "container" then result is null.
     */
    @Override
    public String getText(final UjoProperty property) {
        return readUjoManager().getText(this, property, null);
    }

    /**
     * Set value from a String format by a NULL context. Types Ujo, List, Object[] are not supported by default.
     * <br>The method is an alias for a method writeValueString(...)
     * @param property Property
     * @param value String value
     */
    @Override
    public void setText(final UjoProperty property, final String value) {
        readUjoManager().setText(this, property, value, null, null);
    }

    @Override
    public void writeValue(UjoProperty property, Object value) {
        if (lock) {
            throw new IllegalStateException("Parameters are locked");
        }
        super.writeValue(property, value);
    }

  
}
