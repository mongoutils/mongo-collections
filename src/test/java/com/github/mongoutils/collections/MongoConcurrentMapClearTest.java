package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapClearTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Captor
    ArgumentCaptor<DBObject> dbObjectCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, null, null);
    }
    
    @Test
    public void clear() {
        map.clear();
        
        verify(collection).remove(dbObjectCaptor.capture());
        assertEquals(0, dbObjectCaptor.getValue().toMap().size());
    }
    
}
