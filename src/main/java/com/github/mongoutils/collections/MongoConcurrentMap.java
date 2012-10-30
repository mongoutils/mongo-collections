package com.github.mongoutils.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.github.mongoutils.collections.command.ClearCommand;
import com.github.mongoutils.collections.command.ContainsCommand;
import com.github.mongoutils.collections.command.IsEmptyCommand;
import com.github.mongoutils.collections.command.MapEntrySetCommand;
import com.github.mongoutils.collections.command.MapGetCommand;
import com.github.mongoutils.collections.command.MapKeySetCommand;
import com.github.mongoutils.collections.command.MapPutCommand;
import com.github.mongoutils.collections.command.MapRemoveCommand;
import com.github.mongoutils.collections.command.MapReplaceCommand;
import com.github.mongoutils.collections.command.MapValuesCommand;
import com.github.mongoutils.collections.command.SizeCommand;
import com.github.mongoutils.collections.command.impl.DefaultClearCommand;
import com.github.mongoutils.collections.command.impl.DefaultContainsCommand;
import com.github.mongoutils.collections.command.impl.DefaultIsEmptyCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapEntrySetCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapGetCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapKeySetCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapPutCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapRemoveCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapReplaceCommand;
import com.github.mongoutils.collections.command.impl.DefaultMapValuesCommand;
import com.github.mongoutils.collections.command.impl.DefaultSizeCommand;
import com.mongodb.DBCollection;

public class MongoConcurrentMap<K, V> implements ConcurrentMap<K, V> {
    
    protected DBCollection collection;
    protected DBObjectSerializer<K> keySerializer;
    protected DBObjectSerializer<V> valueSerializer;
    
    protected SizeCommand sizeCommand = new DefaultSizeCommand();
    protected IsEmptyCommand isEmptyCommand = new DefaultIsEmptyCommand();
    protected ContainsCommand<K> containsKeyCommand = new DefaultContainsCommand<K>();
    protected ContainsCommand<V> containsValueCommand = new DefaultContainsCommand<V>();
    protected ClearCommand clearCommand = new DefaultClearCommand();
    protected MapKeySetCommand<K> keySetCommand = new DefaultMapKeySetCommand<K>();
    protected MapValuesCommand<V> valuesCommand = new DefaultMapValuesCommand<V>();
    protected MapEntrySetCommand<K, V> entrySetCommand = new DefaultMapEntrySetCommand<K, V>();
    protected MapGetCommand<K, V> getCommand = new DefaultMapGetCommand<K, V>();
    protected MapPutCommand<K, V> putCommand = new DefaultMapPutCommand<K, V>(getCommand, containsKeyCommand);
    protected MapRemoveCommand<K, V> removeCommand = new DefaultMapRemoveCommand<K, V>();
    protected MapReplaceCommand<K, V> replaceCommand = new DefaultMapReplaceCommand<K, V>();
    
    public MongoConcurrentMap(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer) {
        this.collection = collection;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }
    
    @Override
    public int size() {
        return sizeCommand.size(collection);
    }
    
    @Override
    public boolean isEmpty() {
        return isEmptyCommand.isEmpty(collection);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        return containsKeyCommand.contains(collection, keySerializer, (K) key);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        return containsValueCommand.contains(collection, valueSerializer, (V) value);
    }
    
    @Override
    public void clear() {
        clearCommand.clear(collection);
    }
    
    @Override
    public MongoSet<K> keySet() {
        return keySetCommand.keySet(collection, keySerializer);
    }
    
    @Override
    public MongoCollection<V> values() {
        return valuesCommand.values(collection, valueSerializer);
    }
    
    @Override
    public MongoSet<Map.Entry<K, V>> entrySet() {
        return entrySetCommand.entrySet(collection, new MongoMapEntryDBObjectSerializer<K, V>(collection,
                keySerializer, valueSerializer));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        return getCommand.get(collection, keySerializer, valueSerializer, (K) key);
    }
    
    @Override
    public V put(K key, V value) {
        return putCommand.put(collection, keySerializer, valueSerializer, key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> map) {
        putCommand.putAll(collection, keySerializer, valueSerializer, (Map<K, V>) map);
    }
    
    @Override
    public V putIfAbsent(K key, V value) {
        return putCommand.putIfAbsent(collection, keySerializer, valueSerializer, key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        return removeCommand.remove(collection, keySerializer, valueSerializer, (K) key);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object key, Object value) {
        return removeCommand.remove(collection, keySerializer, valueSerializer, (K) key, (V) value);
    }
    
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return replaceCommand.replace(collection, keySerializer, valueSerializer, key, oldValue, newValue);
    }
    
    @Override
    public V replace(K key, V value) {
        return replaceCommand.replace(collection, keySerializer, valueSerializer, key, value);
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
    
    public ContainsCommand<K> getContainsKeyCommand() {
        return containsKeyCommand;
    }
    
    public void setContainsKeyCommand(ContainsCommand<K> containsKeyCommand) {
        this.containsKeyCommand = containsKeyCommand;
    }
    
    public ContainsCommand<V> getContainsValueCommand() {
        return containsValueCommand;
    }
    
    public void setContainsValueCommand(ContainsCommand<V> containsValueCommand) {
        this.containsValueCommand = containsValueCommand;
    }
    
    public ClearCommand getClearCommand() {
        return clearCommand;
    }
    
    public void setClearCommand(ClearCommand clearCommand) {
        this.clearCommand = clearCommand;
    }
    
    public MapKeySetCommand<K> getKeySetCommand() {
        return keySetCommand;
    }
    
    public void setKeySetCommand(MapKeySetCommand<K> keySetCommand) {
        this.keySetCommand = keySetCommand;
    }
    
    public MapValuesCommand<V> getValuesCommand() {
        return valuesCommand;
    }
    
    public void setValuesCommand(MapValuesCommand<V> valuesCommand) {
        this.valuesCommand = valuesCommand;
    }
    
    public MapEntrySetCommand<K, V> getEntrySetCommand() {
        return entrySetCommand;
    }
    
    public void setEntrySetCommand(MapEntrySetCommand<K, V> entrySetCommand) {
        this.entrySetCommand = entrySetCommand;
    }
    
    public MapGetCommand<K, V> getGetCommand() {
        return getCommand;
    }
    
    public void setGetCommand(MapGetCommand<K, V> getCommand) {
        this.getCommand = getCommand;
    }
    
    public MapPutCommand<K, V> getPutCommand() {
        return putCommand;
    }
    
    public void setPutCommand(MapPutCommand<K, V> putCommand) {
        this.putCommand = putCommand;
    }
    
    public MapRemoveCommand<K, V> getRemoveCommand() {
        return removeCommand;
    }
    
    public void setRemoveCommand(MapRemoveCommand<K, V> removeCommand) {
        this.removeCommand = removeCommand;
    }
    
    public MapReplaceCommand<K, V> getReplaceCommand() {
        return replaceCommand;
    }
    
    public void setReplaceCommand(MapReplaceCommand<K, V> replaceCommand) {
        this.replaceCommand = replaceCommand;
    }
    
}
