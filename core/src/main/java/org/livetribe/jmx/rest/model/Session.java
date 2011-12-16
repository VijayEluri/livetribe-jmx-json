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
package org.livetribe.jmx.rest.model;

import java.util.Set;


/**
 * @author Alan D. Cabrera
 */
public class Session
{
    private int sessionId;
    private int inactivityTimeout;
    private int pollingTimeout;
    private int maxNotifications;
    private int calls;
    private Set<Listener> listeners;

    public Session()
    {
    }

    public Session(int sessionId, int inactivityTimeout, int pollingTimeout, int maxNotifications, int calls, Set<Listener> listeners)
    {
        this.sessionId = sessionId;
        this.inactivityTimeout = inactivityTimeout;
        this.pollingTimeout = pollingTimeout;
        this.maxNotifications = maxNotifications;
        this.calls = calls;
        this.listeners = listeners;
    }

    public int getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(int sessionId)
    {
        this.sessionId = sessionId;
    }

    public int getInactivityTimeout()
    {
        return inactivityTimeout;
    }

    public void setInactivityTimeout(int inactivityTimeout)
    {
        this.inactivityTimeout = inactivityTimeout;
    }

    public int getPollingTimeout()
    {
        return pollingTimeout;
    }

    public void setPollingTimeout(int pollingTimeout)
    {
        this.pollingTimeout = pollingTimeout;
    }

    public int getMaxNotifications()
    {
        return maxNotifications;
    }

    public void setMaxNotifications(int maxNotifications)
    {
        this.maxNotifications = maxNotifications;
    }

    public int getCalls()
    {
        return calls;
    }

    public void setCalls(int calls)
    {
        this.calls = calls;
    }

    public Set<Listener> getListeners()
    {
        return listeners;
    }

    public void setListeners(Set<Listener> listeners)
    {
        this.listeners = listeners;
    }
}
