package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoMapEntryDBObjectSerializerTest {

    class SimpleMapEntry<K, V> implements Entry<K, V> {

        K key;
        V value;

        public SimpleMapEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(final V value) {
            return null;
        }

    }

    MongoMapEntryDBObjectSerializer<String, TestBean> serializer;
    @Mock
    DBCollection collection;
    DBObjectSerializer<String> keySerializer;
    DBObjectSerializer<TestBean> valueSerializer;

    @Before
    public void createSerializer() {
        keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        serializer = new MongoMapEntryDBObjectSerializer<String, TestBean>(collection, keySerializer, valueSerializer);
    }

    @Test
    public void toElement() {
        MongoMapEntry<String, TestBean> entry;
        DBObject dbObject;

        dbObject = new BasicDBObject("key", "key1");
        dbObject.put("value", new BasicDBObject("name", "testbean"));

        entry = serializer.toElement(dbObject);

        assertEquals("key1", entry.getKey());
        assertEquals("testbean", entry.getValue().getName());
    }

    @Test
    public void toDBObject() throws Exception {
        DBObject obj;
        ObjectWriter mapper = new ObjectMapper().writer().without(SerializationFeature.WRITE_NULL_MAP_VALUES);
        SimpleMapEntry<String, TestBean> bean;

        bean = new SimpleMapEntry<String, TestBean>("key1", new TestBean("testbean1"));

        obj = serializer.toDBObject(bean, true, false);
        assertEquals("{\"key\":\"key1\",\"value\":{\"count\":0,\"beans\":[],\"name\":\"testbean1\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, true, true);
        assertEquals(
                "{\"key\":{\"$ne\":\"key1\"},\"value\":{\"$ne\":{\"count\":0,\"beans\":[],\"name\":\"testbean1\"}}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, false);
        assertEquals("{\"key\":\"key1\",\"value\":{\"count\":0,\"beans\":[],\"name\":\"testbean1\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, true);
        assertEquals("{\"key\":\"key1\",\"value\":{\"count\":0,\"beans\":[],\"name\":\"testbean1\"}}",
                mapper.writeValueAsString(obj.toMap()));
    }

}
