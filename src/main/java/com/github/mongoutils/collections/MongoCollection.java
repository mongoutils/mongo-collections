package com.github.mongoutils.collections;

import java.util.Collection;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class MongoCollection<T> implements Collection<T> {
    
    DBObjectSerializer<T> serializer;
    DBCollection collection;
    
    public MongoCollection(DBCollection collection, DBObjectSerializer<T> serializer) {
        this.collection = collection;
        this.serializer = serializer;
    }
    
    @Override
    public int size() {
        return (int) collection.getCount();
    }
    
    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return collection.findOne(serializer.toDBObject((T) o, true, false)) != null;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            
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
            
        };
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <V> V[] toArray(V[] a) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(T e) {
        return collection.insert(serializer.toDBObject(e, false, false)).getN() > 0;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return collection.remove(serializer.toDBObject((T) o, true, false)).getN() > 0;
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        
        for (T e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        
        return changed;
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
    public void clear() {
        collection.remove(new BasicDBObject());
    }
    
}
