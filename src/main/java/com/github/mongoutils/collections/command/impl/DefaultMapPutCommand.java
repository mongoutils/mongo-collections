package com.github.mongoutils.collections.command.impl;

import java.util.Map;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.ContainsCommand;
import com.github.mongoutils.collections.command.MapGetCommand;
import com.github.mongoutils.collections.command.MapPutCommand;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DefaultMapPutCommand<K, V> implements MapPutCommand<K, V> {
    
    MapGetCommand<K, V> getCommand;
    ContainsCommand<K> containsCommand;
    
    public DefaultMapPutCommand() {
    }
    
    public DefaultMapPutCommand(MapGetCommand<K, V> getCommand, ContainsCommand<K> containsCommand) {
        this.getCommand = getCommand;
        this.containsCommand = containsCommand;
    }
    
    @Override
    public V put(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key, V value) {
        return put(collection, keySerializer, valueSerializer, key, value, true, false);
    }
    
    @Override
    public void putAll(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, Map<K, V> data) {
        for (Map.Entry<K, V> entry : data.entrySet()) {
            put(collection, keySerializer, valueSerializer, entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public V putIfAbsent(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key, V value) {
        if (!containsCommand.contains(collection, keySerializer, key)) {
            return put(collection, keySerializer, valueSerializer, key, value, true, true);
        }
        return getCommand.get(collection, keySerializer, valueSerializer, key);
    }
    
    public V put(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key, V value, boolean insertIfAbsent, boolean returnNew) {
        DBObject dbObject;
        DBObject queryObject;
        V old = null;
        
        queryObject = keySerializer.toDBObject(key, true, false);
        dbObject = keySerializer.toDBObject(key, false, false);
        dbObject.putAll(valueSerializer.toDBObject(value, false, false));
        
        dbObject = collection.findAndModify(queryObject, null, null, false, dbObject, returnNew, insertIfAbsent);
        if (dbObject != null) {
            old = valueSerializer.toElement(dbObject);
        }
        
        return old;
    }
    
}
