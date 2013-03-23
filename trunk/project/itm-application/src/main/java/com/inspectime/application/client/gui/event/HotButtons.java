/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.event;

import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Timer;
import com.inspectime.application.client.Application;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.cbo.CUserTask;
import org.ujorm.gxt.client.controller.TableControllerAsync;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.tools.MessageDialog;

public class HotButtons {

    public static final int BUTTON_COUNT = 4;
    private List<Button> buttons = new ArrayList<Button>(BUTTON_COUNT);
    final private EventTable eventTable;
    private boolean firstRun = true;
    LayoutContainer buttonContainer;

    public HotButtons(EventTable eventTable, LayoutContainer buttonContainer) {
        this.eventTable = eventTable;
        this.buttonContainer = buttonContainer;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void addButton(Button button) {
        buttons.add(button);
        button.setHideMode(HideMode.OFFSETS);
        button.setVisible(false);
    }

    /** Init buttons */
    public void init() {
        CQuery<CUserTask> query = CQuery.newInstance(CUserTask.class);
        CCriterion<CUserTask> crn = CUserTask._isFinished.whereEq(false);
        query.setCriterion(crn);
        query.orderBy(CUserTask.order, CUserTask.id);
        //
        TableControllerAsync.Util.getInstance().getCujoList(query, new ClientCallback<List<Cujo>>(eventTable) {

            @Override
            public void onSuccess(final List<Cujo> tasks) {
                
                if (firstRun) {
                    firstRun = false;

                    // The timer is a soluton for bug MSIE 6.0:
                    Timer timer = new Timer() {
                        @Override public void run() {
                            setButtonLabels(tasks);
                        }
                    };
                    timer.schedule(100);
                } else {
                    setButtonLabels(tasks);
                }

            }
        });
    }

    /** Assing new labels to the task buttons */
    private void setButtonLabels(final List<Cujo> tasks) {
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = buttons.get(i);
            final CUserTask userTask = i < tasks.size() ? (CUserTask) tasks.get(i) : null;
            final CTask task = userTask!=null ? userTask.getTask() : null;
            button.setVisible(task != null);
            if (task != null) {
                button.setIcon(task.isPrivate() ? Application.ICONS.hotTaskPrivate() : Application.ICONS.hotTask());
                button.setText(task.get(CTask.DISPLAY_PROPERTY));
                button.setIconAlign(IconAlign.LEFT);
                button.setToolTip(userTask.getName() + " - " + userTask.getProjectName());
                button.removeAllListeners();
                button.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override public void componentSelected(ButtonEvent ce) {
                        CEvent event = eventTable.createEvent();
                        event.set(CEvent.task, task);
                        event.timeSync();
                        TableControllerAsync.Util.getInstance().saveOrUpdate(event, true, new ClientCallback<ValidationMessage>(eventTable) {

                            @Override public void onSuccess(ValidationMessage result) {
                                if (result.isError()) {
                                    new MessageDialog(result.getMessage()).show();
                                } else {
                                    eventTable.reloadTable();
                                }
                                int delay = HotButtons.this.eventTable.getNoticeTime();
                                HotButtons.this.eventTable.getNoticeTask().delay(delay);
                            }
                        });
                    }
                });
                final boolean workaround = true; /// !!! workaround for GXT bug
                if (workaround) {
                    button.setWidth(140);
                    button.setWidth("92%");
                }
            }
        }

        // TODO: How to correct a button position ?
        //buttonContainer.fireEvent(Events.Change);
        //buttonContainer.repaint();
        //buttonContainer.recalculate();


    }
}
