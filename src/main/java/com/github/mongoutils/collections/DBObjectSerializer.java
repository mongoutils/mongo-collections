package com.github.mongoutils.collections;

import com.mongodb.DBObject;

public interface DBObjectSerializer<E> {
    
    DBObject toDBObject(E element, boolean equalFunctions, boolean negate);
    
    E toElement(DBObject dbObject);
    
}
