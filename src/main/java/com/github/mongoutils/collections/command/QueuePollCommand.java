package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface QueuePollCommand<T> extends CollectionCommand {
    
    T poll(DBCollection collection, DBObjectSerializer<T> serializer);
    
}
