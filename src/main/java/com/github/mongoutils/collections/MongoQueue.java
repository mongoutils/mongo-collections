package com.github.mongoutils.collections;

import java.util.NoSuchElementException;
import java.util.Queue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoQueue<E> extends MongoCollection<E> implements Queue<E> {

    private static final BasicDBObject ORDER_BY_ASC = new BasicDBObject("_id", 1);
    private static final BasicDBObject ORDER_BY_DESC = new BasicDBObject("_id", -1);

    boolean asc;

    public MongoQueue(final DBCollection collection, final DBObjectSerializer<E> serializer) {
        super(collection, serializer);
        asc = true;
    }

    public MongoQueue(final DBCollection collection, final DBObjectSerializer<E> serializer, final boolean asc) {
        super(collection, serializer);
        this.asc = asc;
    }

    @Override
    public CloseableIterator<E> iterator() {
        return new CloseableIterator<E>() {

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
            public E next() {
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
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean offer(final E e) {
        return add(e);
    }

    @Override
    public E poll() {
        DBObject dbObject;

        if (isEmpty()) {
            return null;
        }

        dbObject = collection.findAndModify(null, null, asc ? ORDER_BY_ASC : ORDER_BY_DESC, true, null, false, false);
        return serializer.toElement(dbObject);
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peek();
    }

    @Override
    public E peek() {
        DBCursor cursor;

        if (isEmpty()) {
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
