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
package org.livetribe.jmx.rest;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class ConnectorServer extends JMXConnectorServer
{
    static final Logger LOG = LoggerFactory.getLogger(ConnectorServer.class);
    private final int port;
    private Server server;

    public ConnectorServer(int port)
    {
        this.port = port;
    }

    public ConnectorServer(MBeanServer mbeanServer, int port)
    {
        super(mbeanServer);
        this.port = port;
    }

    public void start() throws IOException
    {
        try
        {
            server = new Server(port);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            ServletHolder holder = new ServletHolder(new ServletContainer());
            holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
            holder.setInitParameter("com.sun.jersey.config.property.packages", "org.livetribe.jmx.rest.web");
            holder.getRegistration().setLoadOnStartup(1);

            context.addServlet(holder, "/ws/*");

            server.start();
            server.join();
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
    }

    public boolean isActive()
    {
        return server.isRunning();
    }

    public JMXServiceURL getAddress()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Map<String, ?> getAttributes()
    {
        return Collections.emptyMap();
    }
}
