package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.MapReplaceCommand;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DefaultMapReplaceCommand<K, V> implements MapReplaceCommand<K, V> {
    
    @Override
    public boolean replace(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key, V oldValue, V newValue) {
        DBObject queryObject = keySerializer.toDBObject(key, true, false);
        DBObject dbObject = keySerializer.toDBObject(key, false, false);
        
        queryObject.putAll(valueSerializer.toDBObject(oldValue, true, false));
        dbObject.putAll(valueSerializer.toDBObject(newValue, false, false));
        
        return collection.update(queryObject, dbObject).getN() > 0;
    }
    
    @Override
    public V replace(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key, V value) {
        DBObject dbObject;
        DBObject queryObject;
        V old = null;
        
        queryObject = keySerializer.toDBObject(key, true, false);
        dbObject = keySerializer.toDBObject(key, false, false);
        dbObject.putAll(valueSerializer.toDBObject(value, false, false));
        
        dbObject = collection.findAndModify(queryObject, null, null, false, dbObject, false, false);
        if (dbObject != null) {
            old = valueSerializer.toElement(dbObject);
        }
        
        return old;
    }
    
}
