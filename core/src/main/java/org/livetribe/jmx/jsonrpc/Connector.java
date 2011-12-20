/**
 *
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

import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.security.auth.Subject;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class Connector implements JMXConnector
{
    static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    public void connect() throws IOException
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void connect(Map<String, ?> env) throws IOException
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public MBeanServerConnection getMBeanServerConnection() throws IOException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public MBeanServerConnection getMBeanServerConnection(Subject delegationSubject) throws IOException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void close() throws IOException
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void addConnectionNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void removeConnectionNotificationListener(NotificationListener listener) throws ListenerNotFoundException
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void removeConnectionNotificationListener(NotificationListener l, NotificationFilter f, Object handback) throws ListenerNotFoundException
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getConnectionId() throws IOException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}
