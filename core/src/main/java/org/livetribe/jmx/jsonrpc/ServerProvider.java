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

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerProvider;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class ServerProvider implements JMXConnectorServerProvider
{
    static final Logger LOG = LoggerFactory.getLogger(ServerProvider.class);

    public JMXConnectorServer newJMXConnectorServer(JMXServiceURL serviceURL, Map<String, ?> environment, MBeanServer mbeanServer) throws IOException
    {
        LOG.trace("Allocating connector server for {} at URL {}", mbeanServer, serviceURL);
        return new ConnectorServer(serviceURL, environment, mbeanServer);
    }
}
