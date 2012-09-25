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
import static org.mockito.Mockito.verifyZeroInteractions;
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
public class MongoConcurrentMapRemoveTest {

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
    public void removeNotExisting() {
        assertNull(map.remove("key"));

        verify(keySerializer).toDBObject("key", true, false);

        verify(collection).findOne(keyQueryObject);
        verify(collection).remove(keyQueryObject);

        verifyZeroInteractions(valueSerializer);
    }

    @Test
    public void removeExisting() {
        TestBean testBean = new TestBean("testBean");
        TestBean result;

        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean);

        result = map.remove("key");
        assertNotNull(result);
        assertEquals(testBean, result);

        verify(keySerializer).toDBObject("key", true, false);

        verify(valueSerializer).toElement(resultObject);

        verify(collection).findOne(keyQueryObject);
        verify(collection).remove(keyQueryObject);

        verifyZeroInteractions(valueSerializer);
    }

    @Test
    public void removeValueEqual() {
        TestBean testBean1 = new TestBean("testbean1");

        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean1);

        map = spy(map);
        assertTrue(map.remove("key", testBean1));

        verify(map).containsKey("key");
        verify(map).get("key");
        verify(map).remove("key");
    }

    @Test
    public void removeValueNotEqual() {
        TestBean testBean1 = new TestBean("testbean1");

        when(collection.count(keyQueryObject)).thenReturn(1L);
        when(collection.findOne(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean1);

        map = spy(map);
        assertFalse(map.remove("key", new TestBean("testbean2")));

        verify(map).containsKey("key");
        verify(map).get("key");
        verify(map, never()).remove("key");
    }

    @Test
    public void removeValueNotExisting() {
        when(collection.findOne(keyQueryObject)).thenReturn(null);

        map = spy(map);
        assertFalse(map.remove("key", new TestBean("testbean")));

        verify(map).containsKey("key");
        verify(map, never()).get("key");
        verify(map, never()).remove("key");
    }

}
