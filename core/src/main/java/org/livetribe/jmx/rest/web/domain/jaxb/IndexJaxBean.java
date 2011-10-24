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

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
@XmlRootElement(name = "Index")
public class IndexJaxBean
{
    static final Logger LOG = LoggerFactory.getLogger(IndexJaxBean.class);

    public URI mBeans;
    public URI nodes;

    public IndexJaxBean()
    {
    }

    public IndexJaxBean(UriInfo uriInfo)
    {
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        mBeans = uriBuilder.path("mbeans").build();

        uriBuilder = uriInfo.getAbsolutePathBuilder();
        nodes = uriBuilder.path("nodes").build();
    }
}
