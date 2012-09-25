package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
public class MongoConcurrentMapIsEmptyTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, null, null);
    }
    
    @Test
    public void isEmptyEempty() {
        assertTrue(map.isEmpty());
        verify(collection).count();
    }
    
    @Test
    public void isEmptyFilled() {
        when(collection.count()).thenReturn(123L);
        assertFalse(map.isEmpty());
        verify(collection).count();
    }
    
}
