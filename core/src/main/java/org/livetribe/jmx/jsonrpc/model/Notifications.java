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
package org.livetribe.jmx.jsonrpc.model;

import java.util.Collections;
import java.util.List;


/**
 *
 */
public class Notifications
{
    private List<Notification> notifications = Collections.emptyList();
    private long n;
    private long f;

    public List<Notification> getNotifications()
    {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications)
    {
        this.notifications = notifications;
    }

    public long getN()
    {
        return n;
    }

    public void setN(long n)
    {
        this.n = n;
    }

    public long getF()
    {
        return f;
    }

    public void setF(long f)
    {
        this.f = f;
    }
}
