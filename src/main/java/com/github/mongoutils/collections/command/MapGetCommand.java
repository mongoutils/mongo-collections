package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface MapGetCommand<K, V> extends CollectionCommand {
    
    V get(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer, K key);
    
}
