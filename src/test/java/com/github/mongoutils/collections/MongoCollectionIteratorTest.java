package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionIteratorTest {

    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBCollection dbCollection;
    @Mock
    DBObject serializerResult;
    @Mock
    DBCursor cursor;
    @Mock
    DBObject cursorResult;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }

    @Test
    public void iterator() {
        CloseableIterator<String> iter;

        when(dbCollection.find()).thenReturn(cursor);
        when(serializer.toElement(cursorResult)).thenReturn("value");

        iter = collection.iterator();

        when(cursor.hasNext()).thenReturn(true);
        when(cursor.next()).thenReturn(cursorResult);

        assertTrue(iter.hasNext());
        assertEquals("value", iter.next());

        verify(cursor).hasNext();
        verify(cursor).next();
        verify(serializer).toElement(cursorResult);

        iter.remove();

        verify(cursor).remove();

        assertTrue(iter.hasNext());
        assertEquals("value", iter.next());

        verify(cursor, times(2)).hasNext();
        verify(cursor, times(2)).next();
        verify(serializer, times(2)).toElement(cursorResult);

        iter.close();

        verify(cursor).close();

        when(cursor.hasNext()).thenReturn(false);

        assertFalse(iter.hasNext());

        verify(dbCollection).find();
    }

}
