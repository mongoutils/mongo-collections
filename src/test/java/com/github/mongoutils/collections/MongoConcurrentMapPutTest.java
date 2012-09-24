package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
    DBObject keyQueryObject;
    @Mock
    DBObject valueQueryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<TestBean> valueCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(any(TestBean.class), anyBoolean(), anyBoolean())).thenReturn(valueQueryObject);
        map = new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer);
    }
    
    @Test
    public void putNotExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(collection.findOne(keyQueryObject)).thenReturn(null);
        
        assertNull(map.put("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        
        verify(collection).findOne(keyQueryObject);
        
        verify(keySerializer).toDBObject("key", false, false);
        
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(keyQueryObject).putAll(valueQueryObject);
        verify(collection).save(keyQueryObject);
    }
    
    @Test
    public void replaceExisting() {
        TestBean testBean = new TestBean("testBean");
        TestBean result;
        
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean);
        
        result = map.put("key", testBean);
        assertNotNull(result);
        assertEquals(testBean, result);
        
        verify(keySerializer).toDBObject("key", true, false);
        
        verify(collection).findOne(keyQueryObject);
        
        verify(valueSerializer).toElement(resultObject);
        
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(resultObject).putAll(valueQueryObject);
        verify(collection).save(resultObject);
    }
    
}
