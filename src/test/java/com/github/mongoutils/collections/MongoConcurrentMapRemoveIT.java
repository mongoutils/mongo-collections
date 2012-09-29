package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MongoConcurrentMapRemoveIT extends AbstractMongoIT {
    
    @Test
    public void removeExisting() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection,
                keySerializer, valueSerializer);
        TestBean bean = new TestBean("bean1");
        
        assertFalse(map.containsKey("key1"));
        assertNull(map.put("key1", bean));
        assertTrue(map.containsKey("key1"));
        assertEquals(bean, map.get("key1"));
        
        assertEquals(bean, map.remove("key1"));
        assertFalse(map.containsKey("key1"));
        assertNull(map.get("key1"));
    }
    
    @Test
    public void removeNotExisting() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection,
                keySerializer, valueSerializer);
        
        assertFalse(map.containsKey("key1"));
        assertNull(map.remove("key1"));
        assertFalse(map.containsKey("key1"));
        assertNull(map.get("key1"));
    }
    
}
