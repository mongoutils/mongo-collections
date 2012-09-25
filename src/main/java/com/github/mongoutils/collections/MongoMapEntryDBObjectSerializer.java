package com.github.mongoutils.collections;

import java.util.Map.Entry;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoMapEntryDBObjectSerializer<K, V> implements DBObjectSerializer<Entry<K, V>> {

    DBCollection collection;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;

    public MongoMapEntryDBObjectSerializer(
            final DBCollection collection,
            final DBObjectSerializer<K> keySerializer,
            final DBObjectSerializer<V> valueSerializer) {
        this.collection = collection;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    @Override
    public DBObject toDBObject(final Entry<K, V> element, final boolean equalFunctions, final boolean negate) {
        DBObject obj = keySerializer.toDBObject(element.getKey(), equalFunctions, negate);
        obj.putAll(valueSerializer.toDBObject(element.getValue(), equalFunctions, negate));

        return obj;
    }

    @Override
    public MongoMapEntry<K, V> toElement(final DBObject dbObject) {
        return new MongoMapEntry<K, V>(keySerializer.toElement(dbObject), collection, dbObject, valueSerializer);
    }

}
