package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapRemoveTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObject keyQueryObject;
    @Mock
    DBObject valueQueryObject;
    @Mock
    DBObject resultObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer);
    }
    
    @Test
    public void removeNotExisting() {
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(collection.findAndRemove(keyQueryObject)).thenReturn(null);
        
        assertNull(map.remove("key"));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(collection).findAndRemove(keyQueryObject);
        verify(valueSerializer, never()).toElement(any(DBObject.class));
    }
    
    @Test
    public void removeExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(collection.findAndRemove(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean);
        
        assertSame(testBean, map.remove("key"));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(collection).findAndRemove(keyQueryObject);
        verify(valueSerializer).toElement(resultObject);
    }
    
    @Test
    public void removeValueEqual() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(1);
        
        assertTrue(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
    @Test
    public void removeValueNotEqual() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(0);
        
        assertFalse(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
    @Test
    public void removeValueNotExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(0);
        
        assertFalse(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
}
