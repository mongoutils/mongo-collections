package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
public class MongoConcurrentMapReplaceTest {

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
    public void replaceValueEqual() {
        TestBean testBean1 = new TestBean("testbean1");
        TestBean testBean2 = new TestBean("testbean2");

        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean1);

        map = spy(map);
        assertTrue(map.replace("key", testBean1, testBean2));

        verify(map).containsKey("key");
        verify(map).get("key");
        verify(map).put("key", testBean2);
    }

    @Test
    public void replaceValueNotEqual() {
        TestBean testBean1 = new TestBean("testbean1");
        TestBean testBean2 = new TestBean("testbean2");

        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean2);

        map = spy(map);
        assertFalse(map.replace("key", testBean1, testBean2));

        verify(map).containsKey("key");
        verify(map).get("key");
        verify(map, never()).put("key", testBean2);
    }

    @Test
    public void replaceValueNotExisting() {
        TestBean testBean1 = new TestBean("testbean1");
        TestBean testBean2 = new TestBean("testbean2");

        when(collection.findOne(keyQueryObject)).thenReturn(null);

        map = spy(map);
        assertFalse(map.replace("key", testBean1, testBean2));

        verify(map).containsKey("key");
        verify(map, never()).get("key");
        verify(map, never()).put("key", testBean2);
    }

    @Test
    public void replaceNotExisting() {
        TestBean testBean = new TestBean("testbean");

        when(collection.findOne(keyQueryObject)).thenReturn(null);

        map = spy(map);
        assertNull(map.replace("key", testBean));

        verify(map).containsKey("key");
        verify(map, never()).put("key", testBean);
    }

    @Test
    public void replaceExisting() {
        TestBean testBean1 = new TestBean("testbean1");
        TestBean testBean2 = new TestBean("testbean2");
        TestBean result;

        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean1);

        map = spy(map);
        result = map.replace("key", testBean2);
        assertNotNull(result);
        assertEquals(testBean1, result);

        verify(map).containsKey("key");
        verify(map).put("key", testBean2);
    }

}
