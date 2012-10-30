package com.github.mongoutils.collections.command.impl;

import com.github.mongoutils.collections.command.ClearCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class DefaultClearCommand implements ClearCommand {
    
    private static final BasicDBObject EMPTY_BASICDBOBJECT = new BasicDBObject();
    
    @Override
    public void clear(DBCollection collection) {
        collection.remove(EMPTY_BASICDBOBJECT);
    }
    
}
