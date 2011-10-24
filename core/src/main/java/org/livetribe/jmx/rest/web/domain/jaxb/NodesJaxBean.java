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
package org.livetribe.jmx.rest.web.domain.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
@XmlRootElement(name = "Index")
public class NodesJaxBean
{
    public Set<NodeJaxBean> nodes;

    public NodesJaxBean()
    {
    }

    public NodesJaxBean(Set<NodeJaxBean> nodes)
    {
        this.nodes = nodes;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NodesJaxBean [nodes=");
        builder.append(nodes);
        builder.append("]");
        return builder.toString();
    }

}
