package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MongoConcurrentMapPutIT extends AbstractMongoIT {
    
    @Test
    public void putSingle() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection,
                keySerializer, valueSerializer);
        TestBean bean = new TestBean("myBean's-Name");
        
        assertFalse(map.containsKey("key1"));
        
        assertNull(map.put("key1", bean));
        
        assertTrue(map.containsKey("key1"));
        assertEquals("myBean's-Name", map.get("key1").getName());
    }
    
    @Test
    public void putSameMultipleTimes() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection,
                keySerializer, valueSerializer);
        TestBean bean = new TestBean("myBean's-Name");
        
        assertFalse(map.containsKey("key1"));
        assertEquals(0, map.size());
        assertEquals(0, dbCollection.count());
        
        assertNull(map.put("key1", bean));
        assertEquals("myBean's-Name", map.put("key1", bean).getName());
        assertEquals("myBean's-Name", map.put("key1", bean).getName());
        assertEquals("myBean's-Name", map.put("key1", bean).getName());
        
        assertTrue(map.containsKey("key1"));
        assertEquals("myBean's-Name", map.get("key1").getName());
        assertEquals(1, map.size());
        assertEquals(1, dbCollection.count());
    }
    
    @Test
    public void replaceExisting() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection,
                keySerializer, valueSerializer);
        TestBean bean1 = new TestBean("bean1");
        TestBean bean2 = new TestBean("bean2");
        TestBean bean3 = new TestBean("bean3");
        
        assertFalse(map.containsKey("key1"));
        assertEquals(0, map.size());
        assertEquals(0, dbCollection.count());
        
        assertNull(map.put("key1", bean1));
        assertTrue(map.containsKey("key1"));
        assertEquals("bean1", map.get("key1").getName());
        assertEquals(1, map.size());
        assertEquals(1, dbCollection.count());
        
        assertEquals(bean1, map.put("key1", bean2));
        assertTrue(map.containsKey("key1"));
        assertEquals("bean2", map.get("key1").getName());
        assertEquals(1, map.size());
        assertEquals(1, dbCollection.count());
        
        assertEquals(bean2, map.put("key1", bean3));
        assertTrue(map.containsKey("key1"));
        assertEquals("bean3", map.get("key1").getName());
        assertEquals(1, map.size());
        assertEquals(1, dbCollection.count());
    }
    
}
