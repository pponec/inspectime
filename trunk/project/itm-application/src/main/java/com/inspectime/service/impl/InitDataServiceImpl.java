/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Customer;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.Project;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.UserTask;
import com.inspectime.service.def.AccountService;
import com.inspectime.service.def.CommonService;
import com.inspectime.service.def.CustomerService;
import com.inspectime.service.def.EventLockService;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.InitDataService;
import com.inspectime.service.def.ProductService;
import com.inspectime.service.def.ProjectService;
import com.inspectime.service.def.ReleaseService;
import com.inspectime.service.def.SingleComParamService;
import com.inspectime.service.def.SingleUsrParamService;
import com.inspectime.service.def.TaskService;
import com.inspectime.service.def.UserService;
import com.inspectime.service.def.UserTaskService;
import com.inspectime.service.defPlain.ParamSystemService;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.gxt.server.IServerClassConfig;

/**
 * InitDataServiceImpl
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("initDataService")
public class InitDataServiceImpl extends AbstractServiceImpl<User> implements InitDataService {

    static final private Logger LOGGER = Logger.getLogger(InitDataServiceImpl.class.getName());

    @Autowired
    private ParamSystemService sysParamService;

    @Autowired
    private CommonService commonService;
    @Autowired
    private org.ujorm.orm.OrmHandler ormHandler;
    @Autowired
    protected IServerClassConfig serverClassConfig;
    @Autowired
    protected ProjectService projectService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected CustomerService customerService;
    @Autowired
    protected EventService eventService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected ReleaseService releaseService;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected EventLockService eventLockService;
    @Autowired
    protected UserTaskService userTaskService;
    @Autowired
    protected SingleComParamService singleComParamService;
    @Autowired
    protected SingleUsrParamService singleUsrParamService;


    @Override
    public Class<User> getDefaultClass() {
        return User.class;
    }

    @Override
    public void createDemoData() {
        assertServerContext();
        try {
            if (sysParamService.get(ParamSystemService.createCompanyDemoData) && isNoData()) {
                final String msg = "Create new demo Data for a new Comapy: " + getApplContext().getUserCompany().getName();
                LOGGER.log(Level.INFO, msg);
                doCreateData();
            }
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "The isUserAgreemnt bug", e);
        }
    }

    public boolean isNoData() {
         return productService.isNoData();
    }

    /** Create a demo data */
    private void doCreateData() {
        User currentUser = getApplContext().getUser();

        Customer customer = new Customer();
        customer.set(Customer.name, "My Customer");
        customer.set(Customer.description, "Generated demo customer");
        customer.set(Customer.graphColor, Color.BLUE);
        customerService.save(customer);
        //
        Account accountAction = new Account();
        accountAction.set(Account.name, "My Account");
        accountAction.set(Account.description, "Generated demo account");
        accountAction.set(Account.privateState, false);
        accountAction.set(Account.graphColor, Color.BLUE);
        accountAction.set(Account.company, currentUser.getCompany());
        accountService.save(accountAction);
        //
        Account accountPrivate = new Account();
        accountPrivate.set(Account.name, "Private");
        accountPrivate.set(Account.description, "Generated private demo account");
        accountPrivate.set(Account.privateState, true);
        accountPrivate.set(Account.graphColor, Color.GREEN);
        accountPrivate.set(Account.company, currentUser.getCompany());
        accountService.save(accountPrivate);
        //
        Product product = new Product();
        product.set(Product.name, "My Product");
        product.set(Product.description, "Generated demo product");
        product.set(Product.graphColor, Color.BLUE);
        productService.save(product);
        //
        Project project = new Project();
        project.set(Project.name, "My Project");
        project.set(Project.description, "Generated demo project");
        project.set(Project.graphColor, Color.BLUE);
        project.set(Project.product, product);
        project.set(Project.customer, customer);
        projectService.save(project);
        //
        Task task1 = new Task();
        task1.set(Task.code, "Task A");
        task1.set(Task.title, "Task A");
        task1.set(Task.description, "The first generated task");
        task1.set(Task.account, accountAction);
        task1.set(Task.project, project);
        taskService.save(task1);
        //
        Task task2 = new Task();
        task2.set(Task.code, "Task B");
        task2.set(Task.title, "Task B");
        task2.set(Task.description, "The second generated task");
        task2.set(Task.account, accountAction);
        task2.set(Task.project, project);
        taskService.save(task2);
        //
        Task task3 = new Task();
        task3.set(Task.code, "Pause");
        task3.set(Task.title, "Pause");
        task3.set(Task.description, "Generated the Pause (or Exit) task");
        task3.set(Task.account, accountPrivate);
        task3.set(Task.project, project);
        taskService.save(task3);
        //
        UserTask mytask1 = new UserTask();
        mytask1.set(UserTask.order, (short)10);
        mytask1.set(UserTask.task, task1);
        mytask1.set(UserTask.user, currentUser);
        userTaskService.save(mytask1);
        //
        if (false) {
            UserTask mytask2 = new UserTask();
            mytask2.set(UserTask.order, (short)10);
            mytask2.set(UserTask.task, task2);
            mytask2.set(UserTask.user, currentUser);
            userTaskService.save(mytask2);
        }
        //
        UserTask mytask3 = new UserTask();
        mytask3.set(UserTask.order, (short)10);
        mytask3.set(UserTask.task, task3);
        mytask3.set(UserTask.user, currentUser);
        userTaskService.save(mytask3);
    }

}
