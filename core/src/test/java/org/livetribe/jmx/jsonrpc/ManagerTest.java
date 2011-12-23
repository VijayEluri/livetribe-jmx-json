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
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.acme.AcmeClassLoader;
import com.acme.Hello;
import org.livetribe.jmx.jsonrpc.model.Notification;
import org.livetribe.jmx.jsonrpc.model.Notifications;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
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
    private ObjectName BROTHER_OBJECT_NAME;
    private ObjectName SISTER_OBJECT_NAME;

    @Test
    public void testBadMBeanServer() throws Exception
    {
        try
        {
            new Manager(null, executorService, 5);
            fail("Constructor should have thrown NullPointerException");
        }
        catch (AssertionError ignored)
        {
        }

        try
        {
            new Manager(ManagementFactory.getPlatformMBeanServer(), null, 5);
            fail("Constructor should have thrown NullPointerException");
        }
        catch (AssertionError ignored)
        {
        }
    }

    @Test
    public void testGetMBeanCount() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getMBeanCount()).thenReturn(123);

        Manager manager = new Manager(mBeanServer, executorService, 5);

        assertEquals((int)manager.getMBeanCount(), 123);
    }

    @Test
    public void testGetDomains() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getDomains()).thenReturn(new String[]{"A", "B", "C"});

        Manager manager = new Manager(mBeanServer, executorService, 5);

        assertEquals(manager.getDomains(), new String[]{"A", "B", "C"});
    }

    @Test
    public void testGetDefaultDomain() throws Exception
    {
        MBeanServer mBeanServer = mock(MBeanServer.class);
        when(mBeanServer.getDefaultDomain()).thenReturn("DEFAULT_DOMAIN");

        Manager manager = new Manager(mBeanServer, executorService, 5);

        assertEquals(manager.getDefaultDomain(), "DEFAULT_DOMAIN");
    }

    @Test
    public void testCreateMBeanClassName() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        Manager manager = new Manager(mBeanServer, executorService, 5);

        try
        {
            assertEquals(manager.createMBean("Hello", HELLO_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));

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

            Manager manager = new Manager(mBeanServer, executorService, 5);

            assertEquals(manager.createMBean("Hello",
                                             HELLO_OBJECT_NAME,
                                             PARENT_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));

            assertEquals(mBeanServer.getAttribute(HELLO_OBJECT_NAME, "Name"),
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

        Manager manager = new Manager(mBeanServer, executorService, 5);

        try
        {
            assertEquals(manager.createMBean("Hello",
                                             HELLO_OBJECT_NAME,
                                             new Object[]{"FOO"},
                                             new String[]{String.class.getName()}),
                         new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));

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

        Manager manager = new Manager(mBeanServer, executorService, 5);

        try
        {
            mBeanServer.registerMBean(new AcmeClassLoader(Hello.class.getClassLoader()), PARENT_OBJECT_NAME);

            assertEquals(manager.createMBean("Hello",
                                             HELLO_OBJECT_NAME,
                                             PARENT_OBJECT_NAME,
                                             new Object[]{"FOO"},
                                             new String[]{String.class.getName()}),
                         new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));

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

        Manager manager = new Manager(mBeanServer, executorService, 5);

        assertEquals(manager.createMBean("Hello", HELLO_OBJECT_NAME),
                     new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));

        assertTrue(mBeanServer.isRegistered(HELLO_OBJECT_NAME));
        manager.unregisterMBean(HELLO_OBJECT_NAME);
        assertFalse(mBeanServer.isRegistered(HELLO_OBJECT_NAME));
    }

    @Test
    public void testGetObjectInstance() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Manager manager = new Manager(mBeanServer, executorService, 5);

        try
        {
            mBeanServer.createMBean("Hello", HELLO_OBJECT_NAME);

            assertEquals(manager.getObjectInstance(HELLO_OBJECT_NAME),
                         new ObjectInstance(HELLO_OBJECT_NAME, "Hello"));
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
        }
    }

    @Test
    public void testInactivityTimeout() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Manager manager = new Manager(mBeanServer, executorService, 5);

        int sessionId = manager.createSession(1 /* 1s inactivity timeout */, 100 /* 100ms polling timeout */, 50);

        for (int i = 0; i < 15; i++)
        {
            Thread.sleep(100);
            assertNotNull(manager.getSession(sessionId));
        }

        Thread.sleep(1100);

        assertNull(manager.getSession(sessionId));
    }

    @Test
    public void testListener() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Manager manager = new Manager(mBeanServer, executorService, 5);

        Hello kitty = new Hello();
        try
        {
            mBeanServer.registerMBean(kitty, HELLO_OBJECT_NAME);

            int sessionId = manager.createSession(5 * 60 /* 5m inactivity timeout */, 5 * 60 * 1000 /* 5m polling timeout */, 50);
            manager.addNotificationListener(sessionId, HELLO_OBJECT_NAME);

            kitty.scratch();

            Notifications notifications = manager.fetchNotifications(sessionId, 0);
            assertNotNull(notifications);
            assertEquals(notifications.getSmallest(), 0);
            assertEquals(notifications.getNext(), 1);
            assertNotNull(notifications.getNotifications());
            assertEquals(notifications.getNotifications().size(), 1);

            Notification notification = notifications.getNotifications().get(0);
            assertEquals(notification.getType(), "purr");
            assertEquals(notification.getSequenceNumber(), 0);
            assertEquals(notification.getSource(), HELLO_OBJECT_NAME);
            assertEquals(notification.getMessage(), "Oooh!");
            assertTrue(Arrays.equals((String[])notification.getUserData(), new String[]{"collar", "pillow", "milk"}));

            manager.removeNotificationListener(sessionId, HELLO_OBJECT_NAME);

            kitty.tickle();

            notifications = manager.fetchNotifications(sessionId, 1);
            assertNotNull(notifications);
            assertEquals(notifications.getSmallest(), 0);
            assertEquals(notifications.getNext(), 1);
            assertNotNull(notifications.getNotifications());
            assertEquals(notifications.getNotifications().size(), 0);
        }
        finally
        {
            mBeanServer.unregisterMBean(HELLO_OBJECT_NAME);
        }
    }

    @Test
    public void testSessionListener() throws Exception
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Manager manager = new Manager(mBeanServer, executorService, 1024);

        Hello brother = new Hello("brother");
        Hello sister = new Hello("sister");
        try
        {
            mBeanServer.registerMBean(brother, BROTHER_OBJECT_NAME);
            mBeanServer.registerMBean(sister, SISTER_OBJECT_NAME);

            int session_1 = manager.createSession(5 * 60 /* 5m inactivity timeout */, 5 * 60 * 1000 /* 5m polling timeout */, 50);
            manager.addNotificationListener(session_1, BROTHER_OBJECT_NAME);
            int session_2 = manager.createSession(5 * 60 /* 5m inactivity timeout */, 5 * 60 * 1000 /* 5m polling timeout */, 50);
            manager.addNotificationListener(session_2, SISTER_OBJECT_NAME);

            sister.scratch();
            brother.scratch();
            sister.scratch();
            brother.scratch();
            sister.scratch();
            brother.scratch();

            Notifications notifications = manager.fetchNotifications(session_1, 0);
            assertNotNull(notifications);
            assertEquals(notifications.getSmallest(), 1);
            assertEquals(notifications.getNext(), 6);
            assertNotNull(notifications.getNotifications());
            assertEquals(notifications.getNotifications().size(), 3);

            Notification notification = notifications.getNotifications().get(0);
            assertEquals(notification.getType(), "purr");
            assertEquals(notification.getSequenceNumber(), 0);
            assertEquals(notification.getSource(), BROTHER_OBJECT_NAME);
            assertEquals(notification.getMessage(), "Oooh!");
            assertTrue(Arrays.equals((String[])notification.getUserData(), new String[]{"collar", "pillow", "milk"}));
            notification = notifications.getNotifications().get(1);
            assertEquals(notification.getType(), "purr");
            assertEquals(notification.getSequenceNumber(), 1);
            assertEquals(notification.getSource(), BROTHER_OBJECT_NAME);
            assertEquals(notification.getMessage(), "Oooh!");
            assertTrue(Arrays.equals((String[])notification.getUserData(), new String[]{"collar", "pillow", "milk"}));
            notification = notifications.getNotifications().get(2);
            assertEquals(notification.getType(), "purr");
            assertEquals(notification.getSequenceNumber(), 2);
            assertEquals(notification.getSource(), BROTHER_OBJECT_NAME);
            assertEquals(notification.getMessage(), "Oooh!");
            assertTrue(Arrays.equals((String[])notification.getUserData(), new String[]{"collar", "pillow", "milk"}));

            manager.removeNotificationListener(session_1, BROTHER_OBJECT_NAME);
            manager.removeNotificationListener(session_2, SISTER_OBJECT_NAME);

            brother.tickle();
            sister.tickle();
            brother.tickle();
            sister.tickle();
            brother.tickle();
            sister.tickle();

            notifications = manager.fetchNotifications(session_1, 1);
            assertNotNull(notifications);
            assertEquals(notifications.getSmallest(), 0);
            assertEquals(notifications.getNext(), 1);
            assertNotNull(notifications.getNotifications());
            assertEquals(notifications.getNotifications().size(), 0);
        }
        finally
        {
            mBeanServer.unregisterMBean(BROTHER_OBJECT_NAME);
            mBeanServer.unregisterMBean(SISTER_OBJECT_NAME);
        }
    }

    @BeforeClass
    public void beforeClass() throws Exception
    {
        executorService = new ScheduledThreadPoolExecutor(10);
        HELLO_OBJECT_NAME = ObjectName.getInstance("Hello:name=Kitty");
        PARENT_OBJECT_NAME = ObjectName.getInstance("Hello:type=Parent");
        BROTHER_OBJECT_NAME = ObjectName.getInstance("Hello:type=Brother");
        SISTER_OBJECT_NAME = ObjectName.getInstance("Hello:type=Sister");
    }

    @AfterClass
    public void afterClass() throws Exception
    {
        executorService.shutdownNow();
    }
}
