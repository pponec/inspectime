/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.commons;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface Icons extends ImageBundle {

  @Resource("com/inspectime/application/staticweb/resources/icons/logo16.png")
  AbstractImagePrototype logo16();

  @Resource("com/inspectime/application/staticweb/resources/icons/table.png")
  AbstractImagePrototype table();

  @Resource("com/inspectime/application/staticweb/resources/icons/table_go.png")
  AbstractImagePrototype select();

  @Resource("com/inspectime/application/staticweb/resources/icons/add.png")
  AbstractImagePrototype add();

  @Resource("com/inspectime/application/staticweb/resources/icons/delete2.png")
  AbstractImagePrototype delete();

  @Resource("com/inspectime/application/staticweb/resources/icons/application_edit.png")
  AbstractImagePrototype edit();

  @Resource("com/inspectime/application/staticweb/resources/icons/application_go.png")
  AbstractImagePrototype detail();

  @Resource("com/inspectime/application/staticweb/resources/icons/table_add.png")
  AbstractImagePrototype list();

  @Resource("com/inspectime/application/staticweb/resources/icons/door_out.png")
  AbstractImagePrototype exit();

  @Resource("com/inspectime/application/staticweb/resources/icons/help.png")
  AbstractImagePrototype help();

  @Resource("com/inspectime/application/staticweb/resources/icons/logout.png")
  AbstractImagePrototype logout();

  @Resource("com/inspectime/application/staticweb/resources/icons/bullet_error.png")
  AbstractImagePrototype error();

  @Resource("com/inspectime/application/staticweb/resources/icons/application_view_tile.png")
  AbstractImagePrototype selectionDialog();

  @Resource("com/inspectime/application/staticweb/resources/icons/tick.png")
  AbstractImagePrototype ok();

  @Resource("com/inspectime/application/staticweb/resources/icons/house.png")
  AbstractImagePrototype home();

  @Resource("com/inspectime/application/staticweb/resources/icons/_report.png")
  AbstractImagePrototype report();

  //@Resource("com/inspectime/application/staticweb/resources/icons/chart_bar.png")
  @Resource("com/inspectime/application/staticweb/resources/icons/chart_pie.png")
  AbstractImagePrototype chart();

  @Resource("com/inspectime/application/staticweb/resources/icons/lightning_add.png") // _application_double.png")
  AbstractImagePrototype copy();

  /** Hot Task button */
  @Resource("com/inspectime/application/staticweb/resources/icons/note_add.png")
  AbstractImagePrototype hotTask();

  /** Hot Task button pro a private actions */
  @Resource("com/inspectime/application/staticweb/resources/icons/note_add_private.png")
  AbstractImagePrototype hotTaskPrivate();

  /** Reload table */
  @Resource("com/inspectime/application/staticweb/resources/icons/arrow_rotate_anticlockwise.png")
  AbstractImagePrototype reloadData();

  // ------ MAIN TABS

  @Resource("com/inspectime/application/staticweb/resources/icons/time.png")
  AbstractImagePrototype time();

  @Resource("com/inspectime/application/staticweb/resources/icons/user.png")
  AbstractImagePrototype live();

  @Resource("com/inspectime/application/staticweb/resources/icons/_application_double.png")
  AbstractImagePrototype task();

  @Resource("com/inspectime/application/staticweb/resources/icons/star.png")
  AbstractImagePrototype star();

  @Resource("com/inspectime/application/staticweb/resources/icons/_application_double.png")
  AbstractImagePrototype project();

  @Resource("com/inspectime/application/staticweb/resources/icons/vcard_add.png")
  AbstractImagePrototype customer();

  @Resource("com/inspectime/application/staticweb/resources/icons/application_edit.png")
  AbstractImagePrototype product();

  @Resource("com/inspectime/application/staticweb/resources/icons/flag_green.png")
  AbstractImagePrototype account();

  @Resource("com/inspectime/application/staticweb/resources/icons/user_add.png")
  AbstractImagePrototype user();

  @Resource("com/inspectime/application/staticweb/resources/icons/lock_break.png")
  AbstractImagePrototype unlock();

  @Resource("com/inspectime/application/staticweb/resources/icons/lock.png")
  AbstractImagePrototype lock();

  @Resource("com/inspectime/application/staticweb/resources/icons/_wrench.png")
  AbstractImagePrototype params();

  @Resource("com/inspectime/application/staticweb/resources/icons/_resultset_next.png")
  AbstractImagePrototype goNext();

  @Resource("com/inspectime/application/staticweb/resources/icons/_resultset_previous.png")
  AbstractImagePrototype goPrev();



}
