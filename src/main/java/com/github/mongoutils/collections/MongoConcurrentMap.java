package com.github.mongoutils.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoConcurrentMap<K, V> implements ConcurrentMap<K, V> {
    
    DBCollection collection;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;
    
    public MongoConcurrentMap(DBCollection collection,
            DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer) {
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
    public boolean containsKey(Object key) {
        return collection.findOne(keySerializer.toDBObject((K) key, true, false)) != null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        return collection.findOne(valueSerializer.toDBObject((V) value, true, false)) != null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        DBObject result = collection.findOne(keySerializer.toDBObject((K) key, true, false));
        return result != null ? valueSerializer.toElement(result) : null;
    }
    
    @Override
    public V put(K key, V value) {
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
    public V remove(Object key) {
        DBObject queryObject = keySerializer.toDBObject((K) key, true, false);
        DBObject result = collection.findOne(queryObject);
        V old = result != null ? valueSerializer.toElement(result) : null;
        collection.remove(queryObject);
        return old;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : ((Map<K, V>) map).entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void clear() {
        collection.remove(new BasicDBObject());
    }
    
    @Override
    public Set<K> keySet() {
        return new MongoSet<K>(collection, keySerializer);
    }
    
    @Override
    public Collection<V> values() {
        return new MongoCollection<V>(collection, valueSerializer);
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new MongoSet<Entry<K, V>>(collection, new DBObjectSerializer<Entry<K, V>>() {
            
            @Override
            public DBObject toDBObject(Entry<K, V> element, boolean equalsFunctions, boolean negate) {
                return null;
            }
            
            @Override
            public Entry<K, V> toElement(DBObject dbObject) {
                return new MongoMapEntry<K, V>(keySerializer.toElement(dbObject), collection,
                        (DBObject) dbObject.get("value"), valueSerializer);
            }
            
        });
    }
    
    @Override
    public V putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            return put(key, value);
        }
        return get(key);
    }
    
    @Override
    public boolean remove(Object key, Object value) {
        if (containsKey(key) && get(key).equals(value)) {
            remove(key);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (containsKey(key) && get(key).equals(oldValue)) {
            put(key, newValue);
            return true;
        }
        return false;
    }
    
    @Override
    public V replace(K key, V value) {
        if (containsKey(key)) {
            return put(key, value);
        }
        return null;
    }
    
}
