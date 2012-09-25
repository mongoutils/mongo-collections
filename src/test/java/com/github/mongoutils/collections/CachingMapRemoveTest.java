package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
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
public class CachingMapRemoveTest {

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
    public void removeExisting() {
        when(backstore.remove(any())).thenReturn("oldBackstoreValue");
        when(cache.remove(any())).thenReturn("oldCacheValue");

        assertEquals("oldBackstoreValue", map.remove("key"));
        verify(cache).remove("key");
        verify(backstore).remove("key");
    }

    @Test
    public void removeNotExisting() {
        when(backstore.remove(any())).thenReturn(null);

        assertNull(map.remove("key"));
        verify(cache).remove("key");
        verify(backstore).remove("key");
    }

}
