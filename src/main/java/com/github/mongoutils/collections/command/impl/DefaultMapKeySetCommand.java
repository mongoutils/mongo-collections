package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoSet;
import com.github.mongoutils.collections.command.MapKeySetCommand;
import com.mongodb.DBCollection;

public class DefaultMapKeySetCommand<K> implements MapKeySetCommand<K> {
    
    @Override
    public MongoSet<K> keySet(DBCollection collection, DBObjectSerializer<K> serializer) {
        return new MongoSet<K>(collection, serializer);
    }
    
}
