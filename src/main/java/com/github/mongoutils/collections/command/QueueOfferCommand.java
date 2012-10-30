package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface QueueOfferCommand<T> extends CollectionCommand {
    
    boolean offer(DBCollection collection, DBObjectSerializer<T> serializer, T value);
    
}
