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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.livetribe.jmx.jsonrpc.model.Notification;
import org.livetribe.jmx.jsonrpc.model.Notifications;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 */
public class NotificationQueueTest
{
    ObjectName objectName;

    @Test
    public void testAddFull()
    {
        NotificationQueue queue = new NotificationQueue(2);

        assertEquals(queue.getSmallest(), 0);
        assertEquals(queue.getCapacity(), 2);

        queue.add(new Notification("important", 0, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 0);
        queue.add(new Notification("important", 1, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 0);
        queue.add(new Notification("important", 2, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 1);
        queue.add(new Notification("important", 3, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 2);

        queue.clear();

        queue.offer(new Notification("important", 4, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 4);
        queue.offer(new Notification("important", 5, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 4);
        queue.offer(new Notification("important", 6, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 5);
        queue.offer(new Notification("important", 7, System.currentTimeMillis(), null, "Very important!", objectName));
        assertEquals(queue.getSmallest(), 6);
    }

    @Test
    public void testFetch() throws InterruptedException
    {
        final NotificationQueue queue = new NotificationQueue(3);

        queue.add(new Notification("important", 0, System.currentTimeMillis(), null, "Very important!", objectName));
        queue.add(new Notification("important", 1, System.currentTimeMillis(), null, "Very important!", objectName));
        queue.add(new Notification("important", 2, System.currentTimeMillis(), null, "Very important!", objectName));

        Notifications result = queue.fetch(0, 2, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(result.getSmallest(), 0);
        assertEquals(result.getNext(), 2);
        assertEquals(result.getNotifications().size(), 2);
        assertEquals(result.getNotifications().get(0).getSequenceNumber(), 0);
        assertEquals(result.getNotifications().get(1).getSequenceNumber(), 1);


        queue.add(new Notification("important", 3, System.currentTimeMillis(), null, "Very important!", objectName));

        result = queue.fetch(3, Integer.MAX_VALUE, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(result.getSmallest(), 3);
        assertEquals(result.getNext(), 4);
        assertEquals(result.getNotifications().size(), 1);
        assertEquals(result.getNotifications().get(0).getSequenceNumber(), 3);

        final AtomicReference<Notifications> atomicReference = new AtomicReference<Notifications>();
        final CountDownLatch start = new CountDownLatch(1);
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    start.countDown();
                    atomicReference.set(queue.fetch(4, Integer.MAX_VALUE, 1, TimeUnit.DAYS));
                }
                catch (InterruptedException ignored)
                {
                }
            }
        });

        thread.start();
        start.await();

        assertNull(atomicReference.get());

        queue.add(new Notification("important", 4, System.currentTimeMillis(), null, "Very important!", objectName));

        thread.join();

        result = atomicReference.get();
        assertNotNull(result);
        assertEquals(result.getSmallest(), 4);
        assertEquals(result.getNext(), 5);
        assertEquals(result.getNotifications().size(), 1);
        assertEquals(result.getNotifications().get(0).getSequenceNumber(), 4);

        queue.addAll(Collections.singleton(new Notification("important", 5, System.currentTimeMillis(), null, "Very important!", objectName)));

        result = queue.fetch(5, Integer.MAX_VALUE, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(result.getSmallest(), 5);
        assertEquals(result.getNext(), 6);
        assertEquals(result.getNotifications().size(), 1);
        assertEquals(result.getNotifications().get(0).getSequenceNumber(), 5);

        result = queue.fetch(0, 2, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(result.getSmallest(), 3);
        assertEquals(result.getNext(), 5);
        assertEquals(result.getNotifications().size(), 2);
        assertEquals(result.getNotifications().get(0).getSequenceNumber(), 3);
        assertEquals(result.getNotifications().get(1).getSequenceNumber(), 4);
        assertEquals(queue.size(), 3);

        queue.remove();
        assertEquals(queue.getSmallest(), 4);
        assertEquals(queue.size(), 2);

        queue.peek();
        assertEquals(queue.getSmallest(), 4);
        assertEquals(queue.size(), 2);

        queue.poll();
        assertEquals(queue.getSmallest(), 5);
        assertEquals(queue.size(), 1);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Test
    public void testUnsupported()
    {
        NotificationQueue queue = new NotificationQueue(3);

        try
        {
            queue.removeAll(Collections.emptySet());
            fail("Operation not supported");
        }
        catch (UnsupportedOperationException ignore)
        {
        }

        try
        {
            queue.retainAll(Collections.emptySet());
            fail("Operation not supported");
        }
        catch (UnsupportedOperationException ignore)
        {
        }

        try
        {
            queue.remove(null);
            fail("Operation not supported");
        }
        catch (UnsupportedOperationException ignore)
        {
        }
    }

    @BeforeClass
    public void beforeClass() throws MalformedObjectNameException
    {
        objectName = ObjectName.getInstance("com.acme:name=Hello");
    }
}
