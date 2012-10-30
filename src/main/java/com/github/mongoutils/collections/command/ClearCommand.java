package com.github.mongoutils.collections.command;

import com.mongodb.DBCollection;

public interface ClearCommand extends CollectionCommand {
    
    void clear(DBCollection collection);
    
}
