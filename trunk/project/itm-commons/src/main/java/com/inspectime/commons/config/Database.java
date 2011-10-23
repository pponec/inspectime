/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.config;

import com.inspectime.commons.bo.*;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Table;

/**
 * An table mapping to a database (a sample of usage).
 */
//@Db(schema="db1", dialect=H2Dialect.class, user="sa", password="", jdbcUrl="jdbc:h2:mem:db1")
//@Db(schema="db1", dialect=PostgreSqlDialect.class, user="sa", password="sa", jdbcUrl="jdbc:postgresql://127.0.0.1:5432/db1")
//@Db(schema="db1", dialect=DerbyDialect.class, user="sa", password="", jdbcUrl="jdbc:derby:C:\\temp\\derby-sample;create=true")
//@Db(schema="db1", dialect=HsqldbDialect.class, user="sa", password="", jdbcUrl="jdbc:hsqldb:mem:db1")
//@Db(schema= ""  , dialect=FirebirdDialect.class, user="sysdba", password="masterkey", jdbcUrl="jdbc:firebirdsql:localhost/3050:c:\\progra~1\\firebird\\db\\db1.fdb?lc_ctype=UTF8")
//@Db(schema="db1", dialect=OracleDialect.class, user="sa", password="", jdbcUrl="jdbc:oracle:thin:@myhost:1521:orcl")
public class Database extends OrmTable<Database> {

    /** Inspectime Parameter of The Company */
    @Table("app_parameter")
    public static final RelationToMany<Database, SingleComParam> PARAMETER_COM = newRelation(SingleComParam.class);
    /** Inspectime Parameter of The User */
    @Table("usr_parameter")
    public static final RelationToMany<Database, SingleUsrParam> PARAMETER_USR = newRelation(SingleUsrParam.class);
    /** Comon descripton table */
    @Table("app_detail") 
    public static final RelationToMany<Database, Description> DESCRIPTION = newRelation(Description.class);
    /** Inspectime User */
    @Table("usr_user")
    public static final RelationToMany<Database, User> USER = newRelation(User.class);
    /** User Group */
    @Table("usr_group")
    public static final RelationToMany<Database, UserGroup> USER_GROUP = newRelation(UserGroup.class);
    /** User role */
    @Table("usr_role")
    public static final RelationToMany<Database, UserRole> USER_ROLE = newRelation(UserRole.class);
    /** Product */
    @Table("jws_product")
    public static final RelationToMany<Database, Product> PRODUCT = newRelation(Product.class);
    /** Project */
    @Table("jws_project")
    public static final RelationToMany<Database, Project> PROJECT = newRelation(Project.class);
    /** Customer */
    @Table("jws_customer")
    public static final RelationToMany<Database, Customer> CUSTOMER = newRelation(Customer.class);
    /** Company */
    @Table("jws_company")
    public static final RelationToMany<Database, Company> COMPANY = newRelation(Company.class);
    /** File content **/
    @Table("jws_content")
    public static final RelationToMany<Database, Description> MSG_CONTENT = newRelation(Description.class);
    /** Relation: Project - UserGroup **/
    @Table("rel_proj_usrgroup")
    public static final RelationToMany<Database, RelProjectUsGroup> REL_PROJ_USRGROUP = newRelation(RelProjectUsGroup.class);
    /** Project component */
    @Table("rel_task_action")
    public static final RelationToMany<Database, TaskAction> TASK_ACTION = newRelation(TaskAction.class);
    /** Event */
    @Table("jws_event")
    public static final RelationToMany<Database, Event> EVENT = newRelation(Event.class);
    /** User Task */
    @Table("usr_task")
    public static final RelationToMany<Database, UserTask> USER_TASK = newRelation(UserTask.class);
    /** Period Lock */
    @Table("usr_event_lock")
    public static final RelationToMany<Database, EventLock> EVENT_LOCK = newRelation(EventLock.class);
    /** Release */
    @Table("jws_release")
    public static final RelationToMany<Database, Release> RELEASE = newRelation(Release.class);
    /** Task */
    @Table("jws_task")
    public static final RelationToMany<Database, Task> PRJ_TASK = newRelation(Task.class);
    /** Account */
    @Table("jws_account")
    public static final RelationToMany<Database, Account> ENV_ACCOUNT = newRelation(Account.class);
}
