package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionRemoveTest {

    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DBCollection dbCollection;
    @Mock
    DBObject serializerResult;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }

    @Test
    public void removeExisting() {
        when(serializer.toDBObject("value", true, false)).thenReturn(serializerResult);
        when(dbCollection.remove(serializerResult).getN()).thenReturn(1);

        assertTrue(collection.remove("value"));

        verify(serializer).toDBObject("value", true, false);
        verify(dbCollection, times(2)).remove(serializerResult);
    }

    @Test
    public void removeNotExisting() {
        when(serializer.toDBObject("value", true, false)).thenReturn(serializerResult);
        when(dbCollection.remove(serializerResult).getN()).thenReturn(0);

        assertFalse(collection.remove("value"));

        verify(serializer).toDBObject("value", true, false);
        verify(dbCollection, times(2)).remove(serializerResult);
    }

}
