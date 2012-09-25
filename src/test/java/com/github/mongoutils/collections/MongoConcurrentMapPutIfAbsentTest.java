package com.github.mongoutils.collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
    DBObject valueQueryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<TestBean> valueCaptor;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(any(TestBean.class), anyBoolean(), anyBoolean())).thenReturn(valueQueryObject);
        map = spy(new MongoConcurrentMap<String, TestBean>(collection, keySerializer, valueSerializer));
    }

    @Test
    public void putNotExistingValue() {
        TestBean testBean = new TestBean("testbean");
        when(valueSerializer.toElement(resultObject)).thenReturn(new TestBean("testbean"));
        when(collection.count(keyQueryObject)).thenReturn(0L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);

        assertNotNull(map.putIfAbsent("key", testBean));

        verify(map).containsKey("key");
        verify(map).put("key", testBean);
    }

    @Test
    public void putExistingValue() {
        when(collection.count(keyQueryObject)).thenReturn(1L);

        assertNull(map.putIfAbsent("key", new TestBean("testbean")));

        verify(map).containsKey("key");
        verify(map, never()).put(anyString(), any(TestBean.class));
    }

}
