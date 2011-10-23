/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.report;

import com.inspectime.application.client.gui.user.UserCombo;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.ao.StandardLabel;
import com.google.gwt.core.client.GWT;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.inspectime.application.client.Application;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.FlexTable;
import com.inspectime.application.client.ao.DateRange;
import com.inspectime.application.client.ao.DateRangeEnum;
import com.inspectime.application.client.controller.ReportControllerAsync;
import com.inspectime.application.client.gui.event.EventTable;
import java.util.Date;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.tools.MessageDialog;
import static com.inspectime.application.client.ao.DateRangeEnum.*;

/**
 * Date filter Panel
 * @author pavel
 */
public class DateFilterPanel extends ContentPanel implements CLoginRedirectable {

    private static final String DATA_KEY = "enum";
    private static final String SPACE = "&nbsp;";

    private DateField dateFrom;
    private DateField dateTo;
    private UserCombo userBox;
    private CUser userFilter;
    private RadioGroup radioGroup = new RadioGroup();
    private DelayedTask rangeControllTask;
    private DateRangeEnum rangeRequest = DateRangeEnum.SELECTED_MONTH;
    private ReportTable reportTable;

    public DateFilterPanel() {
        setLayout(new BorderLayout());
        setTitle("Report Filter");
        setStyleName("dateFilterPanel");
        setHeaderVisible(true);
        setCollapsible(true);
        rangeControllTask = new DelayedTask(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                setRangeToCalendars();
            }
        });

        dateTo = createCalendar();
        dateFrom = createCalendar();
        dateTo.setWidth(120);
        dateFrom.setWidth(120);
        userBox = new UserCombo(CUser.name, this) {
            @Override
            public void onChange(CUser value) {
                userFilter = value!=null && value.getId()!=null
                        ? value
                        : null
                        ;
                reportTable.reloadGrid();
            }
        };


        FlexTable table = new FlexTable();
        table.getElement().getStyle().setProperty("margin", "1px");
        table.getElement().getStyle().setProperty("hight", "12px");
        table.setCellSpacing(0);
        table.setCellPadding(0);

        // ---------------------------------------------------

        int i=0;
        int j=0;
        table.setHTML  (i, j++, SPACE);
        table.setWidget(i, j++, new StandardLabel("Date From:"));
        table.setWidget(i, j++, dateFrom);
        table.setWidget(i, j++, userBox);
        Button button = new Button("Submit", Application.ICONS.ok());
        button.setWidth(120);
        table.setWidget(i, j++, button);
        button.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                reportTable.reloadGrid();
            }
        });
        button = new Button("Export", Application.ICONS.report());
        button.setWidth(120);
        table.setWidget(i, j++, button);
        button.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                new MessageDialog("Available only in PREMIUM version").show();
            }
        });

        i++;
        j=0;
        table.setHTML  (i, j++, SPACE);
        table.setWidget(i, j++, new StandardLabel("Date To:"));
        table.setWidget(i, j++, dateTo);

        i++;
        j=0;
        table.setHTML  (i, j++, SPACE);
        table.setWidget(i, j++, new StandardLabel("Quick Select:"));
        table.setWidget(i, j++, newRadio(SELECTED_DAY));
        table.setWidget(i, j++, newRadio(SELECTED_WEEK));
        table.setWidget(i, j++, newRadio(SELECTED_MONTH));
        table.setWidget(i, j++, newRadio(SELECTED_YEAR));

        i++;
        j=0;
        table.setHTML  (i, j++, SPACE);
        table.setHTML  (i, j++, SPACE);
        table.setWidget(i, j++, newRadio(PREVIOUS_DAY));
        table.setWidget(i, j++, newRadio(PREVIOUS_WEEK));
        table.setWidget(i, j++, newRadio(PREVIOUS_MONTH));
        table.setWidget(i, j++, newRadio(PREVIOUS_YEAR));

        add(table, new BorderLayoutData(LayoutRegion.CENTER, 200, 150, 250));

        // ------------------

        radioGroup.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override public void handleEvent(BaseEvent be) {
                if (radioGroup.getValue()!=null) {
                    rangeRequest = radioGroup.getValue().getData(DATA_KEY);
                    rangeControllTask.delay(100);
                }
            }
        });   
    }

    private DateField createCalendar() {
        DateField result = new DateField();
        result.setValue(new Date());
        result.getPropertyEditor().setFormat(EventTable.DEFAULT_DAY_FORMAT);
        result.setEditable(false);
        return result;
    }

    private Radio newRadio(DateRangeEnum rangeEnum) {
        Radio radio = new Radio();
        radio.setBoxLabel(rangeEnum.getLabel(rangeEnum));
        radio.setValue(false);
        radio.setData(DATA_KEY, rangeEnum);
        radioGroup.add(radio);

        if (rangeEnum == rangeRequest) {
            // Select default range:
            radioGroup.setValue(radio);
        }

        return radio;
    }

    /** Get filter item */
    public Date getDateFrom() {
        return dateFrom.getValue();
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom.setValue(dateFrom);
    }

    /** Get filter item */
    public Date getDateTo() {
        return dateTo.getValue();
    }

    public void setDateTo(Date dateTo) {
        this.dateTo.setValue(dateTo);
    }

    /** Get filter item */
    public Long getUserId() {
        return userFilter!=null ? userFilter.getId() : null;
    }

    /** Set range to Calendar components: */
    public void setRangeToCalendars() {
        if (rangeRequest!=null) {
            ReportControllerAsync.Util.getInstance().getRangeRequest(rangeRequest, new Date(), new ClientCallback<com.inspectime.application.client.ao.DateRange>(reportTable) {
                @Override
                public void onSuccess(DateRange result) {
                    GWT.log("Range from calendar ...");
                    dateFrom.setValue(result.getDateFrom());
                    dateTo.setValue(result.getDateTo());
                    reportTable.reloadGrid();
                }
            });
        }
    }

    public DateFilterPanel setReportTable(ReportTable reportTable) {
        this.reportTable = reportTable;
        return this;
    }

    /** Set the report title: */
    public void setReportTitle(String title) {
        super.setHeading(title);
    }

    @Override
    public void redirectToLogin() {
        this.reportTable.redirectToLogin();
    }

    /** Show user filter */
    public void showUserFiler(boolean visible) {
        userBox.setVisible(visible);
    }

}
