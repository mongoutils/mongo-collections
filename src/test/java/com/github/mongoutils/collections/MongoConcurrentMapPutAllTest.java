package com.github.mongoutils.collections;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapPutAllTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObject keyQueryObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(keyQueryObject);
        map = spy(new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer));
    }
    
    @Test
    public void verifyPuts() {
        final TestBean teatBean1 = new TestBean("testebean1");
        final TestBean teatBean2 = new TestBean("testebean2");
        
        map.putAll(new HashMap<String, TestBean>() {
            
            private static final long serialVersionUID = 6031071434688590652L;
            
            {
                put("key1", teatBean1);
                put("key2", teatBean2);
            }
            
        });
        
        verify(map).put("key1", teatBean1);
        verify(map).put("key2", teatBean2);
    }
    
}
