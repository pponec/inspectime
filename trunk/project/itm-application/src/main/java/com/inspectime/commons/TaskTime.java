/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.commons;

import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Task;

/**
 * Task and time
 * @author Ponec
 */
public class TaskTime implements Comparable<TaskTime> {


    final private Task task;
    private int time;

    public TaskTime(Task task) {
        this.task = task;
    }

    public Long getId() {
        return task.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return getId().equals(((TaskTime)obj).getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /** Add minutes */
    public void addTime(Event event) {
        addTime(Event.period.of(event));
    }

    /** Add minutes */
    public void addTime(int minutes) {
        time+=minutes;
    }

    /** Compare by the time descending */
    @Override
    public int compareTo(TaskTime task) {
        int t1 = this.time;
        int t2 = ((TaskTime) task).time;

        if (t1==t2) {
            return 0;
        } else {
            return t1<t2 
                ?  1
                : -1
                ;
        }
    }

    /** Export name to StringBuilder */
    public void exportTo(StringBuilder sb) {
        if (sb.length()>0) {
            sb.append(", ");
        }
        sb.append(Task.code.of(task));
    }
}
