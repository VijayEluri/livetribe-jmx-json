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
package org.livetribe.jmx.rest.web.service.impl;

import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jetty.util.log.Log;

import org.livetribe.jmx.rest.web.domain.JMXNode;
import org.livetribe.jmx.rest.web.service.JMXNodeService;


/**
 *
 */
public class JMXNodeServiceImpl implements JMXNodeService
{
    private Properties properties = new Properties();

    public Set<JMXNode> getNodes()
    {
        @SuppressWarnings("static-access")
        InputStream propertyInputStream = this.getClass().getClassLoader().getResourceAsStream("jmxNodes.properties");
        if (propertyInputStream == null)
            throw new IllegalStateException("Couldn't read jmxNodes.properties file!");
        Set<JMXNode> jmxNodes = new HashSet<JMXNode>();
        try
        {
            properties.load(propertyInputStream);
            String nodeString = (String)properties.get("nodes");
            String[] nodes = nodeString.split(",");
            for (String string : nodes)
            {
                String jmxServiceURL = "service:jmx:rmi:///jndi/rmi://" + string + "/jettyjmx";
                jmxNodes.add(new JMXNode(string, new JMXServiceURL(jmxServiceURL)));
            }
        }
        catch (IOException e)
        {
            Log.warn(e);
        }
        finally
        {
            try
            {
                propertyInputStream.close();
            }
            catch (IOException e)
            {
                Log.warn("getNodes: Couldn't close InputStream. This might lead to a file descriptor leak: ", e);
            }
        }
        return jmxNodes;
    }

}
