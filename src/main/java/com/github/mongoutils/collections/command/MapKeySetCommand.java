package com.github.mongoutils.collections.command;

import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoSet;
import com.mongodb.DBCollection;

public interface MapKeySetCommand<K> extends CollectionCommand {
    
    MongoSet<K> keySet(DBCollection collection, DBObjectSerializer<K> serializer);
    
}
