package com.github.mongoutils.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoConcurrentMap<K, V> implements ConcurrentMap<K, V> {
    
    private static final BasicDBObject EMPTY_BASICDBOBJECT = new BasicDBObject();
    DBCollection collection;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;
    
    public MongoConcurrentMap(final DBCollection collection,
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
    public void clear() {
        collection.remove(EMPTY_BASICDBOBJECT);
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
    @SuppressWarnings("unchecked")
    public V get(final Object key) {
        DBObject result = collection.findOne(keySerializer.toDBObject((K) key, true, false));
        return result != null ? valueSerializer.toElement(result) : null;
    }
    
    @Override
    public V put(final K key, final V value) {
        return put(key, value, true, false);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : ((Map<K, V>) map).entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public V putIfAbsent(final K key, final V value) {
        if (!containsKey(key)) {
            return put(key, value, true, true);
        }
        return get(key);
    }
    
    public V put(final K key, final V value, boolean insertIfAbsent, boolean returnNew) {
        DBObject dbObject;
        DBObject queryObject;
        V old = null;
        
        queryObject = keySerializer.toDBObject(key, true, false);
        dbObject = keySerializer.toDBObject(key, false, false);
        dbObject.putAll(valueSerializer.toDBObject(value, false, false));
        
        dbObject = collection.findAndModify(queryObject, null, null, false, dbObject, returnNew, insertIfAbsent);
        if (dbObject != null) {
            old = valueSerializer.toElement(dbObject);
        }
        
        return old;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V remove(final Object key) {
        DBObject queryObject = keySerializer.toDBObject((K) key, true, false);
        DBObject result;
        V old = null;
        
        result = collection.findAndRemove(queryObject);
        if (result != null) {
            old = valueSerializer.toElement(result);
        }
        
        return old;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(final Object key, final Object value) {
        DBObject queryObject = keySerializer.toDBObject((K) key, true, false);
        queryObject.putAll(valueSerializer.toDBObject((V) value, true, false));
        
        return collection.remove(queryObject).getN() > 0;
    }
    
    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        DBObject queryObject = keySerializer.toDBObject(key, true, false);
        DBObject dbObject = keySerializer.toDBObject(key, false, false);
        
        queryObject.putAll(valueSerializer.toDBObject(oldValue, true, false));
        dbObject.putAll(valueSerializer.toDBObject(newValue, false, false));
        
        return collection.update(queryObject, dbObject).getN() > 0;
    }
    
    @Override
    public V replace(final K key, final V value) {
        return put(key, value, false, false);
    }
    
}
