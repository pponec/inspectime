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
import com.inspectime.commons.bo.UserTask;
import com.inspectime.service.def.AccountService;
import com.inspectime.service.def.ProductService;
import com.inspectime.service.def.ProjectService;
import com.inspectime.service.def.UserTaskService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.utility.OrmTools;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("userTaskService")
public class UserTaskServiceImpl extends AbstractServiceImpl<UserTask> implements UserTaskService {

    static final private Logger LOGGER = Logger.getLogger(UserTaskServiceImpl.class.getName());

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccountService accountService;

    @Override
    public Class<UserTask> getDefaultClass() {
        return UserTask.class;
    }


    @Override
    public void save(UserTask bo) {
        bo.set(UserTask.user, getApplContext().getUser());
        super.save(bo);
    }

    @Override
    public void update(UserTask bo) {
        super.update(bo);
    }

    /** Return Event include calculated Event.period */
    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {

        Criterion<UserTask> crn = Criterion.where(UserTask.user, getApplContext().getUser());
        query.addCriterion(crn);

        List<UserTask> result = super.list(query);
        loadLazy(result);

        return (List<UJO>) result;
    }

    /** Load Accounts */
    private List<UserTask> loadLazy(List<UserTask> usrTasks) {
        List<Task> tasks = new ArrayList<Task>(usrTasks.size());
        for (UserTask usrTask : usrTasks) {
            Task newTask = usrTask.get(UserTask.task);
            int i;
            for (i=tasks.size()-1; i>=0 && tasks.get(i)!=newTask; --i) {}
            if (i<0) {
                tasks.add(newTask);
            }
        }
        
        //OrmTools.loadLazyValuesAsBatch(tasks, Task.project);
        OrmTools.loadLazyValuesAsBatch(tasks, Task.account);
        return usrTasks;
    }


    /** Find the first private task */
    @Override
    public UserTask findFirstTask(boolean privateTask) {
        final UserTask result;
        final Criterion<UserTask> crn1, crn2, crn3, crn4, crn5, crn6, crn7, criterion;

        crn1 = Criterion.where(UserTask.user, getApplContext().getUser());
        crn2 = Criterion.where(UserTask.task.add(Task.active), true);
        crn3 = Criterion.where(UserTask.task.add(Task.finished), false);
        crn4 = Criterion.where(UserTask.task.add(Task.account).add(Account.privateState), privateTask);
        crn5 = Criterion.where(UserTask.task.add(Task.project).add(Project.active), true);
        crn6 = Criterion.where(UserTask.task.add(Task.project).add(Project.finished), false);
        crn7 = Criterion.where(UserTask.task.add(Task.project).add(Project.product).add(Product.active), true);
        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and(crn6).and(crn7);

        if (privateTask) {
            result = getSession().createQuery(criterion)
                    .orderBy(UserTask.order
                    ,        UserTask.id)
                    .setLimit(1)
                    .uniqueResult()
                    ;

        } else {
            result = getSession().createQuery(criterion)
                    .orderBy(UserTask.order.descending()
                    ,        UserTask.id.descending())
                    .setLimit(1)
                    .uniqueResult()
                    ;
        }

        return result;
    }


}

