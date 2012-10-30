package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface MapReplaceCommand<K, V> extends CollectionCommand {
    
    boolean replace(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer, K key, V oldValue, V newValue);
    
    V replace(DBCollection collection, DBObjectSerializer<K> keySerializer, DBObjectSerializer<V> valueSerializer,
            K key, V value);
    
}
