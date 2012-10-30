package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.CloseableIterator;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IteratorCommand;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class DefaultIteratorCommand<T> implements IteratorCommand<T> {
    
    @Override
    public CloseableIterator<T> iterator(final DBCollection collection, final DBObjectSerializer<T> serializer) {
        return new CloseableIterator<T>() {
            
            DBCursor cursor = collection.find();
            
            @Override
            public boolean hasNext() {
                boolean next = cursor.hasNext();
                if (!next) {
                    cursor.close();
                }
                return next;
            }
            
            @Override
            public T next() {
                return serializer.toElement(cursor.next());
            }
            
            @Override
            public void remove() {
                cursor.remove();
            }
            
            @Override
            public void close() {
                cursor.close();
            }
            
        };
    }
    
}
