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
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionContainsTest {

    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBCollection dbCollection;
    @Mock
    DBObject serializerResult;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }

    @Test
    public void contains() {
        when(serializer.toDBObject("value", true, false)).thenReturn(serializerResult);
        when(dbCollection.count(serializerResult)).thenReturn(1L);

        assertTrue(collection.contains("value"));

        verify(serializer).toDBObject("value", true, false);
        verify(dbCollection).count(serializerResult);
    }

    @Test
    public void containsNot() {
        when(serializer.toDBObject("value", true, false)).thenReturn(serializerResult);
        when(dbCollection.count(serializerResult)).thenReturn(0L);

        assertFalse(collection.contains("value"));

        verify(serializer).toDBObject("value", true, false);
        verify(dbCollection).count(serializerResult);
    }

}
