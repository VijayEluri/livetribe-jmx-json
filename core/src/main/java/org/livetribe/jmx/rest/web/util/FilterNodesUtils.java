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
package org.livetribe.jmx.rest.web.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.livetribe.jmx.rest.web.domain.JMXNode;
import org.livetribe.jmx.rest.web.service.JMXNodeService;
import org.livetribe.jmx.rest.web.service.impl.JMXNodeServiceImpl;


/**
 *
 */
public class FilterNodesUtils
{
    //TODO: IOC
    private static JMXNodeService jmxNodeService = new JMXNodeServiceImpl();

    /**
     * @return all known nodes if nodes param == null, otherwise filters known nodes by nodeNames in nodes param
     */
    public static Collection<JMXNode> getNodesToAggregate(String nodes)
    {
        Collection<JMXNode> jmxNodes = jmxNodeService.getNodes();
        if (nodes != null)
        {
            List<String> nodeList = Arrays.asList(nodes.split(","));
            Collection<JMXNode> nodesToCollect = new HashSet<JMXNode>();
            for (JMXNode jmxNode : jmxNodes)
            {
                if (nodeList.contains(jmxNode.getNodeName()))
                    nodesToCollect.add(jmxNode);
            }
            return nodesToCollect;
        }
        return jmxNodes;
    }
}
