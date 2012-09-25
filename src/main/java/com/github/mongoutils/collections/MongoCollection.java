package com.github.mongoutils.collections;

import java.util.Collection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class MongoCollection<T> implements Collection<T> {

    DBObjectSerializer<T> serializer;
    DBCollection collection;

    public MongoCollection(final DBCollection collection, final DBObjectSerializer<T> serializer) {
        this.collection = collection;
        this.serializer = serializer;
    }

    @Override
    public int size() {
        return (int) collection.count();
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return collection.count(serializer.toDBObject((T) o, true, false)) > 0;
    }

    @Override
    public CloseableIterator<T> iterator() {
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

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> V[] toArray(final V[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(final T e) {
        return collection.insert(serializer.toDBObject(e, false, false)).getN() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(final Object o) {
        return collection.remove(serializer.toDBObject((T) o, true, false)).getN() > 0;
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean changed = false;

        for (T e : c) {
            if (add(e)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        collection.remove(new BasicDBObject());
    }

}
