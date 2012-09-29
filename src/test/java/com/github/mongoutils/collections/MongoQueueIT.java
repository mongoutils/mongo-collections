package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class MongoQueueIT extends AbstractMongoIT {
    
    @Test
    public void offerAndPoll() {
        DBObjectSerializer<String> serializer = new SimpleFieldDBObjectSerializer<String>("value");
        MongoQueue<String> queue = new MongoQueue<String>(dbCollection, serializer);
        
        assertEquals(0, queue.size());
        queue.offer("MyValue");
        assertEquals(1, queue.size());
        assertEquals("MyValue", queue.peek());
        assertEquals(1, queue.size());
        assertEquals("MyValue", queue.poll());
        assertEquals(0, queue.size());
    }
    
    @Test
    public void offerAndPollMany() {
        DBObjectSerializer<String> serializer = new SimpleFieldDBObjectSerializer<String>("value");
        MongoQueue<String> queue = new MongoQueue<String>(dbCollection, serializer);
        int count = 1000;
        
        assertEquals(0, queue.size());
        for (int i = 0; i < count; i++) {
            queue.offer("MyValue" + i);
        }
        assertEquals(count, queue.size());
        for (int i = 0; i < count; i++) {
            assertEquals("MyValue" + i, queue.poll());
        }
        assertEquals(0, queue.size());
    }
    
    @Test
    public void offerAndPollManyMultithreaded() throws Exception {
        DBObjectSerializer<String> serializer = new SimpleFieldDBObjectSerializer<String>("value");
        final MongoQueue<String> queue = new MongoQueue<String>(dbCollection, serializer);
        final int count = 1000;
        final AtomicInteger counter = new AtomicInteger();
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
        ExecutorService service = Executors.newFixedThreadPool(4);
        
        assertEquals(0, queue.size());
        
        for (int i = 0; i < 4; i++) {
            tasks.add(new Callable<Void>() {
                
                @Override
                public Void call() throws Exception {
                    while (counter.get() < count) {
                        synchronized (queue) {
                            if (counter.get() < count) {
                                queue.offer("MyValue" + counter.incrementAndGet());
                            }
                        }
                    }
                    
                    return null;
                }
                
            });
        }
        service.invokeAll(tasks);
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        
        assertEquals(count, queue.size());
        for (int i = 0; i < count; i++) {
            assertEquals("MyValue" + (i + 1), queue.poll());
        }
        assertEquals(0, queue.size());
    }
    
}
