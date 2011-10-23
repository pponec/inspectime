/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.commons.bo.item.TimeZone;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import org.ujorm.core.UjoIterator;
import org.ujorm.core.annot.Transient;
import org.ujorm.criterion.Criterion;
import org.ujorm.extensions.Property;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * User of the Company
 * @author Hampl,ponec,gola
 */
@Comment("User of the Company")
final public class User extends AbstractBo {

    private static final String INDEX_NAME = "idx_user";
    public static final String ADMIN_LOGIN = "admin";

    public static final java.sql.Date DEFAULT_LOCK_DATE;
    static {
        Calendar cal = new GregorianCalendar(java.util.TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        DEFAULT_LOCK_DATE = new java.sql.Date(cal.getTimeInMillis());
    }

    /** Primary Key */
    @Column(pk = true)
    public static final Property<User, Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Property<User, Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Login must be an unique text in the all application */
    @Column(length = 80, mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Property<User, String> login = newProperty(String.class);

    /** Email */
    @Column(length = 80, mandatory = true)
    public static final Property<User, String> email = newProperty(String.class);

    /** The Time Zone */
    @Comment("Time zone in [hours]")
    @Column(name="time_zone", length=1, mandatory=true)
    public static final Property<User, TimeZone> timeZone = newProperty(new TimeZone(0));

    /** Company is not part of the unique constraint
     * @see #login
     */
    @Column(mandatory = true /*, uniqueIndex = INDEX_NAME */)
    public static final Property<User, Company> company = newProperty($COMPANY, Company.class);

    /** Password hash */
    @Column(length = 50, mandatory = true, name="password")
    public static final Property<User, String> passwordHash = newProperty("passwordHash", String.class);

    /** Password native */
    @Transient
    public static final Property<User, String> passwordNative = newProperty("password", String.class);

    /** User's sure name and first name */
    @Comment("User's sure name and first name")
    @Column(name = "name", length = 100)
    public static final Property<User, String> name = newProperty(String.class);

    /** User's personal id */
    @Column(name = "pid", length = 100)
    public static final Property<User, String> pid = newProperty(String.class);

    /** Work  fund staff per week. */
    @Comment("Work  fund staff per week")
    @Column(name = "work_fund", mandatory=true)
    public static final Property<User, Short> workFundStafPerWeek = newProperty((short)(8*5));

    /** Description */
    @Column(length = 250)
    public static final Property<User, String> description = newProperty(String.class);

    /** Enabled */
    public static final Property<User, Boolean> enabled = newProperty(Boolean.class);

    /** Account non expired */
    @Column(name = "account_non_expired")
    public static final Property<User, Boolean> accountNonExpired = newProperty(Boolean.class);

    /** Credentials non expired */
    @Column(name = "credentials_non_expired")
    public static final Property<User, Boolean> credentialsNonExpired = newProperty(Boolean.class);

    /** Account non locked */
    @Column(name = "account_non_locked")
    public static final Property<User, Boolean> accountNonLocked = newProperty(Boolean.class);

    /** Date of confirmation with Term of use. The Date can be null if the useer has been created by an administrator. */
    @Comment("Date of confirmation of the contract")
    @Column(name = "contract_date", mandatory=false)
    public static final Property<User, java.sql.Date> contractDate = newProperty(java.sql.Date.class);

    /** The last login date. The Date can be null if the useer has been created by an administrator. */
    @Comment("The last login date")
    @Column(name = "login_date", mandatory=false)
    public static final Property<User, java.sql.Date> lastLoginDate = newProperty(java.sql.Date.class);

    /** User group */
    @Column(mandatory = true)
    public static final Property<User, UserGroup> userGroup = newProperty(UserGroup.class);

    /** Relation to the User roles */
    public static final RelationToMany<User, UserRole> userRoles = newRelation(UserRole.class);

    /** Transient user roles */
    @Transient
    public static final Property<User, String> _userTextRoles = newProperty("roles", String.class);

    /** Date of lock, the last value is relevant. Locked are all days to the required one, include. */
    @Column(mandatory=false)
    public static final Property<User,java.sql.Date> lockDate = newProperty(DEFAULT_LOCK_DATE);

    /** Property initialization */
    static {
        init(User.class);
    }

    /** User Role Set */
    private Set<RoleEnum> roles;

    public User() {
        active.setValue(this, true);
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public String getLogin() {
        return login.getValue(this);
    }

    public void setLogin(String aLogin) {
        login.setValue(this, aLogin);
    }

    public String getEmail() {
        return email.getValue(this);
    }

    public void setEmail(String aEmail) {
        email.setValue(this, aEmail);
    }

    public Company getCompany() {
        return company.getValue(this);
    }

    public void setCompany(Company aCompany) {
        company.setValue(this, aCompany);
    }

    public String getPasswordHash() {
        return passwordHash.getValue(this);
    }

    public void setPasswordHash(String aPassword) {
        passwordHash.setValue(this, aPassword);
    }

    public String getPasswordNative() {
        return passwordNative.getValue(this);
    }

    public void setPasswordNative(String aPassword) {
        passwordNative.setValue(this, aPassword);
    }

    public String getName() {
        return name.getValue(this);
    }

    public void setName(String aUserName) {
        name.setValue(this, aUserName);
    }

    public Boolean isEnabled() {
        return enabled.getValue(this);
    }

    public void setEnabled(Boolean _enabled) {
        enabled.setValue(this, _enabled);
    }

    public Boolean isAccountNonExpired() {
        return accountNonExpired.getValue(this);
    }

    public void setAccountNonExpired(Boolean _enabled) {
        accountNonExpired.setValue(this, _enabled);
    }

    public Boolean isCredentialsNonExpired() {
        return credentialsNonExpired.getValue(this);
    }

    public void setCredentialsNonExpired(Boolean _enabled) {
        credentialsNonExpired.setValue(this, _enabled);
    }

    public Boolean isAccountNonLocked() {
        return accountNonLocked.getValue(this);
    }

    public void setAccountNonLocked(Boolean _enabled) {
        accountNonLocked.setValue(this, _enabled);
    }

    /** Returns all roles */
    public UjoIterator<UserRole> getUserRoles() {
        return userRoles.getValue(this);
    }

    /** Returns a Cached RoleSet */
    public Set<RoleEnum> getRoleSet() {
        if (roles==null) {
            final Set<RoleEnum> rs = EnumSet.noneOf(RoleEnum.class);
            for (UserRole userRole : getUserRoles()) {
                rs.add(userRole.getRole());
            }
            roles = rs;
        }
        return roles;
    }

    /** Return true if the current user have got any role from parameters. */
    public boolean hasAnyRole(RoleEnum ... roles) {
        final Set<RoleEnum> assignedRoles = getRoleSet();

        for (RoleEnum roleEnum : roles) {
            if (assignedRoles.contains(roleEnum)) {
                return true;
            }
        }
        return false;
    }


    /** Returns all roles sorted by <code>UserRole.role</code> */
    public UjoIterator<UserRole> getUserRolesSorted() {
        Criterion<UserRole> crn = Criterion.where(UserRole.user, this);
        return readSession().createQuery(crn).orderBy(UserRole.role).iterator();
    }

    public void addUserRole(UserRole role) {
        UserRole.user.setValue(role, this);
    }

    public List<UserGroup> getUserGroups() {
        List<UserGroup> result = new ArrayList<UserGroup>();
        result.add(userGroup.getValue(this));
        return result;

    }

    /** Get User Roles as String by template "1,2,3" */
    public String getUserRoleAsString() {
        StringBuilder result = new StringBuilder();
        for (UserRole role : getUserRolesSorted()) {
            if (result.length() > 0) {
                result.append(',');
            }
            result.append(role.getRole().ordinal());
        }
        return result.toString();
    }
    // </editor-fold>
}
