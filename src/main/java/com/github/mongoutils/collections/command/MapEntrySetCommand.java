package com.github.mongoutils.collections.command;

import java.util.Map;
import java.util.Map.Entry;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoSet;
import com.mongodb.DBCollection;

public interface MapEntrySetCommand<K, V> extends CollectionCommand {
    
    MongoSet<Map.Entry<K, V>> entrySet(DBCollection collection, DBObjectSerializer<Entry<K, V>> serializer);
    
}
