/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.jobs.def;

import org.springframework.beans.factory.BeanFactoryAware;

/**
 *
 * @author Hampl
 */
public interface StartUpJob extends BeanFactoryAware{

    public void init();

    public void instalDefaultValues();
}
