package com.github.mongoutils.collections.command;

import com.mongodb.DBCollection;

public interface IsEmptyCommand extends CollectionCommand {
    
    boolean isEmpty(DBCollection collection);
    
}
