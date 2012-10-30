package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface QueueRemoveCommand<T> extends CollectionCommand {
    
    T remove(DBCollection collection, DBObjectSerializer<T> serializer);
    
}
