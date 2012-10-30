package com.github.mongoutils.collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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
    @Captor
    ArgumentCaptor<Map<String, TestBean>> dataCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(keyQueryObject);
        map = spy(new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer));
        map.putCommand = spy(map.putCommand);
    }
    
    @Test
    public void verifyPuts() {
        final TestBean testBean1 = new TestBean("testebean1");
        final TestBean testBean2 = new TestBean("testebean2");
        
        map.putAll(new HashMap<String, TestBean>() {
            
            private static final long serialVersionUID = 6031071434688590652L;
            
            {
                put("key1", testBean1);
                put("key2", testBean2);
            }
            
        });
        
        verify(map.putCommand).putAll(eq(collection), eq(keySerializer), eq(valueSerializer), dataCaptor.capture());
        assertSame(testBean1, dataCaptor.getValue().get("key1"));
        assertSame(testBean2, dataCaptor.getValue().get("key2"));
    }
    
}
