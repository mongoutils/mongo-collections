package com.github.mongoutils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
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
public class MongoSetTest {

    MongoSet<String> set;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBObject serializerResult;
    @Mock
    DBObject collectionResult;

    @Before
    public void createSet() {
        set = new MongoSet<String>(collection, serializer);
    }

    @Test
    public void addNotExisting() {
        when(serializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(serializerResult);
        when(collection.findOne(serializerResult)).thenReturn(collectionResult);
        when(collection.count(serializerResult)).thenReturn(0L);
        when(collection.insert(any(DBObject.class)).getN()).thenReturn(1);

        assertTrue(set.add("value"));
        verify(serializer).toDBObject("value", true, false);
        verify(collection).count(serializerResult);
        verify(serializer).toDBObject("value", false, false);
        verify(collection).insert(serializerResult);
    }

    @Test
    public void addExisting() {
        when(serializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(serializerResult);
        when(collection.count(serializerResult)).thenReturn(1L);

        assertFalse(set.add("value"));
        verify(serializer).toDBObject("value", true, false);
        verify(collection).count(serializerResult);
    }

}
