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

import javax.management.ObjectName;


/**
 *
 */
public class Listener
{
    private ObjectName objectName;
    private int notificationListener;
    private String handback;

    public Listener()
    {
    }

    public Listener(ObjectName objectName, int notificationListener, String handback)
    {
        this.objectName = objectName;
        this.notificationListener = notificationListener;
        this.handback = handback;
    }

    public ObjectName getObjectName()
    {
        return objectName;
    }

    public void setObjectName(ObjectName objectName)
    {
        this.objectName = objectName;
    }

    public int getNotificationListener()
    {
        return notificationListener;
    }

    public void setNotificationListener(int notificationListener)
    {
        this.notificationListener = notificationListener;
    }

    public String getHandback()
    {
        return handback;
    }

    public void setHandback(String handback)
    {
        this.handback = handback;
    }
}
