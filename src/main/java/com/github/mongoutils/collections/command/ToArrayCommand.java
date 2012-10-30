package com.github.mongoutils.collections.command;

import com.mongodb.DBCollection;

public interface ToArrayCommand extends CollectionCommand {
    
    int size(DBCollection collection);
    
}
