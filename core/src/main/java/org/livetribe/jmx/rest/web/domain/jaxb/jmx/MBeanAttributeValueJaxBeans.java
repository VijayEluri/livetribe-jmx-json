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
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
@XmlRootElement
public class MBeanAttributeValueJaxBeans
{
    @XmlElement(name = "Attribute")
    public Set<MBeanAttributeValueJaxBean> mBeanAttributeValueJaxBeans = new TreeSet<MBeanAttributeValueJaxBean>();

    public MBeanAttributeValueJaxBeans()
    {
    }

    public MBeanAttributeValueJaxBeans(Set<MBeanAttributeValueJaxBean> mBeanAttributeValueJaxBeans)
    {
        this.mBeanAttributeValueJaxBeans = mBeanAttributeValueJaxBeans;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("MBeanAttributeValuesJaxBean [mBeanAttributeValueJaxBeans=");
        builder.append(mBeanAttributeValueJaxBeans);
        builder.append("]");
        return builder.toString();
    }
}
