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
package com.acme;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;


/**
 *
 */
public class Hello extends NotificationBroadcasterSupport implements HelloMBean
{
    private String name;
    private int cacheSize;
    private long sequenceNumber;

    public Hello()
    {
        this("default");
    }

    public Hello(String name)
    {
        super(null, new MBeanNotificationInfo[]{new MBeanNotificationInfo(new String[]{"purr", "claws", "teeth"}, "behaviors", "Behaviors of this little kitty")});
        this.name = name;
    }

    public void sayHello()
    {
    }

    public int add(int x, int y)
    {
        return x + y;
    }

    public String getName()
    {
        return name;
    }

    public int getCacheSize()
    {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize)
    {
        this.cacheSize = cacheSize;
    }

    public void tickle()
    {
        Notification notification = new Notification("claws", this, sequenceNumber++, System.currentTimeMillis(), "Stop that!");
        notification.setUserData(new String[]{"collar", "pillow", "milk"});
        sendNotification(notification);
    }

    public void scratch()
    {
        Notification notification = new Notification("purr", this, sequenceNumber++, System.currentTimeMillis(), "Oooh!");
        notification.setUserData(new String[]{"collar", "pillow", "milk"});
        sendNotification(notification);
    }

    public void bird()
    {
        Notification notification = new Notification("teeth", this, sequenceNumber++, System.currentTimeMillis(), "Dinner!");
        notification.setUserData(new String[]{"collar", "pillow", "milk"});
        sendNotification(notification);
    }
}
