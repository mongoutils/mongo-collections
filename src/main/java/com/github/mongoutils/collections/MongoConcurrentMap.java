package com.github.mongoutils.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoConcurrentMap<K, V> implements ConcurrentMap<K, V> {

    DBCollection collection;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;

    public MongoConcurrentMap(
            final DBCollection collection,
            final DBObjectSerializer<K> keySerializer,
            final DBObjectSerializer<V> valueSerializer) {
        this.collection = collection;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
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
    public boolean containsKey(final Object key) {
        return collection.count(keySerializer.toDBObject((K) key, true, false)) > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(final Object value) {
        return collection.count(valueSerializer.toDBObject((V) value, true, false)) > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(final Object key) {
        DBObject result = collection.findOne(keySerializer.toDBObject((K) key, true, false));
        return result != null ? valueSerializer.toElement(result) : null;
    }

    @Override
    public V put(final K key, final V value) {
        DBObject dbObject;
        DBObject queryObject = keySerializer.toDBObject(key, true, false);
        DBObject result = collection.findOne(queryObject);
        V old = result != null ? valueSerializer.toElement(result) : null;

        if (old != null) {
            dbObject = result;
        } else {
            dbObject = keySerializer.toDBObject(key, false, false);
        }
        dbObject.putAll(valueSerializer.toDBObject(value, false, false));
        collection.save(dbObject);

        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(final Object key) {
        DBObject queryObject = keySerializer.toDBObject((K) key, true, false);
        DBObject result = collection.findOne(queryObject);
        V old = result != null ? valueSerializer.toElement(result) : null;
        collection.remove(queryObject);
        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : ((Map<K, V>) map).entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        collection.remove(new BasicDBObject());
    }

    @Override
    public MongoSet<K> keySet() {
        return new MongoSet<K>(collection, keySerializer);
    }

    @Override
    public MongoCollection<V> values() {
        return new MongoCollection<V>(collection, valueSerializer);
    }

    @Override
    public MongoSet<Map.Entry<K, V>> entrySet() {
        return new MongoSet<Entry<K, V>>(collection, new MongoMapEntryDBObjectSerializer<K, V>(collection,
                keySerializer, valueSerializer));
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        if (!containsKey(key)) {
            return put(key, value);
        }
        return get(key);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        if (containsKey(key) && get(key).equals(value)) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        if (containsKey(key) && get(key).equals(oldValue)) {
            put(key, newValue);
            return true;
        }
        return false;
    }

    @Override
    public V replace(final K key, final V value) {
        if (containsKey(key)) {
            return put(key, value);
        }
        return null;
    }

}
