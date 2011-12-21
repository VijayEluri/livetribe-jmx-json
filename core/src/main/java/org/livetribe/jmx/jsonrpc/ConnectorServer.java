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

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.weld.environment.servlet.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toolazydogs.jr4me.server.JsonRpcServlet;


/**
 *
 */
public class ConnectorServer extends JMXConnectorServer
{
    public static final String SCHEDULED_EXECUTOR = ConnectorServer.class.getName() + ".SCHEDULED_EXECUTOR";
    public static final String CAPACITY = ConnectorServer.class.getName() + ".CAPACITY";
    static final Logger LOG = LoggerFactory.getLogger(ConnectorServer.class);
    private final JMXServiceURL serviceURL;
    private final Map<String, ?> environment;
    private final Server server;
    private final Manager manager;

    public ConnectorServer(JMXServiceURL serviceURL, Map<String, ?> environment, MBeanServer mbeanServer)
    {
        super(mbeanServer);

        assert mbeanServer != null;

        this.serviceURL = serviceURL;
        this.environment = environment;

        int port = serviceURL.getPort();

        String urlPath = serviceURL.getURLPath();
        urlPath = urlPath.endsWith("/") ? urlPath.substring(0, urlPath.length() - 1) : urlPath;

        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder servletHolder = new ServletHolder(new JsonRpcServlet());
        servletHolder.setInitParameter(JsonRpcServlet.PACKAGES, "org.livetribe.jmx.jsonrpc");
        servletHolder.getRegistration().setLoadOnStartup(1);

        context.addServlet(servletHolder, urlPath + "/*");

        context.addEventListener(new Listener());

        ScheduledExecutorService executorService = (ScheduledExecutorService)environment.get(SCHEDULED_EXECUTOR);
        if (executorService == null) throw new NullPointerException("Missing " + SCHEDULED_EXECUTOR);
        Integer capacity = (Integer)environment.get(CAPACITY);
        if (capacity == null) throw new IllegalArgumentException("Missing " + CAPACITY);

        manager = new Manager(mbeanServer, executorService, capacity);

        context.addFilter(new FilterHolder(new ManagerFilter(manager)), "/*", FilterMapping.DEFAULT);

        server.setHandler(context);
    }

    public void start() throws IOException
    {
        try
        {
            server.start();
        }
        catch (Exception e)
        {
            throw new IOException("Unable to start Jetty server", e);
        }
    }

    public void stop() throws IOException
    {
        try
        {
            server.stop();
        }
        catch (Exception e)
        {
            throw new IOException("Unable to stop Jetty server", e);
        }
        finally
        {
            manager.drain();
        }
    }

    public boolean isActive()
    {
        return server.isRunning();
    }

    public JMXServiceURL getAddress()
    {
        return serviceURL;
    }

    public Map<String, ?> getAttributes()
    {
        return environment;
    }
}
