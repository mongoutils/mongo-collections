package com.github.mongoutils.collections.command;

import java.util.Map;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface MapPutCommand<K, V> extends CollectionCommand {
    
    V put(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer, K key,
            V value);
    
    void putAll(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            Map<K, V> data);
    
    V putIfAbsent(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key, V value);
    
}
