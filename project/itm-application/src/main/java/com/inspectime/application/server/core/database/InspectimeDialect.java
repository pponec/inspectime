/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.core.database;

import com.inspectime.service.defPlain.ParamSystemService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ujorm.orm.dialect.MySqlDialect;
import org.ujorm.orm.metaModel.MetaDatabase;
import org.ujorm.orm.metaModel.MetaParams;

/**
 *
 * @author Ponec
 */
public class InspectimeDialect extends MySqlDialect {

    static final private Logger LOGGER = Logger.getLogger(InspectimeDialect.class.getName());

    
    private MetaParams metaParams;

    @Override
    public Connection createConnection(final MetaDatabase db) throws Exception {

        if (metaParams==null) {
            metaParams = db.getParams();
        }
        Object parContext = metaParams.get(MetaParams.APPL_CONTEXT);

        if (parContext instanceof ParamSystemService) {
            ParamSystemService par = (ParamSystemService) parContext;
            String url  = par.get(ParamSystemService.jdbcUrl);
            String user = par.get(ParamSystemService.dbUser);
            String pswd = par.get(ParamSystemService.dbPassword);

            LOGGER.log(Level.INFO, "JDBC URL: >" + url + "< user: >" + user+"<");
            //
            Class.forName(getJdbcDriver());
            Connection result = DriverManager.getConnection(url, user, pswd);
            return result;

        } else {
            return super.createConnection(db);
        }
    }


}
