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
public class MongoCollectionAddTest {

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
    public void add() {
        when(serializer.toDBObject("value", false, false)).thenReturn(serializerResult);
        when(dbCollection.insert(serializerResult).getN()).thenReturn(1);

        assertTrue(collection.add("value"));

        verify(serializer).toDBObject("value", false, false);
        verify(dbCollection, times(2)).insert(serializerResult);
    }

    @Test
    public void addFailed() {
        when(serializer.toDBObject("value", false, false)).thenReturn(serializerResult);
        when(dbCollection.insert(serializerResult).getN()).thenReturn(0);

        assertFalse(collection.add("value"));

        verify(serializer).toDBObject("value", false, false);
        verify(dbCollection, times(2)).insert(serializerResult);
    }

}
