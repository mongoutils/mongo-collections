package com.github.mongoutils.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class CachingConcurrentMap<K, V> extends CachingMap<K, V> implements ConcurrentMap<K, V> {
    
    Object monitor = new Object();
    
    public CachingConcurrentMap(ConcurrentMap<K, V> cache, MongoConcurrentMap<K, V> backstore) {
        super(cache, backstore);
    }
    
    @Override
    public void clear() {
        synchronized (monitor) {
            super.clear();
        }
    }
    
    @Override
    public boolean containsKey(Object key) {
        synchronized (monitor) {
            return super.containsKey(key);
        }
    }
    
    @Override
    public boolean containsValue(Object value) {
        synchronized (monitor) {
            return super.containsValue(value);
        }
    }
    
    @Override
    public V get(Object key) {
        synchronized (monitor) {
            return super.get(key);
        }
    }
    
    @Override
    public boolean isEmpty() {
        synchronized (monitor) {
            return super.isEmpty();
        }
    }
    
    @Override
    public V put(K key, V value) {
        synchronized (monitor) {
            return super.put(key, value);
        }
    }
    
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        synchronized (monitor) {
            super.putAll(map);
        }
    }
    
    @Override
    public V remove(Object key) {
        synchronized (monitor) {
            return super.remove(key);
        }
    }
    
    @Override
    public V putIfAbsent(K key, V value) {
        synchronized (monitor) {
            if (!super.containsKey(key)) {
                return super.put(key, value);
            }
            return super.get(key);
        }
    }
    
    @Override
    public boolean remove(Object key, Object value) {
        synchronized (monitor) {
            if (super.containsKey(key) && super.get(key).equals(value)) {
                super.remove(key);
                return true;
            }
            return false;
        }
    }
    
    @Override
    public V replace(K key, V value) {
        synchronized (monitor) {
            if (super.containsKey(key)) {
                return super.put(key, value);
            }
            return null;
        }
    }
    
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        synchronized (monitor) {
            if (super.containsKey(key) && super.get(key).equals(oldValue)) {
                super.put(key, newValue);
                return true;
            }
            return false;
        }
    }
    
}
