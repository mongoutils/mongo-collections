package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class CachingConcurrentMapRemoveTest {

    CachingConcurrentMap<String, String> map;
    @Mock
    ConcurrentMap<String, String> cache;
    @Mock
    MongoConcurrentMap<String, String> backstore;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new CachingConcurrentMap<String, String>(cache, backstore);
    }

    @Test
    public void removeEqual() {
        when(cache.containsKey("key")).thenReturn(true);
        when(cache.get("key")).thenReturn("value");

        assertTrue(map.remove("key", "value"));

        verify(cache, times(2)).containsKey("key");
        verify(cache).get("key");
        verify(backstore, never()).get("key");
        verify(cache).remove("key");
        verify(backstore).remove("key");
    }

    @Test
    public void removeNotEqual() {
        when(cache.containsKey("key")).thenReturn(true);
        when(cache.get("key")).thenReturn("oldValue");

        assertFalse(map.remove("key", "value"));

        verify(cache, times(2)).containsKey("key");
        verify(cache).get("key");
        verify(backstore, never()).get("key");
        verify(cache, never()).remove("key");
        verify(backstore, never()).remove("key");
    }

    @Test
    public void removeNotExisting() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(false);

        assertFalse(map.remove("key", "value"));

        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
        verify(cache, never()).get("key");
        verify(backstore, never()).get("key");
        verify(cache, never()).remove("key");
        verify(backstore, never()).remove("key");
    }

}
