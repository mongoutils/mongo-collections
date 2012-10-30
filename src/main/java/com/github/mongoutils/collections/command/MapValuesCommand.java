package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoCollection;
import com.mongodb.DBCollection;

public interface MapValuesCommand<V> extends CollectionCommand {
    
    MongoCollection<V> values(DBCollection collection, DBObjectSerializer<V> serializer);
    
}
