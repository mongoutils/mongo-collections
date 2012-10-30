package com.github.mongoutils.collections.command.impl;

import java.util.Map.Entry;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoSet;
import com.github.mongoutils.collections.command.MapEntrySetCommand;
import com.mongodb.DBCollection;

public class DefaultMapEntrySetCommand<K, V> implements MapEntrySetCommand<K, V> {
    
    @Override
    public MongoSet<Entry<K, V>> entrySet(DBCollection collection, DBObjectSerializer<Entry<K, V>> serializer) {
        return new MongoSet<Entry<K, V>>(collection, serializer);
    }
    
}
