package com.github.mongoutils.collections.command.impl;

import java.util.Collection;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.AddCommand;
import com.mongodb.DBCollection;

public class DefaultAddCommand<T> implements AddCommand<T> {
    
    @Override
    public boolean add(DBCollection collection, DBObjectSerializer<T> serializer, T object) {
        return collection.insert(serializer.toDBObject(object, false, false)).getN() > 0;
    }
    
    @Override
    public boolean addAll(DBCollection collection, DBObjectSerializer<T> serializer, Collection<? extends T> c) {
        boolean changed = false;
        
        for (T e : c) {
            if (add(collection, serializer, e)) {
                changed = true;
            }
        }
        
        return changed;
    }
    
}
