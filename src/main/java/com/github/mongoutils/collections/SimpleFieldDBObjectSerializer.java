package com.github.mongoutils.collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SimpleFieldDBObjectSerializer<E> implements DBObjectSerializer<E> {
    
    ObjectMapper mapper = new ObjectMapper();
    String field;
    
    public SimpleFieldDBObjectSerializer(String field) {
        this.field = field;
    }
    
    @Override
    public DBObject toDBObject(E element, boolean equalFunctions, boolean negate) {
        return new BasicDBObject(field, element);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E toElement(DBObject dbObject) {
        return (E) dbObject.get(field);
    }
    
}
