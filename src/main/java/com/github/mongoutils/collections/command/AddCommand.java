package com.github.mongoutils.collections.command;

import java.util.Collection;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.mongodb.DBCollection;

public interface AddCommand<T> extends CollectionCommand {
    
    boolean add(DBCollection collection, DBObjectSerializer<T> serializer, T object);
    
    boolean addAll(DBCollection collection, DBObjectSerializer<T> serializer, Collection<? extends T> c);
    
}
