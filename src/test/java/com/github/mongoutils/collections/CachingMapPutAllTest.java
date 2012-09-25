package com.github.mongoutils.collections;

import static org.mockito.Mockito.verify;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class CachingMapPutAllTest {

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
    public void putAll() {
        Map<String, String> tmp = new HashMap<String, String>();
        tmp.put("key1", "value1");
        tmp.put("key2", "value2");

        map.putAll(tmp);
        verify(backstore).putAll(tmp);
        verify(cache).putAll(tmp);
    }

}
