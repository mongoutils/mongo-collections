package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionIsEmptyTest {

    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBCollection dbCollection;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }

    @Test
    public void isEmpty() {
        when(dbCollection.count()).thenReturn(0L);
        assertTrue(collection.isEmpty());
        verify(dbCollection).count();
    }

    @Test
    public void isNotEmpty() {
        when(dbCollection.count()).thenReturn(123L);
        assertFalse(collection.isEmpty());
        verify(dbCollection).count();
    }

}
