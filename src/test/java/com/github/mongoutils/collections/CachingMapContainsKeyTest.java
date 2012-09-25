package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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
public class CachingMapContainsKeyTest {

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
    public void containsExistingKeyInCache() {
        when(cache.containsKey("key")).thenReturn(true);

        assertTrue(map.containsKey("key"));
        verify(cache).containsKey("key");
        verifyZeroInteractions(backstore);
    }

    @Test
    public void containsExistingKeyInBackstore() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(true);

        assertTrue(map.containsKey("key"));
        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
    }

    @Test
    public void containsNotExistingKey() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(false);

        assertFalse(map.containsKey("key"));
        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
    }

}
