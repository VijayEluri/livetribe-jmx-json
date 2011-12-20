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

import javax.management.ObjectName;


/**
 *
 */
public class Notification
{
    /**
     * @serial The notification type.
     * A string expressed in a dot notation similar to Java properties.
     * An example of a notification type is network.alarm.router
     */
    private String type;

    /**
     * @serial The notification sequence number.
     * A serial number which identify particular instance
     * of notification in the context of the notification source.
     */
    private long sequenceNumber;

    /**
     * @serial The notification timestamp.
     * Indicating when the notification was generated
     */
    private long timeStamp;

    /**
     * @serial The notification user data.
     * Used for whatever other data the notification
     * source wishes to communicate to its consumers
     */
    private Object userData = null;

    /**
     * @serial The notification message.
     */
    private String message = "";

    /**
     * @serial The object on which the notification initially occurred.
     */
    private ObjectName source = null;

    public Notification()
    {
    }

    public Notification(String type, long sequenceNumber, long timeStamp, Object userData, String message, ObjectName source)
    {
        this.type = type;
        this.sequenceNumber = sequenceNumber;
        this.timeStamp = timeStamp;
        this.userData = userData;
        this.message = message;
        this.source = source;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public long getSequenceNumber()
    {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public Object getUserData()
    {
        return userData;
    }

    public void setUserData(Object userData)
    {
        this.userData = userData;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public ObjectName getSource()
    {
        return source;
    }

    public void setSource(ObjectName source)
    {
        this.source = source;
    }
}
