package com.github.mongoutils.collections;

import java.util.Collection;

import com.github.mongoutils.collections.command.AddCommand;
import com.github.mongoutils.collections.command.ClearCommand;
import com.github.mongoutils.collections.command.ContainsCommand;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.IteratorCommand;
import com.github.mongoutils.collections.command.RemoveCommand;
import com.github.mongoutils.collections.command.SizeCommand;
import com.github.mongoutils.collections.command.impl.DefaultAddCommand;
import com.github.mongoutils.collections.command.impl.DefaultClearCommand;
import com.github.mongoutils.collections.command.impl.DefaultContainsCommand;
import com.github.mongoutils.collections.command.impl.DefaultIsEmptyCommand;
import com.github.mongoutils.collections.command.impl.DefaultIteratorCommand;
import com.github.mongoutils.collections.command.impl.DefaultRemoveCommand;
import com.github.mongoutils.collections.command.impl.DefaultSizeCommand;
import com.mongodb.DBCollection;

public class MongoCollection<T> implements Collection<T> {
    
    protected DBObjectSerializer<T> serializer;
    protected DBCollection collection;
    
    protected SizeCommand sizeCommand = new DefaultSizeCommand();
    protected IsEmptyCommand isEmptyCommand = new DefaultIsEmptyCommand();
    protected ContainsCommand<T> containsCommand = new DefaultContainsCommand<T>();
    protected ClearCommand clearCommand = new DefaultClearCommand();
    protected IteratorCommand<T> iteratorCommand = new DefaultIteratorCommand<T>();
    protected RemoveCommand<T> removeCommand = new DefaultRemoveCommand<T>();
    protected AddCommand<T> addCommand = new DefaultAddCommand<T>();
    
    public MongoCollection(DBCollection collection, DBObjectSerializer<T> serializer) {
        this.collection = collection;
        this.serializer = serializer;
    }
    
    @Override
    public int size() {
        return sizeCommand.size(collection);
    }
    
    @Override
    public boolean isEmpty() {
        return isEmptyCommand.isEmpty(collection);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return containsCommand.contains(collection, serializer, (T) o);
    }
    
    @Override
    public CloseableIterator<T> iterator() {
        return iteratorCommand.iterator(collection, serializer);
    }
    
    @Override
    public void clear() {
        clearCommand.clear(collection);
    }
    
    @Override
    public boolean add(T e) {
        return addCommand.add(collection, serializer, e);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return removeCommand.remove(collection, serializer, (T) o);
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addCommand.addAll(collection, serializer, c);
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <V> V[] toArray(V[] a) {
        throw new UnsupportedOperationException();
    }
    
    public SizeCommand getSizeCommand() {
        return sizeCommand;
    }
    
    public void setSizeCommand(SizeCommand sizeCommand) {
        this.sizeCommand = sizeCommand;
    }
    
    public IsEmptyCommand getIsEmptyCommand() {
        return isEmptyCommand;
    }
    
    public void setIsEmptyCommand(IsEmptyCommand isEmptyCommand) {
        this.isEmptyCommand = isEmptyCommand;
    }
    
    public ContainsCommand<T> getContainsCommand() {
        return containsCommand;
    }
    
    public void setContainsCommand(ContainsCommand<T> containsCommand) {
        this.containsCommand = containsCommand;
    }
    
    public ClearCommand getClearCommand() {
        return clearCommand;
    }
    
    public void setClearCommand(ClearCommand clearCommand) {
        this.clearCommand = clearCommand;
    }
    
    public IteratorCommand<T> getIteratorCommand() {
        return iteratorCommand;
    }
    
    public void setIteratorCommand(IteratorCommand<T> iteratorCommand) {
        this.iteratorCommand = iteratorCommand;
    }
    
    public RemoveCommand<T> getRemoveCommand() {
        return removeCommand;
    }
    
    public void setRemoveCommand(RemoveCommand<T> removeCommand) {
        this.removeCommand = removeCommand;
    }
    
    public AddCommand<T> getAddCommand() {
        return addCommand;
    }
    
    public void setAddCommand(AddCommand<T> addCommand) {
        this.addCommand = addCommand;
    }
    
}
