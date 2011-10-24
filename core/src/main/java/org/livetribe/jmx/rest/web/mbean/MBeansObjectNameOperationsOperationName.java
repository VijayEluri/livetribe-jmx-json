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
package org.livetribe.jmx.rest.web.mbean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.livetribe.jmx.rest.web.BaseAggregateWebController;
import org.livetribe.jmx.rest.web.domain.JMXNode;
import org.livetribe.jmx.rest.web.domain.jaxb.jmx.OperationReturnValueJaxBeans;
import org.livetribe.jmx.rest.web.util.FilterNodesUtils;


/**
 *
 */
@Path("/mbeans/{objectName}/operations/{operationName}")
public class MBeansObjectNameOperationsOperationName extends BaseAggregateWebController
{
    @GET
    @Produces(
    { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public OperationReturnValueJaxBeans invokeOperation(@PathParam("objectName") String objectName, @PathParam("operationName") String operationName,
            @QueryParam("nodes") String nodes)
    {
        Log.info("invokeOperationWithParameters: " + operationName);
        Collection<JMXNode> jmxNodes = FilterNodesUtils.getNodesToAggregate(nodes);

        return aggregateService.invokeOperation(jmxNodes,objectName,operationName);
    }

    @GET
    @Path("/{params}/{signature}")
    @Produces(
    { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public OperationReturnValueJaxBeans invokeOperationWithParameters(@PathParam("objectName") String objectName,
            @PathParam("operationName") String operationName, @PathParam("params") String params, @PathParam("signature") String signature,
            @QueryParam("nodes") String nodes)
    {
        Log.info("invokeOperationWithParameters: " + operationName);
        Collection<JMXNode> jmxNodes = FilterNodesUtils.getNodesToAggregate(nodes);

        String[] paramArray = params.split(",");
        String[] signatureArray = signature.split(",");
        return aggregateService.invokeOperation(jmxNodes,objectName,operationName,paramArray,signatureArray);
    }
}
