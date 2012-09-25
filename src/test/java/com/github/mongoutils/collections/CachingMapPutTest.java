package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class CachingMapPutTest {

    CachingMap<String, String> map;
    @Mock
    Map<String, String> cache;
    @Mock
    MongoConcurrentMap<String, String> backstore;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new CachingMap<String, String>(cache, backstore);
    }

    @Test
    public void putNewEntry() {
        when(backstore.put(anyString(), anyString())).thenReturn(null);

        assertNull(map.put("key", "value"));

        verify(backstore).put("key", "value");
        verify(cache).put("key", "value");
    }

    @Test
    public void replaceExistingEntry() {
        when(backstore.put(anyString(), anyString())).thenReturn("oldBackstoreValue");
        when(cache.put(anyString(), anyString())).thenReturn("oldCacheValue");

        assertEquals("oldBackstoreValue", map.put("key", "value"));

        verify(backstore).put("key", "value");
        verify(cache).put("key", "value");
    }

}
