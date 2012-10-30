package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoCollection;
import com.github.mongoutils.collections.command.MapValuesCommand;
import com.mongodb.DBCollection;

public class DefaultMapValuesCommand<V> implements MapValuesCommand<V> {
    
    @Override
    public MongoCollection<V> values(DBCollection collection, DBObjectSerializer<V> serializer) {
        return new MongoCollection<V>(collection, serializer);
    }
    
}
