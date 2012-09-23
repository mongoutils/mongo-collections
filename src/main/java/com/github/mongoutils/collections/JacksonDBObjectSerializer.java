package com.github.mongoutils.collections;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JacksonDBObjectSerializer<E> implements DBObjectSerializer<E> {
    
    ObjectMapper mapper = new ObjectMapper();
    String field;
    Class<E> type;
    
    public JacksonDBObjectSerializer(String field, Class<E> type) {
        this.field = field;
        this.type = type;
    }
    
    @Override
    public DBObject toDBObject(E element, boolean equalFunctions, boolean negate) {
        return new BasicDBObject(field, mapper.convertValue(element, HashMap.class));
    }
    
    @Override
    public E toElement(DBObject dbObject) {
        return mapper.convertValue(((DBObject) dbObject.get(field)).toMap(), type);
    }
    
}
