package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class SimpleFieldDBObjectSerializerTest {

    SimpleFieldDBObjectSerializer<String> serializer;
    @Mock
    DBObject dbObject;

    @Before
    public void createSerializer() {
        serializer = new SimpleFieldDBObjectSerializer<String>("field");
    }

    @Test
    public void toElement() {
        when(dbObject.get("field")).thenReturn("value");
        assertEquals("value", serializer.toElement(dbObject));

        when(dbObject.get("field")).thenReturn(null);
        assertNull(serializer.toElement(dbObject));
    }

    @Test
    public void toDBObject() throws Exception {
        DBObject obj;

        obj = serializer.toDBObject("value", true, true);
        assertEquals("{\"field\":{\"$ne\":\"value\"}}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", true, false);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", false, true);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", false, false);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));
    }

}
