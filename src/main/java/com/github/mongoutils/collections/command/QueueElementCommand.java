package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface QueueElementCommand<T> extends CollectionCommand {
    
    T element(DBCollection collection, DBObjectSerializer<T> serializer);
    
}
