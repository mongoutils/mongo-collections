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
public class CachingMapIsEmptyTest {

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
    public void isEmptyWithEmptyCache() {
        when(cache.isEmpty()).thenReturn(true);
        when(backstore.isEmpty()).thenReturn(false);

        assertFalse(map.isEmpty());

        verify(cache).isEmpty();
        verify(backstore).isEmpty();
    }

    @Test
    public void isEmptyWithEmptyBackstore() {
        when(cache.isEmpty()).thenReturn(true);
        when(backstore.isEmpty()).thenReturn(true);

        assertTrue(map.isEmpty());

        verify(cache).isEmpty();
        verify(backstore).isEmpty();
    }

    @Test
    public void isEmptyWithFilledCache() {
        when(cache.isEmpty()).thenReturn(false);
        when(backstore.isEmpty()).thenReturn(false);

        assertFalse(map.isEmpty());

        verify(cache).isEmpty();
        verifyZeroInteractions(backstore);
    }

}
