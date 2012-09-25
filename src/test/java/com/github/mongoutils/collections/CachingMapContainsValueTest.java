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
public class CachingMapContainsValueTest {

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
    public void containsExistingInCache() {
        when(cache.containsValue("value")).thenReturn(true);
        when(backstore.containsValue("value")).thenReturn(true);

        assertTrue(map.containsValue("value"));

        verify(cache).containsValue("value");
        verifyZeroInteractions(backstore);
    }

    @Test
    public void containsExistingInBackstore() {
        when(cache.containsValue("value")).thenReturn(false);
        when(backstore.containsValue("value")).thenReturn(true);

        assertTrue(map.containsValue("value"));

        verify(cache).containsValue("value");
        verify(backstore).containsValue("value");
    }

    @Test
    public void containsNotExisting() {
        when(cache.containsValue("value")).thenReturn(false);
        when(backstore.containsValue("value")).thenReturn(false);

        assertFalse(map.containsValue("value"));

        verify(cache).containsValue("value");
        verify(backstore).containsValue("value");
    }

}
