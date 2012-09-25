package com.github.mongoutils.collections;

import java.util.Map;

public class CachingMap<K, V> implements Map<K, V> {

    Map<K, V> cache;
    MongoConcurrentMap<K, V> backstore;

    public CachingMap(final Map<K, V> cache, final MongoConcurrentMap<K, V> backstore) {
        this.cache = cache;
        this.backstore = backstore;
    }

    @Override
    public void clear() {
        cache.clear();
        backstore.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        if (!cache.containsKey(key)) {
            return backstore.containsKey(key);
        }
        return true;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (!cache.containsValue(value)) {
            return backstore.containsValue(value);
        }
        return true;
    }

    @Override
    public MongoSet<Entry<K, V>> entrySet() {
        return backstore.entrySet();
    }

    @Override
    public V get(final Object key) {
        if (!cache.containsKey(key)) {
            if (backstore.containsKey(key)) {
                return backstore.get(key);
            }
            return null;
        }
        return cache.get(key);
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty() ? backstore.isEmpty() : false;
    }

    @Override
    public MongoSet<K> keySet() {
        return backstore.keySet();
    }

    @Override
    public V put(final K key, final V value) {
        cache.put(key, value);
        return backstore.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        cache.putAll(map);
        backstore.putAll(map);
    }

    @Override
    public V remove(final Object key) {
        cache.remove(key);
        return backstore.remove(key);
    }

    @Override
    public int size() {
        return backstore.size();
    }

    @Override
    public MongoCollection<V> values() {
        return backstore.values();
    }

}
