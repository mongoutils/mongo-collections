package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.RemoveCommand;
import com.mongodb.DBCollection;

public class DefaultRemoveCommand<T> implements RemoveCommand<T> {
    
    @Override
    public boolean remove(DBCollection collection, DBObjectSerializer<T> serializer, T object) {
        return collection.remove(serializer.toDBObject(object, true, false)).getN() > 0;
    }
    
}
