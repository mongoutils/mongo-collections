package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
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
public class MongoMapEntryTest {

    MongoMapEntry<String, String> entry;
    @Mock
    DBCollection collection;
    @Mock
    DBObject document;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBObject serializerResult;

    @Before
    public void createEntry() {
        entry = new MongoMapEntry<String, String>("key", collection, document, serializer);
    }

    @Test
    public void key() {
        assertEquals("key", entry.getKey());
    }

    @Test
    public void value() {
        entry.getValue();

        verify(serializer).toElement(document);
    }

    @Test
    public void setValue() {
        when(serializer.toElement(document)).thenReturn("value");
        when(serializer.toDBObject("newValue", false, false)).thenReturn(serializerResult);

        entry.setValue("newValue");

        verify(document).putAll(serializerResult);
        verify(serializer).toElement(document);
        verify(serializer).toDBObject("newValue", false, false);
        verify(collection).save(document);
    }

}
