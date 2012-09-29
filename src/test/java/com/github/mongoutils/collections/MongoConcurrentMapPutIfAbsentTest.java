package com.github.mongoutils.collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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
public class MongoConcurrentMapPutIfAbsentTest {
    
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
    DBObject keyResultObject;
    @Mock
    DBObject valueQueryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<TestBean> valueCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyResultObject);
        when(valueSerializer.toDBObject(any(TestBean.class), anyBoolean(), anyBoolean())).thenReturn(valueQueryObject);
        map = spy(new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer));
    }
    
    @Test
    public void putNotExistingValue() {
        TestBean testBean = new TestBean("testbean");
        
        when(collection.findAndModify(keyQueryObject, null, null, false, keyResultObject, true, true)).thenReturn(
                keyResultObject);
        when(valueSerializer.toElement(keyResultObject)).thenReturn(testBean);
        
        assertSame(testBean, map.putIfAbsent("key", testBean));
        
        verify(keySerializer, times(2)).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer).toElement(keyResultObject);
        verify(collection).findAndModify(keyQueryObject, null, null, false, keyResultObject, true, true);
        verify(keyResultObject).putAll(valueQueryObject);
    }
    
    @Test
    public void putExistingValue() {
        TestBean testBean = new TestBean("testbean");
        TestBean otherBean = new TestBean("otherBean");
        
        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(otherBean);
        
        assertSame(otherBean, map.putIfAbsent("key", testBean));
        
        verify(keySerializer, times(2)).toDBObject("key", true, false);
        verify(keySerializer, never()).toDBObject("key", false, false);
        verify(valueSerializer, never()).toDBObject(testBean, false, false);
        verify(valueSerializer).toElement(resultObject);
        verify(collection, never()).findAndModify(keyQueryObject, null, null, false, keyResultObject, true, true);
        verify(keyResultObject, never()).putAll(valueQueryObject);
    }
    
}
