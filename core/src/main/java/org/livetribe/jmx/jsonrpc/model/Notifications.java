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
    private long next;
    private long smallest;

    public Notifications(List<Notification> notifications, long next, long smallest)
    {
        this.notifications = notifications;
        this.next = next;
        this.smallest = smallest;
    }

    public Notifications(long next, long smallest)
    {
        this.next = next;
        this.smallest = smallest;
    }

    public List<Notification> getNotifications()
    {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications)
    {
        this.notifications = notifications;
    }

    public long getNext()
    {
        return next;
    }

    public void setNext(long next)
    {
        this.next = next;
    }

    public long getSmallest()
    {
        return smallest;
    }

    public void setSmallest(long smallest)
    {
        this.smallest = smallest;
    }
}
