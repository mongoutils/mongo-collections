package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface ContainsCommand<T> extends CollectionCommand {
    
    boolean contains(DBCollection collection, DBObjectSerializer<T> serializer, T object);
    
}
