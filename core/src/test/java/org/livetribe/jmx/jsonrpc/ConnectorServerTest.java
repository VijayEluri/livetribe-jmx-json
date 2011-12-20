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
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.testng.annotations.Test;


/**
 *
 */
public class ConnectorServerTest
{
    @Test
    public void test() throws Exception
    {
        main();
    }

    public static void main(String... args) throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        JMXServiceURL url = new JMXServiceURL("service:jmx:jsonrpc://localhost:8080/ws/");
        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_PACKAGES, "org.livetribe.jmx");
        environment.put(ConnectorServer.SCHEDULED_EXECUTOR, new ScheduledThreadPoolExecutor(10));

        JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mBeanServer);
        server.start();
        server.stop();
    }
}
