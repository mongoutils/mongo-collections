package com.github.mongoutils.collections;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JacksonDBObjectSerializerTest {

    JacksonDBObjectSerializer<TestBean> serializer;

    @Before
    public void createSerializer() {
        serializer = new JacksonDBObjectSerializer<TestBean>("field", TestBean.class);
    }

    @Test
    public void toElement() {
        BasicDBObject obj;
        TestBean bean;

        obj = new BasicDBObject();
        obj.put("name", "testbean");
        obj.put("count", 123);

        bean = serializer.toElement(new BasicDBObject("field", obj));

        assertEquals("testbean", bean.getName());
        assertEquals(123, bean.getCount());
    }

    @Test
    public void toDBObject() throws Exception {
        DBObject obj;
        TestBean bean;
        ObjectWriter mapper = new ObjectMapper().writer().without(SerializationFeature.WRITE_NULL_MAP_VALUES);

        bean = new TestBean("testbean");
        bean.setCount(123);

        obj = serializer.toDBObject(bean, true, false);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, true, true);
        assertEquals("{\"field\":{\"$ne\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, false);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, true);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));
    }

}
