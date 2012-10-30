package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.CloseableIterator;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IteratorCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class QueueIteratorCommand<T> implements IteratorCommand<T> {
    
    private static final BasicDBObject ORDER_BY_ASC = new BasicDBObject("_id", 1);
    private static final BasicDBObject ORDER_BY_DESC = new BasicDBObject("_id", -1);
    
    boolean asc;
    
    public QueueIteratorCommand() {
    }
    
    public QueueIteratorCommand(boolean asc) {
        this.asc = asc;
    }
    
    @Override
    public CloseableIterator<T> iterator(final DBCollection collection, final DBObjectSerializer<T> serializer) {
        return new CloseableIterator<T>() {
            
            DBCursor cursor = collection.find().sort(asc ? ORDER_BY_ASC : ORDER_BY_DESC);
            
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
