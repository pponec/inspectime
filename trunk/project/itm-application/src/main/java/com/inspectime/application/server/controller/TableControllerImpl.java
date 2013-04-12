/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.controller;

import org.ujorm.gxt.client.controller.TableController;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inspectime.application.client.ao.CTimeZone;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.SingleComParam;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.Release;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.UserTask;
import com.inspectime.service.def.AbstractService;
import com.inspectime.service.def.TaskService;
import com.inspectime.service.def.UserService;
import com.inspectime.service.def.UserTaskService;
import org.ujorm.gxt.client.CEnum;
import org.ujorm.gxt.client.EnumItems;
import org.ujorm.gxt.client.PropertyMetadata;
import org.ujorm.gxt.client.ao.ValidationException;
import org.ujorm.gxt.client.ao.ValidationMessage;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.server.callback.UserCallback;
import com.inspectime.commons.WQuery;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;
import org.ujorm.gxt.server.ListExt;
import org.ujorm.gxt.server.QueryTranslator;
import org.ujorm.gxt.server.UjoTranslator;
import org.ujorm.gxt.server.UjoTranslatorCallback;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.commons.bo.Customer;
import com.inspectime.commons.bo.EventLock;
import com.inspectime.commons.bo.SingleUsrParam;
import com.inspectime.commons.bo.Project;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.AccountService;
import com.inspectime.service.def.CommonService;
import com.inspectime.service.def.CustomerService;
import com.inspectime.service.def.EventLockService;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.MessageService;
import com.inspectime.service.def.SingleComParamService;
import com.inspectime.service.def.SingleUsrParamService;
import com.inspectime.service.def.ProductService;
import com.inspectime.service.def.ProjectService;
import com.inspectime.service.def.ReleaseService;
import com.inspectime.service.impl.MessageServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.Ujo;
import org.ujorm.Key;
import org.ujorm.core.UjoManager;
import org.ujorm.criterion.BinaryCriterion;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.ValueCriterion;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.InitItems;
import org.ujorm.gxt.server.IServerClassConfig;
import org.ujorm.orm.Query;
import org.ujorm.orm.Session;
import org.ujorm.orm.metaModel.MetaColumn;

/**
 * Load table data and another support
 * @author Ponec
 */
@org.springframework.stereotype.Controller("tableController")
@Transactional
public class TableControllerImpl extends RemoteServiceServlet implements TableController, BeanFactoryAware {

    final static private Logger LOGGER = Logger.getLogger(TableControllerImpl.class.getName());
    /** Spring bean factory */
    private BeanFactory beanFactory;
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
    protected UserService userService;
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

    private Map<Class<? extends Ujo>, AbstractService> serviceMap;

    /** Returns a service by type */
    private AbstractService getService(Class<? extends Ujo> type) {
        if (serviceMap==null) {
            HashMap<Class<? extends Ujo>, AbstractService> map = new HashMap<Class<? extends Ujo>, AbstractService>();
            map.put(Customer.class, customerService);
            map.put(Product.class, productService);
            map.put(Project.class, projectService);
            map.put(Release.class, releaseService);
            map.put(Task.class, taskService);
            map.put(Event.class, eventService);
            map.put(User.class, userService);
            map.put(UserTask.class, userTaskService);
            map.put(Account.class, accountService);
            map.put(EventLock.class, eventLockService);
            map.put(SingleUsrParam.class, singleUsrParamService);
            map.put(SingleComParam.class, singleComParamService);
            serviceMap = map;
        }
        return serviceMap.get(type);
    }
    
    protected void saveOrUpdate(AbstractBo ujo) {
        AbstractService service = getService(ujo.getClass());
        if (service != null) {
            service.saveOrUpdate(ujo);
        } else {
            commonService.saveOrUpdate(ujo);
        }
    }

    protected void save(AbstractBo ujo) {
        AbstractService service = getService(ujo.getClass());
        if (service != null) {
            service.save(ujo);
        } else {
            commonService.save(ujo);
        }
    }

    protected void update(AbstractBo ujo) {
        AbstractService service = getService(ujo.getClass());
        if (service != null) {
            service.update(ujo);
        } else {
            commonService.update(ujo);
        }
    }

    protected void delete(AbstractBo ujo) {
        AbstractService service = getService(ujo.getClass());
        if (service != null) {
            service.delete(ujo);
        } else {
            commonService.delete(ujo);
        }
    }

    protected List list(WQuery query) {
        AbstractService service = getService(query.getQuery().getTableModel().getType());
        if (service != null) {
            return service.list(query);
        } else {
            return commonService.list(query);
        }
    }

    /** Pink implementation. */
    @Override
    public void pink() {
        return;
    }

    // ---------------------------------------------------------
    /** Returns Messate Service in scope of the "session". */
    public MessageService getMessageService() {
        return (MessageService) beanFactory.getBean(MessageServiceImpl.BEAN_NAME);
    }

    /** Returns a sort property */
    @SuppressWarnings("fallthrough")
    private Key getSorter(Class<? extends Ujo> type, PagingLoadConfig config) {
        Key result = null;
        try {

            boolean desc = false;

            switch (config.getSortInfo().getSortDir()) {
                case DESC:
                    desc = true;
                case ASC:
                    String orderName = config.getSortField();
                    if (orderName!=null) {
                        orderName = orderName.trim();
                    }
                    result = UjoManager.getInstance().findIndirectProperty(type, orderName, true);
                    if (desc) {
                        result = result.descending();
                    }
                    break;
                case NONE:
            }
        } catch (Throwable e) {
            String msg = "Can't find a property for the SORTING: " + type + "." + config.getSortField();
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return result;
    }

    protected Map<Class, UjoTranslatorCallback> getCallBacks() {
        Map<Class, UjoTranslatorCallback> result = new HashMap<Class, UjoTranslatorCallback>();
        result.put(User.class, new UserCallback());
        return result;
    }

    @SuppressWarnings({"unchecked", "unchecked", "unchecked"})
    private <CUJO extends Cujo> ListExt<CUJO> loadRows(CQuery cquery, PagingLoadConfig cfg) {
        // userTaskService.assertServerContext();
        try {
            Class serverClass = serverClassConfig.getServerClass(cquery.getTypeName());
            Key orderBy = getSorter(serverClass, cfg);

            Query query = serverClassConfig.translate(cquery, orderBy, cfg);
            List items = list(new WQuery(query, cquery));
            // Security check: Set max limit on two relations:
            UjoTranslator<CUJO> translator = UjoTranslator.newInstance(cquery, serverClass, Math.min(cquery.getRelations(),2), serverClassConfig);

            Map<Class, UjoTranslatorCallback> callBackMap = getCallBacks();

            this.getCallBacks();
            for (Class key : callBackMap.keySet()) {
                if (serverClass.equals(key)) {
                    translator.setCallBack(callBackMap.get(key));
                }
            }

            ListExt<CUJO> result = translator.translate(items.iterator(), cfg.getOffset(), cfg.getLimit(), query);
            return result;
        } catch (Throwable e) {
            String message = "Unsupported operation";
            LOGGER.log(Level.SEVERE, message, e);
            throw new CMessageException(message, e);
        }
    }

    /** Is ecxeption of the required type ? */
    protected boolean isExceptionType(Throwable e, Class type) {
        while (e!=null) {
            if (type.isInstance(e)) {
                return true;
            } else {
               e = e.getCause();
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cujo> getCujoList(CQuery cQuery) throws CMessageException {
        userTaskService.assertServerContext();
        PagingLoadConfig config = new BasePagingLoadConfig(0, Integer.MAX_VALUE);
        ListExt<Cujo> result = loadRows(cQuery, config);
        return result.list();
    }

    /**
     * Returns database rows by a client query.
     * @param config NULL value means: no paging.
     */
    @Override
    public PagingLoadResult<Cujo> getDbRows(CQuery cQuery, PagingLoadConfig config) throws CMessageException {
        userTaskService.assertServerContext();

        if (config == null) {
            config = new BasePagingLoadConfig(0, Integer.MAX_VALUE);
        }
        ListExt<Cujo> rows = loadRows(cQuery, config);

        BasePagingLoadResult<Cujo> result = new BasePagingLoadResult<Cujo>(
                rows.list(),
                config.getOffset(),
                rows.getTotalCount());

        return result;
    }

    /** Returns all enum items, the current implementation returns a persistent enums only. */
    @Override
    public InitItems getEnums() throws CMessageException {

        Map<String, List<CEnum>> result = new HashMap<String, List<CEnum>>();
        List<CEnum> items = new ArrayList<CEnum>(16);

        // Add the persistent Enums:
        for (Key p : ormHandler.findPropertiesByType(Enum.class)) {
            for (Object o : p.getType().getEnumConstants()) {

                CEnum item = new CEnum();
                CEnum.id.setValue(item, ((Enum) o).ordinal());
                CEnum.name.setValue(item, ((Enum) o).name());
                items.add(item);
            }
            result.put(p.getType().getSimpleName(), new ArrayList<CEnum>(items));
            items.clear();
        }

        {   // Add the more non persistent enums:
           result.put(CTimeZone.getSimpleClassName(), CTimeZone.getAllZones());
        }

        return new InitItems(new EnumItems(result), serverClassConfig.getClientObjectList());
    }

    // TODO: rozdelit na hlavni metodu ktera bude volat
    //metody translate a metodu do SaveOrUpdate
    @SuppressWarnings("unchecked")
    @Override
    public ValidationMessage saveOrUpdate(Cujo cujo, boolean create) throws CMessageException {
        userTaskService.assertServerContext();
        try {
            UjoTranslator trans = new UjoTranslator(cujo, 1, serverClassConfig);

            AbstractBo ujo = (AbstractBo) trans.translateToServer(cujo);
            if (!create) { // if (update)
                ujo.writeSession(commonService.getSession());
            }

            saveOrUpdate(ujo);
            return new ValidationMessage(ujo.getPrimaryKey());

        } catch (Throwable e) {
            final ValidationMessage msg
                = e instanceof ValidationException
                ? ((ValidationException) e).getValidationMessage()
                : new ValidationMessage(null, create ? "Can't create item" : "Can't update item")
                ;
            LOGGER.log(Level.SEVERE, msg.getMessage() + " " + cujo, e);
            return msg;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(List<? extends Cujo> cujos, int deleteActionType) throws CMessageException {
        userTaskService.assertServerContext();
        if (cujos.isEmpty()) {
            return;
        }
        try {
            Class cujoClass = cujos.get(0).getClass();
            Class serverClass = serverClassConfig.getServerClass(cujoClass.getName());
            UjoTranslator trans = new UjoTranslator(cujoClass, serverClass, 0, serverClassConfig);
            Session session = commonService.getSession();

            switch (deleteActionType) {
                case TableController.DELETE_AUTO:
                case TableController.DELETE_LOGICAL:
                    Key active = UjoManager.getInstance().readProperties(serverClass).find(User.active.getName(), false);
                    if (active == null && deleteActionType == TableController.DELETE_AUTO) {
                        delete(cujos, TableController.DELETE_PHYSICAL);
                        return;
                    }
                    for (Cujo cujo : cujos) {
                        AbstractBo ujo = (AbstractBo) trans.translateToServer(cujo);
                        ujo.readChangedProperties(true);
                        ujo.writeSession(session);
                        ujo.set(active, null); // A deleted state has the NULL value.
                        update(ujo);
                    }
                    break;
                case TableController.DELETE_PHYSICAL:
                    for (Cujo cujo : cujos) {
                        AbstractBo ujo = (AbstractBo) trans.translateToServer(cujo);
                        ujo.writeSession(session);
                        delete(ujo);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Value: " + deleteActionType);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Can't delete" + cujos, e);
        }
    }

    /** Returns Property Metadata by properties inside CQuery. */
    @Override
    public List<PropertyMetadata> getMetaModel(List<CQuery> qProperties) throws CMessageException {
        List<PropertyMetadata> result = new ArrayList<PropertyMetadata>(30);

        try {
            MessageService messageService = getMessageService();
            for (CQuery cQuery : qProperties) {
                Class ctype = Class.forName(cQuery.getTypeName());
                cQuery.restore(ctype);

                QueryTranslator qt = new QueryTranslator(cQuery, ormHandler, serverClassConfig);
                Criterion crn = qt.getCriterion();

                List<Key> properties = new ArrayList<Key>(32);
                extractProperty(crn, properties);

                for (Key p : properties) {
                    MetaColumn metaColumn = (MetaColumn) ormHandler.findColumnModel(p);
                    if (metaColumn != null) {
                        String columLabel = messageService.getColumnLabel(p);
                        String sideLabel = messageService.getSideLabel(p);
                        String description = messageService.getDescription(p);
                        final boolean sortable = false;

                        PropertyMetadata propertyMetadata = new PropertyMetadata(
                                MetaColumn.PRIMARY_KEY.of(metaColumn),
                                MetaColumn.MANDATORY.of(metaColumn),
                                sortable,
                                MetaColumn.MAX_LENGTH.of(metaColumn),
                                MetaColumn.PRECISION.of(metaColumn),
                                columLabel,
                                sideLabel,
                                description);

                        result.add(propertyMetadata);

                    } else {
                        result.add(null);
                    }
                }
            }

        } catch (Throwable e) {
            String msg = "bug";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new RuntimeException(msg, e);
        }

        return result;
    }

    private void extractProperty(Criterion crn, List<Key> result) throws CMessageException {
        if (crn.isBinary()) {
            Criterion crn1 = ((BinaryCriterion) crn).getLeftNode();
            Criterion crn2 = ((BinaryCriterion) crn).getRightNode();
            extractProperty(crn1, result);
            extractProperty(crn2, result);
        } else {
            ValueCriterion vc = (ValueCriterion) crn;
            result.add(vc.getLeftNode());
        }
    }

    /**
     * Set the beanFactory.
     * @see http://static.springsource.org/spring/docs/2.0.x/reference/beans.html#beans-factory-method-injection
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o) {
        return o;
    }
    
    @Override
    public void initClientTime(Date clientTime) {
        userService.getApplContext().setClientTimeOffset(clientTime);
    }
}
