package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.MapRemoveCommand;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DefaultMapRemoveCommand<K, V> implements MapRemoveCommand<K, V> {
    
    @Override
    public boolean remove(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key, V value) {
        DBObject queryObject = keySerializer.toDBObject(key, true, false);
        queryObject.putAll(valueSerializer.toDBObject(value, true, false));
        
        return collection.remove(queryObject).getN() > 0;
    }
    
    @Override
    public V remove(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key) {
        DBObject queryObject = keySerializer.toDBObject(key, true, false);
        DBObject result;
        V old = null;
        
        result = collection.findAndRemove(queryObject);
        if (result != null) {
            old = valueSerializer.toElement(result);
        }
        
        return old;
    }
    
}
