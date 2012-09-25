package com.github.mongoutils.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SimpleFieldDBObjectSerializer<E> implements DBObjectSerializer<E> {

    String field;

    public SimpleFieldDBObjectSerializer(final String field) {
        this.field = field;
    }

    @Override
    public DBObject toDBObject(final E element, final boolean equalFunctions, final boolean negate) {
        if (equalFunctions && negate) {
            return new BasicDBObject(field, new BasicDBObject("$ne", element));
        }
        return new BasicDBObject(field, element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E toElement(final DBObject dbObject) {
        return (E) dbObject.get(field);
    }

}
