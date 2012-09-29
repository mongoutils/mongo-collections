package com.github.mongoutils.collections;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapPutTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObject queryDBObject;
    @Mock
    DBObject keyDBObject;
    @Mock
    DBObject valueDBObject;
    @Mock
    DBObject oldDBObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer);
    }
    
    @Test
    public void putNotExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(queryDBObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyDBObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueDBObject);
        when(collection.findAndModify(queryDBObject, null, null, false, keyDBObject, false, true)).thenReturn(null);
        
        assertNull(map.put("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer, never()).toElement(any(DBObject.class));
        verify(collection).findAndModify(queryDBObject, null, null, false, keyDBObject, false, true);
        verify(keyDBObject).putAll(valueDBObject);
    }
    
    @Test
    public void replaceExisting() {
        TestBean testBean = new TestBean("testBean");
        TestBean old;
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(queryDBObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyDBObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueDBObject);
        when(valueSerializer.toElement(oldDBObject)).thenReturn(testBean);
        when(collection.findAndModify(queryDBObject, null, null, false, keyDBObject, false, true)).thenReturn(
                oldDBObject);
        
        old = map.put("key", testBean);
        assertSame(testBean, old);
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer).toElement(oldDBObject);
        verify(collection).findAndModify(queryDBObject, null, null, false, keyDBObject, false, true);
        verify(keyDBObject).putAll(valueDBObject);
    }
    
}
