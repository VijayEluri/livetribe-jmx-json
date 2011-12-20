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


import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import java.util.Set;

import org.livetribe.jmx.jsonrpc.codecs.AttributeDeserializer;
import org.livetribe.jmx.jsonrpc.codecs.AttributeSerializer;
import org.livetribe.jmx.jsonrpc.codecs.ObjectInstanceDeserializer;
import org.livetribe.jmx.jsonrpc.codecs.ObjectInstanceSerializer;
import org.livetribe.jmx.jsonrpc.codecs.ObjectNameDeserializer;
import org.livetribe.jmx.jsonrpc.codecs.ObjectNameSerializer;
import org.livetribe.jmx.jsonrpc.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toolazydogs.jr4me.api.Codecs;
import com.toolazydogs.jr4me.api.MapException;
import com.toolazydogs.jr4me.api.Method;
import com.toolazydogs.jr4me.api.Param;


/**
 *
 */
@MapException({@MapException.Map(exception = AttributeNotFoundException.class, code = -1, message = "The specified attribute does not exist or cannot be retrieved"),
               @MapException.Map(exception = InstanceAlreadyExistsException.class, code = -2, message = "The MBean is already registered in the repository"),
               @MapException.Map(exception = InstanceNotFoundException.class, code = -3, message = "The specified MBean does not exist in the repository"),
               @MapException.Map(exception = IntrospectionException.class, code = -4, message = "An exception occurred during the introspection of an MBean"),
               @MapException.Map(exception = InvalidAttributeValueException.class, code = -5, message = "The value specified is not valid for the attribute"),
               @MapException.Map(exception = ListenerNotFoundException.class, code = -6, message = "The specified MBean listener does not exist in the repository"),
               @MapException.Map(exception = MBeanException.class, code = -7, message = "User defined exception"),
               @MapException.Map(exception = MBeanRegistrationException.class, code = -8, message = "Exceptions thrown by the preRegister(), preDeregister() methods"),
               @MapException.Map(exception = NotCompliantMBeanException.class, code = -9, message = "Exception which occurs when trying to register an  object in the MBean server that is not a JMX compliant MBean"),
               @MapException.Map(exception = ReflectionException.class, code = -10, message = "Represents exceptions thrown in the MBean server when using the * java.lang.reflect classes to invoke methods on MBeans")})
@Codecs({@Codecs.Codec(clazz = Attribute.class, serializer = AttributeSerializer.class, deserializer = AttributeDeserializer.class),
         @Codecs.Codec(clazz = ObjectName.class, serializer = ObjectNameSerializer.class, deserializer = ObjectNameDeserializer.class),
         @Codecs.Codec(clazz = ObjectInstance.class, serializer = ObjectInstanceSerializer.class, deserializer = ObjectInstanceDeserializer.class)})
public class Rpc
{
    static final Logger LOG = LoggerFactory.getLogger(Rpc.class);

    @Method
    public Integer createSession(@Param(name = "inactivityTimeout") int inactivityTimeout,
                                 @Param(name = "pollingTimeout") int pollingTimeout,
                                 @Param(name = "maxNotifications") int maxNotifications)
    {
        return ManagerFilter.getManager().createSession(inactivityTimeout, pollingTimeout, maxNotifications);
    }

    @Method
    public void deleteSession(@Param(name = "sessionId") int sessionId)
    {
        ManagerFilter.getManager().deleteSession(sessionId);
    }

    @Method
    public Session getSession(@Param(name = "sessionId") int sessionId)
    {
        return ManagerFilter.getManager().getSession(sessionId);
    }

    @Method
    public Integer getSessionProperty(@Param(name = "sessionId") int sessionId,
                                      @Param(name = "name") String name)
    {
        return ManagerFilter.getManager().getSessionProperty(sessionId, name);
    }

    @Method
    public Integer setSessionProperty(@Param(name = "sessionId") int sessionId,
                                      @Param(name = "name") String name,
                                      @Param(name = "value") int value)
    {
        return ManagerFilter.getManager().setSessionProperty(sessionId, name, value);
    }

    @Method
    public String[] getDomains()
    {
        return ManagerFilter.getManager().getDomains();
    }

    @Method
    public Integer getMBeanCount()
    {
        return ManagerFilter.getManager().getMBeanCount();
    }

    @Method
    public String getDefaultDomain()
    {
        return ManagerFilter.getManager().getDefaultDomain();
    }

    @Method
    public ObjectInstance createMBean(@Param(name = "className") String className,
                                      @Param(name = "name") ObjectName name) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return ManagerFilter.getManager().createMBean(className, name);
    }

    @Method
    public ObjectInstance createMBeanWithLoader(@Param(name = "className") String className,
                                                @Param(name = "name") ObjectName name,
                                                @Param(name = "loaderName") ObjectName loaderName) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return ManagerFilter.getManager().createMBean(className, name, loaderName);
    }

    @Method
    public ObjectInstance createMBeanWithParams(@Param(name = "className") String className,
                                                @Param(name = "name") ObjectName name,
                                                @Param(name = "params") Object[] params,
                                                @Param(name = "signature") String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
    {
        return ManagerFilter.getManager().createMBean(className, name, params, signature);
    }

    @Method
    public ObjectInstance createMBeanWitParamsAndLoader(@Param(name = "className") String className,
                                                        @Param(name = "name") ObjectName name,
                                                        @Param(name = "loaderName") ObjectName loaderName,
                                                        @Param(name = "params") Object[] params,
                                                        @Param(name = "signature") String[] signature) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
    {
        return ManagerFilter.getManager().createMBean(className, name, loaderName, params, signature);
    }

    @Method
    public ObjectInstance registerMBean(@Param(name = "sessionId") Object object,
                                        @Param(name = "sessionId") ObjectName name) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
    {
        return ManagerFilter.getManager().registerMBean(object, name);
    }

    @Method
    public void unregisterMBean(@Param(name = "sessionId") ObjectName name) throws InstanceNotFoundException, MBeanRegistrationException
    {
        ManagerFilter.getManager().unregisterMBean(name);
    }

    @Method
    public ObjectInstance getObjectInstance(@Param(name = "sessionId") ObjectName name) throws InstanceNotFoundException
    {
        return ManagerFilter.getManager().getObjectInstance(name);
    }

    @Method
    public Set<ObjectInstance> queryMBeans(@Param(name = "name") ObjectName name,
                                           @Param(name = "query") QueryExp query)
    {
        return ManagerFilter.getManager().queryMBeans(name, query);
    }

    @Method
    public Set<ObjectName> queryNames(@Param(name = "name") ObjectName name,
                                      @Param(name = "query") QueryExp query)
    {
        return ManagerFilter.getManager().queryNames(name, query);
    }

    @Method
    public boolean isRegistered(@Param(name = "name") ObjectName name)
    {
        return ManagerFilter.getManager().isRegistered(name);
    }

    @Method
    public Object getAttribute(@Param(name = "name") ObjectName name,
                               @Param(name = "attribute") String attribute) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
    {
        return ManagerFilter.getManager().getAttribute(name, attribute);
    }

    @Method
    public AttributeList getAttributes(@Param(name = "name") ObjectName name,
                                       @Param(name = "attributes") String[] attributes) throws InstanceNotFoundException, ReflectionException
    {
        return ManagerFilter.getManager().getAttributes(name, attributes);
    }

    @Method
    public void setAttribute(@Param(name = "name") ObjectName name,
                             @Param(name = "attribute") Attribute attribute) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        ManagerFilter.getManager().setAttribute(name, attribute);
    }

    @Method
    public AttributeList setAttributes(@Param(name = "name") ObjectName name,
                                       @Param(name = "attributes") AttributeList attributes) throws InstanceNotFoundException, ReflectionException
    {
        return ManagerFilter.getManager().setAttributes(name, attributes);
    }

    @Method
    public Object invoke(@Param(name = "name") ObjectName name,
                         @Param(name = "operationName") String operationName,
                         @Param(name = "params") Object[] params,
                         @Param(name = "signature") String[] signature) throws InstanceNotFoundException, MBeanException, ReflectionException
    {
        return ManagerFilter.getManager().invoke(name, operationName, params, signature);
    }

    @Method
    public void addNotificationListener(@Param(name = "sessionId") ObjectName name) throws InstanceNotFoundException
    {
        ManagerFilter.getManager().addNotificationListener(name);
    }

    @Method
    public void addNotificationListenerForListener(@Param(name = "name") ObjectName name,
                                                   @Param(name = "listener") ObjectName listener,
                                                   @Param(name = "handback") String handback) throws InstanceNotFoundException
    {
        ManagerFilter.getManager().addNotificationListener(name, listener, handback);
    }

    @Method
    public void removeNotificationListener(@Param(name = "name") ObjectName name) throws InstanceNotFoundException, ListenerNotFoundException
    {
        ManagerFilter.getManager().removeNotificationListener(name);
    }

    @Method
    public void removeNotificationListenerForListener(@Param(name = "name") ObjectName name,
                                                      @Param(name = "listener") ObjectName listener,
                                                      @Param(name = "handback") String handback) throws InstanceNotFoundException, ListenerNotFoundException
    {
        ManagerFilter.getManager().removeNotificationListener(name, listener, handback);
    }

    @Method
    public MBeanInfo getMBeanInfo(@Param(name = "name") ObjectName name) throws InstanceNotFoundException, IntrospectionException, ReflectionException
    {
        return ManagerFilter.getManager().getMBeanInfo(name);
    }

    @Method
    public boolean isInstanceOf(@Param(name = "name") ObjectName name,
                                @Param(name = "className") String className) throws InstanceNotFoundException
    {
        return ManagerFilter.getManager().isInstanceOf(name, className);
    }
}
