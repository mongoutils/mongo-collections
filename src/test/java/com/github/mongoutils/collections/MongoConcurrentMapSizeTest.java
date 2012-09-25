package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapSizeTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, null, null);
    }
    
    @Test
    public void sizeEmpty() {
        assertEquals(0, map.size());
        verify(collection).count();
    }
    
    @Test
    public void sizeFilled() {
        when(collection.count()).thenReturn(123L);
        assertEquals(123, map.size());
        verify(collection).count();
    }
    
}
