/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.Project;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.UserTask;
import com.inspectime.service.def.AccountService;
import com.inspectime.service.def.CustomerService;
import com.inspectime.service.def.ProductService;
import com.inspectime.service.def.ProjectService;
import com.inspectime.service.def.TaskService;
import com.inspectime.service.def.UserTaskService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("taskService")
public class TaskServiceImpl extends AbstractServiceImpl<Task> implements TaskService {

    static final private Logger LOGGER = Logger.getLogger(TaskServiceImpl.class.getName());

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserTaskService userTaskService;

    /** Is not the user deleted? */
    protected static final Criterion<Task> crnActive = Criterion.where(Task.active, true);

    @Override
    public Class<Task> getDefaultClass() {
        return Task.class;
    }

    /** Create default Project and Task for a new Company */
    @Override
    public Task createDefaultTask(User user) {
        Date date = new Date();

        Account account = new Account();
        account.set(Account.name, "DEFAULT");
        account.set(Account.description, "Undefined account");
        account.set(Account.createdBy, user);
        account.set(Account.modifiedBy, user);
        account.set(Account.company, user.getCompany());
        accountService.save(account);

        Task task = new Task();
        Project project = new Project();
        project.set(Project.name, "The First Project");
        project.set(Project.product, productService.getFirstProduct(user));
        project.set(Project.createdBy, user);
        project.set(Project.modifiedBy, user);
        project.set(Project.created, date);
        project.set(Project.modified, date);
        project.set(Project.customer, customerService.getFirstCustomer(user));
        projectService.save(project);


        task.set(Task.project, project); // temporary relation
        task.set(Task.release, project.getFirstRelease());
        task.set(Task.code, "TASK-01");
        task.set(Task.title, "The First Task");
        task.set(Task.createdBy, user);
        task.set(Task.modifiedBy, user);
        task.set(Task.created, date);
        task.set(Task.modified, date);
        task.set(Task.company, user.getCompany());
        task.set(Task.account, account);
        save(task);
        // getSession().commit();

        return task;
    }

    private void setModified(Task bo, boolean created) {
        Date currentDate = new Date();

        if (created) {
            bo.set(Task.created, currentDate);
            if (bo.get(Task.createdBy)==null) {
                bo.set(Task.createdBy, getApplContext().getUser());
            }
        }
        bo.set(Task.modified, currentDate);
        if (bo.get(Task.modifiedBy)==null) {
            bo.set(Task.modifiedBy, getApplContext().getUser());
        }

    }

    @Override
    public void save(Task bo) {
        setModified(bo, true);
        if (bo.get(Task.company)==null) {
            bo.set(Task.company, getApplContext().getUser().getCompany());
        }
        super.save(bo);
    }

    @Override
    public void update(Task bo) {
        setModified(bo, false);
        super.update(bo);
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(final WQuery query) {
        final Criterion crn = Criterion.where(Task.company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        final List<UJO> result = super.list(query);
        return result;
    }

    /** Try to find the first private MyTask or first Company task by ID */
    @Override
    public Task findFirstPrivateTask() {
        final boolean PRIVATE = true;
        UserTask userTask = userTaskService.findFirstTask(PRIVATE);
        if (userTask!=null) {
            return userTask.getTask();
        }

        Task task = findFirstTask(PRIVATE);
        if (task!=null) {
            return task;
        }

        task = findFirstTask(!PRIVATE);
        return task;
    }

    /** Try to find the first commercial MyTask or first Company task by ID */
    @Override
    public Task findFirstCommercialTask() {
        final boolean PRIVATE = false;
        UserTask userTask = userTaskService.findFirstTask(PRIVATE);
        if (userTask!=null) {
            return userTask.getTask();
        }

        Task task = findFirstTask(PRIVATE);
        if (task!=null) {
            return task;
        }

        task = findFirstTask(!PRIVATE);
        return task;
    }

    /** Find first task */
    @Override
    public Task findFirstTask(boolean privateTask) {
        final Task result;
        final Criterion<Task> crn1, crn2, crn3, crn4, crn5, crn6, crn7, criterion;

        crn1 = Criterion.where(Task.company, getApplContext().getUserCompany());
        crn2 = Criterion.where(Task.active, true);
        crn3 = Criterion.where(Task.finished, false);
        crn4 = Criterion.where(Task.account.add(Account.privateState), privateTask);
        crn5 = Criterion.where(Task.project.add(Project.active), true);
        crn6 = Criterion.where(Task.project.add(Project.finished), false);
        crn7 = Criterion.where(Task.project.add(Project.product).add(Product.active), true);
        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and(crn6).and(crn7);

        result = getSession().createQuery(criterion).orderBy(Task.id).setLimit(1).uniqueResult();
        return result;
    }

}

