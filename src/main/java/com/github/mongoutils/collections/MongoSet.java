package com.github.mongoutils.collections;

import java.util.Set;

import com.github.mongoutils.collections.command.impl.SetAddCommand;
import com.mongodb.DBCollection;

public class MongoSet<T> extends MongoCollection<T> implements Set<T> {
    
    public MongoSet(DBCollection collection, DBObjectSerializer<T> serializer) {
        super(collection, serializer);
        addCommand = new SetAddCommand<T>(containsCommand);
    }
    
}
