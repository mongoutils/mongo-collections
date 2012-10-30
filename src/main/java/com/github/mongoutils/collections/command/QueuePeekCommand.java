package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface QueuePeekCommand<T> extends CollectionCommand {
    
    T peek(DBCollection collection, DBObjectSerializer<T> serializer);
    
}
