package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionClearTest {

    MongoCollection<String> collection;
    @Mock
    DBCollection dbCollection;
    @Captor
    ArgumentCaptor<DBObject> dbObjectCaptor;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, null);
    }

    @Test
    public void clear() {
        collection.clear();

        verify(dbCollection).remove(dbObjectCaptor.capture());
        assertEquals(0, dbObjectCaptor.getValue().toMap().size());
    }

}
