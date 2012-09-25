package com.github.mongoutils.collections;

import java.util.Set;

import com.mongodb.DBCollection;

public class MongoSet<T> extends MongoCollection<T> implements Set<T> {
    
    public MongoSet(DBCollection collection, DBObjectSerializer<T> serializer) {
        super(collection, serializer);
    }
    
    @Override
    public boolean add(T e) {
        if (!contains(e)) {
            return super.add(e);
        }
        return false;
    }
    
}
