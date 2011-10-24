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
package org.livetribe.jmx.rest.web.domain.jaxb.jmx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 */
@XmlRootElement(name = "AttributeValues")
public class MBeanAttributeValueJaxBean implements Comparable<MBeanAttributeValueJaxBean>
{
    @XmlElement(name = "AttributeName")
    public String attributeName;
    @XmlElement(name = "NodeName")
    public String nodeName;
    @XmlElement(name = "ObjectName")
    public String objectName;
    @XmlElement(name = "Value")
    public String value;

    public MBeanAttributeValueJaxBean()
    {
    }

    public MBeanAttributeValueJaxBean(String attributeName, String nodeName, String objectName, Object value)
    {
        this.attributeName = attributeName;
        this.nodeName = nodeName;
        this.objectName = objectName;
        parseValue(value);
    }

    private void parseValue(Object value)
    {
        if (value == null)
        {
            this.value = "null";
        }
        else if (value instanceof Object[])
        {
            Object[] values = (Object[])value;
            StringBuilder sb = new StringBuilder();
            for (Object string : values)
            {
                if (sb.length() != 0)
                    sb.append(", ");
                sb.append(string);
            }
            this.value = sb.toString();
        }
        else
        {
            this.value = value.toString();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MBeanAttributeValueJaxBean [attributeName=");
        builder.append(attributeName);
        builder.append(", nodeName=");
        builder.append(nodeName);
        builder.append(", objectName=");
        builder.append(objectName);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }

    public int compareTo(MBeanAttributeValueJaxBean o)
    {
        int i = this.attributeName.compareTo(o.attributeName);
        if (i == 0)
            i = this.nodeName.compareTo(o.nodeName);
        if (i == 0)
            i = this.objectName.compareTo(o.objectName);
        return i;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
        result = prime * result + ((nodeName == null) ? 0 : nodeName.hashCode());
        result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MBeanAttributeValueJaxBean other = (MBeanAttributeValueJaxBean)obj;
        if (attributeName == null)
        {
            if (other.attributeName != null)
                return false;
        }
        else if (!attributeName.equals(other.attributeName))
            return false;
        if (nodeName == null)
        {
            if (other.nodeName != null)
                return false;
        }
        else if (!nodeName.equals(other.nodeName))
            return false;
        if (objectName == null)
        {
            if (other.objectName != null)
                return false;
        }
        else if (!objectName.equals(other.objectName))
            return false;
        if (value == null)
        {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
}
