package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.CloseableIterator;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface IteratorCommand<T> extends CollectionCommand {
    
    CloseableIterator<T> iterator(DBCollection collection, DBObjectSerializer<T> serializer);
    
}
