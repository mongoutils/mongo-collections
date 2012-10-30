package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface MapRemoveCommand<K, V> extends CollectionCommand {
    
    boolean remove(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key, V value);
    
    V remove(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer, K key);
    
}
