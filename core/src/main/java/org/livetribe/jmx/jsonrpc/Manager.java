/**
 * Copyright 2011 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.jmx.jsonrpc;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.livetribe.jmx.jsonrpc.model.Notifications;
import org.livetribe.jmx.jsonrpc.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Alan D. Cabrera
 */
public class Manager
{
    private final static Logger LOGGER = LoggerFactory.getLogger(Manager.class);
    private final MBeanServer mBeanServer;
    private final ScheduledExecutorService executorService;
    private final Map<Integer, Session> sessions = new HashMap<Integer, Session>();
    private int nextSessionId = 0;

    public Manager(MBeanServer mBeanServer, ScheduledExecutorService executorService)
    {
        assert mBeanServer != null;
        assert executorService != null;

        this.mBeanServer = mBeanServer;
        this.executorService = executorService;
    }

    public Integer createSession(int inactivityTimeout, int pollingTimeout, int maxNotifications)
    {
        synchronized (sessions)
        {
            int sessionId = nextSessionId++;

            Future future = executorService.schedule(new DeleteSession(sessionId), inactivityTimeout, TimeUnit.SECONDS);

            sessions.put(sessionId, new Session(sessionId, inactivityTimeout, pollingTimeout, maxNotifications, future));


            return sessionId;
        }
    }

    public void deleteSession(int sessionId)
    {
        synchronized (sessions)
        {
            Session session = sessions.remove(sessionId);
            if (session != null) session.getFuture().cancel(false);
        }
    }

    public Session getSession(int sessionId)
    {
        synchronized (sessions)
        {
            Session session = sessions.remove(sessionId);
            refresh(session);
            return session;
        }
    }

    public Integer getSessionProperty(int sessionId, String name)
    {
        synchronized (sessions)
        {
            Session session = sessions.get(sessionId);
            if (session == null) return null;

            refresh(session);

            if ("inactivityTimeout".equals(name))
            {
                return session.getInactivityTimeout();
            }
            else if ("pollingTimeout".equals(name))
            {
                return session.getPollingTimeout();
            }
            else if ("maxNotifications".equals(name))
            {
                return session.getMaxNotifications();
            }

            return null;
        }
    }

    public Integer setSessionProperty(int sessionId, String sessionPropertyName, int sessionPropertyValue)
    {
        synchronized (sessions)
        {
            Session session = sessions.get(sessionId);
            if (session == null) return null;

            refresh(session);

            if ("inactivityTimeout".equals(sessionPropertyName))
            {
                int result = session.getInactivityTimeout();
                session.setInactivityTimeout(sessionPropertyValue);
                return result;
            }
            else if ("pollingTimeout".equals(sessionPropertyName))
            {
                int result = session.getPollingTimeout();
                session.setPollingTimeout(sessionPropertyValue);
                return result;
            }
            else if ("maxNotifications".equals(sessionPropertyName))
            {
                int result = session.getMaxNotifications();
                session.setMaxNotifications(sessionPropertyValue);
                return result;
            }

            return null;
        }
    }

    public Notifications fetchNotifications(int sessionId, long start)
    {
        return null;  //Todo change body of created methods use File | Settings | File Templates.
    }

    public String[] getDomains()
    {
        return mBeanServer.getDomains();
    }

    public Integer getMBeanCount()
    {
        return mBeanServer.getMBeanCount();
    }

    public String getDefaultDomain()
    {
        return mBeanServer.getDefaultDomain();
    }

    public ObjectInstance createMBean(String className, ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return mBeanServer.createMBean(className, name);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return mBeanServer.createMBean(className, name, loaderName);
    }

    public ObjectInstance createMBean(String className, ObjectName name, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return mBeanServer.createMBean(className, name, params, signature);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object[] params, String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return mBeanServer.createMBean(className, name, loaderName, params, signature);
    }

    public ObjectInstance registerMBean(Object object, ObjectName name) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
    {
        return mBeanServer.registerMBean(object, name);
    }

    public void unregisterMBean(ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException
    {
        mBeanServer.unregisterMBean(name);
    }

    public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException
    {
        return mBeanServer.getObjectInstance(name);
    }

    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query)
    {
        return mBeanServer.queryMBeans(name, query);
    }

    public Set<ObjectName> queryNames(ObjectName name, QueryExp query)
    {
        return mBeanServer.queryNames(name, query);
    }

    public boolean isRegistered(ObjectName name)
    {
        return mBeanServer.isRegistered(name);
    }

    public Object getAttribute(ObjectName name, String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
    {
        return mBeanServer.getAttribute(name, attribute);
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes) throws InstanceNotFoundException, ReflectionException
    {
        return mBeanServer.getAttributes(name, attributes);
    }

    public void setAttribute(ObjectName name, Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        mBeanServer.setAttribute(name, attribute);
    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes) throws InstanceNotFoundException, ReflectionException
    {
        return mBeanServer.setAttributes(name, attributes);
    }

    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException
    {
        return mBeanServer.invoke(name, operationName, params, signature);
    }

    public void addNotificationListener(ObjectName name) throws InstanceNotFoundException
    {
        mBeanServer.addNotificationListener(name, (NotificationListener)null, null, null);
    }

    public void addNotificationListener(ObjectName name, ObjectName listener, String handback) throws InstanceNotFoundException
    {
        mBeanServer.addNotificationListener(name, listener, null, handback);
    }

    public void removeNotificationListener(ObjectName name) throws InstanceNotFoundException, ListenerNotFoundException
    {
        mBeanServer.removeNotificationListener(name, (NotificationListener)null, null, null);
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener, String handback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        mBeanServer.removeNotificationListener(name, listener, null, handback);
    }

    public MBeanInfo getMBeanInfo(ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException
    {
        return mBeanServer.getMBeanInfo(name);
    }

    public boolean isInstanceOf(ObjectName name, String className) throws InstanceNotFoundException
    {
        return mBeanServer.isInstanceOf(name, className);
    }

    private void refresh(Session session)
    {
        if (session != null)
        {
            LOGGER.trace("Refreshing session {}", session.getSessionId());
            session.getFuture().cancel(false);
            Future future = executorService.schedule(new DeleteSession(session.getSessionId()), session.getInactivityTimeout(), TimeUnit.SECONDS);
            session.setFuture(future);
        }
    }

    class DeleteSession implements Runnable
    {

        private final int sessionId;

        DeleteSession(int sessionId) { this.sessionId = sessionId; }

        @Override
        public void run()
        {
            synchronized (sessions)
            {
                sessions.remove(sessionId);
            }
        }
    }
}
