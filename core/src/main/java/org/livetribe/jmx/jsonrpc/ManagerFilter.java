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
package org.livetribe.jmx.jsonrpc;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class ManagerFilter implements Filter
{
    static final Logger LOG = LoggerFactory.getLogger(ManagerFilter.class);
    private static ThreadLocal<Manager> MANAGERS = new ThreadLocal<Manager>();
    private final Manager manager;

    public ManagerFilter(Manager manager)
    {
        assert manager != null;
        this.manager = manager;
    }

    public static Manager getManager()
    {
        return MANAGERS.get();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        MANAGERS.set(manager);
        try
        {
            chain.doFilter(request, response);
        }
        finally
        {
            MANAGERS.set(null);
        }
    }

    @Override
    public void destroy()
    {
    }
}
