/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.jobs.impl;

import com.inspectime.application.jobs.def.StartUpJob;
import com.inspectime.application.server.core.database.InspectimeDatabase;
import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.UserGroup;
import com.inspectime.commons.bo.UserRole;
import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.CommonService;
import com.inspectime.service.def.TaskService;
import com.inspectime.service.defPlain.ParamSystemService;
import com.inspectime.service.def.UserGroupService;
import com.inspectime.service.def.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.metaModel.MetaParams;

/**
 *
 * @author Hampl
 */
public class StartUpJobImpl implements StartUpJob {

    private BeanFactory beanFactory;
    @Autowired
    private CommonService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private OrmHandler handler;
    @Autowired
    private ParamCompService appParamService;
    @Autowired
    private ParamSystemService sysParamService;
    @Autowired
    private TaskService taskService;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void init() {
        
        // Store database configuration:
        String dbConfigString = ParamSystemService.dbConfig.getValue(sysParamService);
        if (!ParamSystemService.dbConfig.getDefault().equals(dbConfigString)) {
            handler.config(dbConfigString);
        }
        // Store parameter context:
        final MetaParams params = new MetaParams().setApplContext(sysParamService);
        params.set(MetaParams.LOGBACK_LOGGING_SUPPORT, true);

        handler.config(params);
        handler.loadDatabase(InspectimeDatabase.class);

        // Toto volani zajisti obaleni session pomoci AOP
        StartUpJob suc = (StartUpJob) beanFactory.getBean("startUpJob");
        suc.instalDefaultValues();
    }

    @Override
    public void instalDefaultValues() {
       //maybe junit test change versiun in compilation time
        instalAdmin();

        // ...
    }

    private void instalAdmin() {
        if (userService.isNoUser()) {
            UserGroup usrGroup = new UserGroup();
            usrGroup.set(UserGroup.name, "admin");
            usrGroup.set(UserGroup.description, "automatically generated group");

            Company company = new Company();
            company.set(Company.name, "[DEFAULT]");
            company.set(Company.description, "Undefined compay");

            User admin = new User();
            admin.setAccountNonExpired(Boolean.TRUE);
            admin.setAccountNonLocked(Boolean.TRUE);
            admin.setEnabled(Boolean.TRUE);
            admin.setCredentialsNonExpired(Boolean.TRUE);
            admin.setLogin(User.ADMIN_LOGIN);
            admin.setName(User.ADMIN_LOGIN);
            admin.setPasswordNative(User.ADMIN_LOGIN);
            admin.setEmail(User.ADMIN_LOGIN + "@company.net");
            admin.setCompany(company);
            admin.set(User.userGroup, usrGroup);

            userGroupService.save(usrGroup);
            companyService.save(company);
            userService.save(admin);

            // Assign all available roles:
            for (RoleEnum roleEnum : RoleEnum.values()) {
                userService.saveRole(new UserRole(admin, roleEnum));
            }

            // ---------------------------------------------------
            
            taskService.createDefaultTask(admin);

            // ---------------------------------------------------

            instalDemoUsers();
        }
    }

    /** Install demo users */
    private void instalDemoUsers() {

        if (sysParamService.get(ParamSystemService.debug)) {

            UserGroup usrGroup = new UserGroup();
            usrGroup.set(UserGroup.name, "demo");
            usrGroup.set(UserGroup.description, "demo user group");
            userGroupService.save(usrGroup);

            for (int i = 1; i < 20; i++) {

                User user = new User();
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setEnabled(i % 2 == 0);
                user.setCredentialsNonExpired(true);
                user.setLogin("demo-user-" + i);
                user.setPasswordNative("a");
                user.setEmail(" ");
                user.set(User.userGroup, usrGroup);

                UserRole role = new UserRole();
                role.setRole(RoleEnum.USER);
                role.serUser(user);

                userService.save(user);
                userService.saveRole(role);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">

    // </editor-fold>
}
