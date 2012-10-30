package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.MapGetCommand;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DefaultMapGetCommand<K, V> implements MapGetCommand<K, V> {
    
    boolean equalFunctions = true;
    boolean negate = false;
    
    public DefaultMapGetCommand() {
    }
    
    public DefaultMapGetCommand(boolean equalFunctions, boolean negate) {
        this.equalFunctions = equalFunctions;
        this.negate = negate;
    }
    
    @Override
    public V get(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key) {
        DBObject result = collection.findOne(keySerializer.toDBObject(key, equalFunctions, negate));
        return result != null ? valueSerializer.toElement(result) : null;
    }
    
}
