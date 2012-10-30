package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface RemoveCommand<T> extends CollectionCommand {
    
    boolean remove(DBCollection collection, DBObjectSerializer<T> serializer, T object);
    
}
