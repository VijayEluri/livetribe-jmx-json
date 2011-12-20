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

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.acme.AcmeClassLoader;
import com.acme.Hello;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 */
public class ManagerTest
{
    private ScheduledExecutorService executorService;
    private ObjectName HELLO_OBJECT_NAME;
    private ObjectName PARENT_OBJECT_NAME;

    @Test
    public void testBadMBeanServer() throws Exception
    {
        try
        {
            new Manager(null, executorService);
            fail("Constructor should have thrown NullPointerException");
        }
        catch (NullPointerException ignored)
        {
        }

        try
        {
            new Manager(ManagementFactory.getPlatformMBeanServer(), null);
            fail("Constructor should have thrown NullPointerException");
        }
        catch (NullPointerException ignored)
        {
        }
    }

    @Test
    public void testGetMBeanCount() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getMBeanCount()).thenReturn(123);

        Manager manager = new Manager(mBeanServer, executorService);

        assertEquals((int)manager.getMBeanCount(), 123);
    }

    @Test
    public void testGetDomains() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getDomains()).thenReturn(new String[]{"A", "B", "C"});

        Manager manager = new Manager(mBeanServer, executorService);

        assertEquals(manager.getDomains(), new String[]{"A", "B", "C"});
    }

    @Test
    public void testGetDefaultDomain() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getDefaultDomain()).thenReturn("DEFAULT_DOMAIN");

        Manager manager = new Manager(mBeanServer, executorService);

        assertEquals(manager.getDefaultDomain(), "DEFAULT_DOMAIN");
    }

    @Test
    public void testCreateMBeanClassName() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService);

        try
        {
            assertEquals(manager.createMBean("com.acme.Hello", HELLO_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

            assertEquals(mBeanServer.getAttribute(HELLO_OBJECT_NAME, "Name"),
                         "default");
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
        }
    }

    @Test
    public void testCreateMBeanClassNameLoaderName() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try
        {
            mBeanServer.registerMBean(new AcmeClassLoader(Hello.class.getClassLoader()), PARENT_OBJECT_NAME);

            Manager manager = new Manager(mBeanServer, executorService);

            assertEquals(manager.createMBean("com.acme.Hello",
                                             HELLO_OBJECT_NAME,
                                             PARENT_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

            assertEquals(mBeanServer.getAttribute(ObjectName.getInstance("com.acme.Hello:type=Hello"), "Name"),
                         "default");
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
            mBeanServer.unregisterMBean(PARENT_OBJECT_NAME);
        }
    }

    @Test
    public void testCreateMBeanClassNameParamsSignature() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService);

        try
        {
            assertEquals(manager.createMBean("com.acme.Hello",
                                             HELLO_OBJECT_NAME,
                                             new Object[]{"FOO"},
                                             new String[]{String.class.getName()}),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

            assertEquals(mBeanServer.getAttribute(HELLO_OBJECT_NAME, "Name"),
                         "FOO");
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
        }
    }

    @Test
    public void testCreateMBeanClassNameLoaderNameParamsSignature() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService);

        try
        {
            mBeanServer.registerMBean(new AcmeClassLoader(Hello.class.getClassLoader()), PARENT_OBJECT_NAME);

            assertEquals(manager.createMBean("com.acme.Hello",
                                             HELLO_OBJECT_NAME,
                                             PARENT_OBJECT_NAME,
                                             new Object[]{"FOO"},
                                             new String[]{String.class.getName()}),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

            assertEquals(mBeanServer.getAttribute(HELLO_OBJECT_NAME, "Name"),
                         "FOO");
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
            mBeanServer.unregisterMBean(PARENT_OBJECT_NAME);
        }
    }

    @Test
    public void testRegisterMBean() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService);

        try
        {
            manager.registerMBean(new AcmeClassLoader(Hello.class.getClassLoader()), PARENT_OBJECT_NAME);

            assertEquals(mBeanServer.createMBean("com.acme.Hello",
                                                 HELLO_OBJECT_NAME,
                                                 PARENT_OBJECT_NAME,
                                                 new Object[]{"FOO"},
                                                 new String[]{String.class.getName()}),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

            assertEquals(mBeanServer.getAttribute(HELLO_OBJECT_NAME, "Name"),
                         "FOO");
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
            mBeanServer.unregisterMBean(PARENT_OBJECT_NAME);
        }
    }

    @Test
    public void testUnregisterMBean() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService);

        assertEquals(manager.createMBean("com.acme.Hello", HELLO_OBJECT_NAME),
                     new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));

        assertTrue(mBeanServer.isRegistered(HELLO_OBJECT_NAME));
        manager.unregisterMBean(HELLO_OBJECT_NAME);
        assertFalse(mBeanServer.isRegistered(HELLO_OBJECT_NAME));
    }

    @Test
    public void testGetObjectInstance() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Manager manager = new Manager(mBeanServer, executorService);

        try
        {
            mBeanServer.createMBean("com.acme.Hello", HELLO_OBJECT_NAME);

            assertEquals(manager.getObjectInstance(HELLO_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "com.acme.Hello"));
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
        }
    }

    @BeforeClass
    public void beforeClass() throws Exception
    {
        executorService = new ScheduledThreadPoolExecutor(10);
        HELLO_OBJECT_NAME = ObjectName.getInstance("com.acme.Hello:type=Hello");
        PARENT_OBJECT_NAME = ObjectName.getInstance("com.acme.Hello:type=Parent");
    }

    @AfterClass
    public void afterClass() throws Exception
    {
        executorService.shutdownNow();
    }
}
