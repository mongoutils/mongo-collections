package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.QueuePollCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DefaultQueuePollCommand<T> implements QueuePollCommand<T> {
    
    private static final BasicDBObject ORDER_BY_ASC = new BasicDBObject("_id", 1);
    private static final BasicDBObject ORDER_BY_DESC = new BasicDBObject("_id", -1);
    
    boolean asc;
    IsEmptyCommand isEmptyCommand;
    
    public DefaultQueuePollCommand() {
    }
    
    public DefaultQueuePollCommand(boolean asc, IsEmptyCommand isEmptyCommand) {
        this.asc = asc;
        this.isEmptyCommand = isEmptyCommand;
    }
    
    @Override
    public T poll(DBCollection collection, DBObjectSerializer<T> serializer) {
        DBObject dbObject;
        
        if (isEmptyCommand.isEmpty(collection)) {
            return null;
        }
        
        dbObject = collection.findAndModify(null, null, asc ? ORDER_BY_ASC : ORDER_BY_DESC, true, null, false, false);
        return serializer.toElement(dbObject);
    }
    
}
