package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.QueuePeekCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class DefaultQueuePeekCommand<T> implements QueuePeekCommand<T> {
    
    private static final BasicDBObject ORDER_BY_ASC = new BasicDBObject("_id", 1);
    private static final BasicDBObject ORDER_BY_DESC = new BasicDBObject("_id", -1);
    
    boolean asc;
    IsEmptyCommand isEmptyCommand;
    
    public DefaultQueuePeekCommand() {
    }
    
    public DefaultQueuePeekCommand(boolean asc, IsEmptyCommand isEmptyCommand) {
        this.asc = asc;
        this.isEmptyCommand = isEmptyCommand;
    }
    
    @Override
    public T peek(DBCollection collection, DBObjectSerializer<T> serializer) {
        DBCursor cursor;
        
        if (isEmptyCommand.isEmpty(collection)) {
            return null;
        }
        
        cursor = collection.find().sort(asc ? ORDER_BY_ASC : ORDER_BY_DESC);
        try {
            if (cursor.hasNext()) {
                return serializer.toElement(cursor.next());
            }
            return null;
        } finally {
            cursor.close();
        }
    }
    
}
