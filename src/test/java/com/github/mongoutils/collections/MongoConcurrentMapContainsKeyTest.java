package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
public class MongoConcurrentMapContainsKeyTest {

    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObject queryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<String> keyCaptor;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(queryObject);
        map = new MongoConcurrentMap<String, TestBean>(collection, keySerializer, null);
    }

    @Test
    public void keyNotContained() {
        when(collection.findOne(queryObject)).thenReturn(null);

        assertFalse(map.containsKey("key"));

        verify(keySerializer).toDBObject(keyCaptor.capture(), eq(true), eq(false));
        assertEquals("key", keyCaptor.getValue());
    }

    @Test
    public void keyContained() {
        when(collection.count(queryObject)).thenReturn(1L);

        assertTrue(map.containsKey("key"));

        verify(keySerializer).toDBObject(keyCaptor.capture(), eq(true), eq(false));
        assertEquals("key", keyCaptor.getValue());
    }

}
