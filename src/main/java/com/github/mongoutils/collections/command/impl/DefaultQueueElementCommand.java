package com.github.mongoutils.collections.command.impl;

import java.util.NoSuchElementException;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.QueueElementCommand;
import com.github.mongoutils.collections.command.QueuePeekCommand;
import com.mongodb.DBCollection;

public class DefaultQueueElementCommand<T> implements QueueElementCommand<T> {
    
    IsEmptyCommand isEmptyCommand;
    QueuePeekCommand<T> queuePeekCommand;
    
    public DefaultQueueElementCommand() {
    }
    
    public DefaultQueueElementCommand(IsEmptyCommand isEmptyCommand, QueuePeekCommand<T> queuePeekCommand) {
        this.isEmptyCommand = isEmptyCommand;
        this.queuePeekCommand = queuePeekCommand;
    }
    
    @Override
    public T element(DBCollection collection, DBObjectSerializer<T> serializer) {
        if (isEmptyCommand.isEmpty(collection)) {
            throw new NoSuchElementException();
        }
        return queuePeekCommand.peek(collection, serializer);
    }
    
}
