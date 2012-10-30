package com.github.mongoutils.collections;

import java.util.Queue;

import com.github.mongoutils.collections.command.QueueElementCommand;
import com.github.mongoutils.collections.command.QueueOfferCommand;
import com.github.mongoutils.collections.command.QueuePeekCommand;
import com.github.mongoutils.collections.command.QueuePollCommand;
import com.github.mongoutils.collections.command.QueueRemoveCommand;
import com.github.mongoutils.collections.command.impl.DefaultQueueElementCommand;
import com.github.mongoutils.collections.command.impl.DefaultQueueOfferCommand;
import com.github.mongoutils.collections.command.impl.DefaultQueuePeekCommand;
import com.github.mongoutils.collections.command.impl.DefaultQueuePollCommand;
import com.github.mongoutils.collections.command.impl.DefaultQueueRemoveCommand;
import com.github.mongoutils.collections.command.impl.QueueIteratorCommand;
import com.mongodb.DBCollection;

public class MongoQueue<E> extends MongoCollection<E> implements Queue<E> {
    
    protected QueuePollCommand<E> queuePollCommand;
    protected QueueRemoveCommand<E> queueRemoveCommand;
    protected QueuePeekCommand<E> queuePeekCommand;
    protected QueueOfferCommand<E> queueOfferCommand;
    protected QueueElementCommand<E> queueElementCommand;
    
    public MongoQueue(DBCollection collection, DBObjectSerializer<E> serializer) {
        this(collection, serializer, true);
    }
    
    public MongoQueue(DBCollection collection, DBObjectSerializer<E> serializer, boolean asc) {
        super(collection, serializer);
        iteratorCommand = new QueueIteratorCommand<E>(asc);
        queuePollCommand = new DefaultQueuePollCommand<E>(asc, isEmptyCommand);
        queueRemoveCommand = new DefaultQueueRemoveCommand<E>(isEmptyCommand, queuePollCommand);
        queuePeekCommand = new DefaultQueuePeekCommand<E>(asc, isEmptyCommand);
        queueOfferCommand = new DefaultQueueOfferCommand<E>(addCommand);
        queueElementCommand = new DefaultQueueElementCommand<E>(isEmptyCommand, queuePeekCommand);
    }
    
    @Override
    public E remove() {
        return queueRemoveCommand.remove(collection, serializer);
    }
    
    @Override
    public boolean offer(E e) {
        return queueOfferCommand.offer(collection, serializer, e);
    }
    
    @Override
    public E poll() {
        return queuePollCommand.poll(collection, serializer);
    }
    
    @Override
    public E element() {
        return queueElementCommand.element(collection, serializer);
    }
    
    @Override
    public E peek() {
        return queuePeekCommand.peek(collection, serializer);
    }
    
    public QueuePollCommand<E> getQueuePollCommand() {
        return queuePollCommand;
    }
    
    public void setQueuePollCommand(QueuePollCommand<E> queuePollCommand) {
        this.queuePollCommand = queuePollCommand;
    }
    
    public QueueRemoveCommand<E> getQueueRemoveCommand() {
        return queueRemoveCommand;
    }
    
    public void setQueueRemoveCommand(QueueRemoveCommand<E> queueRemoveCommand) {
        this.queueRemoveCommand = queueRemoveCommand;
    }
    
    public QueuePeekCommand<E> getQueuePeekCommand() {
        return queuePeekCommand;
    }
    
    public void setQueuePeekCommand(QueuePeekCommand<E> queuePeekCommand) {
        this.queuePeekCommand = queuePeekCommand;
    }
    
    public QueueOfferCommand<E> getQueueOfferCommand() {
        return queueOfferCommand;
    }
    
    public void setQueueOfferCommand(QueueOfferCommand<E> queueOfferCommand) {
        this.queueOfferCommand = queueOfferCommand;
    }
    
    public QueueElementCommand<E> getQueueElementCommand() {
        return queueElementCommand;
    }
    
    public void setQueueElementCommand(QueueElementCommand<E> queueElementCommand) {
        this.queueElementCommand = queueElementCommand;
    }
    
}
