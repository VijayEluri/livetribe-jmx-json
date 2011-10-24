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
package org.livetribe.jmx.rest.web.domain.jaxb;

import javax.management.remote.JMXServiceURL;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

import org.livetribe.jmx.rest.web.Builder;
import org.livetribe.jmx.rest.web.util.JMXServiceURLUtils;


/**
 *
 */
@XmlRootElement(name = "Node")
public class NodeJaxBean implements Comparable<NodeJaxBean>
{
    public final String name;
    public final String jettyVersion;
    public final int threadCount;
    public final int peakThreadCount;
    public final Long heapUsed;
    public final Long heapInit;
    public final Long heapCommitted;
    public final Long heapMax;
    public final String jmxServiceURL;

    public NodeJaxBean()
    {
        this(null, null, 0, 0, null, null, null, null, null);
    }

    private NodeJaxBean(String name, String jettyVersion, int threadCount, int peakThreadCount, Long heapUsed, Long init, Long committed, Long max, JMXServiceURL jmxServiceURL)
    {
        this.name = name;
        this.threadCount = threadCount;
        this.peakThreadCount = peakThreadCount;
        this.jettyVersion = jettyVersion;
        this.heapInit = init;
        this.heapUsed = heapUsed;
        this.heapCommitted = committed;
        this.heapMax = max;
        this.jmxServiceURL = jmxServiceURL.toString();
    }

    public static class NodeJaxBeanBuilder implements Builder<NodeJaxBean>
    {
        private final String name;
        private final String jettyVersion;
        private final JMXServiceURL jmxServiceURL;

        // optional
        private int threadCount = 0;
        private int peakThreadCount = 0;
        private Long heapUsed = 0L;
        private Long init = 0L;
        private Long committed = 0L;
        private Long max = 0L;

        public NodeJaxBeanBuilder(String name, String jettyVersion, JMXServiceURL jmxServiceURL)
        {
            this.name = name;
            this.jettyVersion = jettyVersion;
            this.jmxServiceURL = jmxServiceURL;
        }

        public NodeJaxBeanBuilder memory(Map<String, Long> memory)
        {
            heapUsed = memory.get("used");
            init = memory.get("init");
            committed = memory.get("committed");
            max = memory.get("max");

            return this;
        }

        public NodeJaxBeanBuilder threadCount(int threadCount)
        {
            this.threadCount = threadCount;
            return this;
        }

        public NodeJaxBeanBuilder peakThreadCount(int peakThreadCount)
        {
            this.peakThreadCount = peakThreadCount;
            return this;
        }

        public NodeJaxBean build()
        {
            return new NodeJaxBean(name, jettyVersion, threadCount, peakThreadCount, heapUsed, init, committed, max, jmxServiceURL);
        }
    }

    public String getName()
    {
        return name;
    }

    public String getJettyVersion()
    {
        return jettyVersion;
    }

    public int getThreadCount()
    {
        return threadCount;
    }

    public int getPeakThreadCount()
    {
        return peakThreadCount;
    }

    public Long getHeapUsed()
    {
        return heapUsed;
    }

    public Long getInit()
    {
        return heapInit;
    }

    public Long getCommitted()
    {
        return heapCommitted;
    }

    public Long getMax()
    {
        return heapMax;
    }

    public JMXServiceURL getJmxServiceURL()
    {
        return JMXServiceURLUtils.getJMXServiceURL(jmxServiceURL);
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (jettyVersion != null ? jettyVersion.hashCode() : 0);
        result = 31 * result + threadCount;
        result = 31 * result + peakThreadCount;
        result = 31 * result + (heapUsed != null ? heapUsed.hashCode() : 0);
        result = 31 * result + (heapInit != null ? heapInit.hashCode() : 0);
        result = 31 * result + (heapCommitted != null ? heapCommitted.hashCode() : 0);
        result = 31 * result + (heapMax != null ? heapMax.hashCode() : 0);
        result = 31 * result + (jmxServiceURL != null ? jmxServiceURL.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeJaxBean that = (NodeJaxBean)o;

        if (peakThreadCount != that.peakThreadCount) return false;
        if (threadCount != that.threadCount) return false;
        if (heapCommitted != null ? !heapCommitted.equals(that.heapCommitted) : that.heapCommitted != null) return false;
        if (heapInit != null ? !heapInit.equals(that.heapInit) : that.heapInit != null) return false;
        if (heapMax != null ? !heapMax.equals(that.heapMax) : that.heapMax != null) return false;
        if (heapUsed != null ? !heapUsed.equals(that.heapUsed) : that.heapUsed != null) return false;
        if (jettyVersion != null ? !jettyVersion.equals(that.jettyVersion) : that.jettyVersion != null) return false;
        if (jmxServiceURL != null ? !jmxServiceURL.equals(that.jmxServiceURL) : that.jmxServiceURL != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeJaxBean [name=");
        builder.append(name);
        builder.append(", jettyVersion=");
        builder.append(jettyVersion);
        builder.append(", threadCount=");
        builder.append(threadCount);
        builder.append(", peakThreadCount=");
        builder.append(peakThreadCount);
        builder.append(", heapUsed=");
        builder.append(heapUsed);
        builder.append(", init=");
        builder.append(heapInit);
        builder.append(", committed=");
        builder.append(heapCommitted);
        builder.append(", max=");
        builder.append(heapMax);
        builder.append(", jmxServiceURL=");
        builder.append(jmxServiceURL);
        builder.append("]");
        return builder.toString();
    }

    public int compareTo(NodeJaxBean o)
    {
        return this.name.compareTo(o.name);
    }
}
