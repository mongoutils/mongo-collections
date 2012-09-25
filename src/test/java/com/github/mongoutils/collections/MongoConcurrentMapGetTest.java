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
public class MongoConcurrentMapGetTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObject queryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<TestBean> valueCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(queryObject);
        when(valueSerializer.toDBObject(any(TestBean.class), anyBoolean(), anyBoolean())).thenReturn(queryObject);
        map = new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer);
    }
    
    @Test
    public void getNotExisting() {
        when(collection.findOne(queryObject)).thenReturn(null);
        
        assertNull(map.get("key"));
        
        verify(keySerializer).toDBObject("key", true, false);
    }
    
    @Test
    public void getExisting() {
        TestBean testBean = new TestBean("testbean");
        TestBean result;
        
        when(collection.findOne(queryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean);
        
        result = map.get("key");
        assertNotNull(result);
        assertEquals(testBean, result);
        
        verify(keySerializer).toDBObject("key", true, false);
        
        verify(valueSerializer).toElement(resultObject);
    }
    
}
