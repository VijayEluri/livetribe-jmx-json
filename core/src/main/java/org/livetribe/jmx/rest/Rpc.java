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


import javax.inject.Inject;
import java.util.Map;

import com.toolazydogs.jr4me.api.Method;
import com.toolazydogs.jr4me.api.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.livetribe.jmx.rest.model.Session;


/**
 *
 */
public class Rpc
{
    static final Logger LOG = LoggerFactory.getLogger(Rpc.class);
    @Inject Manager manager;

    @Method(name = "createSession")
    public Integer createSession(@Param(name = "inactivityTimeout") int inactivityTimeout,
                                 @Param(name = "pollingTimeout") int pollingTimeout,
                                 @Param(name = "maxNotifications") int maxNotifications)
    {
        return manager.createSession(inactivityTimeout, pollingTimeout, maxNotifications);
    }

    @Method(name = "deleteSession")
    public void deleteSession(@Param(name = "sessionId") int sessionId)
    {
        manager.deleteSession(sessionId);
    }

    @Method(name = "getSession")
    public Session getSession(@Param(name = "sessionId") int sessionId)
    {
        return manager.getSession(sessionId);
    }

    @Method(name = "getSessionProperty")
    public Map<String, Object> getSessionProperty(@Param(name = "sessionId") int sessionId,
                                                  @Param(name = "name") String name)
    {
        return manager.getSessionProperty(sessionId, name);
    }

    @Method(name = "setSessionProperty")
    public Integer setSessionProperty(@Param(name = "sessionId") int sessionId,
                                      @Param(name = "name") String name,
                                      @Param(name = "value") int value)
    {
        return manager.setSessionProperty(sessionId, name, value);
    }

    @Method(name = "getDefaultDomain")
    public String getDefaultDomain()
    {
        return manager.getDefaultDomain();
    }

    @Method(name = "getDomains")
    public String[] getDomains()
    {
        return manager.getDomains();
    }

    @Method(name = "getMBeanCount")
    public Integer getMBeanCount()
    {
        return 15;
//        return manager.getMBeanCount();
    }
}
