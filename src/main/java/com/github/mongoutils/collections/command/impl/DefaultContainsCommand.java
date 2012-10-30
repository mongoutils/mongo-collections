package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.ContainsCommand;
import com.mongodb.DBCollection;

public class DefaultContainsCommand<T> implements ContainsCommand<T> {
    
    boolean equalFunctions = true;
    boolean negate = false;
    
    public DefaultContainsCommand() {
    }
    
    public DefaultContainsCommand(boolean equalFunctions, boolean negate) {
        this.equalFunctions = equalFunctions;
        this.negate = negate;
    }
    
    @Override
    public boolean contains(DBCollection collection, DBObjectSerializer<T> serializer, T object) {
        return collection.count(serializer.toDBObject(object, equalFunctions, negate)) > 0;
    }
    
}
