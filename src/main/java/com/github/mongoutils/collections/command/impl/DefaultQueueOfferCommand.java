package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.command.AddCommand;
import com.github.mongoutils.collections.command.QueueOfferCommand;
import com.mongodb.DBCollection;

public class DefaultQueueOfferCommand<T> implements QueueOfferCommand<T> {
    
    AddCommand<T> addCommand;
    
    public DefaultQueueOfferCommand() {
    }
    
    public DefaultQueueOfferCommand(AddCommand<T> addCommand) {
        this.addCommand = addCommand;
    }
    
    @Override
    public boolean offer(DBCollection collection, DBObjectSerializer<T> serializer, T value) {
        return addCommand.add(collection, serializer, value);
    }
    
}
