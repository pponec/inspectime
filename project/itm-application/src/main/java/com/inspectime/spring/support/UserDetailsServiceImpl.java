/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.spring.support;

import com.inspectime.commons.bo.UserRole;
import com.inspectime.service.def.ApplContextService;
import com.inspectime.service.def.UserService;
import com.inspectime.service.impl.ApplContextServiceImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService, BeanFactoryAware {

    static final private Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class.getName());

    private UserService userService;

    /** Spring bean factory */
    protected BeanFactory beanFactory;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {

        com.inspectime.commons.bo.User user = userService.loadByLogin(login);

        // uzivatel neexistuje v databazi - vyhodime vyjimku
        if (user == null) {
            throw new UsernameNotFoundException("User with username=" + login + " was not found.");
        }

        int size = (int) user.getUserRoles().count();
        GrantedAuthority[] grantedAuthority = new GrantedAuthority[size];

        int i = 0;
        for (UserRole userRole : user.getUserRolesSorted()) {
            grantedAuthority[i] = new GrantedAuthorityImpl(userRole.getRoleName());
            i++;
        }



        // pridelime zakladni roli(pouze pro pristup do systemu) o ostatni
        //toto je uplne spatne musi to byt uplne jinak
/*
        if ("admin".equals(login)) {
        grantedAuthority = new GrantedAuthority[]{new GrantedAuthorityImpl(Role.ADMIN.getRoleName()), new GrantedAuthorityImpl(Role.USER.getRoleName())};
        } else {
        grantedAuthority = new GrantedAuthority[]{new GrantedAuthorityImpl(Role.USER.getRoleName())};
        }
         */
        User userDetails = new User(
            user.getLogin(),
            user.getPasswordHash(),
            user.isEnabled(),
            user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.isAccountNonLocked(),
            grantedAuthority);

        try {
            getApplContext().resetCache(); // Reset a User cache
            String msg = "User loaded for the login: '" + login + "'";
            LOGGER.log(Level.INFO, msg) ; // TODO: remove it
        } catch (Exception e) {
            LOGGER.log(Level.OFF, "Clear cache", e); // TODO: případ prvního přihlášení
        }

        return userDetails;
    }

    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

   /**
     * Set the beanFactory.
     * @see http://static.springsource.org/spring/docs/2.0.x/reference/beans.html#beans-factory-method-injection
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /** Get ApplContextService for the current user */
    protected ApplContextService getApplContext() {
        return (ApplContextService) beanFactory.getBean(ApplContextServiceImpl.BEAN_NAME);
    }


}
