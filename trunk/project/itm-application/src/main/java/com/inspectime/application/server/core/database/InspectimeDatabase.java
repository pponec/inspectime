/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.core.database;

import com.inspectime.commons.config.Database;
import org.ujorm.orm.annot.Db;

/**
 * The database configuration class.
 * @author Hampl
 */
//@Db(jndi="jdbc/inspectime", dialect = org.ujorm.orm.dialect.MySqlDialect.class )
@Db(schema = "inspectime", dialect = InspectimeDialect.class, user = "inspectime", password = "inspectime", jdbcUrl = "jdbc:mysql://127.0.0.1:3306/")
//@Db(schema = "inspectime", dialect = org.ujorm.orm.dialect.MySqlDialect.class, user = "root", password = "datamaster", jdbcUrl = "jdbc:mysql://localhost:3306/")
//@Db(schema = "inspectime", dialect = org.ujorm.orm.dialect.MySqlDialect.class, user = "root", password = "tomasek", jdbcUrl = "jdbc:mysql://192.168.132.1:3306/")
//@Db(schema = "inspectime", dialect = org.ujorm.orm.dialect.MySqlDialect.class, user = "inspectime", password = "datamaster", jdbcUrl = "jdbc:mysql://127.0.0.1:3306/")

//@Db(schema = "inspectime", dialect = org.ujorm.orm.dialect.MySqlOldDialect.class, user = "root", password = "datamaster", jdbcUrl = "jdbc:mysql://127.0.0.1:3306/")
//@Db(schema = "inspectime", dialect = org.ujorm.orm.dialect.PostgreSqlDialect.class, user = "root", password = "datamaster", jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/inspectime")
public class InspectimeDatabase extends Database {
}
