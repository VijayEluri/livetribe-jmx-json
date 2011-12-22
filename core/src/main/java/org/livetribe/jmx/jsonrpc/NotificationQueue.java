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

import javax.management.ObjectName;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.livetribe.jmx.jsonrpc.model.Notification;
import org.livetribe.jmx.jsonrpc.model.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class NotificationQueue implements Queue<Notification>
{
    static final Logger LOG = LoggerFactory.getLogger(NotificationQueue.class);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final int capacity;
    private final Queue<Notification> queue;
    private long first;

    public NotificationQueue(int capacity)
    {
        assert capacity > 0;

        this.capacity = capacity;
        this.queue = new LinkedList<Notification>();
    }

    public long getFirst()
    {
        lock.lock(); // for visibility
        try
        {
            return first;
        }
        finally
        {
            lock.unlock();
        }
    }

    public int getCapacity()
    {
        return capacity;
    }

    @Override
    public boolean add(Notification notification)
    {
        lock.lock();
        try
        {
            if (queue.size() == capacity)
            {
                LOG.trace("Queue at capacity");
                first++;
                queue.remove();
            }
            return queue.add(notification);
        }
        finally
        {
            notEmpty.signalAll();
            lock.unlock();
        }
    }

    @Override
    public boolean offer(Notification notification)
    {
        return add(notification);
    }

    @Override
    public Notification remove()
    {
        lock.lock();
        try
        {
            if (!queue.isEmpty()) first++;
            return queue.remove();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Notification poll()
    {
        lock.lock();
        try
        {
            first++;
            return queue.poll();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Notification element()
    {
        lock.lock();
        try
        {
            return queue.element();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Notification peek()
    {
        lock.lock();
        try
        {
            return queue.peek();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public int size()
    {
        lock.lock();
        try
        {
            return queue.size();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty()
    {
        lock.lock();
        try
        {
            return queue.isEmpty();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o)
    {
        lock.lock();
        try
        {
            return queue.contains(o);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Iterator<Notification> iterator()
    {
        lock.lock();
        try
        {
            return queue.iterator();
        }
        finally
        {
            lock.unlock();
        }
    }

    public Notifications fetch(long start, int max, Set<ObjectName> names, long timeout, TimeUnit unit) throws InterruptedException
    {
        long nanos = unit.toNanos(timeout);

        lock.lockInterruptibly();
        try
        {
            /**
             * index "moves" along the queue as elements are checked against the names.
             * This allows the structure to wait if no elements match the set of names.
             */
            long index = start;
            int next = 0;
            long smallest = -1;

            List<Notification> list = new LinkedList<Notification>();
            while (list.isEmpty())
            {
                while (first + queue.size() <= index)
                {
                    if (nanos <= 0)
                    {
                        LOG.trace("Timeout");
                        return new Notifications(index, 0);
                    }
                    LOG.trace("Waiting {}ns", nanos);
                    nanos = notEmpty.awaitNanos(nanos);
                }

                LOG.trace("No longer waiting");

                int begin = (int)(Math.max(start, first) - first);
                next = (int)first + begin;
                Iterator<Notification> iterator = queue.iterator();

                for (int i = 0; i < begin; i++) iterator.next();

                while (iterator.hasNext())
                {
                    index++;
                    next++;
                    Notification notification = iterator.next();
                    if (names.contains(notification.getSource()))
                    {
                        if (smallest < 0) smallest = next - 1;
                        list.add(notification);
                        if (list.size() == max) break;
                    }
                }
            }

            return new Notifications(list, next, smallest);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public Object[] toArray()
    {
        lock.lock();
        try
        {
            return queue.toArray();
        }
        finally
        {
            lock.unlock();
        }
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(T[] a)
    {
        lock.lock();
        try
        {
            return queue.toArray(a);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        lock.lock();
        try
        {
            return queue.containsAll(c);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends Notification> c)
    {
        lock.lock();
        try
        {
            for (Notification notification : c) add(notification);
            return !c.isEmpty();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        lock.lock();
        try
        {
            first += queue.size();
            queue.clear();
        }
        finally
        {
            lock.unlock();
        }
    }
}
