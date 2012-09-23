package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoConcurrentMapTest {
    
    MongoConcurrentMap<String, TestBean> map;
    private Mongo mongo;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        mongo = new Mongo("localhost", 27017);
        
        map = new MongoConcurrentMap<String, TestBean>(mongo, "testdb", "testcollection", TestBean.class,
                new SimpleFieldDBObjectSerializer<String>("key"), new JacksonDBObjectSerializer<TestBean>("value",
                        TestBean.class));
    }
    
    @After
    public void closeMongoConnection() {
        mongo.close();
    }
    
    @Test
    public void putGetAndRemove() {
        TestBean bean = new TestBean("testbeanname");
        
        map.put("key1", bean);
        assertEquals("testbeanname", map.get("key1").getName());
        assertEquals(1, map.size());
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsValue(bean));
        
        map.remove("key1");
        assertNull(map.get("key1"));
        assertEquals(0, map.size());
        assertFalse(map.containsKey("key1"));
        assertFalse(map.containsValue(bean));
    }
    
}
