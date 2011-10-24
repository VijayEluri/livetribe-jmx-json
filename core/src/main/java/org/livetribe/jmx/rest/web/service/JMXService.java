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
package org.livetribe.jmx.rest.web.service;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import java.util.Set;


/**
 *
 */
public interface JMXService
{
    public abstract Set<ObjectName> getObjectNames(JMXServiceURL jmxServiceURL);

    public abstract Set<String> getObjectNamesByPrefix(JMXServiceURL jmxServiceURL, String prefix);

    public abstract MBeanAttributeInfo[] getAttributes(JMXServiceURL jmxServiceURL, String objectName) throws InstanceNotFoundException;

    public abstract MBeanOperationInfo[] getOperations(JMXServiceURL jmxServiceURL, String objectName) throws InstanceNotFoundException;

    public abstract Object invoke(JMXServiceURL jmxServiceURL, String objectName, String operationName, Object[] params, String[] signature);

    public abstract Object getAttribute(JMXServiceURL jmxServiceURL, String objectName, String attributeName) throws InstanceNotFoundException;
}