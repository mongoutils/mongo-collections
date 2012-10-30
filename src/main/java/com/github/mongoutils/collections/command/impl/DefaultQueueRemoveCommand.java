package com.github.mongoutils.collections.command.impl;

import java.util.NoSuchElementException;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.QueuePollCommand;
import com.github.mongoutils.collections.command.QueueRemoveCommand;
import com.mongodb.DBCollection;

public class DefaultQueueRemoveCommand<T> implements QueueRemoveCommand<T> {
    
    IsEmptyCommand isEmptyCommand;
    QueuePollCommand<T> queuePollCommand;
    
    public DefaultQueueRemoveCommand() {
    }
    
    public DefaultQueueRemoveCommand(IsEmptyCommand isEmptyCommand, QueuePollCommand<T> queuePollCommand) {
        super();
        this.isEmptyCommand = isEmptyCommand;
        this.queuePollCommand = queuePollCommand;
    }
    
    @Override
    public T remove(DBCollection collection, DBObjectSerializer<T> serializer) {
        if (isEmptyCommand.isEmpty(collection)) {
            throw new NoSuchElementException();
        }
        return queuePollCommand.poll(collection, serializer);
    }
    
}
