package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.ContainsCommand;
import com.mongodb.DBCollection;

public class SetAddCommand<T> extends DefaultAddCommand<T> {
    
    ContainsCommand<T> containsCommand;
    
    public SetAddCommand() {
    }
    
    public SetAddCommand(ContainsCommand<T> containsCommand) {
        this.containsCommand = containsCommand;
    }
    
    @Override
    public boolean add(DBCollection collection, DBObjectSerializer<T> serializer, T object) {
        if (!containsCommand.contains(collection, serializer, object)) {
            return collection.insert(serializer.toDBObject(object, false, false)).getN() > 0;
        }
        return false;
    }
    
}
