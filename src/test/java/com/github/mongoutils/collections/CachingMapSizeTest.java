package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
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
public class CachingMapSizeTest {

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
    public void sizeEmpty() {
        assertEquals(0, map.size());
        verify(backstore).size();
        verifyZeroInteractions(cache);
    }

    @Test
    public void sizeFilled() {
        when(backstore.size()).thenReturn(123);
        assertEquals(123, map.size());
        verify(backstore).size();
        verifyZeroInteractions(cache);
    }

}
